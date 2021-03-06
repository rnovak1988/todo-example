package org.rnovak;


import org.apache.log4j.Logger;
import org.rnovak.iface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserControllerImpl implements UserController {

    private static final Logger logger = Logger.getLogger(UserController.class);

    private DataSource dataSource;

    private SecurityContextHolder securityContextHolder;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private static final String CREATE_QUERY = "INSERT INTO todo.users (username, password, enabled) VALUES (?, ?, 1)";
    private static final String AUTH_QUERY = "INSERT INTO todo.authorities (username, authority) VALUES (?, 'ROLE_USER')";

    private static final String SELECT_STATEMENT = "SELECT u.username AS username, u.enabled AS enabled FROM todo.users u";

    private static final String ERROR_STRING = "Should Not Be Returning Results on INSERT";

    private static final ModelAndView adminView = new ModelAndView();

    static {
        adminView.setViewName("/admin.html");
    }

    @Override
    public ModelAndView admin() {
        return adminView;
    }

    @Override
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "msg", required = false) String msg) {
        ModelAndView result = new ModelAndView();

        if (error != null) result.addObject("error", error);
        if (msg != null) result.addObject("msg", msg);

        result.setViewName("/login.jsp");
        return result;
    }

    @Override
    @Transactional
    public HttpEntity<Boolean> createUser(
        @RequestBody User user
    )
    {
        String username = user.getUsername();
        String password = user.getPassword();
        boolean result = false;

            // Get rid of the password in memory
        password = bCryptPasswordEncoder.encode(password);

            // Hopefully this will reclaim memory
        System.gc();

        Connection connection = null; PreparedStatement sth = null;

        int updated = 0; boolean is_resultset;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(CREATE_QUERY);

                if (sth != null) {
                    sth.setString(1, username);
                    sth.setString(2, password);

                    is_resultset = sth.execute();

                    if (is_resultset) throw new AssertionError(ERROR_STRING);

                    updated = sth.getUpdateCount();

                    sth.close(); sth = null; System.gc();

                    if (updated > 0) {
                        sth = connection.prepareStatement(AUTH_QUERY);

                        sth.setString(1, username);

                        is_resultset = sth.execute();

                        if (is_resultset) throw new AssertionError(ERROR_STRING);

                        updated = sth.getUpdateCount();

                        if (updated > 0) {
                            result = true;
                            logger.debug(String.format("Successfully created user: \"%s\" ", username));
                        } else {
                            logger.error(String.format("Something went wrong, couldn't create user \"%s\"", username));
                        }
                    }

                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            // Release the statement
            try {
                if (sth != null && !sth.isClosed()) sth.close();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage(), e);
            }

            // Release the Connection
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @Override
    public HttpEntity<List<Map<String, Object>>> list() {
        List<Map<String, Object>> result = new LinkedList<>();

        Connection connection = null; PreparedStatement sth = null; ResultSet rth = null;

        boolean is_resultset;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(SELECT_STATEMENT);

                is_resultset = sth.execute();

                if (is_resultset) {     // Tells us that there were indeed results
                    rth = sth.getResultSet();
                    if (rth != null) {
                        while (rth.next()) {

                            String username = rth.getString("username");
                            int enabled = rth.getInt("enabled");

                            Map<String, Object> tmp = new HashMap<>();
                            tmp.put("username", username);
                            tmp.put("enabled", enabled > 0);

                            result.add(tmp);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                if (rth != null && !rth.isClosed()) rth.close();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            try {
                if (sth != null && !sth.isClosed()) sth.close();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            try {
                if (connection != null && !connection.isClosed()) connection.close();
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return new ResponseEntity<List<Map<String, Object>>>(result, HttpStatus.OK);
    }

    public HttpEntity<Map<String, String>> current() {
        String username = "";
        SecurityContext currentContext = null;
        Authentication currentAuth = null;
        UserDetails userDetails = null;

        try {
            currentContext = securityContextHolder.getContext();
            currentAuth = currentContext.getAuthentication();

            Object principal = currentAuth.getPrincipal();

            userDetails = UserDetails.class.cast(principal);

            username = userDetails.getUsername();

        } catch (NullPointerException|ClassCastException e) {
            logger.error(e.getLocalizedMessage(), e);
            username = "";
        }
        Map<String, String> result = new LinkedHashMap<>();
        result.put("username", username);
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    public HttpEntity<Map<String, String>> currentRole() {
        String role = null;
        SecurityContext currentContext = null;
        Authentication currentAuth = null;
        UserDetails userDetails = null;

        try {
            currentContext = securityContextHolder.getContext();
            currentAuth = currentContext.getAuthentication();

            Object principal = currentAuth.getPrincipal();

            userDetails = UserDetails.class.cast(principal);

            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

            for (GrantedAuthority authority : authorities)
                if (authority.getAuthority().contains("ADMIN"))
                    role = authority.getAuthority();

            if (role == null) role = "ROLE_USER";

        } catch (NullPointerException|ClassCastException e) {
            logger.error(e.getLocalizedMessage(), e);
            role = "ROLE_USER";
        }
        Map<String, String> result = new LinkedHashMap<>();
        result.put("role", role);
        return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setSecurityContextHolder(SecurityContextHolder securityContextHolder) {
        this.securityContextHolder = securityContextHolder;
    }
}

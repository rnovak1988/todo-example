package org.rnovak;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.rnovak.iface.TaskController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class TaskControllerImpl implements TaskController {

    private static final Logger logger = Logger.getLogger(TaskControllerImpl.class);

    private static final String DEFAULT_USERNAME = "rnovak";

    private static final String SELECT_QUERY = "SELECT t.title, t.details, t.completed, t.archived FROM todo.tasks t WHERE t.id = ? AND t.username = ?";

    private static final String LIST_QUERY = "SELECT t.id, t.title, t.details, t.completed, t.archived FROM todo.tasks t WHERE t.username = ? AND t.archived = 0";
    private static final String LIST_QUERY_SHOW_ARCHIVED = "SELECT t.id, t.title, t.details, t.completed, t.archived FROM todo.tasks t WHERE t.username = ?";

    private static final String INSERT_QUERY = "INSERT INTO todo.tasks (username, title, details, completed, archived) VALUES (?, ?, ?, 0, 0)";
    private static final String UPDATE_QUERY = "UPDATE todo.tasks t SET t.title = ?, t.details = ?, t.completed = ?, t.archived = ? WHERE t.id = ? AND t.username = ?";

    private static final String DELETE_QUERY = "DELETE FROM todo.tasks t WHERE t.id = ? AND t.username = ?";

    private SecurityContextHolder securityContextHolder;
    private DataSource dataSource;

    private String getUsername() {
        String username = DEFAULT_USERNAME;
        SecurityContext context = securityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails details = UserDetails.class.cast(principal);
                username = details.getUsername();
            }
        }
        return username;
    }

    private void closeAll(Connection connection, PreparedStatement sth, ResultSet rth) {
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

    @Override
    public HttpEntity<List<Task>> list(boolean archived) {
        List<Task> result = new LinkedList<>();

        Connection connection = null; PreparedStatement sth = null; ResultSet rth = null;

        boolean is_resultset;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(archived ? LIST_QUERY_SHOW_ARCHIVED : LIST_QUERY);

                String username = this.getUsername();   // Get username of currently logged in user (or rnovak)

                sth.setString(1, username);
                is_resultset = sth.execute();

                if (is_resultset) {
                    rth = sth.getResultSet();

                    int is_archived = 0, is_completed = 0, id = 0;
                    String title, details;

                    if (rth != null) while (rth.next()) {

                        id = rth.getInt("t.id");
                        title = rth.getString("t.title");

                            // Get and Decode the string
                        details = rth.getString("t.details");
                        details = new String(Base64.decodeBase64(details.getBytes()), "UTF-8");

                        is_archived = rth.getInt("t.archived");
                        is_completed = rth.getInt("t.completed");

                        result.add(new Task(id, title, details, is_completed > 0, is_archived > 0));

                    }

                }
            }
        } catch (SQLException|UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            this.closeAll(connection, sth, rth);
        }

        return new ResponseEntity<List<Task>>(result, HttpStatus.OK);
    }

    @Override
    @Transactional
    public HttpEntity<Task> put(String title, String details) {
        Task result = new Task(null, title, details, false, false);

        String currentUsername = this.getUsername();    // Get the name of the logged in user

        Connection connection = null; PreparedStatement sth = null; ResultSet rth = null;

        boolean is_resultset;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(INSERT_QUERY);

                sth.setString(1, currentUsername);
                sth.setString(2, title);

                details = new String(Base64.encodeBase64(details.getBytes()));

                sth.setString(3, details);

                is_resultset = sth.execute();

                if (is_resultset) throw new AssertionError("Should Not Return Results for Insert");

                rth = sth.getGeneratedKeys();

                if (rth != null) while (rth.next()) {
                    result.setId(rth.getInt(1));
                    break;
                }

            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            closeAll(connection, sth, rth);
        }

        return new ResponseEntity<Task>(result, HttpStatus.OK);
    }

    @Override
    @Transactional
    public HttpEntity<Task> post(Integer id, String title,String details, boolean completed, boolean archived) {

        if (id == null || title == null || details == null) throw new AssertionError("Can't Have Null values");

        String username = this.getUsername();

        Task result = null;

        Connection connection = null; PreparedStatement sth = null;

        int updateCount = 0;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(UPDATE_QUERY);

                sth.setString(1, title);
                sth.setString(2, new String(Base64.encodeBase64(details.getBytes("UTF-8")), "UTF-8"));
                sth.setInt(3, completed ? 1 : 0);
                sth.setInt(4, completed ? 1 : 0);

                sth.setInt(5, id);
                sth.setString(6, username);

                updateCount = sth.executeUpdate();

                if (updateCount > 0) {
                    result = new Task(id, title, details, completed, archived);
                }
            }
        } catch (SQLException|UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            closeAll(connection, sth, null);
        }

        return new ResponseEntity<Task>(result, HttpStatus.OK);
    }

    @Override
    public HttpEntity<Task> get(Integer id) {

        Task result = null;

        if (id == null) throw new AssertionError("ID Can't be Null");

        String username = this.getUsername();

        Connection connection = null; PreparedStatement sth = null; ResultSet rth = null;

        boolean is_resultset;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {

                sth = connection.prepareStatement(SELECT_QUERY);

                sth.setInt(1, id);
                sth.setString(2, username);

                is_resultset = sth.execute();

                if (is_resultset && (rth = sth.getResultSet()) != null && rth.next()) {

                    String title = rth.getString("t.title");
                    String deets = new String(Base64.decodeBase64(rth.getString("t.details").getBytes("UTF-8")), "UTF-8");
                    boolean is_completed = rth.getInt("t.completed") > 0;
                    boolean is_archived = rth.getInt("t.archived") > 0;

                    result = new Task(id, title, deets, is_completed, is_archived);
                }

            }
        } catch (SQLException|UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            closeAll(connection, sth, rth);
        }

        return new ResponseEntity<Task>(result, HttpStatus.OK);
    }

    @Override
    public HttpEntity<Boolean> delete(Integer id) {
        boolean result = false;

        String user = this.getUsername();

        Connection connection = null; PreparedStatement sth = null; int updateCount = 0;

        try {
            if ((connection = dataSource.getConnection()) != null && connection.isValid(2000)) {
                sth = connection.prepareStatement(DELETE_QUERY);
                sth.setInt(1, id);
                sth.setString(2, user);
                sth.execute();
                updateCount = sth.getUpdateCount();
                if (updateCount > 0) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            this.closeAll(connection, sth, null);
        }

        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @Autowired
    public void setSecurityContextHolder(SecurityContextHolder holder) {
        this.securityContextHolder = holder;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

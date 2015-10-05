package org.rnovak.iface;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public interface UserController {

    @RequestMapping(value="/login")
    public ModelAndView login(
            @RequestParam (value="error", required=false)   String error,
            @RequestParam (value="msg", required=false)     String msg
    );

    @RequestMapping(value="/current", method = {RequestMethod.GET})
    public HttpEntity<Map<String, String>> current();

    @RequestMapping(value="/current/role", method={RequestMethod.GET})
    public HttpEntity<Map<String, String>> currentRole();

    @RequestMapping(value = "/new", method = {RequestMethod.GET})
    public HttpEntity<Boolean> createUser(
            @RequestParam(value="username", required=true) String username,
            @RequestParam(value="password", required=true) String password
    );

    @RequestMapping(value="/list", method= {RequestMethod.GET})
    public HttpEntity<List<Map<String, Object>>> list ();


}

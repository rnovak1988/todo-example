package org.rnovak.iface;

import org.rnovak.User;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value="/admin/")
    public ModelAndView admin();

    @RequestMapping(value="/current", method = {RequestMethod.GET})
    public HttpEntity<Map<String, String>> current();

    @RequestMapping(value="/current/role", method={RequestMethod.GET})
    public HttpEntity<Map<String, String>> currentRole();

    @RequestMapping(value = "/new", method = {RequestMethod.PUT})
    public HttpEntity<Boolean> createUser(
        @RequestBody User user
    );

    @RequestMapping(value="/list", method= {RequestMethod.GET})
    public HttpEntity<List<Map<String, Object>>> list ();


}

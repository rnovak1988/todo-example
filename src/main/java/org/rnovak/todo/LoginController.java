package org.rnovak.todo;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.JstlView;

@Controller
public class LoginController {

    private static Logger logger = Logger.getLogger(LoginController.class);

    @RequestMapping(value={"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "msg", required = false) String msg
    ) {
        logger.debug("IN FUCKIN HERE");
        ModelAndView result = new ModelAndView();
        if (error != null) {
            result.addObject("error", error);
        }

        if (msg != null) {
            result.addObject("msg", msg);
        }

        result.setViewName("/login.jsp");
        return result;
    }

}

package org.rnovak;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class UserController {

    @RequestMapping("/login")
    public ModelAndView login(
            @RequestParam(value="error", required=false) String error,
            @RequestParam(value="msg", required=false) String msg
    ) {
        ModelAndView result = new ModelAndView();

        if (error != null) result.addObject("error", error);
        if (msg != null) result.addObject("msg", msg);

        result.setViewName("/login.jsp");
        return result;
    }

}

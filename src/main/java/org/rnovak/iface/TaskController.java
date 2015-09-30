package org.rnovak.iface;

import org.rnovak.Task;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public interface TaskController {

    @RequestMapping(value="/task/list", method=RequestMethod.GET)
    public HttpEntity<List<Task>> list(
            @RequestParam(value="includeArchived", required=false, defaultValue = "false") boolean archived
    );

    @RequestMapping(value="/task", method= RequestMethod.PUT)
    public HttpEntity<Task> put(
            @RequestParam(value="title",        required=true)   String title,
            @RequestParam(value="details",      required=true)   String details
    );

    @RequestMapping(value="/task", method=RequestMethod.POST)
    public HttpEntity<Task> post (
            @RequestParam(value="id",           required=true)   Integer id,
            @RequestParam(value="title",        required=true)   String title,
            @RequestParam(value="details",      required=true)   String details,
            @RequestParam(value="isCompleted",  required=true)   boolean completed,
            @RequestParam(value="isArchived",   required=true)   boolean archived
    );

    @RequestMapping(value="/task", method=RequestMethod.GET)
    public HttpEntity<Task> get (
            @RequestParam(value="id",           required=true)  Integer id
    );

    @RequestMapping(value="/task", method=RequestMethod.DELETE)
    public HttpEntity<Boolean> delete(
            @RequestParam(value="id",           required=true)  Integer id
    );

}

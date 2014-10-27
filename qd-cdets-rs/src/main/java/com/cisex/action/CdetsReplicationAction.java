package com.cisex.action;

import com.cisex.service.CdetsReplicator;
import org.apache.http.impl.cookie.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/13/13
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
@RequestMapping("/rep")
public class CdetsReplicationAction {
    private static final Logger log = LoggerFactory.getLogger(CdetsReplicationAction.class);

    @RequestMapping(method = RequestMethod.GET)
    public String get() {
        return "rep.form";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public String post(@RequestParam String project, @RequestParam String product) {

        try {
            log.debug(">>> manual rep: " + project + ", " + product);
            CdetsReplicator.replicate(project, product);
            log.debug("<<< manual rep: " + project + ", " + product);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "success";
    }


    @ResponseBody
    @RequestMapping("/{id}")
    public String replicateById(@PathVariable String id) {
        try {
            CdetsReplicator.replicate(id);
        } catch (Exception e) {
            return e.getMessage();
        }

        return "success: " + id + ", "
                + DateUtils.formatDate(
                Calendar.getInstance().getTime(),
                "yyyy-MM-dd HH:mm:ss"
        );
    }
}

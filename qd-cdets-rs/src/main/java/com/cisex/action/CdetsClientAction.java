package com.cisex.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/13/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/client")
public class CdetsClientAction {
    @RequestMapping(value = "/daily")
    public String daily() {
        return "daily.form";
    }

    @RequestMapping(value = "/ssc")
    public String ssc() {
        return "ssc.form";
    }

    @RequestMapping(value = "/qddts")
    public String qddts() {
        return "qddts.query.form";
    }

    @RequestMapping(value = "c2m")
    public String c2m() {
        return "c2m.form";
    }
}

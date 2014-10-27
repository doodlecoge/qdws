package com.cisex.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-30
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
public class Severity {
    private static final Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("catastrophic", "1");
        map.put("severe", "2");
        map.put("moderate", "3");
        map.put("minor", "4");
        map.put("cosmetic", "5");
        map.put("enhancement", "6");
    }

    public static String getValue(String desc) {
        return map.get(desc);
    }
}
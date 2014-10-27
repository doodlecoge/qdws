package com.cisex.util;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/10/13
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class UrlUtils {
    public static String joinParameters(List<NameValuePair> params) {
        int len = params.size();
        String str = "";

        for (int i = 0; i < len; i++) {
            if (i != 0) str += "&";
            NameValuePair pair = params.get(i);
            String key = pair.getName();
            String val = pair.getValue();
            str += key + "=" + val;
        }

        return str;
    }
}

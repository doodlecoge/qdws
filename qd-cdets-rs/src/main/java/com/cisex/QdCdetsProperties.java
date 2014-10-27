package com.cisex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/19/13
 * Time: 9:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class QdCdetsProperties {
    private static final Logger log = LoggerFactory.getLogger(QdCdetsProperties.class);
    private static final Map<String, String> props = new HashMap<String, String>();

    static {
        InputStream is = QdCdetsProperties.class.getResourceAsStream("/system.properties");
        Properties _props = new Properties();
        try {
            _props.load(is);
        } catch (IOException e) {
            log.error("load /system.properties error.", e);
        }
        Enumeration<Object> keys = _props.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object val = _props.get(key);
            props.put(key.toString(), val.toString());
        }
    }

    public static String getString(String key) {
        return props.get(key);
    }

    public static int getInt(String key) {
        String val = getString(key);
        return Integer.valueOf(val);
    }
}

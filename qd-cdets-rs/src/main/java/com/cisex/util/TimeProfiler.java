package com.cisex.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-30
 * Time: 下午2:21
 * To change this template use File | Settings | File Templates.
 */
public class TimeProfiler {
    private static final Logger log = LoggerFactory.getLogger(TimeProfiler.class);

    private long startMilliseconds;
    private long stopMilliseconds;
    private String identifier;

    public TimeProfiler(String identifier) {
        this.identifier = identifier;
    }

    public void start() {
        startMilliseconds = System.currentTimeMillis();
    }

    public void stop() {
        long ts = System.currentTimeMillis();
        long diff = ts - startMilliseconds;
        log.debug(identifier + " - " + diff + "ms");
    }
}

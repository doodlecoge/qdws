package com.cisex.migrate;

import org.apache.http.impl.cookie.DateParseException;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-30
 * Time: 下午4:39
 * To change this template use File | Settings | File Templates.
 */
public class Accumulate {
    private static long millisecondsInOneDay = 1000 * 60 * 60 * 24;
    private static String[] ptns = new String[]{"yyyy-MM-dd HH:mm:ss"};


    // audit trial field
    private final static String AF_STATUS = "Status";
    private final static String AF_VERSION = "Version";
    private final static String AF_TO_BE_FIXED = "To-be-fixed";
    private final static String AF_SEVERITY = "Severity-desc";

    // operations
    private final static String OP_MODIFY = "Modify";
    private final static String OP_NEW = "New Record";
    private final static String OP_DELETE = "Delete";


    private final static Set<String> OpenedStatus = new HashSet<String>();
    private final static Set<String> ClosedStatus = new HashSet<String>();

    static {
        String str = "SNAOWHIPMJ";
        for (int i = 0; i < str.length(); i++) {
            OpenedStatus.add(str.charAt(i) + "");
        }

        str = "UFCDRV";
        for (int i = 0; i < str.length(); i++) {
            ClosedStatus.add(str.charAt(i) + "");
        }
    }


    public static void main(String[] args) throws DateParseException {
//        String project = "CSC.csg";
//        String product = "identity";
//
//
//        CdetsDefectAccumulator accumulator = new CdetsDefectAccumulator(project, product);
//
//        Set<String> toBeFixed = new HashSet<String>();
//        toBeFixed.add("3.0");
//        Set<String> severity = new HashSet<String>();
////        severity.add("catastrophic");
////        severity.add("severe");
////        severity.add("moderate");
////        severity.add("minor");
////        severity.add("cosmetic");
////        severity.add("enhancement");
//
//        severity.add("1");
//        severity.add("2");
//        severity.add("3");
//        severity.add("4");
//        severity.add("5");
//        severity.add("6");
//
//        long st = System.currentTimeMillis();
////        accumulator.accumulateByToBeFixedVersions(toBeFixed);
////        accumulator.accumulateByVersion(toBeFixed);
////        accumulator.accumulateBySeverity(severity);
//        accumulator.accumulate3(new HashSet<String>(), toBeFixed, severity);
//        long et = System.currentTimeMillis();
//
//        accumulator.showAccumulation();
//
//        System.out.println("use time: " + (et - st));

    }

    public static int daySpan(Date max, Date min) {
        long ts = max.getTime() - min.getTime();
        return (int) (ts / millisecondsInOneDay);
    }
}

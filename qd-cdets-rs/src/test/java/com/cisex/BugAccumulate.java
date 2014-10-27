package com.cisex;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/26/13
 * Time: 10:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class BugAccumulate {
//    private static long millisecondsInOneDay = 1000 * 60 * 60 * 24;
//    static String[] ptns = new String[]{"yyyy-MM-dd HH:mm:ss"};
//    private static int n = 0;
//
//    public static void main(String[] args) throws IOException, DateParseException, NoSuchFieldException, IllegalAccessException {
//
//        n = 0;
//
//        String project = "CSC.csg";
//        String product = "identity";
//
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//        Object[] timeBounds = DbUtils.getTimeBounds(project, product);
//
//
//        Date maxDate = DateUtils.parseDate(timeBounds[0].toString(), ptns);
//        Date minDate = DateUtils.parseDate(timeBounds[1].toString(), ptns);
//
//        String maxDateString = DateUtils.formatDate(maxDate, "yyyy-MM-dd");
//        String minDateString = DateUtils.formatDate(minDate, "yyyy-MM-dd");
//
//        System.out.println(maxDateString + ", " + maxDate);
//        System.out.println(minDateString + ", " + minDate);
//
//        int days = daySpan(maxDate, minDate) + 1;
//
//        int[] openedBugs = new int[days];
//        int[] closedBugs = new int[days];
//
//        for (int i = 0; i < days; i++) {
//            openedBugs[i] = 0;
//        }
//
//        // ('NAOWHIPM')
//
//        CdetsReplication cdets = new CdetsReplicationImpl();
//
////        List<Defect> defects = DbUtils.getDefects(project, product);
//
//
////        String query = "[Product] = '" + product + "' AND [Project] = '" + project + "' AND [Version] = '2.0' AND ([Severity] = '1' OR [Severity] = '2' OR [Severity] = '3')";
//
//
//        String query = "[Product] = 'identity' AND [Project] = 'CSC.csg' AND ([To-be-fixed] = '2.0')";
//
//
//        List<Defect> defects = cdets.getDefectsByCdetsQuery(query);
//
//        System.out.println("defects: " + defects.size());
//
//        for (Defect defect : defects) {
//
//            Date sTime = defect.getSTime();
//            int idx = daySpan(sTime, minDate);
//            openedBugs[idx] += 1;
//
//            Date completedTime = getCompletedTime(defect);
//
//            if (completedTime == null) continue;
//            openedBugs[daySpan(completedTime, minDate)] -= 1;
//
//        }
//
//
//        int total = 0;
//        for (int i = 0; i < days; i++) {
//            if (openedBugs[i] == 0) continue;
//            long ts = minDate.getTime() + i * millisecondsInOneDay;
//            Calendar date = Calendar.getInstance();
//            date.setTimeInMillis(ts);
//            String dateString = DateUtils.formatDate(date.getTime(), "yyyy-MM-dd");
//            total += openedBugs[i];
//            System.out.println(dateString + "\t" + total);
//        }
//
//        System.out.println(days);
//        System.out.println(n);
//    }
//
//    public static Date getCompletedTime(Defect defect) throws NoSuchFieldException, IllegalAccessException, DateParseException {
//        if (defect.getCTime() != null) return defect.getCTime();
//        if (defect.getDTime() != null) return defect.getDTime();
//        if (defect.getFTime() != null) return defect.getFTime();
//        if (defect.getJTime() != null) return defect.getJTime();
//        if (defect.getRTime() != null) return defect.getRTime();
//        if (defect.getUTime() != null) return defect.getUTime();
//        if (defect.getVTime() != null) return defect.getVTime();
//        n++;
//        return null;
////
////
////
////        for (TimeField timeField : TimeField.values()) {
//////            if ("UVJFCD".indexOf(timeField.name()) == -1) {
//////                continue;
//////            }
////
////            if(timeField == TimeField.SYS_LAST_UPDATED) continue;
////
//////            if ("NAOWHIPMJ".indexOf(timeField.name()) != -1) {
//////                continue;
//////            }
////
////            if("CDFRVU".indexOf(timeField.name()) == -1) continue;
////
////            String dbColName = timeField.dbColName;
////            Field field = Defect.class.getDeclaredField(dbColName);
////            field.setAccessible(true);
////            Object obj = field.get(defect);
////            if (obj == null) continue;
////            return DateUtils.parseDate(obj.toString(), ptns);
////        }
////        System.out.println("null" + n++);
////        return null;
//    }
//
//    public static int daySpan(Date max, Date min) {
//        long ts = max.getTime() - min.getTime();
//        return (int) (ts / millisecondsInOneDay);
//    }
}


package com.cisex.service;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-9-2
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDefectAccumulator {
//    private static final Logger log = LoggerFactory.getLogger(CdetsDefectAccumulator.class);
//
//    // audit trial field
//    private final static String AF_STATUS = "Status";
//    private final static String AF_VERSION = "Version";
//    private final static String AF_TO_BE_FIXED = "To-be-fixed";
//    private final static String AF_SEVERITY = "Severity-desc";
//
//    // operations
//    private final static String OP_MODIFY = "Modify";
//    private final static String OP_NEW = "New Record";
//    private final static String OP_DELETE = "Delete";
//
//
//    private final static Set<String> OpenedStatus = new HashSet<String>();
//    private final static Set<String> ClosedStatus = new HashSet<String>();
//
//    static {
//        String str = "SNAOWHIPM";
//        for (int i = 0; i < str.length(); i++) {
//            OpenedStatus.add(str.charAt(i) + "");
//        }
//
//        str = "UFCDRVJ";
//        for (int i = 0; i < str.length(); i++) {
//            ClosedStatus.add(str.charAt(i) + "");
//        }
//    }
//
//    private static long millisecondsInOneDay = 1000 * 60 * 60 * 24;
//    private static String[] ptns = new String[]{"yyyy-MM-dd HH:mm:ss"};
//
//
//    private Date minDate;
//    private Date maxDate;
//
//    private String project;
//    private String product;
//
//    private int[] openedDefects;
//
//    {
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//    }
//
//    public CdetsDefectAccumulator(String project, String product) {
//        this.project = project;
//        this.product = product;
//    }
//
//    public synchronized void accumulate(Set<String> targetVersions, Set<String> targetToBeFixedVersions, Set<String> targetSeverities) throws DateParseException {
//        getTimeBounds();
//        int days = daySpan(maxDate, minDate) + 1;
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
////            Criteria criteria = session.createCriteria(Defect.class, "de");
////            criteria.createAlias("de.auditTrials", "audit");
////            criteria.add(Restrictions.eq("project", project));
////            criteria.add(Restrictions.eq("product", product));
////            criteria.add(Restrictions.or(Restrictions.eq("audit.oldValue", "3.0"), Restrictions.eq("audit.newValue", "3.0")));
////            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
////            defects = criteria.list();
////
////
////            System.out.println(">>> " + defects.size() + " <<<");
//
//            defects = session.createCriteria(Defect.class, "de")
//                    .add(Restrictions.eq("project", project))
//                    .add(Restrictions.eq("product", product)).list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectHistory> histories = new ArrayList<DefectHistory>();
//
//            Set<String> currentVersions = getVersions(defect);
//            Set<String> currentToBeFixedVersion = getToBeFixedVersions(defect);
//            String currentStatus = defect.getStatus();
//            String currentSeverity = defect.getSeverity();
//
//
//            for (DefectAuditTrial auditTrial : defect.getAuditTrials()) {
//                Date date = auditTrial.getDate();
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//
//                DefectHistory dh = new DefectHistory();
//
//                dh.setDefectId(defect.getId());
//                dh.setDate(date);
//                dh.setStatus(currentStatus);
//                dh.setSeverity(currentSeverity);
//                dh.getToBeFixed().addAll(currentToBeFixedVersion);
//                dh.getVersion().addAll(currentVersions);
//
//                histories.add(dh);
//
//
//                if (AF_TO_BE_FIXED.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentToBeFixedVersion.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentToBeFixedVersion.remove(newValue);
//                    }
//                } else if (AF_VERSION.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentVersions.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentVersions.remove(newValue);
//                    }
//                } else if (AF_STATUS.equals(field)) {
//                    currentStatus = oldValue;
//                } else if (AF_SEVERITY.equals(field)) {
//                    currentSeverity = oldValue;
//                }
//            }
//
//
//            if (!currentStatus.equals("N")) {
//                log.error("status should be N, but " + currentStatus);
//            }
//
//            /**
//             * should be new status
//             */
//            DefectHistory dh = new DefectHistory();
//
//            dh.setDefectId(defect.getId());
//            dh.setDate(defect.getSTime());
//            dh.setStatus(currentStatus);
//            dh.setSeverity(currentSeverity);
//            dh.getToBeFixed().addAll(currentToBeFixedVersion);
//            dh.getVersion().addAll(currentVersions);
//
//            histories.add(dh);
//
//
//            Collections.reverse(histories);
//
//
//            int cnt = 0;
//            for (DefectHistory history : histories) {
//                if (cnt == 0) {
//                    if (ClosedStatus.contains(history.getStatus())) continue;
//                    if (!targetSeverities.isEmpty() && !targetSeverities.contains(history.getSeverity())) continue;
//                    if (!targetVersions.isEmpty() && !isInterSet(history.getVersion(), targetVersions)) continue;
//                    if (!targetToBeFixedVersions.isEmpty() && !isInterSet(history.getToBeFixed(), targetToBeFixedVersions))
//                        continue;
//
//                    int idx = daySpan(history.getDate(), minDate);
//                    openedDefects[idx]++;
//                    cnt = 1;
//                } else if (cnt == 1) {
//                    if (!targetSeverities.isEmpty() && !targetSeverities.contains(history.getSeverity())
//                            || ClosedStatus.contains(history.getStatus())
//                            || !targetVersions.isEmpty() && !isInterSet(history.getVersion(), targetVersions)
//                            || !targetToBeFixedVersions.isEmpty() && !isInterSet(history.getToBeFixed(), targetToBeFixedVersions)) {
//                        int idx = daySpan(history.getDate(), minDate);
//                        openedDefects[idx]--;
//                        cnt = 0;
//                    }
//                }
//            }
//
//        }
//
//        if (session != null) session.close();
//    }
//
//    public synchronized void accumulate3(Set<String> targetVersions, Set<String> targetToBeFixedVersions, Set<String> targetSeverities) throws DateParseException {
//        getTimeBounds();
//        int days = daySpan(maxDate, minDate) + 1;
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
////            Criteria criteria = session.createCriteria(Defect.class, "de");
////            criteria.createAlias("de.auditTrials", "audit");
////            criteria.add(Restrictions.eq("project", project));
////            criteria.add(Restrictions.eq("product", product));
////            criteria.add(Restrictions.or(Restrictions.eq("audit.oldValue", "3.0"), Restrictions.eq("audit.newValue", "3.0")));
////            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
////            defects = criteria.list();
////
////
////            System.out.println(">>> " + defects.size() + " <<<");
//
//            defects = session.createCriteria(Defect.class, "de")
//                    .add(Restrictions.eq("project", project))
//                    .add(Restrictions.eq("product", product)).list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectHistory> histories = new ArrayList<DefectHistory>();
//
//            Set<String> currentVersions = getVersions(defect);
//            Set<String> currentToBeFixedVersion = getToBeFixedVersions(defect);
//            String currentStatus = defect.getStatus();
//            String currentSeverity = defect.getSeverity();
//
//
//            for (DefectAuditTrial auditTrial : defect.getAuditTrials()) {
//                Date date = auditTrial.getDate();
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//
//                DefectHistory dh = new DefectHistory();
//
//                dh.setDefectId(defect.getId());
//                dh.setDate(date);
//                dh.setStatus(currentStatus);
//                dh.setSeverity(currentSeverity);
//                dh.getToBeFixed().addAll(currentToBeFixedVersion);
//                dh.getVersion().addAll(currentVersions);
//
//                histories.add(dh);
//
//
//                if (AF_TO_BE_FIXED.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentToBeFixedVersion.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentToBeFixedVersion.remove(newValue);
//                    }
//                } else if (AF_VERSION.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentVersions.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentVersions.remove(newValue);
//                    }
//                } else if (AF_STATUS.equals(field)) {
//                    currentStatus = oldValue;
//                } else if (AF_SEVERITY.equals(field)) {
//                    currentSeverity = oldValue;
//                }
//            }
//
//
//            if (!currentStatus.equals("N")) {
//                log.error("status should be N, but " + currentStatus);
//            }
//
//            /**
//             * should be new status
//             */
//            DefectHistory dh = new DefectHistory();
//
//            dh.setDefectId(defect.getId());
//            dh.setDate(defect.getSTime());
//            dh.setStatus(currentStatus);
//            dh.setSeverity(currentSeverity);
//            dh.getToBeFixed().addAll(currentToBeFixedVersion);
//            dh.getVersion().addAll(currentVersions);
//
//            histories.add(dh);
//
//
//            Collections.reverse(histories);
//
//
//            int cnt = 0;
//            for (DefectHistory history : histories) {
//                if (cnt == 0) {
//                    if (ClosedStatus.contains(history.getStatus())) continue;
//                    if (!targetSeverities.contains(history.getSeverity())) continue;
//                    if (!isInterSet(history.getVersion(), targetVersions)) continue;
//                    if (!isInterSet(history.getToBeFixed(), targetToBeFixedVersions))
//                        continue;
//
//                    int idx = daySpan(history.getDate(), minDate);
//                    openedDefects[idx]++;
//                    cnt = 1;
//                } else if (cnt == 1) {
//                    if (!targetSeverities.contains(history.getSeverity())
//                            || ClosedStatus.contains(history.getStatus())
//                            ||  !isInterSet(history.getVersion(), targetVersions)
//                            ||  !isInterSet(history.getToBeFixed(), targetToBeFixedVersions)) {
//                        int idx = daySpan(history.getDate(), minDate);
//                        openedDefects[idx]--;
//                        cnt = 0;
//                    }
//                }
//            }
//
//        }
//
//        if (session != null) session.close();
//    }
//
//
//    public synchronized void accumulateByToBeFixedVersions(Set<String> targetToBeFixedVersions) throws DateParseException {
//        getTimeBounds();
//        int days = daySpan(maxDate, minDate) + 1;
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
//            Criteria criteria = session.createCriteria(Defect.class, "de");
//            criteria.createAlias("de.auditTrials", "audit");
//            criteria.add(Restrictions.eq("project", project));
//            criteria.add(Restrictions.eq("product", product));
//            criteria.add(
//                    Restrictions.or(
//                            Restrictions.in("audit.oldValue", targetToBeFixedVersions),
//                            Restrictions.in("audit.newValue", targetToBeFixedVersions)
//                    )
//            );
//            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//            defects = criteria.list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectHistory> histories = new ArrayList<DefectHistory>();
//
//            Set<String> currentVersions = getVersions(defect);
//            Set<String> currentToBeFixedVersion = getToBeFixedVersions(defect);
//            String currentStatus = defect.getStatus();
//            String currentSeverity = defect.getSeverity();
//
//
//            for (DefectAuditTrial auditTrial : defect.getAuditTrials()) {
//                Date date = auditTrial.getDate();
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//
//                DefectHistory dh = new DefectHistory();
//
//                dh.setDefectId(defect.getId());
//                dh.setDate(date);
//                dh.setStatus(currentStatus);
//                dh.getToBeFixed().addAll(currentToBeFixedVersion);
//
//                histories.add(dh);
//
//
//                if (AF_TO_BE_FIXED.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentToBeFixedVersion.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentToBeFixedVersion.remove(newValue);
//                    }
//                } else if (AF_STATUS.equals(field)) {
//                    currentStatus = oldValue;
//                }
//            }
//
//
//            if (!currentStatus.equals("N")) {
//                log.error("status should be N, but " + currentStatus);
//            }
//
//            /**
//             * should be new status
//             */
//            DefectHistory dh = new DefectHistory();
//
//            dh.setDefectId(defect.getId());
//            dh.setDate(defect.getSTime());
//            dh.setStatus(currentStatus);
//            dh.getToBeFixed().addAll(currentToBeFixedVersion);
//
//            histories.add(dh);
//
//
//            Collections.reverse(histories);
//
//
//            int cnt = 0;
//            for (DefectHistory history : histories) {
//                if (cnt == 0) {
//                    if (ClosedStatus.contains(history.getStatus())) continue;
//                    if (!isInterSet(history.getToBeFixed(), targetToBeFixedVersions)) continue;
//
//                    int idx = daySpan(history.getDate(), minDate);
//                    openedDefects[idx]++;
//                    cnt = 1;
//                } else if (cnt == 1) {
//                    if (ClosedStatus.contains(history.getStatus())
//                            || !isInterSet(history.getToBeFixed(), targetToBeFixedVersions)) {
//                        int idx = daySpan(history.getDate(), minDate);
//                        openedDefects[idx]--;
//                        cnt = 0;
//                    }
//                }
//            }
//
//        }
//
//        if (session != null) session.close();
//    }
//
//
//    public synchronized void accumulateByVersion(Set<String> targetVersions) throws DateParseException {
//        getTimeBounds();
//        int days = daySpan(maxDate, minDate) + 1;
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
//            Criteria criteria = session.createCriteria(Defect.class, "de");
//            criteria.createAlias("de.auditTrials", "audit");
//            criteria.createAlias("de.versions", "version");
//            criteria.add(Restrictions.eq("project", project));
//            criteria.add(Restrictions.eq("product", product));
//            criteria.add(
//                    Restrictions.or(
//                            Restrictions.or(
//                                    Restrictions.in("audit.oldValue", targetVersions),
//                                    Restrictions.in("audit.newValue", targetVersions)
//                            ),
//                            Restrictions.in("version.id.version", targetVersions)
//                    )
//            );
//            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//            defects = criteria.list();
//
//
////            defects = session.createCriteria(Defect.class, "de")
////                    .add(Restrictions.eq("project", project))
////                    .add(Restrictions.eq("product", product)).list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectHistory> histories = new ArrayList<DefectHistory>();
//
//            Set<String> currentVersions = getVersions(defect);
//            Set<String> currentToBeFixedVersion = getToBeFixedVersions(defect);
//            String currentStatus = defect.getStatus();
//            String currentSeverity = defect.getSeverity();
//
//
//            for (DefectAuditTrial auditTrial : defect.getAuditTrials()) {
//                Date date = auditTrial.getDate();
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//
//                DefectHistory dh = new DefectHistory();
//
//                dh.setDefectId(defect.getId());
//                dh.setDate(date);
//                dh.setStatus(currentStatus);
//                dh.getVersion().addAll(currentVersions);
//
//                histories.add(dh);
//
//
//                if (AF_VERSION.equals(field)) {
//                    if (OP_DELETE.equals(op)) {
//                        currentVersions.add(oldValue);
//                    } else if (OP_NEW.equals(op)) {
//                        currentVersions.remove(newValue);
//                    }
//                } else if (AF_STATUS.equals(field)) {
//                    currentStatus = oldValue;
//                }
//            }
//
//
//            if (!currentStatus.equals("N")) {
//                log.error("status should be N, but " + currentStatus);
//            }
//
//            /**
//             * should be new status
//             */
//            DefectHistory dh = new DefectHistory();
//
//            dh.setDefectId(defect.getId());
//            dh.setDate(defect.getSTime());
//            dh.setStatus(currentStatus);
//            dh.getVersion().addAll(currentVersions);
//
//            histories.add(dh);
//
//
//            Collections.reverse(histories);
//
//
//            int cnt = 0;
//            for (DefectHistory history : histories) {
//                if (cnt == 0) {
//                    if (ClosedStatus.contains(history.getStatus())) continue;
//                    if (!isInterSet(history.getVersion(), targetVersions)) continue;
//
//                    int idx = daySpan(history.getDate(), minDate);
//                    openedDefects[idx]++;
//                    cnt = 1;
//                } else if (cnt == 1) {
//                    if (ClosedStatus.contains(history.getStatus()) || !isInterSet(history.getVersion(), targetVersions)) {
//                        int idx = daySpan(history.getDate(), minDate);
//                        openedDefects[idx]--;
//                        cnt = 0;
//                    }
//                }
//            }
//
//        }
//
//        if (session != null) session.close();
//    }
//
//
//    public synchronized void accumulateBySeverity(Set<String> targetSeverities) throws DateParseException {
//        getTimeBounds();
//        int days = daySpan(maxDate, minDate) + 1;
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
//            Criteria criteria = session.createCriteria(Defect.class, "de");
//            criteria.createAlias("de.auditTrials", "audit");
//            criteria.add(Restrictions.eq("project", project));
//            criteria.add(Restrictions.eq("product", product));
//            criteria.add(
//                    Restrictions.or(
//                            Restrictions.or(
//                                    Restrictions.in("audit.oldValue", targetSeverities),
//                                    Restrictions.in("audit.newValue", targetSeverities)
//                            ),
//                            Restrictions.in("de.severity", targetSeverities)
//                    )
//            );
//            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//            defects = criteria.list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectHistory> histories = new ArrayList<DefectHistory>();
//
//            Set<String> currentVersions = getVersions(defect);
//            Set<String> currentToBeFixedVersion = getToBeFixedVersions(defect);
//            String currentStatus = defect.getStatus();
//            String currentSeverity = defect.getSeverity();
//
//
//            for (DefectAuditTrial auditTrial : defect.getAuditTrials()) {
//                Date date = auditTrial.getDate();
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//
//                DefectHistory dh = new DefectHistory();
//
//                dh.setDefectId(defect.getId());
//                dh.setDate(date);
//                dh.setStatus(currentStatus);
//                dh.setSeverity(currentSeverity);
//
//                histories.add(dh);
//
//                if (AF_STATUS.equals(field)) {
//                    currentStatus = oldValue;
//                } else if (AF_SEVERITY.equals(field)) {
//                    currentSeverity = oldValue;
//                }
//            }
//
//
//            if (!currentStatus.equals("N")) {
//                log.error("status should be N, but " + currentStatus);
//            }
//
//            /**
//             * should be new status
//             */
//            DefectHistory dh = new DefectHistory();
//
//            dh.setDefectId(defect.getId());
//            dh.setDate(defect.getSTime());
//            dh.setStatus(currentStatus);
//            dh.setSeverity(currentSeverity);
//
//            histories.add(dh);
//
//
//            Collections.reverse(histories);
//
//
//            int cnt = 0;
//            for (DefectHistory history : histories) {
//                if (cnt == 0) {
//                    if (ClosedStatus.contains(history.getStatus())) continue;
//                    if (!targetSeverities.contains(history.getSeverity())) continue;
//
//                    int idx = daySpan(history.getDate(), minDate);
//                    openedDefects[idx]++;
//                    cnt = 1;
//                } else if (cnt == 1) {
//                    if (!targetSeverities.contains(history.getSeverity()) || ClosedStatus.contains(history.getStatus())) {
//                        int idx = daySpan(history.getDate(), minDate);
//                        openedDefects[idx]--;
//                        cnt = 0;
//                    }
//                }
//            }
//
//        }
//
//        if (session != null) session.close();
//    }
//
//
//    public void accumulate2() throws DateParseException {
//        getTimeBounds();
//
//        int days = daySpan(maxDate, minDate) + 1;
//
//        openedDefects = new int[days];
//
//
//        Session session = null;
//
//        List<Defect> defects = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//
//            defects = session.createCriteria(Defect.class)
//                    .add(Restrictions.eq("project", project))
//                    .add(Restrictions.eq("product", product))
//                    .list();
//        } catch (HibernateException e) {
//            if (session != null) session.close();
//            throw new GeneralException(e);
//        }
//
//
//        for (Defect defect : defects) {
//            List<DefectAuditTrial> auditTrials = defect.getAuditTrials();
//
//            boolean toBeFixed20 = false;
//            int cnt = 0;
//            String currentStatus = "N";
//
//            for (DefectAuditTrial auditTrial : auditTrials) {
//                int idx = daySpan(auditTrial.getDate(), minDate);
//
//                String field = auditTrial.getField();
//                String op = auditTrial.getOperation();
//                String newValue = auditTrial.getNewValue();
//                String oldValue = auditTrial.getOldValue();
//
//                if ("CSCui55301".equals(defect.getId())) {
//                    Date name = defect.getHTime();
//                    System.out.println(name);
//                }
//
//
//                if (AF_TO_BE_FIXED.equals(field)) {
//                    if (OpenedStatus.contains(currentStatus) && OP_DELETE.equals(op) && "2.0".equals(oldValue)) {
//                        openedDefects[idx]--;
//                        toBeFixed20 = false;
//                        cnt--;
//                    } else if (OpenedStatus.contains(currentStatus) && OP_NEW.equals(op) && "2.0".equals(newValue)) {
//                        openedDefects[idx]++;
//                        toBeFixed20 = true;
//                        cnt++;
//                    }
//                } else if (AF_STATUS.equals(field)) {
//
//                    currentStatus = newValue;
//
//                    if (toBeFixed20) {
//                        if (OP_NEW.equals(op) && OpenedStatus.contains(newValue)) {
//                            openedDefects[idx]++;
//                            cnt++;
//                        } else if (OP_MODIFY.equals(op)) {
//                            if (ClosedStatus.contains(newValue) && OpenedStatus.contains(oldValue)) {
//                                openedDefects[idx]--;
//                                cnt--;
//                            } else if (ClosedStatus.contains(oldValue) && OpenedStatus.contains(newValue)) {
//                                openedDefects[idx]++;
//                                cnt++;
//                            }
//
//                        }
//                    }
//                }
//            }
//
//
//            System.out.println(defect.getId() + ": " + cnt);
//        }
//
//        if (session != null) session.close();
//    }
//
//    public int[] getOpenedDefects() {
//        return openedDefects;
//    }
//
//    public Date getMinDate() {
//        return minDate;
//    }
//
//    public void showAccumulation() {
//        int total = 0;
//
//        for (int i = 0; i < openedDefects.length; i++) {
//            total += openedDefects[i];
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(i * millisecondsInOneDay + minDate.getTime());
//            String dateStr = DateUtils.formatDate(cal.getTime(), "yyyy-MM-dd");
//            System.out.println(dateStr + "\t" + total);
//        }
//    }
//
//    private Set<String> getVersions(Defect defect) {
//        Set<String> set = new HashSet<String>();
//        Set<DefectVersion> versions = defect.getVersions();
//        for (DefectVersion version : versions) {
//            set.add(version.getId().getVersion());
//        }
//        return set;
//    }
//
//    private Set<String> getToBeFixedVersions(Defect defect) {
//        Set<String> set = new HashSet<String>();
//        Set<DefectToBeFixedVersion> versions = defect.getToBeFixedVersions();
//        for (DefectToBeFixedVersion version : versions) {
//            set.add(version.getId().getVersion());
//        }
//        return set;
//    }
//
//    private boolean isInterSet(Set<String> setA, Set<String> setB) {
//        if (setA.isEmpty() || setB.isEmpty()) return true;
//
//        for (String s : setA) {
//            if (setB.contains(s)) return true;
//        }
//
//        return false;
//    }
//
//    private void getTimeBounds() throws DateParseException {
//        Object[] timeBounds = DbUtils.getTimeBounds(project, product);
//
//        maxDate = DateUtils.parseDate(timeBounds[0].toString(), ptns);
//        minDate = DateUtils.parseDate(timeBounds[1].toString(), ptns);
//    }
//
//    private int daySpan(Date max, Date min) {
//        long ts = max.getTime() - min.getTime();
//        return (int) (ts / millisecondsInOneDay);
//    }


}

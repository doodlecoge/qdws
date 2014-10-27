package com.cisex.model;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-30
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public enum AuditTrialField {
    STATUS("Status"),
    VERSION("Version"),
    TO_BE_FIXED("To-be-fixed"),
    SEVERITY("Severity-desc");

    public String val;

    AuditTrialField(String val) {
        this.val = val;
    }
}

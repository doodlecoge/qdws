package com.cisex.model;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/3/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class SeverityStatusCount {
    private String severity;
    private String status;
    private int cnt;

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}

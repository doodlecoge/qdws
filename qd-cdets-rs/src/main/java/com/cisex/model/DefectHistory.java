package com.cisex.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-9-2
 * Time: 下午3:13
 * To change this template use File | Settings | File Templates.
 */
public class DefectHistory {
    private String defectId;
    private Date date;
    private Set<String> version = new HashSet<String>();
    private Set<String> toBeFixed = new HashSet<String>();
    private String status;
    private String severity;

    public String getDefectId() {
        return defectId;
    }

    public void setDefectId(String defectId) {
        this.defectId = defectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<String> getVersion() {
        return version;
    }

    public void setVersion(Set<String> version) {
        this.version = version;
    }

    public Set<String> getToBeFixed() {
        return toBeFixed;
    }

    public void setToBeFixed(Set<String> toBeFixed) {
        this.toBeFixed = toBeFixed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }
}

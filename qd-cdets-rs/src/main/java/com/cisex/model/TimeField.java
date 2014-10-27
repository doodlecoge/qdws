package com.cisex.model;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/28/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TimeField {

    A("Assigned Date",  "ATime"),
    C("Closed-on",      "CTime"),
    D("Duplicate-on",   "DTime"),
    F("Forwarded-on",   "FTime"),
    H("Held-on",        "HTime"),
    I("Info-req-on",    "ITime"),
    J("Junked-on",      "JTime"),
    M("More-on",        "MTime"),
    N("New-on",         "NTime"),
    O("Opened-on",      "OTime"),
    P("Postponed-on",   "PTime"),
    R("Resolved-on",    "RTime"),
    S("Submitted-on",   "STime"),
    U("Unreproducible", "UTime"),
    V("Verified-on",    "VTime"),
    W("Waiting-on",     "WTime"),
    SYS_LAST_UPDATED("Sys-Last-Updated", "sys_last_updated_time");


    public String cdetsFieldName;
    public String dbColName;

    TimeField(String cdetsFieldName, String dbColName) {
        this.cdetsFieldName = cdetsFieldName;
        this.dbColName = dbColName;
    }
}

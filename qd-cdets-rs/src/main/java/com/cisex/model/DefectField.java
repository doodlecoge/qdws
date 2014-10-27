package com.cisex.model;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/28/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public enum DefectField {
    ID          ("Identifier",  "id"),
    PROJECT     ("Project",     "project"),
    PRODUCT     ("Product",     "product"),
    STATUS      ("Status",      "status"),
    SEVERITY    ("Severity",    "severity"),
    DE_MANAGER  ("DE-manager",  "de_manager"),
    ENGINEER    ("Engineer",    "engineer"),
    SUBMITTER   ("Submitter",   "submitter"),
    COMPONENT   ("Component",   "component"),
    VERSION     ("Version",     "version"),
    TO_BE_FIXED ("To-be-fixed", "to_be_fixed"),
    FOUND       ("Found",       "found"),
    KEYWORD     ("Keyword",     "keyword"),
    ATTRIBUTE   ("Attribute",   "attribute");

    public String cdetsFieldName;
    public String dbColName;

    DefectField(String cdetsFieldName, String dbColName) {
        this.cdetsFieldName = cdetsFieldName;
        this.dbColName = dbColName;
    }
}

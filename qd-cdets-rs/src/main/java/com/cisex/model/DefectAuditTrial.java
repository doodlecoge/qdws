package com.cisex.model;

import com.cisex.annotaion.CdetsDumpXmlFild;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-8-28
 * Time: 下午1:34
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "defects_audit_trials")
@org.hibernate.annotations.Entity(
        dynamicUpdate = true
)
public class DefectAuditTrial {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "defect_id")
    private String defectId;

    @Column(name = "date")
    private Date date;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "field")
    private String field;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "operation")
    private String operation;


    @Override
    public int hashCode() {
        return (defectId + date.getTime() + newValue).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefectId() {
        return defectId;
    }

    public void setDefectId(String defectId) {
        this.defectId = defectId;
    }

    public Date getDate() {
        return date;
    }

    @CdetsDumpXmlFild(value = "Date")
    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    @CdetsDumpXmlFild(value = "EmployeeLogin")
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getField() {
        return field;
    }

    @CdetsDumpXmlFild(value = "Field")
    public void setField(String field) {
        this.field = field;
    }

    public String getOldValue() {
        return oldValue;
    }

    @CdetsDumpXmlFild(value = "OldValue")
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    @CdetsDumpXmlFild(value = "NewValue")
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOperation() {
        return operation;
    }

    @CdetsDumpXmlFild(value = "Operation2")
    public void setOperation(String operation) {
        this.operation = operation;
    }
}

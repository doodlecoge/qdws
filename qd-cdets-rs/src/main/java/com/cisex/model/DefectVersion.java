package com.cisex.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/24/13
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "defects_versions")
public class DefectVersion {
    @EmbeddedId
    private DefectVersionPK id = new DefectVersionPK();

//    @ManyToOne
//    @JoinColumn(name = "defect_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Defect defect;

    public DefectVersionPK getId() {
        return id;
    }

    public void setId(DefectVersionPK id) {
        this.id = id;
    }

//    public Defect getDefect() {
//        return defect;
//    }
//
//    public void setDefect(Defect defect) {
//        this.defect = defect;
//    }
}

package com.cisex.model;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/24/13
 * Time: 2:53 PMproducts
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "defects_to_be_fixed_versions")
public class DefectToBeFixedVersion {
    @EmbeddedId
    private DefectToBeFixedVersionPK id = new DefectToBeFixedVersionPK();

//    @ManyToOne
//    @JoinColumn(name = "defect_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Defect defect;

    public DefectToBeFixedVersionPK getId() {
        return id;
    }

    public void setId(DefectToBeFixedVersionPK id) {
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

package com.cisex.model;


import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hch
 * Date: 13-6-30
 * Time: 下午9:11
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "defects")
@org.hibernate.annotations.Entity(
        dynamicUpdate = true
)
public class Defect {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "project")
    private String project;

    @Column(name = "product")
    private String product;

    @Column(name = "component")
    private String component;

    @Column(name = "severity")
    private String severity;

    @Column(name = "status")
    private String status;

    @Column(name = "de_manager")
    private String deManager;

    @Column(name = "engineer")
    private String engineer;

    @Column(name = "submitter")
    private String submitter;

    @Column(name = "found")
    private String found;

    @Column(name = "attribute")
    private String attribute;

    @Column(name = "ATime")
    private Date ATime;

    @Column(name = "CTime")
    private Date CTime;

    @Column(name = "DTime")
    private Date DTime;

    @Column(name = "FTime")
    private Date FTime;

    @Column(name = "HTime")
    private Date HTime;

    @Column(name = "ITime")
    private Date ITime;

    @Column(name = "JTime")
    private Date JTime;

    @Column(name = "MTime")
    private Date MTime;

    @Column(name = "NTime")
    private Date NTime;

    @Column(name = "OTime")
    private Date OTime;

    @Column(name = "PTime")
    private Date PTime;

    @Column(name = "RTime")
    private Date RTime;

    @Column(name = "STime")
    private Date STime;

    @Column(name = "UTime")
    private Date UTime;

    @Column(name = "VTime")
    private Date VTime;

    @Column(name = "WTime")
    private Date WTime;

    @Column(name = "sys_last_updated_time")
    private Date lastUpdatedTime;

    @Column(name = "stale")
    private int stale;

//    @Column(name = "audit_updated_time")
//    private Date auditUpdatedTime;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", referencedColumnName = "id", updatable = false)
    @BatchSize(size = 100)
    private Set<DefectVersion> versions = new HashSet<DefectVersion>();


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", referencedColumnName = "id", updatable = false)
    private Set<DefectToBeFixedVersion> toBeFixedVersions =
            new HashSet<DefectToBeFixedVersion>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", referencedColumnName = "id", updatable = false)
    private Set<Keyword> keywords = new HashSet<Keyword>();

//    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
//    @JoinColumn(name = "defect_id", referencedColumnName = "id")
////    @Fetch(FetchMode.JOIN)
//    @BatchSize(size = 100)
//    @OrderBy("date desc, id desc")
//    private List<DefectAuditTrial> auditTrials = new ArrayList<DefectAuditTrial>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

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

    public String getDeManager() {
        return deManager;
    }

    public void setDeManager(String deManager) {
        this.deManager = deManager;
    }

    public Date getATime() {
        return ATime;
    }

    public void setATime(Date ATime) {
        this.ATime = ATime;
    }

    public Date getUTime() {
        return UTime;
    }

    public void setUTime(Date UTime) {
        this.UTime = UTime;
    }

    public String getDe_manager() {
        return deManager;
    }

    public void setDe_manager(String de_manager) {
        this.deManager = de_manager;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Date getCTime() {
        return CTime;
    }

    public void setCTime(Date CTime) {
        this.CTime = CTime;
    }

    public Date getDTime() {
        return DTime;
    }

    public void setDTime(Date DTime) {
        this.DTime = DTime;
    }

    public Date getFTime() {
        return FTime;
    }

    public void setFTime(Date FTime) {
        this.FTime = FTime;
    }

    public Date getHTime() {
        return HTime;
    }

    public void setHTime(Date HTime) {
        this.HTime = HTime;
    }

    public Date getITime() {
        return ITime;
    }

    public void setITime(Date ITime) {
        this.ITime = ITime;
    }

    public Date getJTime() {
        return JTime;
    }

    public void setJTime(Date JTime) {
        this.JTime = JTime;
    }

    public Date getMTime() {
        return MTime;
    }

    public void setMTime(Date MTime) {
        this.MTime = MTime;
    }

    public Date getNTime() {
        return NTime;
    }

    public void setNTime(Date NTime) {
        this.NTime = NTime;
    }

    public Date getOTime() {
        return OTime;
    }

    public void setOTime(Date OTime) {
        this.OTime = OTime;
    }

    public Date getPTime() {
        return PTime;
    }

    public void setPTime(Date PTime) {
        this.PTime = PTime;
    }

    public Date getRTime() {
        return RTime;
    }

    public void setRTime(Date RTime) {
        this.RTime = RTime;
    }

    public Date getSTime() {
        return STime;
    }

    public void setSTime(Date STime) {
        this.STime = STime;
    }

    public Date getVTime() {
        return VTime;
    }

    public void setVTime(Date VTime) {
        this.VTime = VTime;
    }

    public Date getWTime() {
        return WTime;
    }

    public void setWTime(Date WTime) {
        this.WTime = WTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }


    public Date getSys_last_updated_time() {
        return lastUpdatedTime;
    }

    public void setSys_last_updated_time(Date sys_last_updated_time) {
        this.lastUpdatedTime = sys_last_updated_time;
    }

    public int getStale() {
        return stale;
    }

    public void setStale(int stale) {
        this.stale = stale;
    }

    public Set<DefectVersion> getVersions() {
        return versions;
    }

    public void setVersions(Set<DefectVersion> versions) {
        this.versions = versions;
    }

    public void setVersions(String versionString) {
        Set<String> versions = splitToSet(versionString);
        Set<DefectVersion> versionSet = new HashSet<DefectVersion>();
        for (String version : versions) {
            DefectVersionPK id = new DefectVersionPK();
            id.setId(this.id);
            id.setVersion(version);

            DefectVersion defectVersion = new DefectVersion();
            defectVersion.setId(id);
            versionSet.add(defectVersion);
        }
        this.versions = versionSet;
    }

    public Set<DefectToBeFixedVersion> getToBeFixedVersions() {
        return toBeFixedVersions;
    }

    public void setToBeFixedVersions(Set<DefectToBeFixedVersion> toBeFixedVersions) {
        this.toBeFixedVersions = toBeFixedVersions;
    }

    public void setToBeFixedVersions(String versionString) {
        Set<String> toBeFixedVersions = splitToSet(versionString);


        Set<DefectToBeFixedVersion> toBeFixedVersionSet = new HashSet<DefectToBeFixedVersion>();
        for (String version : toBeFixedVersions) {
            DefectToBeFixedVersionPK id = new DefectToBeFixedVersionPK();
            id.setId(this.id);
            id.setVersion(version);

            DefectToBeFixedVersion defectToBeFixedVersion = new DefectToBeFixedVersion();
            defectToBeFixedVersion.setId(id);
            toBeFixedVersionSet.add(defectToBeFixedVersion);
        }
        this.toBeFixedVersions = toBeFixedVersionSet;
    }


    private Set<String> splitToSet(String versionString) {
        String[] versions = versionString.split(",");
        String[] version2 = versionString.toLowerCase().split(",");

        Set<String> set = new HashSet<String>();
        Set<String> se2 = new HashSet<String>();

        int len = version2.length;

        for (int i = 0; i < len; i++) {
            String version = version2[i].trim();

            if (version.length() == 0) continue;
            if (se2.contains(version)) continue;

            se2.add(version);
            set.add(versions[i]);
        }

        return set;
    }

    public Set<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void setKeywords(String string) {
        Set<String> set = splitToSet(string);

        Set<Keyword> keywords = new HashSet<Keyword>();

        for (String keywordString : set) {
            KeywordPK pk = new KeywordPK();
            pk.setDefectId(this.getId());
            pk.setKeyword(keywordString);

            Keyword keyword = new Keyword();
            keyword.setId(pk);

            keywords.add(keyword);
        }

        this.keywords = keywords;
    }


    //    public List<DefectAuditTrial> getAuditTrials() {
//        return auditTrials;
//    }
//
//    public void setAuditTrials(List<DefectAuditTrial> auditTrials) {
//        this.auditTrials = auditTrials;
//    }

//    public Date getAuditUpdatedTime() {
//        return auditUpdatedTime;
//    }
//
//    public void setAuditUpdatedTime(Date auditUpdatedTime) {
//        this.auditUpdatedTime = auditUpdatedTime;
//    }
}

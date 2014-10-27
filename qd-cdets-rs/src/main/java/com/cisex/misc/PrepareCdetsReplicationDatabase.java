package com.cisex.misc;

import com.cisex.GeneralException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/28/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class PrepareCdetsReplicationDatabase {
    private static Connection conn;
    private static final String engine = "InnoDB";

    static {
        String url = "jdbc:mysql://10.224.138.205:3306";
        String username = "cdets";
        String password = "cdets@pass";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new GeneralException(e);
        }

    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        create();
    }

    public static void create() throws SQLException, ClassNotFoundException {

        createDatabase();
        createDefectsTable();
        createProductsTable();
        createDefectsVersionsTable();
        createDefectsToBeFixedVersionsTable();
        createDefectsKeywordsTable();

    }


    public static void createDatabase() throws SQLException, ClassNotFoundException {
        String sql = "create database if not exists cdets";
        executeUpdate(sql);
    }

    public static void createDefectsTable() throws SQLException, ClassNotFoundException {
        String sql = "" +
                "CREATE TABLE if not exists cdets.defects (" +
                "id         VARCHAR(16) NOT NULL," +
                "project    VARCHAR(100) NULL," +
                "product    VARCHAR(100) NULL," +
                "component  VARCHAR(100) NULL," +
                "severity   varchar(4)   NULL," +
                "status     VARCHAR(4)   NULL," +
                "de_manager VARCHAR(16)  NULL," +
                "engineer   VARCHAR(16)  NULL," +
                "submitter  VARCHAR(16)  NULL," +
                "found      VARCHAR(32)  NULL," +
                "attribute  VARCHAR(32)  NULL," +

                "ATime  datetime NULL," +
                "CTime  datetime NULL," +
                "DTime  datetime NULL," +
                "FTime  datetime NULL," +
                "HTime  datetime NULL," +
                "ITime  datetime NULL," +
                "JTime  datetime NULL," +
                "MTime  datetime NULL," +
                "NTime  datetime NULL," +
                "OTime  datetime NULL," +
                "PTime  datetime NULL," +
                "RTime  datetime NULL," +
                "STime  datetime NULL," +
                "UTime  datetime NULL," +
                "VTime  datetime NULL," +
                "WTime  datetime NULL," +

                "sys_last_updated_time     datetime NULL," +
                "stale                     tinyint  0," +

                "primary key (id)," +
                "unique index uidx_id (id asc)," +

                "index idx_id           (id         ASC)," +
                "index idx_project      (project    ASC)," +
                "index idx_product      (product    ASC)," +
                "index idx_component    (component  ASC)," +
                "index idx_severity     (severity   ASC)," +
                "index idx_status       (status     ASC)," +
                "index idx_de_manager   (de_manager ASC)," +
                "index idx_engineer     (engineer   ASC)," +
                "index idx_submitter    (submitter  ASC)," +
                "index idx_found        (found      ASC)," +
                "index idx_attribute    (attribute  ASC)," +

                "index idx_ATime        (ATime      ASC)," +
                "index idx_CTime        (CTime      ASC)," +
                "index idx_DTime        (DTime      ASC)," +
                "index idx_FTime        (FTime      ASC)," +
                "index idx_HTime        (HTime      ASC)," +
                "index idx_ITime        (ITime      ASC)," +
                "index idx_JTime        (JTime      ASC)," +
                "index idx_MTime        (MTime      ASC)," +
                "index idx_NTime        (NTime      ASC)," +
                "index idx_OTime        (OTime      ASC)," +
                "index idx_PTime        (PTime      ASC)," +
                "index idx_RTime        (RTime      ASC)," +
                "index idx_STime        (STime      ASC)," +
                "index idx_UTime        (UTime      ASC)," +
                "index idx_VTime        (VTime      ASC)," +
                "index idx_WTime        (WTime      ASC)," +
                "index idx_sys_last_updated_time  (sys_last_updated_time      ASC)," +
                "index idx_stale        (stale      ASC)" +
                ") ENGINE=" + engine;

        executeUpdate(sql);
    }


    public static void createProductsTable() throws SQLException, ClassNotFoundException {
        String sql = "" +
                "CREATE TABLE IF NOT EXISTS `cdets`.`products` (" +
                "`project`                VARCHAR(32) NOT NULL," +
                "`product`                VARCHAR(32) NOT NULL," +
                "`last_update_time`       DATETIME        NULL," +
                "`last_health_check_time` DATETIME        NULL," +

                "PRIMARY KEY (`project`, `product`)," +

                "INDEX `idx_last_update_time`         (`last_update_time`       ASC)," +
                "INDEX `idx_last_health_check_time`   (`last_health_check_time` ASC)" +

                ") ENGINE=" + engine;


        executeUpdate(sql);
    }


    public static void createDefectsVersionsTable() throws SQLException, ClassNotFoundException {
        String sql = "" +
                "CREATE  TABLE IF NOT EXISTS `cdets`.`defects_versions` ( " +
                " `defect_id`   VARCHAR(16)     NOT NULL , " +
                " `version`     VARCHAR(32)     NOT NULL , " +

                " UNIQUE INDEX `unique` (`defect_id` ASC, `version` ASC) , " +

                " INDEX `idx_defect_id`     (`defect_id`       ASC) , " +
                " INDEX `idx_version`       (`version`      ASC) , " +
                " INDEX `fk`                (`defect_id`       ASC) , " +

                " CONSTRAINT `fk_version` " +
                "   FOREIGN KEY (`defect_id` ) " +
                "   REFERENCES `cdets`.`defects` (`id` ) " +
                "   ON DELETE CASCADE " +
                "   ON UPDATE CASCADE" +
                ") ENGINE=" + engine;

        executeUpdate(sql);
    }


    public static void createDefectsToBeFixedVersionsTable() throws SQLException, ClassNotFoundException {
        String sql = "" +
                "CREATE  TABLE IF NOT EXISTS `cdets`.`defects_to_be_fixed_versions` ( " +
                " `defect_id`   VARCHAR(16)     NOT NULL , " +
                " `version`     VARCHAR(32)     NOT NULL , " +

                " UNIQUE INDEX `unique` (`defect_id` ASC, `version` ASC) , " +

                " INDEX `idx_defect_id`     (`defect_id`    ASC) , " +
                " INDEX `idx_version`       (`version`      ASC) , " +
                " INDEX `fk`                (`defect_id`    ASC) , " +

                " CONSTRAINT `fk_to_be_fiexed_version` " +
                "   FOREIGN KEY (`defect_id` ) " +
                "   REFERENCES `cdets`.`defects` (`id` ) " +
                "   ON DELETE CASCADE " +
                "   ON UPDATE CASCADE" +
                ") ENGINE=" + engine;

        executeUpdate(sql);
    }


    public static void createDefectsKeywordsTable() throws SQLException, ClassNotFoundException {
        String sql = "" +
                "CREATE TABLE IF NOT EXISTS cdets.defects_keywords " +
                "(" +
                "   defect_id  VARCHAR(16) NOT NULL," +
                "   keyword    VARCHAR(32) NOT NULL," +

                "   UNIQUE INDEX `unique`          (`defect_id` ASC, `keyword` ASC)," +
                "          INDEX `idx_defect_id`   (`defect_id` ASC)," +
                "          INDEX `idx_keyword`     (`keyword` ASC)," +

                "   CONSTRAINT `fk_keywords` " +
                "       FOREIGN KEY (`defect_id`) " +
                "       REFERENCES `cdets`.`defects` (`id`) " +
                "       ON DELETE CASCADE" +
                "       ON UPDATE CASCADE" +

                ") ENGINE=" + engine;

        executeUpdate(sql);
    }

    public static void addColumn() throws SQLException, ClassNotFoundException {
        String sql = "ALTER TABLE `cdets`.`defects` ADD COLUMN `sys_last_updated_time` DATETIME NULL  AFTER `WTime`, ADD INDEX `idx_sys_last_updated_time` (`sys_last_updated_time` ASC)";
        executeUpdate(sql);
    }

    private static void executeUpdate(String sql) throws ClassNotFoundException, SQLException {
        Statement stmt = conn.createStatement();
        System.out.println(sql);
        stmt.executeUpdate(sql);
    }
}

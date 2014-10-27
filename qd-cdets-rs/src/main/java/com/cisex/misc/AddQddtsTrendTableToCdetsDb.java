package com.cisex.misc;

import com.cisex.GeneralException;
import com.cisex.model.QddtsTrend;
import com.cisex.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

/**
 * User: huaiwang
 * Date: 13-9-13
 * Time: 下午5:16
 */
public class AddQddtsTrendTableToCdetsDb {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            String sql = "";

//            sql = "CREATE SCHEMA if NOT EXISTS cdets";
//            session.createSQLQuery(sql).executeUpdate();


            sql = "CREATE TABLE if NOT EXISTS cdets.qddts_trends" +
                    "(" +
                    "   queryHash INT           NOT NULL PRIMARY KEY," +
                    "   query     VARCHAR(1024) NOT NULL," +
                    "   trendData TEXT          NOT NULL," +
                    "   startDate DATETIME      NOT NULL," +
                    "   endDate   DATETIME      NOT NULL," +
                    "   createdOn DATETIME          NULL," +
                    "   updatedOn DATETIME          NULL," +
                    "   visitedOn DATETIME          NULL" +
                    ")";

            session.createSQLQuery(sql).executeUpdate();



//            sql = "CREATE index if NOT EXISTS idx_queryHash " +
//                    "ON qddts.trends(queryHash)";
//
//            session.createSQLQuery(sql).executeUpdate();

//            sql = "ALTER TABLE qddts.trends ALTER COLUMN createdOn SET DEFAULT CURRENT_TIMESTAMP";
//            session.createSQLQuery(sql).executeUpdate();


            List list = session.createCriteria(QddtsTrend.class).list();
            System.out.println(list.size());

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new GeneralException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

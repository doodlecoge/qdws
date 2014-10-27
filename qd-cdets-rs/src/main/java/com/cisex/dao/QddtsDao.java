package com.cisex.dao;

import com.cisex.GeneralException;
import com.cisex.QdCdetsProperties;
import com.cisex.model.QddtsTrend;
import com.cisex.util.HibernateUtil;
import com.hch.utils.time.TimeUtils;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.List;

/**
 * User: huaiwang
 * Date: 13-9-17
 * Time: 上午9:06
 */
public class QddtsDao {
    public static List<QddtsTrend> getAllTrends() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createCriteria(QddtsTrend.class).list();
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    public static List<QddtsTrend> getAllStaleQuery() {
        Session session = null;

        long gap = TimeUtils.timeGap(QdCdetsProperties.getString("qddts.query.update.time.gap"));
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MILLISECOND, (int) -gap);

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createCriteria(QddtsTrend.class)
                    .add(Restrictions.le("updatedOn", now.getTime()))
                    .addOrder(Order.asc("visitedOn"))
                    .list();
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }
}

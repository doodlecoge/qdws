package com.cisex;

import com.cisex.model.DefectAuditTrial;
import com.cisex.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 13-9-3
 * Time: 下午2:49
 * To change this template use File | Settings | File Templates.
 */
public class HibernateTest {
    public static void main(String[] args) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            DefectAuditTrial da = (DefectAuditTrial)
                    session.createCriteria(DefectAuditTrial.class)
                            .add(Restrictions.idEq(9718)).uniqueResult();

            da.setNewValue("1.1");

            session.saveOrUpdate(da);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

    }
}

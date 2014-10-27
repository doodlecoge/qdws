package com.cisex;

import com.cisex.model.Defect;
import com.cisex.model.DefectVersion;
import com.cisex.model.DefectVersionPK;
import com.cisex.util.HibernateUtil;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: huaiwang
 * Date: 13-9-29
 * Time: 下午1:32
 */
public class QueryDefect {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();



        Set<DefectVersion> set = new HashSet<DefectVersion>();

        DefectVersionPK pk=new DefectVersionPK();
        pk.setId("CSCts27649");
        pk.setVersion("1.4");

        DefectVersion dv = new DefectVersion();
        dv.setId(pk);

        set.add(dv);


        List list = session.createCriteria(DefectVersion.class)
                .add(Restrictions.in("id.version", set))
                .list();


        System.out.println(list.size());

        session.close();
    }

    public static void queryWithRestrictionInCollection() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Defect> list = (List<Defect>) session.createCriteria(Defect.class)
                .add(Restrictions.eq("project", "CSC.csg"))
                .add(Restrictions.eq("product", "identity"))
                .createAlias("versions", "v")
                .add(Restrictions.eq("v.id.version", "3.0"))
                .list();


        System.out.println(list.size());

        session.close();
    }
}

package com.cisex.util;

import com.cisex.GeneralException;
import com.cisex.Global;
import com.cisex.QdCdetsProperties;
import com.cisex.model.Defect;
import com.cisex.model.Product;
import com.cisex.model.ProductPK;
import com.cisex.model.TimeField;
import org.apache.commons.lang.StringUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 6/18/13
 * Time: 8:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtils {
    public static final Logger log = LoggerFactory.getLogger(DbUtils.class);

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    public static void save(Object obj) {
        exec(obj, "save");
    }


    public static void update(Object obj) {
        exec(obj, "update");
    }

    public static void saveOrUpdate(Object obj) {
        exec(obj, "save_or_update");

    }

    private static void exec(Object obj, String op) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();

            if (op.equalsIgnoreCase("save"))
                session.save(obj);
            else if (op.equalsIgnoreCase("update"))
                session.update(obj);
            else if (op.equalsIgnoreCase("save_or_update"))
                session.saveOrUpdate(obj);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    public static List<Product> getStaleProducts() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Product> lst = new ArrayList<Product>();

        try {
            session.beginTransaction();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MILLISECOND, -Global.UPDATE_GAP_TIME);
            Date date = cal.getTime();

            lst = session.createCriteria(Product.class)
                    .add(Restrictions.lt("lastUpdateTime", date))
                    .addOrder(Order.asc("lastUpdateTime"))
                    .list();

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return lst;
    }

    public static Date getLatestSubmitTime(String project, String product) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Date date = null;

        try {
            session.beginTransaction();

            Object obj = session.createCriteria(Defect.class)
                    .add(Restrictions.eq("project", project))
                    .add(Restrictions.eq("product", product))
                    .add(Restrictions.eq("stale", 0))
                    .setProjection(Projections.max("STime"))
                    .uniqueResult();

//            Object obj = session.createSQLQuery("SELECT MAX(STime) FROM defects")
//                    .uniqueResult();

            date = (Date) obj;

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return date;
    }

    public static Date getLatestLastUpdatedTime(String project, String product) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        Date date = null;

        try {
            session.beginTransaction();

            Object obj = session.createCriteria(Defect.class)
                    .add(Restrictions.eq("project", project))
                    .add(Restrictions.eq("product", product))
                    .add(Restrictions.eq("stale", 0))
                    .setProjection(Projections.max("lastUpdatedTime"))
                    .uniqueResult();

            date = (Date) obj;

            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return date;
    }

    public static Product getProduct(String project, String product) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        ProductPK id = new ProductPK();
        id.setProduct(product);
        id.setProject(project);

        Product prod = null;
        try {

            prod = (Product) session.createCriteria(Product.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();

        } catch (HibernateException e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return prod;
    }


    public static boolean needUpdate(String project, String product) {
        boolean bret = false;


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MILLISECOND, -Global.UPDATE_GAP_TIME);
        Date date = cal.getTime();
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            List list = session.createCriteria(Product.class)
                    .add(Restrictions.lt("lastUpdateTime", date))
                    .addOrder(Order.asc("lastUpdateTime"))
                    .list();

            bret = list.size() > 0;
        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return bret;
    }


    public static List<Product> getAllProducts() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Product> prods = null;

        try {
            prods = session.createCriteria(Product.class).list();

        } catch (HibernateException e) {

            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return prods;
    }


    public static List<Object> getAllProductsOrderByDefectCount() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object> prods = null;
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, -1 * QdCdetsProperties.getInt("cdets.health.check.gap"));
        String s = DateUtils.formatDate(now.getTime(), "yyyy-MM-dd HH:mm:ss");

        try {
            String sql = "" +
                    " SELECT b.project,b.product" +
                    " FROM" +
                    "   (" +
                    "       SELECT project,product" +
                    "       FROM products" +
                    "       WHERE last_health_check_time is null or last_health_check_time < '" + s + "'" +
                    "   ) b " +
                    " LEFT JOIN" +
                    " (" +
                    "       SELECT project,product,COUNT(*) cnt " +
                    "       FROM defects " +
                    "       GROUP BY project,product" +
                    " ) a " +
                    " ON a.project = b.project AND a.product = b.product;";

            log.error(sql);

            prods = session.createSQLQuery(sql)
                    .addScalar("project", StandardBasicTypes.STRING)
                    .addScalar("product", StandardBasicTypes.STRING)
                    .list();

        } catch (HibernateException e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return prods;
    }

    public static List<Defect> getDefects(String project, String product) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Defect> defects = null;

        try {
            defects = session.createCriteria(Defect.class)
                    .add(Restrictions.eq("project", project))
                    .add(Restrictions.eq("product", product))
                    .add(Restrictions.eq("stale", 0))
                    .list();
        } catch (HibernateException e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return defects;
    }

    public static List<String> getDefectIds(String project, String product) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<String> defects = null;

        try {
            defects = session.createCriteria(Defect.class)
                    .add(Restrictions.eq("project", project))
                    .add(Restrictions.eq("product", product))
                    .add(Restrictions.eq("stale", 0))
                    .setProjection(Projections.property("id"))
                    .list();
        } catch (HibernateException e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }

        return defects;
    }

    public static Object[] getTimeBounds(String project, String product) {
        StringBuilder sql = new StringBuilder();

        sql.append("select max(tmax) tmax, min(tmin) tmin from (\n");

        boolean first = true;
        for (TimeField timeField : TimeField.values()) {
            if (timeField == TimeField.SYS_LAST_UPDATED) continue;

            String col = timeField.dbColName;

            if (!first) sql.append("union\n");
            else first = false;

            if (col.length() != 5) continue;
            sql.append("select max(");
            sql.append(col);
            sql.append(") tmax, min(" + col + ") tmin from defects where project='");
            sql.append(project);
            sql.append("' and product='");
            sql.append(product);
            //todo: add stale = 0 restriction
            sql.append("'\n");
        }
        sql.append(") mm;");


        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return (Object[]) session.createSQLQuery(sql.toString()).uniqueResult();
        } catch (HibernateException e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static <T> T querySingleEntity(Class<T> cls, Object id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return (T) session.createCriteria(cls)
                    .add(Restrictions.idEq(id))
                    .uniqueResult();
        } catch (Exception e) {
            log.error("=== querySingleEntity " + cls, e);
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public static int getCount(String project, String product, Date startTime, Date endTime) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria = session.createCriteria(Defect.class)
                    .add(Restrictions.eq("project", project))
                    .add(Restrictions.eq("product", product))
                    .add(Restrictions.eq("stale", 0));

            if (startTime != null) {
                criteria.add(Restrictions.ge("STime", startTime));
            }

            if (endTime != null) {
                criteria.add(Restrictions.le("STime", endTime));
            }

            Object o = criteria.setProjection(Projections.rowCount()).uniqueResult();
            return Integer.parseInt(o.toString());

        } catch (Exception e) {
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public static void staleProduct(String project, String product) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            String sql = " UPDATE defects SET stale = 1" +
                    " WHERE project='{pj}' AND product='{pd}'";


            sql = StringUtils.replaceEach(
                    sql,
                    new String[]{"{pj}", "{pd}"},
                    new String[]{project, product}
            );

            session.createSQLQuery(sql).executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    public static void updateHealthCheckTime(String project, String product) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            String time = DateUtils.formatDate(Calendar.getInstance().getTime(), "yyyy-MM-dd HH:mm:ss");

            String sql = "" +
                    " UPDATE products" +
                    " SET last_health_check_time='{tm}'" +
                    " WHERE project='{pj}' AND product='{pd}'";

            sql = StringUtils.replaceEach(
                    sql,
                    new String[]{"{pj}", "{pd}", "{tm}"},
                    new String[]{project, product, time}
            );

            session.createSQLQuery(sql).executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }

    public static void updateTimeToDbNow(String table, String column, String where) {
        String sql = "update {TABLE} set {COLUMN} = now() where {WHERE}";
        sql = StringUtils.replaceEach(
                sql,
                new String[]{"{TABLE}", "{COLUMN}", "{WHERE}"},
                new String[]{table, column, where}
        );

        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }


    public static List<Defect> getProductsFromCdetsQuery(String query) {
        String sql = "select distinct project, product from cdets.defects where (" + query + ")";
        Session session = null;

        List<Defect> lst;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            lst = session.createSQLQuery(sql)
                    .addScalar("project")
                    .addScalar("product")
                    .setResultTransformer(Transformers.aliasToBean(Defect.class))
                    .list();
            session.getTransaction().commit();
            return lst;
        } catch (Exception e) {
            if (session != null) session.getTransaction().rollback();
            throw new GeneralException(e);
        } finally {
            if (session != null) session.close();
        }
    }
}

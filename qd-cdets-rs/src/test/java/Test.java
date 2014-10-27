import org.apache.http.impl.cookie.DateParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/15/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) throws IOException, DateParseException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, URISyntaxException, InvalidKeyException, InvalidKeySpecException {


//        List<String> products = new ArrayList<String>();
//
//        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
//
//        Date startDate = DateUtils.parseDate("02/08/2008 06:07:13", new String[]{"MM/dd/yyyy HH:mm:ss"});
//        Date endDate = DateUtils.parseDate("02/08/2008 06:07:13", new String[]{"MM/dd/yyyy HH:mm:ss"});
//
////        products.add("all");
//        products.add("edison");
//
////        products.add("porter");
//        for (String product : products) {
//            CdetsReplicator.replicate("CSC.labtrunk", product, null, null);
//
//
//        }

//        HttpEngine.setConnectionTimeout(1000000);
//        HttpEngine.setReadTimeout(1000000);

//        QddtsCacher.updateAll(true);

//        long ts = Calendar.getInstance().getTimeInMillis();
//        try {
//            QddtsCacher.cache("( Version~T26,t27,T28,T29 ) minus Version:PSO minus Version~WebEx11,WAPI or Product:db or Product:mmp or Product:app-mc or Product:web-com or Product:telephony or Product:client-com or Product:eureka or Product:app-webacd or Product:app-framework or Product:app-sac and Severity:1,2,3 and Project:CSC.csg");
//        } catch (Exception e) {
//
//        }
//        long te = Calendar.getInstance().getTimeInMillis();
//
//
//        System.out.println(te - ts);


        Calendar now = Calendar.getInstance();
        Calendar bak = (Calendar)now.clone();

        System.out.println(now);
        System.out.println(bak);

        bak.add(Calendar.DAY_OF_MONTH, -10);

        System.out.println(now);
        System.out.println(bak);

    }
}


//http://cdetsweb-prd.cisex.com/findsimple/findcr_simple?noprint=1&order=Submitted-on&username=huaiwang&fieldDelimiter= 分隔符&field=Identifier,Project,Product,Status,Severity,DE-manager,Engineer,Submitter,Component,Version,To-be-fixed,Found,Attribute,Closed-on,Duplicate-on,Forwarded-on,Held-on,Info-req-on,Junked-on,More-on,New-on,Opened-on,Postponed-on,Resolved-on,Submitted-on,Verified-on,Waiting-on,Sys-Last-Updated&query=Project='CSC.csg'
// AND Product='telephony' AND [Sys-Last-Updated]>='08/16/2013 02:58:45'
//http://cdetsweb-prd.cisex.com/findsimple/findcr_simple?noprint=1&order=Submitted-on&username=huaiwang&fieldDelimiter= 分隔符&field=Identifier,Project,Product,Status,Severity,DE-manager,Engineer,Submitter,Component,Version,To-be-fixed,Found,Attribute,Closed-on,Duplicate-on,Forwarded-on,Held-on,Info-req-on,Junked-on,More-on,New-on,Opened-on,Postponed-on,Resolved-on,Submitted-on,Verified-on,Waiting-on,Sys-Last-Updated&query=Project='CSC.csg'
// AND Product='telephony' AND [Sys-Last-Updated]>='08/15/2013 06:58:48'

//http://cdetsweb-prd.cisex.com/findsimple/findcr_simple?noprint=1&order=Submitted-on&username=huaiwang&fieldDelimiter= 分隔符&field=Identifier,Project,Product,Status,Severity,DE-manager,Engineer,Submitter,Component,Version,To-be-fixed,Found,Attribute,Closed-on,Duplicate-on,Forwarded-on,Held-on,Info-req-on,Junked-on,More-on,New-on,Opened-on,Postponed-on,Resolved-on,Submitted-on,Verified-on,Waiting-on,Sys-Last-Updated&query=Project='CSC.csg' AND Product='telephony'
//                         AND [Sys-Last-Updated]>='08/15/2013 07:08:26'

//http://cdetsweb-prd.cisex.com/findsimple/findcr_simple?noprint=1&order=Submitted-on&username=huaiwang&fieldDelimiter= 分隔符&field=Identifier,Project,Product,Status,Severity,DE-manager,Engineer,Submitter,Component,Version,To-be-fixed,Found,Attribute,Closed-on,Duplicate-on,Forwarded-on,Held-on,Info-req-on,Junked-on,More-on,New-on,Opened-on,Postponed-on,Resolved-on,Submitted-on,Verified-on,Waiting-on,Sys-Last-Updated&query=Project='CSC.csg' AND Product='telephony'
//                         AND [Sys-Last-Updated]>='08/15/2013 07:08:26'
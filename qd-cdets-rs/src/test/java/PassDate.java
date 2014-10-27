import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 8/15/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class PassDate {
    public static void main(String[] args) throws NoSuchAlgorithmException {

//        List<Product> products = DbUtils.getStaleProducts();
//
//        for (Product product : products) {
//            System.out.println(product.getId().getProduct());
//        }


        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest("aaaa0000000000000000000b000000000000000000000aaa".getBytes());
        String s = new String(Base64.encodeBase64(digest));
        System.out.println(s);

        int i = md5.getDigestLength();

        System.out.println(i);


        Calendar now = Calendar.getInstance();
        Calendar bak = now;

        now.add(Calendar.MONTH, 10);

        System.out.println(now.getTime());
        System.out.println(bak.getTime());

    }

}

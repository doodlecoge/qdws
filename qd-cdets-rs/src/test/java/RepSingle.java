import com.cisex.service.CdetsReplicator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: huaiwang
 * Date: 13-10-23
 * Time: 下午6:33
 */
public class RepSingle {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<String> lst = new ArrayList<String>();

        lst.add("CSCug24522");
        lst.add("CSCui40567");

        for (String s : lst) {

        CdetsReplicator.replicate(s);
        }

    }
}

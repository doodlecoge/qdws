import com.cisex.GeneralException;
import com.hch.utils.net.HttpEngine;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: huaiwang
 * Date: 13-11-4
 * Time: 下午3:27
 */
public class GetUserVCard {
    private static Connection conn;

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

    public static void main(String[] args) throws IOException, URISyntaxException {

        String[] names = "linzhou,admin,aggchen,gangche,zhcheng,ahsiung,alicem,araina,docheng,antwang,benstse,binsong,byang2,yobi,bonniey,bravoc,brzhu,habustam,celiang,cecguo,chagao,charlin,charzhao,cheney,chuye,xiaomhu,xingszha,xifan,danzheng,dhiggins,chaoding,meiyhuan,ealo,eqiu,fairyw,fangl,qidong,gafeng,grdeng,guojhuan,hdou,haosu,huzhu,shapan,hople,haoche,huaiwang,jingwan2,itong,yiwzhang,guzhou,zizhou,jacao,jneubaue,jassun,dorvillj,jerej,jbao,jiafu,jiangzho,jiebao,juzhang2,jschalla,jianzha2,shengzli,joskoch,justchen,tancao,bohua,kenlane,ganma,xipeng,yanlwang,kinjin,Jinxxu,kynguye,laisun,lennda,leonzhao,lialin,lillli,cutong,ranli,lingmeng,linkl,lisay,xiaoxche,locnguy,loksingh,lonnieh,mmanusan,junliu2,xiaopzho,maryin,marryw,sixu,meerak,nalini,nweng,nveskovi,patring,yanghliu,weiqhu,peteryu,phyllisc,pbasler,pning,psathaye,qianden,qinqian,radzhao,rajeevkg,rangerl,rkankipa,halin2,ndutt,rmurugan,rizhao,qingswan,lingli2,rofeng,roguo,xiafu,saiqis,saahn,sallyy,shengl,sammuthu,sammyy,sandralo,sdehury,xiwan,segarg,arasu,sherrlee,shuochen,xshe,skyy,smao,shzhong,stezhou,stelee,skhatua,jianbfan,yaqizhan,survuppa,Spotiny,taochen2,taof,taliu2,yifeliu,tihong,anthall,guangyuw,tpatnoe,qinye,vezhou,vincchan,wardwalk,weiminxu,weiqli,wenzha2,xiaofliu,xini,xili4,xuexu,yontang2,yongwang,yuanylu,yueshi,ygonen,zeaz,chunyazh".split(",");


        for (String name : names) {
            foo(name);
        }

    }


    public static void foo(String cec) throws IOException, URISyntaxException {
        String urlPtn = "http://wwwin-tools.cisex.com/dir/vcard/{1}.vcf";

        String url = StringUtils.replaceEach(
                urlPtn,
                new String[]{"{1}"},
                new String[]{cec}
        );


        HttpEngine eng = new HttpEngine();

        String html = eng.get(url).getHtml();

        Pattern ptn = Pattern.compile("ORG:(.*)");

        Matcher matcher = ptn.matcher(html);

        if(matcher.find()) {
            System.out.println(cec + "\t" + matcher.group(1));
        } else {
            System.out.println("===" + cec);
        }
    }
}

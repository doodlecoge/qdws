package com.cisex.net;

import com.cisex.GeneralException;
import com.cisex.QdCdetsProperties;
import com.cisex.util.CommandExecutor;
import com.hch.utils.net.HttpEngine;
import com.hch.utils.security.RsaUtils;
import com.hch.utils.time.TimeUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: huaiwang
 * Date: 13-9-10
 * Time: 下午1:24
 */
public class QddtsTrendQuery {
    private static final Logger log = LoggerFactory.getLogger(QddtsTrendQuery.class);

    static {
        HttpEngine.setConnectionTimeout(300000);
        HttpEngine.setReadTimeout(300000);
    }

    private static void addExecutePermission(String file) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec("chmod a+x " + file);
        int exitCode = p.waitFor();
        InputStream is = p.getInputStream();
        is.close();
    }

    public static List<String> query2(String query, String date0, String date1) throws IOException, URISyntaxException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
        String path = QddtsTrendQuery.class.getResource("").getPath();
        String script = QdCdetsProperties.getString("qddts.script.name");

        addExecutePermission(path + script);

        ProcessBuilder pb = new ProcessBuilder(path + script, query, date0, date1);
        Process p = pb.start();
        int exitCode = p.waitFor();

        if (exitCode == 0) {
            List<String> strings = extractData(p.getInputStream());
            log.error("trend data size: " + strings.size());
            return strings;
        } else {
            InputStream es = p.getErrorStream();
            String error = IOUtils.toString(es);
            throw new GeneralException("command execute failed: " + exitCode + ", " + error);
        }
    }

    public static List<String> query3(String query, String date0, String date1) throws InterruptedException, TimeoutException, IOException {
        String outFile = "./out";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String ts = fmt.format(new Date());
        if (new File(outFile).exists()) outFile += ts;

        String str = QdCdetsProperties.getString("qddts.query.timeout");
        long timeGap = TimeUtils.timeGap(str);
        CommandExecutor.exec("query.pl " + query, null, outFile, timeGap);


        str = QdCdetsProperties.getString("qddts.table.timeout");
        timeGap = TimeUtils.timeGap(str);
        CommandExecutor.exec("table.pl "
                + date0 + " " + date1
                + " -daily -closed CJUDFMRVPH out inc closed",
                outFile, outFile + "tbl", timeGap);

        FileInputStream fis = new FileInputStream(outFile + "tbl");
        List<String> strings = extractData(fis);

        new File(outFile).delete();
        new File(outFile + "tbl").delete();

        return strings;
    }

    public static List<String> query(String query, String date0, String date1) throws IOException, URISyntaxException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        HttpEngine eng = new HttpEngine();

        String url = "http://wwwin-metrics.cisex.com/protected-cgi-bin/expert.cgi";
        String password = QdCdetsProperties.getString("qddts.password");

        eng.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(
                        QdCdetsProperties.getString("qddts.username"),
                        RsaUtils.decryptUsingPrivateKey(password)
                )
        );

        // expert page
        eng.get(url);


        // get query form and then post it
        List<WebForm> webForms = WebForm.fromHtml(eng.getHtml());
        WebForm webForm = new WebForm();
        webForm.addParam("expert", query);
        webForm.addParam("type", "selection");
        webForm.setAction(webForms.get(0).getAction());
        webForm.setMethod(webForms.get(0).getMethod());

        eng.post(webForm.getAction(), webForm.getFormData());


        // parse result from html
        // and get 'generate trend' form
        webForms = WebForm.fromHtml(eng.getHtml(), "form[action$=trend.cgi]");
        webForm = webForms.get(0);

        eng.post(webForm.getAction(), webForm.getFormData());


        // get generate complex trend form
        webForms = WebForm.fromHtml(eng.getHtml(), "form[action$=trend_advanced.cgi]");
        webForm = webForms.get(0);

        // post to generate complex trend
        eng.post(webForm.getAction(), webForm.getFormData());


        // get complex form
        webForms = WebForm.fromHtml(eng.getHtml(), "form[action$=trend_advanced.cgi]");

        webForm = webForms.get(0);
        webForm.addParam("interval", "-daily");

        webForm.addParam("trendtype1", "out");
        webForm.addParam("trendtype2", "inc");
        webForm.addParam("trendtype3", "closed");

        webForm.addParam("columntype1", "line");
        webForm.addParam("columntype2", "line");
        webForm.addParam("columntype3", "line");

        webForm.addParam("sevrange1", "s1 - s6");
        webForm.addParam("sevrange2", "s1 - s6");
        webForm.addParam("sevrange3", "s1 - s6");

        webForm.addParam("date0", date0);
        webForm.addParam("date1", date1);
        webForm.addParam("closed", QdCdetsProperties.getString("qddts.closed.status"));

        String html = eng.post(webForm.getAction(), webForm.getFormData()).getHtml();

        String tblUrl = Jsoup.parse(html).select("a[href$=tbl]").attr("href");

        InputStream is = eng.get(tblUrl).getInputStream();
        return extractData(is);
    }


    private static List<String> extractData(InputStream is) throws IOException {
        List<String> lst = new ArrayList<String>();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        int parseErrors = 0;
        int lines = 0;

        Pattern ptn = Pattern.compile("(\\d{6})\t(\\d{1,6})\t(\\d{1,6})\t(\\d{1,6})");

        while ((line = br.readLine()) != null) {
            line = line.trim();
            lines++;
            if ("".equals(line)) continue;

            Matcher m = ptn.matcher(line);

            if (!m.find()) {
                log.error("line: " + line);
                parseErrors++;
                continue;
            }

            Long dateMs = null;
            try {
                dateMs = getDateMs(m.group(1));
            } catch (DateParseException e) {
                log.error("line: " + line, e);
                continue;
            }

            lst.add("" + dateMs + "," + m.group(2) + "," + m.group(3) + "," + m.group(4));
        }

        log.error("lines: " + lines);

        //todo: remove debug log
        log.error("parse errors: " + parseErrors);

        if (parseErrors > 10) return null;
        else return lst;
    }


    private static Long getDateMs(String dateString) throws DateParseException {
        String[] ptn = new String[]{"yyMMdd"};
        Date date = DateUtils.parseDate(dateString, ptn);
        return date.getTime();
    }

    public static void main(String[] args) throws IOException, URISyntaxException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
//        query("Project:CSC.csg and Product:identity");
        List<String> outBugNumber = query2("Project:CSC.csg and Product:identity", "130611", "130911");

        for (String s : outBugNumber) {
            System.out.println(s);
        }
    }
}

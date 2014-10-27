package com.cisex.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.TimeoutException;


public class CommandExecutor {
    public static final Logger log =
            LoggerFactory.getLogger(CommandExecutor.class);

    public static void main(String[] args)
            throws InterruptedException, TimeoutException, IOException {
//        exec("query.pl", "./in.txt", "./out.txt", 5000);
//        exec("sleep 100", null,null, 10000);

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("sleep 1000");

        InputStream os = process.getInputStream();
        System.out.println("...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.out.println("...2");
        int b;
        while ((b = os.read())!=-1)
            baos.write(b);
        System.out.println(baos.toString());
    }

    public static int exec(
            final String commandLine,
            final String inFile,
            final String outFile,
            final long timeout)
            throws IOException, InterruptedException, TimeoutException {

        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(commandLine);

        Worker worker = new Worker(process, inFile, outFile);
        worker.start();

        try {
            worker.join(timeout);
            if (worker.exit != null)
                return worker.exit;
            else
                throw new TimeoutException();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }

    private static class Worker extends Thread {
        private final Process process;
        private Integer exit;
        private String inFile;
        private String outFile;

        private Worker(Process process, String inFile, String outFile) {
            this.process = process;
            this.inFile = inFile;
            this.outFile = outFile;
        }

        public void run() {
            try {
                input();
                output();
                exit = process.waitFor();
            } catch (Exception e) {
                log.error("-", e);
            }
        }

        private void input() throws IOException {
            if (inFile == null) return;

            OutputStream os = process.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);

            FileInputStream fis = new FileInputStream(inFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }

            bis.close();
            bos.close();
        }

        private void output() throws IOException {
            if (outFile == null) return;

            InputStream is = process.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            FileOutputStream fos = new FileOutputStream(outFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b);
            }

            bos.close();
            bis.close();
        }
    }

}

package com.cisex.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/26/13
 * Time: 5:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class CdetsDefectReader extends Reader {
    private static final int bufferSize = 1024 * 32;
    private static final byte[] buffer = new byte[bufferSize];
    private InputStream inputStream;

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        return 0;
    }

    @Override
    public void close() throws IOException {
    }

    public CdetsDefectReader(InputStream is) {
        this.inputStream = is;
    }

    public String readDefect() throws IOException {
        int read = inputStream.read(buffer);

        return null;
    }
}

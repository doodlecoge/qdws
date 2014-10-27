package com.cisex.service;

import com.cisex.model.Product;
import com.cisex.util.DbUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: huaiwang
 * Date: 7/1/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class FindNewProductJob {
    public static void execute() throws IOException, URISyntaxException {
        List<String> products = CdetsReplicator.getProducts("CSC.csg");

        for (String product : products) {
            Product prod = DbUtils.getProduct("CSC.csg", product);

            if (prod != null) continue;

            CdetsReplicator.replicate("CSC.csg", product);

            break;
        }
    }
}

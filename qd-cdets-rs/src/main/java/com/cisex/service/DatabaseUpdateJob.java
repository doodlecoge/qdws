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
 * Time: 9:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseUpdateJob {
    public static boolean execute() throws IOException, URISyntaxException {
        List<Product> products = DbUtils.getStaleProducts();

        for (Product product : products) {
            ReplicationWorker.getInstance().addJob(
                    product.getId().getProject(),
                    product.getId().getProduct(),
                    false
            );
        }

//        if (products.size() == 0) return false;
//        Product product = products.get(0);
//
//        CdetsReplicator.updateProject(
//                product.getId().getProject(),
//                product.getId().getProduct(),
//                false
//        );

        return true;
    }
}

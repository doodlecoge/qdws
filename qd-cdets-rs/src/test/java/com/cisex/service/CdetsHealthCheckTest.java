package com.cisex.service;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * User: huaiwang
 * Date: 13-11-13
 * Time: 上午9:59
 */
public class CdetsHealthCheckTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
//        List<Product> allProducts = DbUtils.getAllProducts();
//        for (Product prod : allProducts) {
//            ProductPK id = prod.getId();
//            String project = id.getProject();
//            String product = id.getProduct();
//
//            CdetsHealthCheck.check(project, product);
//        }


        CdetsHealthCheck.check("CSC.spv.ipvs", "mos", true);

//        List<Object[]> prods = DbUtils.getAllProductsOrderByDefectCount();
//
//        for (Object[] prod : prods) {
//            String project = prod[0].toString();
//            String product = prod[1].toString();
//
//            try {
//                CdetsHealthCheck.check(project, product);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


    }
}

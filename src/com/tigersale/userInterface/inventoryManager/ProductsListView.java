package com.tigersale.userInterface.inventoryManager;

import com.tigersale.database.ProductTable;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;

import java.util.List;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class ProductsListView extends AbstractView {
    public ProductsListView() {
        super(null);
    }

    public void runView() {
        List<Product> productList = ProductTable.viewProducts();

        System.out.println("=========================================================");
        System.out.println("                      Product list!                      ");
        System.out.println("=========================================================");

        for(Product product : productList) {
            System.out.println(product);
        }

        System.out.println("=========================================================");
    }
}

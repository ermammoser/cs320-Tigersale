package com.tigersale.main;

import com.tigersale.database.*;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermam on 2/6/2017 for the tigersale.com application.
 *
 * The main entrance to the tigersale.com application
 */
public class Tigersale {

    public static void main(String []args)
    {

        DatabaseConnection.initializeConnection(true);
        CustomerUser john = CustomerUserTable.login("johnDoe", "johnsPass");
        System.out.println(john);
        System.out.println(OrderTable.getOrders(john));
        System.out.println();
        System.out.println(OrderTable.getProducts(OrderTable.getOrders(john).get(0)));
        System.out.println("================================================");

        ArrayList<Pair<Product, Integer>> productsToOrder = new ArrayList<>();
        List<Product> prods = ProductTable.searchForProducts("");
        productsToOrder.add(new Pair<>(prods.get(0), 2));
        productsToOrder.add(new Pair<>(prods.get(1), 3));
        System.out.println(OrderTable.placeOrder(john, AddressTable.getAddresses(john).get(0), productsToOrder));
        System.out.println(OrderTable.getOrders(john));
        System.out.println(OrderTable.getProducts(OrderTable.getOrders(john).get(2)));

    }
}

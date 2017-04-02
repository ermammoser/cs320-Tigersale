package com.tigersale.main;

import com.tigersale.database.DatabaseConnection;
import com.tigersale.database.ProductTable;
import com.tigersale.model.Product;

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
    }
}

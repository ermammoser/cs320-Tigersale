package com.tigersale.main;

import com.tigersale.database.DatabaseConnection;
import com.tigersale.model.InventoryManager;

import java.util.Date;

/**
 * Created by ermam on 2/6/2017 for the tigersale.com application.
 *
 * The main entrance to the tigersale.com application
 */
public class Tigersale {

    public static void main(String []args)
    {
        Date d = new Date(2017,04,04);
        DatabaseConnection.initializeConnection(true);
        InventoryManager newManager = new InventoryManager( "mt2915", "123456", 23, d ,
            "My", "Tran", "T");
        System.out.println(newManager);
    }
}

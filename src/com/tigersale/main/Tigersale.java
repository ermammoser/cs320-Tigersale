package com.tigersale.main;

import com.tigersale.database.AddressTable;
import com.tigersale.database.DatabaseConnection;
import com.tigersale.database.ProductTable;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;

import java.util.Date;
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

        CustomerUser user = new CustomerUser("ericMammoser", "test", new Date(), "","","");
    }
}

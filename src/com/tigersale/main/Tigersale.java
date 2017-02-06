package com.tigersale.main;

import com.tigersale.database.DatabaseConnection;

/**
 * Created by ermam on 2/6/2017.
 */
public class Tigersale {

    public static void main(String []args)
    {
        DatabaseConnection.initializeConnection(false);
    }
}

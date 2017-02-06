package com.tigersale.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ermam on 2/6/2017.
 */
public class DatabaseConnection {

    public static final String DATABASE_LOCATION = "../" + File.separator + "tigersale" + File.separator + "tigersale";
    public static final String USERNAME = "tigersaleUser";
    public static final String PASSWORD = "tigersalePassword";

    public static Connection conn = null;

    public static void initializeConnection(boolean createNewDatabase)
    {
        try {

            // The location of the database
            String url = "jdbc:h2:" + DATABASE_LOCATION;

            // Specify the h2 driver
            Class.forName("org.h2.Driver");

            // Create the actual connection
            conn = DriverManager.getConnection(url,
                    USERNAME,
                    PASSWORD);

        } catch (SQLException | ClassNotFoundException e) {
            //You should handle this better
            e.printStackTrace();
        }
    }
}

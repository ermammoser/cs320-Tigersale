package com.tigersale.database;

import com.tigersale.model.PaymentMethod;
import org.h2.store.fs.FileUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ermam on 2/6/2017 for the tigersale.com application.
 *
 * Helpful class to setup and maintain the database connection
 */
public class DatabaseConnection {

    public static final String DATABASE_LOCATION = "." + File.separator + "tigersale";
    public static final String DATABASE_NAME = "tigersale";
    public static final String USERNAME = "tigersaleUser";
    public static final String PASSWORD = "tigersalePassword";

    public static Connection conn = null;

    /**
     * Creates the connection with the H2 database supporting the tigersale application
     *
     * @param createNewDatabase Whether or not to create a new database
     */
    public static void initializeConnection(boolean createNewDatabase)
    {
        try {

            // Delete the old database if it exists
            if(createNewDatabase)
            {
                FileUtils.deleteRecursive(DATABASE_LOCATION, true);
            }
            else
            {
                if(FileUtils.exists(DATABASE_LOCATION)) {
                    if (FileUtils.exists(DATABASE_LOCATION + File.separator + DATABASE_NAME + ".mv.db")) {
                        if (FileUtils.size(DATABASE_LOCATION + File.separator + DATABASE_NAME + ".mv.db") == 0) {
                            createNewDatabase = true;
                        }
                    } else {
                        createNewDatabase = true;
                    }
                }
                else
                {
                    createNewDatabase = true;
                }
            }

            // The location and date of the database
            String url = "jdbc:h2:" + DATABASE_LOCATION + File.separator + DATABASE_NAME;

            // Specify the h2 driver
            Class.forName("org.h2.Driver");

            // Create the actual connection
            conn = DriverManager.getConnection(url,
                    USERNAME,
                    PASSWORD);

            // Create all of the tables
            if(createNewDatabase) {
                createTables();
            }

        } catch (SQLException | ClassNotFoundException e) {
            //You should handle this better
            e.printStackTrace();
        }
    }

    /**
     * Helper function to create all of the tables
     */
    private static void createTables()
    {
        CustomerUserTable.createTable();
        AddressTable.createTable();
        InventoryManagerTable.createTable();
        ProductTable.createTable();
        PaymentMethodTable.createTable(); // Cannot go before CustomerUserTable.createTable()
        ShoppingListTable.createTable(); // Cannot go before CustomerUserTable.createTable() or ProductTable.createTable()
        OrderTable.createTable(); // Cannot go before CustomerUserTable.createTable() or AddressTable.createTable()
        ProdToOrderTable.createTable(); // Cannot go before ProductTable.createTable() or OrderTable.createTable()
    }
}

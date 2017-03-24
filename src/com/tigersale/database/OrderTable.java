package com.tigersale.database;

import com.tigersale.model.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;

/**
 * Created by ermam on 3/24/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the order table within the tigersale.com's database
 */
public class OrderTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "PurchaseOrder";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "Order.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        TransactionId("TransactionId"),
        Date("PurchaseDate"),
        Cost("Cost"),
        Status("Status"),
        AddressId("AddressId"),
        CustomerUsername("CustomerUsername");

        String name;

        Fields(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    /**
     * Creates and populates the Order table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + Fields.TransactionId + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + Fields.Date + " DATE,"
                    + Fields.Cost + " DECIMAL(10,2),"
                    + Fields.Status + " INTEGER,"
                    + Fields.AddressId + " INTEGER,"
                    + Fields.CustomerUsername + " VARCHAR(30),"
                    + "FOREIGN KEY (" + Fields.AddressId + ") REFERENCES " + AddressTable.TABLE_NAME + "(" + AddressTable.Fields.AddressId + "),"
                    + "FOREIGN KEY (" + Fields.CustomerUsername + ") REFERENCES " + CustomerUserTable.TABLE_NAME + "(" + CustomerUserTable.Fields.CustomerUsername + ")"
                    +");" ;

            // Create the createStatement
            Statement stmt = DatabaseConnection.conn.createStatement();
            stmt.execute(createStatement);
            stmt.close();

            // Load all of the mock data
            BufferedReader br = new BufferedReader(new FileReader(PaymentMethodTable.class.getClassLoader().getResource(MOCK_DATA).getFile()));

            // Skip first line because it is just headers
            br.readLine();

            // Create blank insert statement
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setDate(2, Date.valueOf(values[1]));
                insertStatement.setBigDecimal(3, BigDecimal.valueOf(Double.valueOf(values[2])));
                insertStatement.setInt(4, Integer.valueOf(values[3]));
                insertStatement.setInt(5, Integer.valueOf(values[4]));
                insertStatement.setString(6, values[5]);
                insertStatement.execute();
            }
            insertStatement.close();
            br.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}

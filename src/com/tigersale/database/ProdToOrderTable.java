package com.tigersale.database;

import com.tigersale.model.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the relationship between products and orders within the tigersale.com's database
 */
public class ProdToOrderTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "ProdToOrder";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "prodToOrder.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        ProductId("ProductId"),
        TransactionId("TransactionId"),
        Amount("Amount");

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
     * Creates and populates the Shopping List table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.ProductId + " INTEGER,"
                    + Fields.TransactionId + " INTEGER,"
                    + Fields.Amount + " INTEGER,"
                    + "PRIMARY KEY (" + Fields.ProductId + "," + Fields.TransactionId+ "),"
                    + "FOREIGN KEY (" + Fields.ProductId+ ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.Fields.ProductId+ "),"
                    + "FOREIGN KEY (" + Fields.TransactionId + ") REFERENCES " + OrderTable.TABLE_NAME + "(" + OrderTable.Fields.TransactionId + ")"
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
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setInt(2, Integer.valueOf(values[1]));
                insertStatement.setInt(3, Integer.valueOf(values[2]));
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

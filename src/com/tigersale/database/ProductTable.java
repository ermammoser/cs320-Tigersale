package com.tigersale.database;

import com.tigersale.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the product table within the tigersale.com's database
 */
public class ProductTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "Product";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "Product.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        ProductId("TransactionId"),
        Name("Date"),
        Description("Cost"),
        Price("Status"),
        Stock("AddressId"),
        Brand("Brand"),
        Category("Category");

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
     * Creates and populates the Product table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.ProductId + " INTEGER PRIMARY KEY,"
                    + Fields.Name + " VARCHAR(30),"
                    + Fields.Description + " VARCHAR(500),"
                    + Fields.Price + " DECIMAL(10,2),"
                    + Fields.Stock + " INTEGER,"
                    + Fields.Brand + " VARCHAR(30),"
                    + Fields.Category + " VARCHAR(30)"
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
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setString(2, values[1]);
                insertStatement.setString(3, values[2]);
                insertStatement.setBigDecimal(4, BigDecimal.valueOf(Double.valueOf(values[3])));
                insertStatement.setInt(5, Integer.valueOf(values[4]));
                insertStatement.setString(6, values[5]);
                insertStatement.setString(7, values[6]);
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

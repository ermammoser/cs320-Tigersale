package com.tigersale.database;

import com.tigersale.model.InventoryManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the inventory manager table within the tigersale.com's database
 */
public class InventoryManagerTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "InventoryManager";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "InventoryManager.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        Username("TransactionId"),
        Password("Cost"),
        Salary("Status"),
        HireDate("AddressId"),
        FirstName("Brand"),
        LastName("Category"),
        MiddleInitial("MiddleInitial");

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
     * Creates and populates the InventoryManager table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.Username + " VARCHAR(50) PRIMARY KEY,"
                    + Fields.Password + " VARCHAR(25),"
                    + Fields.Salary + " INTEGER,"
                    + Fields.HireDate + " DATE,"
                    + Fields.FirstName + " VARCHAR(30),"
                    + Fields.LastName + " VARCHAR(30),"
                    + Fields.MiddleInitial + " CHAR(1)"
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
                insertStatement.setString(1, values[0]);
                insertStatement.setString(2, values[1]);
                insertStatement.setInt(3, Integer.valueOf(values[2]));
                insertStatement.setDate(4, Date.valueOf(values[3]));
                insertStatement.setString(5, values[4]);
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

package com.tigersale.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the address table within the tigersale.com's database
 */
public class AddressTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "Address";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "Address.csv";

    /**
     * Helpful enumeration for all of the fields in the Address table
     */
    public enum Fields
    {
        AddressId("AddressId"),
        Street("CustomerUsername"),
        City("NameOnCard"),
        State("CreditCardNumber"),
        ZipCode("CVCCode");

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
     * Creates and populates the Address table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.AddressId + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + Fields.Street + " VARCHAR(255),"
                    + Fields.City + " VARCHAR(20),"
                    + Fields.State + " VARCHAR(20),"
                    + Fields.ZipCode + " CHAR(5)"
                    + ");" ;

            // Create the createStatement
            Statement stmt = DatabaseConnection.conn.createStatement();
            stmt.execute(createStatement);
            stmt.close();

            // Load all of the mock data
            BufferedReader br = new BufferedReader(new FileReader(AddressTable.class.getClassLoader().getResource(MOCK_DATA).getFile()));

            // Skip first line because it is just headers
            br.readLine();

            // Create blank insert statement
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setString(2, values[1]);
                insertStatement.setString(3, values[2]);
                insertStatement.setString(4, values[3]);
                insertStatement.setString(5, values[4]);
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

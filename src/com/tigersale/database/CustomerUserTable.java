package com.tigersale.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the customer user table within the tigersale.com's database
 */
public class CustomerUserTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "CustomerUser";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "CustomerUser.csv";

    /**
     * Helpful enumeration for all of the fields in the CustomerUser table
     */
    public enum Fields
    {
        CustomerUsername("CustomerUsername"),
        Password("Cost"),
        DateOfBirth("DateOfBirth"),
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
     * Creates and populates the CustomerUser table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.CustomerUsername + " VARCHAR(50) PRIMARY KEY,"
                    + Fields.Password + " VARCHAR(25),"
                    + Fields.DateOfBirth + " DATE(25),"
                    + Fields.FirstName + " VARCHAR(255),"
                    + Fields.LastName + " VARCHAR(255),"
                    + Fields.MiddleInitial + " CHAR(1),"
                    + ");" ;

            // Create the createStatement
            Statement stmt = DatabaseConnection.conn.createStatement();
            stmt.execute(createStatement);
            stmt.close();

            // Load all of the mock data
            BufferedReader br = new BufferedReader(new FileReader(CustomerUserTable.class.getClassLoader().getResource(MOCK_DATA).getFile()));

            // Skip first line because it is just headers
            br.readLine();

            // Create blank insert statement
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setString(1, values[0]);
                insertStatement.setString(2, values[1]);
                insertStatement.setDate(3, Date.valueOf(values[2]));
                insertStatement.setString(4, values[3]);
                insertStatement.setString(5, values[4]);
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

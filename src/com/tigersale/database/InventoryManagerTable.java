package com.tigersale.database;


import com.tigersale.model.InventoryManager;
import com.tigersale.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final String MOCK_DATA =  "mockData/InventoryManager.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        Username("Username"),
        Password("Password"),
        Salary("Salary"),
        HireDate("HireDate"),
        FirstName("FirstName"),
        LastName("LastName"),
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
     * Checks if the supplied credentials match a user. If so, return that user.
     *
     * @param username The username of the inventory manager
     * @param password The password of the inventory manager
     *
     * @return The inventory manager if it exists
     */
    public static InventoryManager login(String username, String password)
    {
        InventoryManager inventoryManager = null;
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.Username + " = ? AND " + Fields.Password + " = ?");
            searchStatement.setString(1, username);
            searchStatement.setString(2, password);
            ResultSet rs = searchStatement.executeQuery();
            if(rs.next()) {
                inventoryManager = inventoryManagerFromResultSet(rs);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return inventoryManager;
    }


    /**
     * Returns whether or not the username is available
     *
     * @param username The username in question
     *
     * @return Whether or not the username is available
     */
    public static boolean usernameAvaliable(String username)
    {
        boolean available = false;
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.Username + " = ?");
            searchStatement.setString(1, username);
            ResultSet rs = searchStatement.executeQuery();
            available = !rs.next();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return available;
    }


    /**
     * Inserts a new inventory manager into the table.  This should be used to register a new inventory manager
     *
     * @param username The inventory manager username
     * @param password The inventory manager password
     * @param salary   The inventory manager salary
     * @param hireDate The inventory manager hire date
     * @param firstName The inventory manager first name
     * @param lastName The inventory manager last name
     * @param middleInitial The inventory manager middle initial
     *
     * @return How many rows were updated
     */
    public static int insertInventoryManager(String username, String password, int salary, Date hireDate, String firstName,
                                         String lastName, String middleInitial)
    {
        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                    TABLE_NAME + "("+ Fields.Username + "," + Fields.Password+ "," + Fields.Salary + "," + Fields.HireDate + "," +
                    Fields.FirstName + "," + Fields.LastName + "," + Fields.MiddleInitial + ") VALUES (?,?,?,?,?,?,?)");

            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement.setInt(3, salary);
            insertStatement.setDate(4, hireDate);
            insertStatement.setString(5, firstName);
            insertStatement.setString(6, lastName);
            insertStatement.setString(7, middleInitial);
            numChanged = insertStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }

    /**
     * Creates a customer user from the given result set
     *
     * @param rs A result set containing a inventory manager
     *
     * @return A inventory manager from the current result
     *
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static InventoryManager inventoryManagerFromResultSet(ResultSet rs) throws SQLException
    {
        String username = rs.getString(Fields.Username.toString());
        String password = rs.getString(Fields.Password.toString());
        int salary = rs.getInt(Fields.Salary.toString());
        Date hireDate = rs.getDate(Fields.HireDate.toString());
        String firstName = rs.getString(Fields.FirstName.toString());
        String lastName = rs.getString(Fields.LastName.toString());
        String middleInitial = rs.getString(Fields.MiddleInitial.toString());

        return new InventoryManager(username, password, salary, hireDate, firstName, lastName, middleInitial);
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
            BufferedReader br = new BufferedReader(new InputStreamReader(InventoryManagerTable.class.getClassLoader().getResourceAsStream(MOCK_DATA)));

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

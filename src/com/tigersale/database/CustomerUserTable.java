package com.tigersale.database;

import com.tigersale.model.CustomerUser;

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
        Password("Password"),
        DateOfBirth("DateOfBirth"),
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
     * Updates the data for the customer user
     *
     * @param user The user to update
     *
     * @return The number of updated rows
     */
    public static int updateCustomerUserValues(CustomerUser user)
    {
        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("UPDATE " +
                    TABLE_NAME + " SET " + Fields.Password + " = ?, " +
                    Fields.DateOfBirth + " = ?, " +
                    Fields.FirstName + " = ?, " +
                    Fields.LastName + " = ?, " +
                    Fields.MiddleInitial + " = ? " +
                    "WHERE " + Fields.CustomerUsername + " = ?"
            );

            insertStatement.setString(1, user.password);
            insertStatement.setDate(2, user.dateOfBirth);
            insertStatement.setString(3, user.firstName);
            insertStatement.setString(4, user.lastName);
            insertStatement.setString(5, user.middleInitial);
            insertStatement.setString(6, user.customerUsername);

            numChanged = insertStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }


    /**
     * Checks if the supplied credentials match a user. If so, return that user.
     *
     * @param username The username of the customer user
     * @param password The password of the customer user
     *
     * @return The customer user if it exists
     */
    public static CustomerUser login(String username, String password)
    {
        CustomerUser customerUser = null;
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ? AND " + Fields.Password + " = ?");
            searchStatement.setString(1, username);
            searchStatement.setString(2, password);
            ResultSet rs = searchStatement.executeQuery();
            if(rs.next()) {
                customerUser = customerUserFromResultSet(rs);
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return customerUser;
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
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ?");
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
     * Inserts a new customer user into the table.  This should be used to register a new user
     *
     * @param customerUsername The customer's username
     * @param password The customer's password
     * @param dateOfBirth The customer's date of birth
     * @param firstName The customer's first name
     * @param lastName The customer's last name
     * @param middleInitial The customer's middle initial
     *
     * @return How many rows were updated
     */
    public static int insertCustomerUser(String customerUsername, String password, Date dateOfBirth, String firstName,
                                         String lastName, String middleInitial)
    {
        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                    TABLE_NAME + "("+ Fields.CustomerUsername + "," + Fields.Password+ "," + Fields.DateOfBirth+ "," +
                    Fields.FirstName + "," + Fields.LastName + "," + Fields.MiddleInitial + ") VALUES (?,?,?,?,?,?)");

            insertStatement.setString(1, customerUsername);
            insertStatement.setString(2, password);
            insertStatement.setDate(3, dateOfBirth);
            insertStatement.setString(4, firstName);
            insertStatement.setString(5, lastName);
            insertStatement.setString(6, middleInitial);
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
     * @param rs A result set containing a customer user
     *
     * @return A customer user from the current result
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static CustomerUser customerUserFromResultSet(ResultSet rs) throws SQLException
    {
        String customerUsername = rs.getString(Fields.CustomerUsername.toString());
        String password = rs.getString(Fields.Password.toString());
        Date dateOfBirth = rs.getDate(Fields.DateOfBirth.toString());
        String firstName = rs.getString(Fields.FirstName.toString());
        String lastName = rs.getString(Fields.LastName.toString());
        String middleInitial = rs.getString(Fields.MiddleInitial.toString());

        return new CustomerUser(customerUsername, password, dateOfBirth, firstName, lastName, middleInitial);
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

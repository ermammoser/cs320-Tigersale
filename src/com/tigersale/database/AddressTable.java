package com.tigersale.database;

import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        Street("Street"),
        City("City"),
        State("State"),
        ZipCode("ZipCode"),
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
     * Returns a list of all of the addresses for a Customer User
     *
     * @param user The user to search for addresses for
     *
     * @return A list of addresses for the user
     */
    public static List<Address> getAddresses(CustomerUser user)
    {
        ArrayList<Address> adresses = new ArrayList<>();
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ?");
            searchStatement.setString(1, user.customerUsername);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                adresses.add(addressFromResultSet(rs));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return adresses;
    }

    /**
     * Creates an address from the given result set
     *
     * @param rs A result set containing an address
     *
     * @return A address from the current result
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static Address addressFromResultSet(ResultSet rs) throws SQLException
    {
        int id = rs.getInt(Fields.AddressId.toString());
        String street = rs.getString(Fields.Street.toString());
        String city = rs.getString(Fields.City.toString());
        String state = rs.getString(Fields.State.toString());
        String zipcode = rs.getString(Fields.ZipCode.toString());
        String customerUsername = rs.getString(Fields.CustomerUsername.toString());

        return new Address(id, street, city, state, zipcode, customerUsername);
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
                    + Fields.ZipCode + " CHAR(5),"
                    + Fields.CustomerUsername + " VARCHAR(30),"
                    + "FOREIGN KEY (" + PaymentMethodTable.Fields.CustomerUsername + ") REFERENCES " + CustomerUserTable.TABLE_NAME + "(" + CustomerUserTable.Fields.CustomerUsername + ")"
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
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?)");

            String line;
            while((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setString(2, values[1]);
                insertStatement.setString(3, values[2]);
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

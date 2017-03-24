package com.tigersale.database;

import com.tigersale.model.PaymentMethod;

import java.sql.ResultSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the payment method table within the tigersale.com's database
 */
public class PaymentMethodTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "PaymentMethod";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "PaymentMethod.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        PaymentMethodName("PaymentMethodName"),
        CustomerUsername("CustomerUsername"),
        NameOnCard("NameOnCard"),
        CreditCardNumber("CreditCardNumber"),
        CVCCode("CVCCode"),
        ExpirationDate("ExpirationDate");

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
     * Creates and populates the PaymentMethod table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.PaymentMethodName + " VARCHAR(50),"
                    + Fields.CustomerUsername + " VARCHAR(255),"
                    + Fields.NameOnCard + " VARCHAR(50),"
                    + Fields.CreditCardNumber + " VARCHAR(16),"
                    + Fields.CVCCode + " CHAR(3),"
                    + Fields.ExpirationDate + " CHAR(5),"
                    + "PRIMARY KEY (" + Fields.PaymentMethodName + "," + Fields.CustomerUsername + "),"
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
                insertStatement.setString(1, values[0]);
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

package com.tigersale.database;

import com.tigersale.model.CustomerUser;
import com.tigersale.model.PaymentMethod;

import java.io.InputStreamReader;
import java.sql.ResultSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static final String MOCK_DATA =  "mockData/PaymentMethod.csv";

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


    /**'
     * Removes a payment method from the table
     *
     * @param paymentMethod The payment method to remove
     *
     * @return The number of rows updated
     */
    public static int deletePaymentMethod(PaymentMethod paymentMethod)
    {
        int numChanged = 0;
        try {
            PreparedStatement deleteStatement = DatabaseConnection.conn.prepareStatement("DELETE FROM " +
                    TABLE_NAME + " WHERE " + Fields.PaymentMethodName + " = ? AND " + Fields.CustomerUsername + " = ?");

            deleteStatement.setString(1, paymentMethod.paymentMethodName);
            deleteStatement.setString(2, paymentMethod.customerUsername);
            numChanged = deleteStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }

    /**
     * Inserts a payment method into the table
     *
     * @param paymentMethodName The name of the payment method
     * @param nameOnCard The name on the card
     * @param creditCardNumber The credit card number
     * @param cvcCode The cvc code for the card
     * @param expirationDate The expiration date for the card
     * @param user The user who is adding the card
     *
     * @return The number of rows updated
     */
    public static int insertPaymentMethod(String paymentMethodName, String nameOnCard, String creditCardNumber,
                                          String cvcCode, String expirationDate, CustomerUser user)
    {
        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                    TABLE_NAME + "("+ Fields.PaymentMethodName + "," + Fields.CustomerUsername+ "," + Fields.NameOnCard+ "," +
                    Fields.CreditCardNumber + "," + Fields.CVCCode + "," + Fields.ExpirationDate + ") VALUES (?,?,?,?,?,?)");

            insertStatement.setString(1, paymentMethodName);
            insertStatement.setString(2, user.customerUsername);
            insertStatement.setString(3, nameOnCard);
            insertStatement.setString(4, creditCardNumber);
            insertStatement.setString(5, cvcCode);
            insertStatement.setString(6, expirationDate);
            numChanged = insertStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }


    /**
     * Returns a list of all of the payment methods for a Customer User
     *
     * @param user The user to search for payment methods for
     *
     * @return A list of payment methods for the user
     */
    public static List<PaymentMethod> getPaymentMethods(CustomerUser user)
    {
        ArrayList<PaymentMethod> paymentMethods = new ArrayList<>();
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ?");
            searchStatement.setString(1, user.customerUsername);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                paymentMethods.add(paymentMethodFromResultSet(rs));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return paymentMethods;
    }

    /**
     * Creates a payment method from the given result set
     *
     * @param rs A result set containing a payment method
     *
     * @return A payment method from the current result
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static PaymentMethod paymentMethodFromResultSet(ResultSet rs) throws SQLException
    {
        String paymentMethodName = rs.getString(Fields.PaymentMethodName.toString());
        String customerUsername = rs.getString(Fields.CustomerUsername.toString());
        String nameOnCard = rs.getString(Fields.NameOnCard.toString());
        String creditCardNumber = rs.getString(Fields.CreditCardNumber.toString());
        String cvcCode = rs.getString(Fields.CVCCode.toString());
        String expirationDate = rs.getString(Fields.ExpirationDate.toString());

        return new PaymentMethod(paymentMethodName, customerUsername, nameOnCard, creditCardNumber, cvcCode, expirationDate);
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
            BufferedReader br = new BufferedReader(new InputStreamReader(PaymentMethodTable.class.getClassLoader().getResourceAsStream(MOCK_DATA)));

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

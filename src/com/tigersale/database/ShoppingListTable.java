package com.tigersale.database;

import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the shopping list table within the tigersale.com's database
 */
public class ShoppingListTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "ShoppingList";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "ShoppingList.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        CustomerUsername("CustomerUsername"),
        ProductId("ProductId"),
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
     * Adds a product to a user's shopping cart
     *
     * @param user The user's cart to add it to
     * @param product The product to add to the shopping cart
     *
     * @return The number of rows changed
     */
    public static int addProduct(CustomerUser user, Product product)
    {
        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                    TABLE_NAME + "("+ Fields.CustomerUsername + "," + Fields.ProductId + "," + Fields.Amount + ") VALUES (?,?,?)");

            insertStatement.setString(1, user.customerUsername);
            insertStatement.setInt(2, product.productId);
            insertStatement.setInt(3, 1);
            numChanged = insertStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }


    /**
     * Change the amount of a product that the user wants in their list
     *
     * @param user The user's cart to modify
     * @param product The product to modify in the shopping cart
     * @param amount The amount of the product to change to
     *
     * @return The number of rows changed
     */
    public static int changeAmountOfProduct(CustomerUser user, Product product, int amount)
    {
        if(amount == 0)
        {
            return deleteProduct(user, product);
        }

        int numChanged = 0;
        try {
            PreparedStatement insertStatement = DatabaseConnection.conn.prepareStatement("UPDATE " +
                    TABLE_NAME + " SET " + Fields.Amount + " = ? " +
                    " WHERE " + Fields.CustomerUsername + " = ? AND " + Fields.ProductId + " = ?");

            insertStatement.setInt(1, amount);
            insertStatement.setString(2, user.customerUsername);
            insertStatement.setInt(3, product.productId);
            numChanged = insertStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }

    /**
     * Removes a product from the user's shopping list
     *
     * @param user The user whos shopping list is being changed
     * @param product The product to remove
     *
     * @return The number of rows to update
     */
    public static int deleteProduct(CustomerUser user, Product product)
    {
        int numChanged = 0;
        try {
            PreparedStatement deleteStatement = DatabaseConnection.conn.prepareStatement("DELETE FROM " +
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ? AND " + Fields.ProductId + " = ?");

            deleteStatement.setString(1, user.customerUsername);
            deleteStatement.setInt(2, product.productId);
            numChanged = deleteStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }

    /**
     * Clears a user's shopping list
     *
     * @param user The user who's shopping list is being cleared
     *
     * @return The number of changed rows
     */
    public static int clearShoppingList(CustomerUser user)
    {
        int numChanged = 0;
        try {
            PreparedStatement deleteStatement = DatabaseConnection.conn.prepareStatement("DELETE FROM " +
                    TABLE_NAME + " WHERE " + Fields.CustomerUsername + " = ?");

            deleteStatement.setString(1, user.customerUsername);
            numChanged = deleteStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return numChanged;
    }

    /**
     * Returns a list of all products in the user's shopping list
     *
     * @param user The user to remove products from
     *
     * @return A list of pairs where the key is the product and the value is the amount of product
     */
    public static List<Pair<Product, Integer>> getShoppingList(CustomerUser user)
    {
        ArrayList<Pair<Product, Integer>> shoppingList  = new ArrayList<>();
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + ", " + ProductTable.TABLE_NAME +
                    " WHERE " + Fields.CustomerUsername + " = ? AND " +
                    TABLE_NAME + "." + Fields.ProductId + " = " + ProductTable.TABLE_NAME + "." + ProductTable.Fields.ProductId);
            searchStatement.setString(1, user.customerUsername);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                shoppingList.add(new Pair(ProductTable.productFromResultSet(rs), rs.getInt(Fields.Amount.toString())));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return shoppingList;
    }

    /**
     * Creates and populates the Shopping List table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.CustomerUsername + " VARCHAR(30),"
                    + Fields.ProductId + " INTEGER,"
                    + Fields.Amount + " INTEGER,"
                    + "check(Amount > 0),"
                    + "PRIMARY KEY (" + Fields.CustomerUsername + "," + Fields.ProductId + "),"
                    + "FOREIGN KEY (" + Fields.CustomerUsername + ") REFERENCES " + CustomerUserTable.TABLE_NAME + "(" + CustomerUserTable.Fields.CustomerUsername + "),"
                    + "FOREIGN KEY (" + Fields.ProductId + ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.Fields.ProductId + ")"
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
                insertStatement.setString(1, values[0]);
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

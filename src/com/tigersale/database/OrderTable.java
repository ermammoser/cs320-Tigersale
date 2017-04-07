package com.tigersale.database;

import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Order;
import com.tigersale.model.Product;
import javafx.util.Pair;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermam on 3/24/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the order table within the tigersale.com's database
 */
public class OrderTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "PurchaseOrder";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "Order.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        TransactionId("TransactionId"),
        Date("PurchaseDate"),
        Cost("Cost"),
        Status("Status"),
        AddressId("AddressId"),
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
     * Returns a list of all of the orders for a Customer User
     *
     * @param user The user to search for orders for
     *
     * @return A list of addresses for the user
     */
    public static List<Order> getOrders(CustomerUser user)
    {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + ", " + AddressTable.TABLE_NAME + " WHERE " + TABLE_NAME  + "." + Fields.CustomerUsername + " = ? AND " +
            AddressTable.TABLE_NAME + "." + AddressTable.Fields.AddressId + " = " + TABLE_NAME + "." + Fields.AddressId);
            searchStatement.setString(1, user.customerUsername);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                orders.add(orderFromResultSet(rs));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * Places an order for a user
     *
     * @param user The user to place an order for
     * @param address The address for the order to go to
     * @param products The products that the user ordered as well as the amount
     *
     * @return The number of rows changed
     */
    public static int placeOrder(CustomerUser user, Address address, List<Pair<Product, Integer>> products)
    {
        int numChanged = 0;
        try {

            DatabaseConnection.conn.setAutoCommit(false);

            PreparedStatement insertOrderStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                    TABLE_NAME + "("+ Fields.Date + "," + Fields.Cost + "," + Fields.Status + "," +
                    Fields.AddressId + "," + Fields.CustomerUsername + ") VALUES (GETDATE(),?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            int cost = 0;
            for(Pair<Product, Integer> prodPair : products)
            {
                cost += prodPair.getKey().price * prodPair.getValue();
            }
            insertOrderStatement.setInt(1, cost);
            insertOrderStatement.setInt(2, Order.Status.Placed.ordinal());
            insertOrderStatement.setInt(3, address.id);
            insertOrderStatement.setString(4, user.customerUsername);
            numChanged += insertOrderStatement.executeUpdate();
            ResultSet genKeys = insertOrderStatement.getGeneratedKeys();

            if(genKeys.next())
            {
                int orderId = genKeys.getInt(1);
                for(Pair<Product, Integer> prodPair : products)
                {
                    PreparedStatement insertProdOrderStatement = DatabaseConnection.conn.prepareStatement("INSERT INTO " +
                            ProdToOrderTable.TABLE_NAME + "(" + ProdToOrderTable.Fields.TransactionId + "," +
                            ProdToOrderTable.Fields.ProductId + "," + ProdToOrderTable.Fields.Amount + ") VALUES (?,?,?)");
                    insertProdOrderStatement.setInt(1, orderId);
                    insertProdOrderStatement.setInt(2, prodPair.getKey().productId);
                    insertProdOrderStatement.setInt(3, prodPair.getValue());
                    numChanged += insertProdOrderStatement.executeUpdate();
                }
            }
            else
            {
                DatabaseConnection.conn.rollback();
            }


        }
        catch(SQLException e)
        {
            try {
                DatabaseConnection.conn.rollback();
            }
            catch(SQLException exc)
            {
                exc.printStackTrace();
            }
            e.printStackTrace();
        }
        finally {
            try {
                DatabaseConnection.conn.setAutoCommit(true);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        return numChanged;
    }


    /**
     * Returns a list of all of the products for the order
     *
     * @param order The order to get products for
     *
     * @return A list of products for the order
     */
    public static List<Pair<Product, Integer>> getProducts(Order order)
    {
        List<Pair<Product, Integer>> products = new ArrayList<>();
        try {
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    ProductTable.TABLE_NAME + ", " + ProdToOrderTable.TABLE_NAME +
                    " WHERE " + ProdToOrderTable.TABLE_NAME + "." + ProdToOrderTable.Fields.TransactionId + " = ? AND " +
                    ProdToOrderTable.TABLE_NAME + "." + ProdToOrderTable.Fields.ProductId + " = " + ProductTable.TABLE_NAME + "." + ProductTable.Fields.ProductId);
            searchStatement.setInt(1,order.transactionId);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                products.add(new Pair(ProductTable.productFromResultSet(rs), rs.getInt(ProdToOrderTable.Fields.Amount.toString())));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Creates an order from the given result set
     *
     * @param rs A result set containing an order
     *
     * @return An order from the current result
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static Order orderFromResultSet(ResultSet rs) throws SQLException
    {
        int transactionId = rs.getInt(Fields.TransactionId.toString());
        Date date = rs.getDate(Fields.Date.toString());
        double cost = rs.getBigDecimal(Fields.Cost.toString()).doubleValue();
        Order.Status status = Order.Status.getStatus(rs.getInt(Fields.Status.toString()));
        int addressId = rs.getInt(Fields.AddressId.toString());
        String customerUsername = rs.getString(Fields.CustomerUsername.toString());

        return new Order(transactionId, date, cost, status, AddressTable.addressFromResultSet(rs), customerUsername);
    }



    /**
     * Creates and populates the Order table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + Fields.TransactionId + " INTEGER PRIMARY KEY AUTO_INCREMENT,"
                    + Fields.Date + " DATE,"
                    + Fields.Cost + " DECIMAL(10,2),"
                    + Fields.Status + " INTEGER,"
                    + Fields.AddressId + " INTEGER,"
                    + Fields.CustomerUsername + " VARCHAR(30),"
                    + "FOREIGN KEY (" + Fields.AddressId + ") REFERENCES " + AddressTable.TABLE_NAME + "(" + AddressTable.Fields.AddressId + "),"
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
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setDate(2, Date.valueOf(values[1]));
                insertStatement.setBigDecimal(3, BigDecimal.valueOf(Double.valueOf(values[2])));
                insertStatement.setInt(4, Integer.valueOf(values[3]));
                insertStatement.setInt(5, Integer.valueOf(values[4]));
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

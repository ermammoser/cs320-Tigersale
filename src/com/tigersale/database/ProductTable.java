package com.tigersale.database;

import com.tigersale.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ermam on 3/22/2017 for the tigersale.com application.
 *
 * This class provides an application level interface to interact with
 * the product table within the tigersale.com's database
 */
public class ProductTable {

    /**
     * The date of the table
     */
    public static final String TABLE_NAME = "Product";

    /**
     * The date of the file that contains mock data for the table
     */
    private static final String MOCK_DATA =  "Product.csv";

    /**
     * Helpful enumeration for all of the fields in the table
     */
    public enum Fields
    {
        ProductId("ProductId"),
        Name("Date"),
        Description("Cost"),
        Price("Status"),
        Stock("Stock"),
        Brand("Brand"),
        Category("Category");

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

    public static List<Product> searchForProducts(String searchString)
    {
        ArrayList<Product> products = new ArrayList<>();
        try {
            searchString = "%" + searchString + "%";
            PreparedStatement searchStatement = DatabaseConnection.conn.prepareStatement("SELECT * FROM " +
                    TABLE_NAME + " WHERE " + Fields.Stock + " > 0 AND (" + Fields.Brand + " like ? " +
                    "OR " + Fields.Category + " like ? OR " + Fields.Name + " like ? OR " +
                    Fields.Description + " like ?)");
            searchStatement.setString(1, searchString);
            searchStatement.setString(2, searchString);
            searchStatement.setString(3, searchString);
            searchStatement.setString(4, searchString);
            ResultSet rs = searchStatement.executeQuery();
            while(rs.next())
            {
                products.add(productFromResultSet(rs));
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Creates a product from the given result set
     *
     * @param rs A result set containing a product
     *
     * @return A product from the current result
     * @throws SQLException If there are any missing columns that are requested here
     */
    protected static Product productFromResultSet(ResultSet rs) throws SQLException
    {
        int productId = rs.getInt(Fields.ProductId.toString());
        String name = rs.getString(Fields.Name.toString());
        String description = rs.getString(Fields.Description.toString());
        double price = rs.getBigDecimal(Fields.Price.toString()).doubleValue();
        int stock = rs.getInt(Fields.Stock.toString());
        String brand = rs.getString(Fields.Brand.toString());
        String category = rs.getString(Fields.Category.toString());

        return new Product(productId, name, description, price, stock, brand, category);
    }

    /**
     * Creates and populates the Product table in the database
     */
    protected static void createTable()
    {
        try {
            String createStatement  = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + Fields.ProductId + " INTEGER PRIMARY KEY,"
                    + Fields.Name + " VARCHAR(30),"
                    + Fields.Description + " VARCHAR(500),"
                    + Fields.Price + " DECIMAL(10,2),"
                    + Fields.Stock + " INTEGER,"
                    + Fields.Brand + " VARCHAR(30),"
                    + Fields.Category + " VARCHAR(30)"
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
                insertStatement.setInt(1, Integer.valueOf(values[0]));
                insertStatement.setString(2, values[1]);
                insertStatement.setString(3, values[2]);
                insertStatement.setBigDecimal(4, BigDecimal.valueOf(Double.valueOf(values[3])));
                insertStatement.setInt(5, Integer.valueOf(values[4]));
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

package com.test.database;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.database.DatabaseConnection;
import com.tigersale.database.ProductTable;
import com.tigersale.database.ShoppingListTable;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author James Haller
 */
public class ShoppingListTableTest {
    private static final boolean CREATE_NEW_DATABASE = true;
    private static final String CUSTOMER_USER_NAME = "johnDoe";
    private static final String PASSWORD = "johnsPass";

    private static final String PRODUCT_SEARCH = "RIT Hoodie";

    private CustomerUser testUser;
    private Product testProduct;

    @Before
    public void setUp() throws Exception {
        // Create the database and create new tables
        DatabaseConnection.initializeConnection(CREATE_NEW_DATABASE);



        // Ensure test user exists in mock data
        testUser = CustomerUserTable.login(CUSTOMER_USER_NAME, PASSWORD);
        assertNotNull("User no longer exists in mock data", testUser);

        // Ensure test testProduct exists in mock data
        List<Product> productList = ProductTable.searchForProducts(PRODUCT_SEARCH);
        assertEquals("Only 1 Product in mock data should match '" + PRODUCT_SEARCH + "'", 1, productList.size());
        testProduct = productList.get(0);
    }

    @After
    public void tearDown() throws Exception {
        // Close the database
        DatabaseConnection.conn.close();
    }

    @Test
    public void addProduct() throws Exception {
        // Add product to database
        int updatedRows = ShoppingListTable.addProduct(testUser, testProduct, 1);
        assertEquals("Error inserting product to shopping list table", 1, updatedRows);

        // Check that product exists
        Pair<Product, Integer> resultPair = getTestPair();
        assertNotNull("Test product not found in shopping list", resultPair);

        assertEquals("The shopping cart should only have 1 of the test product", 1, resultPair.getValue().longValue());
        assertEquals("The product was not inserted/retrieved correctly", testProduct.toString(), resultPair.getKey().toString());
    }

    @Test
    public void changeAmountOfProduct() throws Exception {
        int newAmount = 5;

        // Add item to shopping list
        ShoppingListTable.addProduct(testUser, testProduct, 1);

        // Change amount
        ShoppingListTable.changeAmountOfProduct(testUser, testProduct, newAmount);

        // Check that product exists
        Pair<Product, Integer> resultPair = getTestPair();
        assertNotNull("Test product not found in shopping list", resultPair);

        // Check new amount
        assertEquals(newAmount, resultPair.getValue().longValue());
    }

    @Test
    public void deleteProduct() throws Exception {
        // Change amount
        ShoppingListTable.changeAmountOfProduct(testUser, testProduct, 0);

        // Check that product does not exist
        Pair<Product, Integer> resultPair = getTestPair();
        assertNull("Test product still found in shopping list", resultPair);
    }

    @Test
    public void clearShoppingList() throws Exception {
        ShoppingListTable.clearShoppingList(testUser);

        List<Pair<Product, Integer>> shoppingList = ShoppingListTable.getShoppingList(testUser);
        assertEquals("", 0, shoppingList.size());
    }

    private Pair<Product, Integer> getTestPair() {
        List<Pair<Product, Integer>> shoppingList = ShoppingListTable.getShoppingList(testUser);

        Pair<Product, Integer> resultPair = null;
        for(Pair<Product, Integer> pair : shoppingList) {
            if(pair.getKey().productId == testProduct.productId)
                resultPair = pair;
        }

        return resultPair;
    }

}
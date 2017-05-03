package com.test.database;

import com.tigersale.database.DatabaseConnection;
import com.tigersale.database.ProductTable;
import com.tigersale.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by James Haller
 */
public class ProductTableTest {
    private static final boolean CREATE_NEW_DATABASE = true;
    private static final String NAME = "Test Product";
    private static final String DESCRIPTION = "Used for testing purposes";
    private static final double PRICE = 12.34;
    private static final int STOCK = 5;
    private static final String BRAND = "ACME";
    private static final String CATEGORY = "Education";

    private static final String UPDATED_NAME = "Updated Product Testing";
    private static final String UPDATED_DESCRIPTION = "Used for testing purposes (updated)";
    private static final double UPDATED_PRICE = 43.21;
    private static final int UPDATED_STOCK = 15;
    private static final String UPDATED_BRAND = "ACMMostThings";
    private static final String UPDATED_CATEGORY = "Entertainment";

    @Before
    public void setUp() throws Exception {
        // Create the database and create new tables
        DatabaseConnection.initializeConnection(CREATE_NEW_DATABASE);
    }

    @After
    public void tearDown() throws Exception {
        // Close the database
        DatabaseConnection.conn.close();
    }

    @Test
    public void insertProduct() throws Exception {
        // Insert test product
        ProductTable.insertProduct(NAME, DESCRIPTION, PRICE, STOCK, BRAND, CATEGORY);

        // Query for product and check it exists
        List<Product> productList = ProductTable.searchForProducts(NAME);
        assertEquals("Wrong number of products returned by search by Product Name.", 1, productList.size());

        Product resultProduct = productList.get(0);
        assertEquals(NAME, resultProduct.name);
        assertEquals(DESCRIPTION, resultProduct.description);
        assertEquals(PRICE, resultProduct.price, 0.01);
        assertEquals(STOCK, resultProduct.stock);
        assertEquals(BRAND, resultProduct.brand);
        assertEquals(CATEGORY, resultProduct.category);
    }

    @Test
    public void updateProductValues() throws Exception {
        // Insert test product
        ProductTable.insertProduct(NAME, DESCRIPTION, PRICE, STOCK, BRAND, CATEGORY);

        // Get and update product values
        List<Product> productList = ProductTable.searchForProducts(NAME);
        assertEquals("Wrong number of products returned by search by Product Name.", 1, productList.size());

        Product product = productList.get(0);
        product.name = UPDATED_NAME;
        product.description = UPDATED_DESCRIPTION;
        product.price = UPDATED_PRICE;
        product.stock = UPDATED_STOCK;
        product.brand = UPDATED_BRAND;
        product.category = UPDATED_CATEGORY;

        ProductTable.updateProductValues(product);

        // Query for and check updated product
        productList = ProductTable.searchForProducts(NAME);
        assertEquals("Product was not updated.", 0, productList.size());
        productList = ProductTable.searchForProducts(UPDATED_NAME);
        assertEquals("Wrong number of products returned by search by updated Product Name.", 1, productList.size());

        product = productList.get(0);
        assertEquals(UPDATED_NAME, product.name);
        assertEquals(UPDATED_DESCRIPTION, product.description);
        assertEquals(UPDATED_PRICE, product.price, 0.01);
        assertEquals("Stock should not change", STOCK, product.stock);
        assertEquals(UPDATED_BRAND, product.brand);
        assertEquals(UPDATED_CATEGORY, product.category);
    }

    @Test
    public void updateStockValue() {
        // Insert test product
        ProductTable.insertProduct(NAME, DESCRIPTION, PRICE, STOCK, BRAND, CATEGORY);

        // Get product and update stock values
        List<Product> productList = ProductTable.searchForProducts(NAME);
        assertEquals("Wrong number of products returned by search by Product Name.", 1, productList.size());

        Product product = productList.get(0);
        product.stock = UPDATED_STOCK;

        ProductTable.updateStockValue(product, product.stock);

        // Query for and check updated product
        productList = ProductTable.searchForProducts(NAME);
        assertEquals("Wrong number of products returned by search by updated Product Name.", 1, productList.size());
        assertEquals(UPDATED_STOCK, product.stock);
    }

}
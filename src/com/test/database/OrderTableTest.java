package com.test.database;

import com.tigersale.database.*;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Order;
import com.tigersale.model.Product;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by James Haller
 */
public class OrderTableTest {
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
    public void changeOrderStatus() throws Exception {
        List<Order> orderList = OrderTable.getOrders(testUser);
        assertTrue("User " + CUSTOMER_USER_NAME + " must have at least 1 Order in mock data", orderList.size() > 0);

        Order order = orderList.get(0);
        Order.Status status;
        Order testOrder;

        // Test set status to None
        status = Order.Status.None;
        OrderTable.changeOrderStatus(order, status);
        testOrder = OrderTable.getOrders(testUser).get(0);
        assertEquals(status.ordinal(), testOrder.status.ordinal());

        // Test set status to Placed
        status = Order.Status.Placed;
        OrderTable.changeOrderStatus(order, status);
        testOrder = OrderTable.getOrders(testUser).get(0);
        assertEquals(status.ordinal(), testOrder.status.ordinal());

        // Test set status to Shipping
        status = Order.Status.Shipping;
        OrderTable.changeOrderStatus(order, status);
        testOrder = OrderTable.getOrders(testUser).get(0);
        assertEquals(status.ordinal(), testOrder.status.ordinal());

        // Test set status to Delivered
        status = Order.Status.Delivered;
        OrderTable.changeOrderStatus(order, status);
        testOrder = OrderTable.getOrders(testUser).get(0);
        assertEquals(status.ordinal(), testOrder.status.ordinal());

        // Test set status to Canceled
        status = Order.Status.Canceled;
        OrderTable.changeOrderStatus(order, status);
        testOrder = OrderTable.getOrders(testUser).get(0);
        assertEquals(status.ordinal(), testOrder.status.ordinal());
    }

    @Test
    public void placeOrder() throws Exception {
        // Setup
        Address address = AddressTable.getAddresses(testUser).get(0);

        List<Pair<Product, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(testProduct, 1));

        OrderTable.placeOrder(testUser, address, list);

        // Tests
        List<Order> orderList = OrderTable.getOrders(testUser);
        assertTrue("User " + testUser.customerUsername + " should have at least 1 Order in mock data", orderList.size() > 0);
        Order order = orderList.get(orderList.size() - 1);

        // Test order status
        assertEquals(Order.Status.Placed, order.status);
        assertEquals(testProduct.price, order.cost, 0.01);
        assertEquals("Address '" + order.address + "' does not belong to user " + CUSTOMER_USER_NAME,
                address.toString(), order.address.toString());
        assertEquals(testUser.customerUsername, order.customerUsername);
    }

}

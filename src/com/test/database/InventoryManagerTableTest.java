package com.test.database;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.database.DatabaseConnection;
import com.tigersale.database.InventoryManagerTable;
import com.tigersale.model.InventoryManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by James Haller
 */
public class InventoryManagerTableTest {
    private static final boolean CREATE_NEW_DATABASE = true;
    private static final String USER_NAME = "johnDoe";
    private static final String PASSWORD = "password";
    private static final Date HIRE_DATE = Date.valueOf("2017-01-31");
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String MIDDLE_INITIAL = "H";

    private static final String USER_NAME_UNIQUE = "johnDoeUnique";
    
    @Before
    public void setUp() throws Exception {
        // Create the database and create new tables
        DatabaseConnection.initializeConnection(CREATE_NEW_DATABASE);

        // Insert test customer user
        CustomerUserTable.insertCustomerUser(USER_NAME, PASSWORD,
                HIRE_DATE, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL);

    }

    @After
    public void tearDown() throws Exception {
        // Close the database
        DatabaseConnection.conn.close();
    }

    @Test
    public void login() throws Exception {
        InventoryManager inventoryManager = InventoryManagerTable.login(USER_NAME, PASSWORD);
        assertSame(USER_NAME, inventoryManager.username);
        assertSame(PASSWORD, inventoryManager.password);
        assertSame(HIRE_DATE, inventoryManager.hireDate);
        assertSame(FIRST_NAME, inventoryManager.firstName);
        assertSame(LAST_NAME, inventoryManager.lastName);
    }

    @Test
    public void usernameAvaliable() throws Exception {
        // Case: username available
        assertTrue(CustomerUserTable.usernameAvaliable(USER_NAME_UNIQUE));

        // Case: username not available
        assertFalse(CustomerUserTable.usernameAvaliable(USER_NAME));
    }

}

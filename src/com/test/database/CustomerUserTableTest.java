package com.test.database;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.database.DatabaseConnection;
import com.tigersale.model.CustomerUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;

import static org.junit.Assert.*;

/**
 * Created by James Haller
 */
public class CustomerUserTableTest {
    private static final boolean CREATE_NEW_DATABASE = true;
    private static final String CUSTOMER_USER_NAME = "johnDoe";
    private static final String PASSWORD = "password";
    private static final Date DATE_OF_BIRTH = Date.valueOf("2017-01-31");
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String MIDDLE_INITIAL = "H";

    private static final String CUSTOMER_USER_NAME_UNIQUE = "johnDoeUnique";

    @Before
    public void setUp() throws Exception {
        // Create the database and create new tables
        DatabaseConnection.initializeConnection(CREATE_NEW_DATABASE);

        // Insert test customer user
        CustomerUserTable.insertCustomerUser(CUSTOMER_USER_NAME, PASSWORD,
                DATE_OF_BIRTH, FIRST_NAME, LAST_NAME, MIDDLE_INITIAL);
    }

    @After
    public void tearDown() throws Exception {
        // Close the database
        DatabaseConnection.conn.close();
    }

    @Test
    public void login() throws Exception {
        CustomerUser customerUser = CustomerUserTable.login(CUSTOMER_USER_NAME, PASSWORD);
        assertSame(CUSTOMER_USER_NAME, customerUser.customerUsername);
        assertSame(PASSWORD, customerUser.password);
        assertSame(DATE_OF_BIRTH, customerUser.dateOfBirth);
        assertSame(FIRST_NAME, customerUser.firstName);
        assertSame(LAST_NAME, customerUser.lastName);
        assertSame(MIDDLE_INITIAL, customerUser.middleInitial);
    }

    @Test
    public void usernameAvaliable() throws Exception {
        // Case: username available
        assertTrue(CustomerUserTable.usernameAvaliable(CUSTOMER_USER_NAME_UNIQUE));

        // Case: username not available
        assertFalse(CustomerUserTable.usernameAvaliable(CUSTOMER_USER_NAME));
    }

}

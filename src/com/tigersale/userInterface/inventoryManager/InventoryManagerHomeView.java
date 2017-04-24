package com.tigersale.userInterface.inventoryManager;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.tigersale.database.InventoryManagerTable;
import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class InventoryManagerHomeView extends AbstractView {

    /**
     * The logged in user
     */
    private InventoryManager user;

    /**
     * Constructor for the InventoryManagerHomeView
     *
     * @param scanner The scanner to retrieve user input with
     * @param user The logged in user
     */
    public InventoryManagerHomeView(Scanner scanner, InventoryManager user)
    {
        super(scanner);
        this.user = user;
    }

    /**
     * This method represents the logged in users home view
     */
    public void runView() {
        int choice = 0;

        while (true) {
            System.out.println("=========================================================");
            System.out.println("                     Welcome to Home!                    ");
            System.out.println("=========================================================");
            System.out.println("Hello " + user.firstName);

            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Add a new product");
            System.out.println("2: Remove a product");
            System.out.println("3: Edit product description");
            System.out.println("4: Edit price of a product");
            System.out.println("5: Register a new account");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {
                // Go back/log out
                case 0:
                    System.out.println("Logging out.");
                    return;

                // Add a new product
                case 1:
                    break;

                // Remove a new product
                case 2:
                    break;

                // Edit product description
                case 3:
                    break;

                // Edit product price
                case 4:
                    break;

                // Register new InventoryManager
                case 5:
                    (new RegisterInventoryManagerView(scanner)).runView();
                    return;

                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;

            }
        }
    }
}

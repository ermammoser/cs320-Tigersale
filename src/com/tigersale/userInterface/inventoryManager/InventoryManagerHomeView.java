package com.tigersale.userInterface.inventoryManager;

import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

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
            System.out.println("1: View/Edit product list");
            System.out.println("2: View/Edit orders");
            System.out.println("3: Register a new account");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
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

                // View product list
                case 1:
                    (new ProductsListView(scanner)).runView();
                    break;
                //View orders
                case 2:
                    (new InventoryManagerOrderView(scanner, user)).runInventoryManagerOrderView();
                // Register new InventoryManager
                    break;
                case 3:
                    (new RegisterInventoryManagerView(scanner)).runView();
                    break;

                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;

            }
        }
    }
}

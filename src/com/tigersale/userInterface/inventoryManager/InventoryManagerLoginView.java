package com.tigersale.userInterface.inventoryManager;

import com.tigersale.database.InventoryManagerTable;
import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class InventoryManagerLoginView extends AbstractView {

    public InventoryManagerLoginView(Scanner scanner) {
        super(scanner);
    }

    /**
     * This method acts as the view for the InventoryManager Login screen
     */
    public void runInventoryManagerLoginView() {
        int choice = 0;

        while(true)
        {

            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Login");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            }
            catch(InputMismatchException e)
            {
                System.out.println("Enter an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice)
            {
                // Exit this view
                case 0:
                    return;
                // Login
                case 1:
                    System.out.println("Please enter your username.");
                    String username = scanner.next();
                    System.out.println("Please enter your password.");
                    String password = scanner.next();
                    InventoryManager user = InventoryManagerTable.login(username, password);
                    if(user == null)
                    {
                        System.out.println("Sorry, that username-password combination does not exist. Please try again.");
                    }
                    else
                    {
                        // Go to InventoryManagerHomeView
                        System.out.println("You have successfully logged in.");
                        (new InventoryManagerHomeView(scanner, user)).runView();
                    }
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;
            }
        }
    }
}

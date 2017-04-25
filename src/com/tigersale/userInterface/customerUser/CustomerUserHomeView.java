package com.tigersale.userInterface.customerUser;

import com.tigersale.model.CustomerUser;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by ermam on 4/12/2017. for the tigersale.com application.
 */
public class CustomerUserHomeView extends AbstractView{

    /**
     * The logged in user
     */
    CustomerUser user;

    /**
     * Constructor for the CustomerUserHomeView
     *
     * @param scanner The scanner to retrieve user input with
     * @param user The logged in user
     */
    public CustomerUserHomeView(Scanner scanner, CustomerUser user)
    {
        super(scanner);
        this.user = user;
    }

    /**
     * This method represents the logged in users home view
     */
    public void runCustomerUserHomeView()
    {
        int choice = 0;

        System.out.println("=========================================================");
        System.out.println("                     Welcome to Home!                    ");
        System.out.println("=========================================================");
        System.out.println("Hello " + user.firstName);
        while(true)
        {
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Logout");
            System.out.println("3: Update Address Information");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            }
            catch(InputMismatchException e)
            {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice)
            {
                // Quit the application
                case 0:
                    System.out.println("Logging you out.");
                    return;
                case 3:
                    (new CustomerUserAddressView(scanner, user)).runCustomerUserAddressView();
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;

            }
        }
    }
}

package com.tigersale.userInterface;

import com.tigersale.userInterface.customerUser.CustomerUserLoginView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by ermam on 4/12/2017. for the tigersale.com application.
 */
public class WelcomeView extends AbstractView{

    /**
     * Constructor for the WelcomeView
     *
     * @param scanner The scanner to retrieve user input with
     */
    public WelcomeView(Scanner scanner)
    {
        super(scanner);
    }

    /**
     * This method represents the Welcome screen for users
     */
    public void runWelcomeView()
    {
        int choice = 0;

        System.out.println("=========================================================");
        System.out.println("                Welcome to Tigersale.com!                ");
        System.out.println("=========================================================");

        while(true)
        {

            System.out.println("Please choose from the following options (Type the number corresponding to your choice):");
            System.out.println("0: Exit Application");
            System.out.println("1: Enter as a Customer");
            System.out.println("2: Enter as an Inventory Manger");

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
                    System.out.println("Thank you for using Tigersale.com!");
                    return;
                // Go to the CustomerUserLoginView
                case 1:
                    (new CustomerUserLoginView(scanner)).runCustomerUserLoginView();
                    break;
                // Go to the InventoryManagerLoginView
                case 2:
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;

            }
        }
    }
}

package com.tigersale.userInterface.customerUser;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.model.CustomerUser;
import com.tigersale.userInterface.AbstractView;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by ermam on 4/12/2017. for the tigersale.com application.
 */
public class CustomerUserLoginView extends AbstractView{

    /**
     * Constructor for the CustomerUserLoginView
     *
     * @param scanner The scanner to retrieve user input from
     */
    public CustomerUserLoginView(Scanner scanner)
    {
        super(scanner);
    }

    /**
     * This method acts as the view for the CustomerUser Login screen
     */
    public void runCustomerUserLoginView()
    {
        int choice = 0;

        while(true)
        {
            System.out.println();
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Login");
            System.out.println("2: Register");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
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
                    String username = scanner.nextLine();
                    System.out.println("Please enter your password.");
                    String password = scanner.nextLine();
                    CustomerUser user = CustomerUserTable.login(username, password);
                    if(user == null)
                    {
                        System.out.println("Sorry, that username-password combination does not exist. Please try again.");
                    }
                    else
                    {
                        // Go to CustomerUserHomeView
                        System.out.println("You have successfully logged in.");
                        (new CustomerUserHomeView(scanner, user)).runCustomerUserHomeView();
                    }
                    break;
                // Register
                case 2:
                    registerView();
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;
            }
        }
    }

    /**
     * This method acts as the screen for a user to register a new account
     */
    private void registerView()
    {
        String username;
        String password;
        Date dateOfBirth;
        String firstName;
        String lastName;
        String middleInitial;

        // Get the username
        System.out.println("Please type in your username.");
        while (true)
        {
            username = scanner.nextLine();
            if(CustomerUserTable.usernameAvaliable(username))
            {
                break;
            }
            else
            {
                System.out.println("Sorry, that username is already taken. Please try again.");
            }
        }
        // Get the password
        while(true)
        {
            System.out.println("Please type in your desired password.");
            password = scanner.nextLine();
            System.out.println("Please confirm your password.");
            String tempPassword = scanner.nextLine();
            if(password.equals(tempPassword))
            {
                break;
            }
            else
            {
                System.out.println("Sorry, the password to confirm did not match the original. Please try again.");
            }
        }
        // Get the Date of birth
        while(true)
        {
            System.out.println("Please enter your date of birth. (Use the format 'yyyy-mm-dd')");
            try {
                dateOfBirth = Date.valueOf(scanner.nextLine());
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Sorry, what you typed in did not match the correct format. Please try again.");
                continue;
            }
            break;
        }
        System.out.println("Please enter your first name.");
        firstName = scanner.nextLine();
        System.out.println("Please enter your last name.");
        lastName = scanner.nextLine();
        System.out.println("Please enter your middle initial.");
        middleInitial = scanner.next().substring(0,1);

        int result = CustomerUserTable.insertCustomerUser(username, password, dateOfBirth, firstName, lastName, middleInitial);

        if(result > 0) {
            System.out.println("You have successfully been registered");
        }
        else
        {
            System.out.println("Please try again.");
        }

    }
}

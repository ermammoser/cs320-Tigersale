package com.tigersale.userInterface.inventoryManager;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.tigersale.database.InventoryManagerTable;
import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by ermam on 4/12/2017. for the tigersale.com application.
 */
public class InventoryManagerHomeView extends AbstractView{

    /**
     * The logged in user
     */
    InventoryManager user;

    /**
     * Constructor for the CustomerUserHomeView
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
    public void runCustomerUserHomeView() {
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

            Scanner scanner = new Scanner(System.in);
            switch (choice) {
                // Quit the application
                case 0:
                    return;
                case 1:
                    registerView();
                    break;
                case 2:
                    //(new ListOfProductView(scanner, user)).runListOfProductView();
                    break;
                case 3:
                    //(new ListOfProductView(scanner, user)).runListOfProductView();
                    break;
                case 4:
                    //(new ListOfProductView(scanner, user)).runListOfProductView();
                    break;
                case 5:
                    System.out.println("Logging you out.");
                    return;
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
        int salary;
        Date dateOfBirth;
        String firstName;
        String lastName;
        String middleInitial;

        // Get the username
        System.out.println("Please type in your username.");
        while (true)
        {
            username = scanner.next();
            if(InventoryManagerTable.usernameAvaliable(username))
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
            password = scanner.next();
            System.out.println("Please confirm your password.");
            String tempPassword = scanner.next();
            if(password.equals(tempPassword))
            {
                break;
            }
            else
            {
                System.out.println("Sorry, the password to confirm did not match the original. Please try again.");
            }
        }
        // Get salary
        System.out.println("Please type in your salary.");
        while(true)
        {
            try
            {
                salary = scanner.nextInt();
                break;
            }
            catch(TypeMismatchException tme)
            {
                System.out.println("Salary must be an integer. Please try again.");
            }
        }
        // Get the Date of birth
        while(true)
        {
            System.out.println("Please enter your date of birth. (Use the format 'yyyy-mm-dd'");
            try {
                dateOfBirth = Date.valueOf(scanner.next());
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Sorry, what you typed in did not match the correct format. Please try again.");
                continue;
            }
            break;
        }
        System.out.println("Please enter your first name.");
        firstName = scanner.next();
        System.out.println("Please enter your last name.");
        lastName = scanner.next();
        System.out.println("Please enter your middle initial.");
        middleInitial = scanner.next().substring(0,1);

        int result = InventoryManagerTable.insertInventoryManager(username, password, salary, dateOfBirth, firstName, lastName, middleInitial);

        if(result > 0) {
            System.out.println("You have successfully been registered");
        }
        else
        {
            System.out.println("Please try again.");
        }

    }
}

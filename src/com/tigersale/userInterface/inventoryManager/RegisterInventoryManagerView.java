package com.tigersale.userInterface.inventoryManager;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.tigersale.database.InventoryManagerTable;
import com.tigersale.userInterface.AbstractView;

import java.sql.Date;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class RegisterInventoryManagerView extends AbstractView {
    public RegisterInventoryManagerView(Scanner scanner) {
        super(scanner);
    }

    public void runView() {
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
            System.out.println("Please enter your date of birth. (Use the format 'yyyy-mm-dd')");
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
            System.out.println("You have successfully been registered.");
        }
        else
        {
            System.out.println("Please try again.");
        }
    }
}

package com.tigersale.userInterface.customerUser;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.Scanner;

import static com.tigersale.database.AddressTable.getAddresses;
import static com.tigersale.database.AddressTable.insertAddress;

/**
 * Created by JimmmerS on 4/22/17.
 */
public class CustomerUserAddressView extends AbstractView {

    CustomerUser user;

    public CustomerUserAddressView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }

    public void runCustomerUserAddressView(){


        int choice = 0;
        while(true) {
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Add Address Information");
            System.out.println("2: Update Address Information");
            System.out.println("3: View Current Address Information");

            // Try to get a numeric response from the user
            try {

                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {
                // Exit this view
                case 0:
                    return;

                case 1:
                    System.out.println("Please enter your street address.");
                    String street = scanner.next();
                    System.out.println("Please enter your city.");
                    String city = scanner.next();
                    System.out.println("Please enter your state.");
                    String state = scanner.next();
                    System.out.println("Please enter your zip code.");
                    String zip = scanner.next();
                    //CustomerUser user = CustomerUserTable.login(username, password);

                    System.out.println("You have successfully added an address.");
                    insertAddress(street, city, state, zip, user);

                    break;
                // Register
                case 3:
                    System.out.println("Your addresses:\n");

                    for(Address adr: getAddresses(user)){
                        System.out.println("\nStreet:\t\t" + adr.street);
                        System.out.println("City:\t\t" + adr.city);
                        System.out.println("State:\t\t" + adr.state);
                        System.out.println("Zip Code:\t" + adr.zipCode);
                    }
                    System.out.println();
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;

            }
        }

    }
}

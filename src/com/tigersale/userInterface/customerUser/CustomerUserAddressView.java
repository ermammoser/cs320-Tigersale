package com.tigersale.userInterface.customerUser;

import com.tigersale.database.CustomerUserTable;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.tigersale.database.AddressTable.deleteAddress;
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
            System.out.println("2: View/Delete Address Information");
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
                    System.out.flush();
                    String street = scanner.next();

                    System.out.println("Please enter your city.");
                    System.out.flush();
                    String city = scanner.next();

                    System.out.println("Please enter your state.");
                    System.out.flush();
                    String state = scanner.next();

                    System.out.println("Please enter your zip code.");
                    System.out.flush();
                    String zip = scanner.next();

                    while(zip.length() != 5){
                        System.out.println("Error! Zip codes must be 5 digits.");
                        System.out.println("Please enter your zip code.");
                        System.out.flush();
                        zip = scanner.next();
                    }

                    System.out.println("You have successfully added an address.\n");
                    insertAddress(street, city, state, zip, user);

                    break;

                // Update an existing address
                case 2:
                    System.out.println("Your current addresses:");
                    int addrNum = 0;
                    List<Address> addressList = getAddresses(user);

                    for(Address adr: addressList){
                        System.out.println("\nAddress #" + addrNum);
                        System.out.println("Street:\t\t" + adr.street);
                        System.out.println("City:\t\t" + adr.city);
                        System.out.println("State:\t\t" + adr.state);
                        System.out.println("Zip Code:\t" + adr.zipCode);
                        addrNum++;
                    }

                    System.out.println("Enter the address # to remove.");

                    int killChoice = 0;
                    // Try to get a numeric response from the user
                    try {
                        killChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        break;
                    }

                    if(killChoice >= addressList.size()){
                        System.out.println("Error! The number entered is too large.\n");
                        break;
                    }

                    deleteAddress(addressList.get(killChoice));

                    System.out.println();
                    break;
                case 3:
                    System.out.println("Your addresses:");

                    for(Address adr: getAddresses(user)){
                        System.out.println("\nStreet:\t\t" + adr.street);
                        System.out.println("City:\t\t" + adr.city);
                        System.out.println("State:\t\t" + adr.state);
                        System.out.println("Zip Code:\t" + adr.zipCode);
                    }

                    System.out.println();
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.\n");
                    break;

            }
        }

    }
}

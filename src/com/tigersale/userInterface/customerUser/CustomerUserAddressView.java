package com.tigersale.userInterface.customerUser;

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
 * Created by JimmmerS on 4/22/17 for the tigersale.com application.
 */
public class CustomerUserAddressView extends AbstractView {

    CustomerUser user;

    public CustomerUserAddressView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }

    /**
     * Provides a view for a Customer User to edit their addresses
     */
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
                scanner.nextLine();
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
                    String street = scanner.nextLine();

                    System.out.println("Please enter your city.");
                    String city = scanner.nextLine();

                    System.out.println("Please enter your state.");
                    String state = scanner.nextLine();

                    System.out.println("Please enter your zip code.");
                    String zip = scanner.nextLine();

                    while(zip.length() != 5){
                        System.out.println("Error! Zip codes must be 5 digits.");
                        System.out.println("Please enter your zip code.");
                        System.out.flush();
                        zip = scanner.nextLine();
                    }

                    System.out.println("You have successfully added an address.\n");
                    insertAddress(street, city, state, zip, user);

                    break;

                // Delete an existing address
                case 2:
                    System.out.println("Your current addresses:");
                    int addrNum = 1;
                    List<Address> addressList = getAddresses(user);

                    for(Address adr: addressList){
                        System.out.println("\nAddress #" + addrNum);
                        System.out.println("Street:\t\t" + adr.street);
                        System.out.println("City:\t\t" + adr.city);
                        System.out.println("State:\t\t" + adr.state);
                        System.out.println("Zip Code:\t" + adr.zipCode);
                        addrNum++;
                    }

                    System.out.println();
                    System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                    System.out.println("0: Go back");
                    System.out.println("#: Address to delete");

                    int killChoice = 0;
                    // Try to get a numeric response from the user
                    try {
                        killChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        break;
                    }

                    if(killChoice == 0)
                    {
                        continue;
                    }
                    if(killChoice > addressList.size() || killChoice < 0){
                        System.out.println("Error! The number entered is incorrect. Please try again.\n");
                        break;
                    }
                    else {
                        deleteAddress(addressList.get(killChoice - 1), user);
                        System.out.println("Address deleted.");
                    }

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

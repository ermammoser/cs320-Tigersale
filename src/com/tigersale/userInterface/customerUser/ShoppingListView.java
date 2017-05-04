package com.tigersale.userInterface.customerUser;

import com.tigersale.database.AddressTable;
import com.tigersale.database.OrderTable;
import com.tigersale.database.PaymentMethodTable;
import com.tigersale.database.ShoppingListTable;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.PaymentMethod;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;
import javafx.util.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Shopping List view
 * @author My Tran 4/20/2017 for the tigersale.com application.
 */

public class ShoppingListView extends AbstractView {

    /**
     * The logged in user
     */
    private CustomerUser user;

    /**
     * Constructor for the ListOfProductView
     *
     * @param scanner The scanner to retrieve user input from
     */
    public ShoppingListView(Scanner scanner, CustomerUser user)
    {
        super(scanner);
        this.user = user;
    }

    /**
     * This method act as Shopping List view for CustomerUser
     */
    public void runShoppingListView() {
        while (true) {
            int choice;
            //Print items in the list
            List<Pair<Product, Integer>> shoppingList = ShoppingListTable.getShoppingList(user);

            if(shoppingList.size()  <= 0)
            {
                System.out.println("Your shopping list is empty.");
                System.out.println();
                return;
            }

            printShoppingList(shoppingList);

            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println(0 + ": Go Back To View Product List");
            System.out.println(1 + ": Edit Amount");
            System.out.println(2 + ": Remove Product");
            System.out.println(3 + ": Place The Order");


            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch(choice)
            {
                case 0:
                    return;
                case 1:
                    editAmountView(shoppingList);
                    break;
                case 2:
                    removeProductView(shoppingList);
                    break;
                case 3:
                    List<Address> addresses = AddressTable.getAddresses(user);
                    List<PaymentMethod> paymentMethods = PaymentMethodTable.getPaymentMethods(user);
                    if(addresses.size() == 0)
                    {
                        System.out.println("Sorry, you dont have any addresses in the system.  Please exit and input an address, then try again.");
                    }
                    if(paymentMethods.size() == 0)
                    {
                        System.out.println("Sorry, you dont have any payment methods in the system.  Please exit and input a payment method, then try again.");
                    }
                    else
                    {
                        while(true)
                        {
                            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                            System.out.println(0 + ": Go Back To View Product List");
                            System.out.println("#: Address to ship to");

                            int addressNum = 1;
                            for(Address address: addresses)
                            {
                                System.out.println(addressNum + ": " + address);
                                addressNum++;
                            }

                            int addressChoice = 0;
                            // Try to get a numeric response from the user
                            try {
                                addressChoice = scanner.nextInt();
                                scanner.nextLine();
                            } catch (InputMismatchException e) {
                                System.out.println("Please type in an integer corresponding to your preferred option.");
                                scanner.next();
                                continue;
                            }

                            if(addressChoice == 0)
                            {
                                break;
                            }
                            else if(addressChoice > addresses.size() || addressChoice < 0)
                            {
                                System.out.println("Error! The option you chose is invalid. Please try again.");
                            }
                            else
                            {
                                System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                                System.out.println(0 + ": Go Back To View Product List");
                                System.out.println("#: Payment Method to use");

                                int payNum = 1;
                                for(PaymentMethod paymentMethod : paymentMethods)
                                {
                                    System.out.println(payNum + ": " + paymentMethod);
                                    payNum++;
                                }

                                int payChoice = 0;
                                // Try to get a numeric response from the user
                                try {
                                    payChoice = scanner.nextInt();
                                    scanner.nextLine();
                                } catch (InputMismatchException e) {
                                    System.out.println("Please type in an integer corresponding to your preferred option.");
                                    scanner.next();
                                    continue;
                                }

                                if(payChoice == 0)
                                {
                                    break;
                                }
                                else if(payChoice > paymentMethods.size() || payChoice < 0)
                                {
                                    System.out.println("Error! The option you chose is invalid. Please try again.");
                                }
                                else {

                                    OrderTable.placeOrder(user, addresses.get(addressChoice - 1), ShoppingListTable.getShoppingList(user));
                                    ShoppingListTable.clearShoppingList(user);
                                    System.out.println("Your order has been placed.");
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;
            }
        }
    }

    /**
     * Print the Shopping list with numbers associated with items
     * @param shoppingList the shopping list of the CustomerUser
     */
    private void printShoppingList( List<Pair<Product, Integer>> shoppingList) {

        int size = shoppingList.size();
        System.out.println();
        System.out.println("=========================================================");
        System.out.println("                     Shopping List                       ");
        System.out.println("=========================================================");
        int i = 0;
        if (size == 0)
        {
            System.out.println();
            System.out.println("Your shopping list if empty.");
            return;
        }
        while (i < size)
        {
            System.out.println((i+1) + ": " + shoppingList.get(i).getKey().name + ". Amount: " + shoppingList.get(i).getValue());
            i++;
        }
        System.out.println("--------------------------------------------------------");
    }

    /**
     * This method acts as the Edit Amount view for CustomerUser
     * @param shoppingList the current shoppingList
     */
    private void editAmountView( List<Pair<Product, Integer>> shoppingList) {
        int numchanged, newAmount, choice;
        int size = shoppingList.size();
        while (true) {
            //Edit Amount
            printShoppingList(shoppingList);
            System.out.println("Which product do you want to edit amount?");
            System.out.println("Put the number associated with the product");
            System.out.println("OR");
            System.out.println("O: Go Back");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your product.");
                scanner.next();
                continue;
            }
            //Invalid option
            if (choice > size) {
                System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                continue;
            }
            //Go Back to ShoppingListView
            if (choice == 0) {
                return;
            }
            //Valid option, run editAmountView
            Product chosen = shoppingList.get(choice - 1).getKey();

            System.out.println("What is the amount of " + chosen.name + " that you want to change to?");
            // Try to get a numeric response from the user
            while (true) {
                //Try to get an integer
                try {
                    newAmount = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please type in an integer corresponding to your preferred amount.");
                    scanner.next();
                    continue;
                }
                //If newAmount is 0, remove it from the Shopping list:
                if (newAmount == 0) {
                    ShoppingListTable.deleteProduct(user, chosen);
                    System.out.println("Removed " + chosen.name + " from the shopping list.");
                } else {
                    numchanged = ShoppingListTable.changeAmountOfProduct(user, chosen, newAmount);
                    if (numchanged > 0) {
                        System.out.println("You have changed the amount of " + chosen.name + " to " + newAmount);
                    }
                }
                return;
            }
        }
    }

    /**
     * This method acts as a Remove Product view for CustomerUser
     * @param shoppingList the current Shopping List
     */
    private void removeProductView(List<Pair<Product, Integer>> shoppingList) {
        int choice;
        int size = shoppingList.size();
        while (true) {
            //Edit Amount
            printShoppingList(shoppingList);
            System.out.println("Which product do you want to remove from Shopping List?");
            System.out.println("(Put the number associated with the product)");
            System.out.println("OR");
            System.out.println("O: Go Back");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your product.");
                scanner.next();
                continue;
            }
            //Invalid option
            if (choice > size) {
                System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                continue;
            }
            //Go Back to ShoppingListView
            if (choice == 0) {
                return;
            }
            //Valid option, run editAmountView
            Product chosen = shoppingList.get(choice - 1).getKey();
            System.out.println("Are you sure that you want to delete " + chosen.name + "?");
            System.out.println("0: No. Bring me back to Shopping List");
            System.out.println("1: Yes");

            while (true) {
                //Try to get an integer input
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please type in an integer corresponding to your preferred amount.");
                    scanner.next();
                    continue;
                }
                switch (choice)
                {
                    case 0:
                        //Go Back to ShoppingListView
                        break;
                    case 1:
                        //Remove the chosen
                        ShoppingListTable.deleteProduct(user, chosen);
                        System.out.println("Removed " + chosen.name + " from Shopping List.");

                }
                return;
            }
        }
    }

}

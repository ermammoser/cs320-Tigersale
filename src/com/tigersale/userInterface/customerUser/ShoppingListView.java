package com.tigersale.userInterface.customerUser;

import com.tigersale.database.ShoppingListTable;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;
import javafx.util.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Shopping List view
 * @author My Tran
 */

public class ShoppingListView extends AbstractView {


    /**
     * Constructor for the ListOfProductView
     *
     * @param scanner The scanner to retrieve user input from
     */
    public ShoppingListView(Scanner scanner)
    {
        super(scanner);
    }

    /**
     * This method act as Shopping List view for CustomerUser
     * @param user the working CustomerUser
     */
    public void runShoppingListView(CustomerUser user) {
        while (true) {
            int choice, size;
            //Print items in the list
            List<Pair<Product, Integer>> shoppingList = ShoppingListTable.getShoppingList(user);
            size = shoppingList.size();
            printShoppingList(shoppingList);

            System.out.println("---------------------------------------------------------");
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println(size + ": Go Back To View Product List");
            System.out.println((size + 1) + ": Edit Amount");
            System.out.println((size + 2) + ": Remove Product");
            System.out.println((size + 3) + ": Place The Order");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }
            //Invalid choice
            if (choice > (size + 3)) {
                System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                continue;
            }
            //Valid choice
            Product chosen;
            if (choice == size) {
                //Go Back to the List Of Product view
                (new ListOfProductView(scanner)).runListOfProductView(user);
            } else if (choice == (size + 1)) {
                //Edit Amount of the chosen product
                System.out.println("Which product do you want to edit amount?");
                System.out.println("(Put the number associated with the product)");

                // Try to get a numeric response from the user
                while (true) {
                    try {
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Please type in an integer corresponding to your product.");
                        scanner.next();
                        continue;
                    }
                    //Invalid option
                    if (choice > (size + 3)) {
                        System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                        continue;
                    }
                    //Valid option, run editAmountView
                    chosen = shoppingList.get(choice).getKey();
                    editAmountView(user, chosen);
                    return;
                }
            } else if (choice == (size + 2)) {
                //Remove product
                System.out.println("Which product do you want to remove from Shopping List?");
                System.out.println("(Put the number associated with the product)");

                // Try to get a numeric response from the user
                while (true) {
                    try {
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Please type in an integer corresponding to your product.");
                        scanner.next();
                        continue;
                    }
                    //Invalid option
                    if (choice >= shoppingList.size()) {
                        System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                        continue;
                    }
                    //Valid option, run deleteProductView
                    chosen = shoppingList.get(choice).getKey();
                    removeProductView(user, chosen);
                    return;
                }
            } else if (choice == (size + 4))
            {
                //Place an order
                // TODO
                return;
            }

        }
    }

    /**
     * Print the Shopping list with numbers associated with items
     * @param shoppingList the shopping list of the CustomerUser
     */
    private void printShoppingList( List<Pair<Product, Integer>> shoppingList) {

        int size = shoppingList.size();
        System.out.println("=========================================================");
        System.out.println("                     Shopping List                       ");
        System.out.println("=========================================================");
        int i = 0;
        if (size == 0)
        {
            System.out.println("Your shopping list if empty.");
            return;
        }
        while (i < size)
        {
            System.out.println(i + ": " + shoppingList.get(i).getKey().name + ". Amount: " + shoppingList.get(i).getValue());
            i++;
        }

    }

    /**
     * This method acts as the Edit Amount view for CustomerUser
     * @param user the working CustomerUser
     * @param chosen the chosen Product to edit amount
     */
    private void editAmountView(CustomerUser user, Product chosen) {
        int numchanged, newAmount, choice;

        System.out.println("What is the amount of " + chosen.name + " that you want to change to?");
        // Try to get a numeric response from the user
        while (true)
        {
            //Try to get an integer
            try
            {
                newAmount = scanner.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please type in an integer corresponding to your preferred amount.");
                scanner.next();
                continue;
            }
            //If newAmount is 0, remove it from the Shopping list:
            if (newAmount == 0)
            {
                ShoppingListTable.deleteProduct(user, chosen);
                System.out.println("Removed " + chosen.name + " from the shopping list.");
            } else
                {
                numchanged = ShoppingListTable.changeAmountOfProduct(user, chosen, newAmount);
                if (numchanged > 0) {
                    System.out.println("You have changed the amount of " + chosen.name + " to " + newAmount);
                }
            }
            //Type 0 to go back to ShoppingListView
            while (true)
            {
                System.out.println("Please type '0' to Go Back to Shopping List");
                try
                {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e)
                {
                    scanner.next();
                    continue;
                }
                if (choice == 0)
                {
                    runShoppingListView(user);
                    return;
                }
            }
        }
    }

    /**
     * This method acts as a Remove Product view for CustomerUser
     * @param user the working CustomerUser
     * @param chosen the chosen Product to remove from Shopping list
     */
    private void removeProductView(CustomerUser user, Product chosen) {
        System.out.println("Are you sure that you want to delete " + chosen.name + "?");
        System.out.println("0: No. Bring me back to Shopping List");
        System.out.println("1: Yes");

        int choice;
        while (true)
        {
            //Try to get an integer input
            try
            {
                choice = scanner.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please type in an integer corresponding to your preferred amount.");
                scanner.next();
                continue;
            }
            switch (choice)
            {
                //Go Back to ShoppingListView
                case 0:
                    runShoppingListView(user);
                    return;
                //Remove the chosen
                case 1:
                    ShoppingListTable.deleteProduct(user, chosen);
                    System.out.println("Removed " + chosen.name + " from Shopping List.");
                    //Type 0 to go back to ShoppingList
                    while (true)
                    {
                        System.out.println("Please type '0' to Go Back to Shopping List");
                        try
                        {
                            choice = scanner.nextInt();
                        } catch (InputMismatchException e)
                        {
                            scanner.next();
                            continue;
                        }
                        if (choice == 0)
                        {
                            runShoppingListView(user);
                            return;
                        }
                    }
            }
        }
    }

}

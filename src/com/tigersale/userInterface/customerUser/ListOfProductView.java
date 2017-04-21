package com.tigersale.userInterface.customerUser;

import com.tigersale.database.ProductTable;
import com.tigersale.database.ShoppingListTable;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * List of Products View
 * @author My Tran
 */
public class ListOfProductView extends AbstractView {

    /**
     * The logged in user
     */
    private CustomerUser user;
    /**
     * Constructor for the ListOfProductView
     * @param scanner The scanner to retrieve user input from
     */
    public ListOfProductView(Scanner scanner, CustomerUser user) {
        super(scanner);
        this.user = user;
    }

    /**
     * This method acts as the view for the CustomerUser to view the Product list
     */
    public void runListOfProductView() {
        while (true) {
            System.out.println("=========================================================");
            System.out.println("                     List of Products                    ");
            System.out.println("=========================================================");

            //Retrieve the list of products from the database
            List<Product> products = ProductTable.viewProducts();
            int size = products.size();
            int choice;

            if (size == 0)
            {
                System.out.println("There is no products in the system yet");
                return;
            } else
            {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                System.out.println(0 + ": Go Back");
                System.out.println(1 + ": View Shopping List");
                System.out.println("OR");
                System.out.println("-----------------------------------------------------");
                //Print all the products with associated numbers
                System.out.println("Please choose the number associated with the product to view detail:");
                int i = 0;
                while (i < size) {
                    System.out.println((i+2) + ": " + products.get(i).name);
                    i++;
                }


                // Try to get a numeric response from the user
                try {
                    choice = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Please type in an integer corresponding to your preferred option.");
                    scanner.next();
                    continue;
                }

                //Invalid option
                if (choice > (size + 1))
                {
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    continue;
                }
                //Valid option, view detail of the chosen product
                if (choice == 0)
                {
                    return;
                } else if (choice == 1)
                {
                    (new ShoppingListView(scanner, user)).runShoppingListView();
                } else
                {
                    //Product's index = choice - 2 because we have used 0 and 1 for other options above
                    Product chosen = products.get(choice-2);
                    viewProductDetail(chosen);
                }
            }
        }

    }

    /**
     * This method acts as a Product Detail view for CustomerUser
     * @param chosen the chosen Product to view detail
     */
    private void viewProductDetail(Product chosen) {
        while (true)
        {
            System.out.println("=========================================================");
            System.out.println("                     Product Detail                      ");
            System.out.println("=========================================================");
            System.out.println(chosen.toString());
            System.out.println("------------------------------------------------------------");
            System.out.println("Do you want to add this product to your shopping list?");
            System.out.println("0: No. Bring me back to Product List");
            System.out.println("1: Yes");


            int choice;
            // Try to get a numeric response from the user

            try
            {
                choice = scanner.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }
            //Invalid option:
            if (choice > 1)
            {
                System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                continue;
            }
            //Valid option:
            switch (choice)
            {
                //Go Back to ListOfProduct view
                case 0:
                    return;
                //Add the chosen product to Shopping List
                case 1:
                    addProductView(chosen);

            }
        }
    }



    /**
     * This method act as a view for the CustomerUser to add product to the Shopping List
     * @param chosen the chosen Product to add the Shopping List
     */

    private void addProductView(Product chosen) {
        int existingAmount, numchanged, newAmount, choice;
        //Ask for a desired amount
        while (true)
        {
            //Ask for a desired amount of the product to add
            System.out.println("Please type in the amount of " + chosen.name + " that you want to add");
            try
            {
                newAmount = scanner.nextInt();
            } catch (InputMismatchException e)
            {
                System.out.println("Please type in an integer corresponding to your preferred product amount.");
                scanner.next();
                continue;
            }

            //Check for the existing amount first
            existingAmount = ShoppingListTable.searchForAmountOfProducts(user, chosen);
            //If there is none of the chosen, add a new one
            if (existingAmount == 0)
            {
                numchanged = ShoppingListTable.addProduct(user, chosen, newAmount);
                if (numchanged > 0)
                {
                    System.out.println("Added " + newAmount + " of " + chosen.name);
                }
            }
            //Else, update the amount of the product in the Shopping List
            else {
                numchanged = ShoppingListTable.changeAmountOfProduct(user, chosen, (newAmount + existingAmount));
                if (numchanged > 0)
                {
                    System.out.println("Added " + newAmount + " more to " + chosen.name + ". You now have " + (newAmount + existingAmount));
                }
            }
            break;
        }
    }
}


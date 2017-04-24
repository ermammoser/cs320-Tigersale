package com.tigersale.userInterface.inventoryManager;

import com.tigersale.database.ProductTable;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class ProductsListView extends AbstractView {
    public ProductsListView(Scanner scanner) {
        super(scanner);
    }

    public void runView() {
        int choice;

        displayProductList(ProductTable.viewProducts());

        System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
        while(true) {
            System.out.println("0: Go Back");
            System.out.println("1: Search for products");
            System.out.println("2: Add a new product");
            System.out.println("3: Remove a product");
            System.out.println("4: Edit product description or price");
            System.out.println("5: Display product list");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.skip("\n");
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {
                // Go back
                case 0:
                    return;

                // Search products
                case 1:
                    System.out.println("Input search criteria.");
                    String searchString = scanner.nextLine();
                    displayProductList(ProductTable.searchForProducts(searchString));

                    break;

                // Add a new product
                case 2:
                    (new AddProductView(scanner)).runView();
                    break;

                // Remove a product
                case 3:
                    (new RemoveProductView(scanner)).runView();
                    break;

                // Edit product
                case 4:
                    (new EditProductView(scanner)).runView();
                    break;

                // Display product list
                case 5:
                    displayProductList(ProductTable.viewProducts());
                    break;

                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;
            }
        }
    }

    public void displayProductList(List<Product> productList) {
        System.out.println("=========================================================");
        System.out.println("                      Product list!                      ");
        System.out.println("=========================================================");

        for(Product product : productList) {
            System.out.println(product);
        }

        System.out.println("=========================================================");
    }
}

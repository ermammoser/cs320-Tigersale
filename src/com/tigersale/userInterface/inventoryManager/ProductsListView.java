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

        displayProductList();

        while(true) {
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Add a new product");
            System.out.println("2: Remove a product");
            System.out.println("3: Edit product description");
            System.out.println("4: Edit price of a product");
            System.out.println("5: Display product list");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {
                // Go back
                case 0:
                    return;

                // Add a new product
                case 1:
                    (new AddProductView(scanner)).runView();
                    break;

                // Remove a product
                case 2:
                    break;

                // Edit product description
                case 3:
                    break;

                // Edit product price
                case 4:
                    break;

                // Display product list
                case 5:
                    displayProductList();
                    break;

                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.");
                    break;
            }
        }
    }

    public void displayProductList() {
        List<Product> productList = ProductTable.viewProducts();

        System.out.println("=========================================================");
        System.out.println("                      Product list!                      ");
        System.out.println("=========================================================");

        for(Product product : productList) {
            System.out.println(product);
        }

        System.out.println("=========================================================");
    }
}

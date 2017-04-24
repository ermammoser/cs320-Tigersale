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
public class EditProductView extends AbstractView {

    public EditProductView(Scanner scanner) {
        super(scanner);
    }

    public void runView() {
        int choice = 0;
        Product product;


        System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
        while(true) {
            System.out.println("0: Go Back");
            System.out.println("1: Edit product description");
            System.out.println("2: Edit price of a product");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            // Go back
            if(choice == 0) {
                return;
            }

            // Get product from user's input criteria
            System.out.println("Input search criteria for product to update.");
            while(true) {
                List<Product> productList = ProductTable.searchForProducts(scanner.next());

                if(productList.size() < 1) {
                    System.out.println("No products found.");
                    return;
                } else if(productList.size() > 1) {
                    System.out.println("Too many products found. Please narrow search criteria.");
                } else {
                    product = productList.get(0);
                    break;
                }
            }

            // Guard against null (IntelliJ is complaining)
            if(product == null) {
                System.out.println("No products found.");
                return;
            }

            // Edit description
            if(choice == 1) {
                editDescription(product);
                break;
            }

            // Edit price
            if(choice == 2) {
                editPrice(product);
                break;
            }
        }

        // Update database
        ProductTable.updateProductValues(product);
    }

    public void editDescription(Product product) {
        // Display old description
        System.out.println("Current product description.");
        System.out.println(product.description);

        // Get new description
        System.out.println("Input new description.");
        product.description = scanner.next();
    }

    public void editPrice(Product product) {
        // Display old price
        System.out.println("Current product price.");
        System.out.println(product.price);

        // Get new price
        System.out.println("Input new price.");
        while(true) {
            try {
                product.price = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Price must be a decimal number. Please try again.");
            }
        }
    }
}

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
        Product product = null;
        Product oldProduct = null;

        System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
        while(true) {
            System.out.println("0: Go Back");
            System.out.println("1: Edit product description");
            System.out.println("2: Edit price of a product");
            System.out.println("3: Edit product amount");
            System.out.println("4: Save and execute changes");

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.skip("\n");
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            // Go back
            if(choice == 0) {
                return;
            }

            // Only search for new product if no product has been specified yet
            if(product == null) {
                // Get product from user's input criteria
                System.out.println("Input search criteria for product to update.");
                while (true) {
                    List<Product> productList = ProductTable.searchForProducts(scanner.next());

                    if (productList.size() < 1) {
                        System.out.println("No products found.");
                        return;
                    } else if (productList.size() > 1) {
                        System.out.println("Too many products found. Please narrow search criteria.");
                    } else {
                        product = productList.get(0);
                        oldProduct = product.copy();
                        break;
                    }
                }
            }

            switch(choice) {
                // Edit description
                case 1:
                    editDescription(product);
                    break;

                // Edit price
                case 2:
                    editPrice(product);
                    break;

                // Edit amount
                case 3:
                    editAmount(product);
                    break;

                // Save and execute
                case 4:
                    // Ask for confirmation
                    System.out.println("\t" + oldProduct);
                    System.out.println("will be changed to");
                    System.out.println("\t" + product);
                    System.out.println("Are you sure you want to update?");
                    String confirm = scanner.next();
                    if(confirm.charAt(0) != 'Y') {
                        System.out.println("Update aborted.");
                        break;
                    }

                    int result = ProductTable.updateProductValues(product);
                    if(result > 0) {
                        System.out.println("Changes made successfully.");
                    } else {
                        System.out.println("There was a problem with making changes");
                    }
                    return;
            }
        }
    }

    private void editDescription(Product product) {
        // Display old description
        System.out.println("Current product description.");
        System.out.println(product.description);

        // Get new description
        System.out.println("Input new description.");
        product.description = scanner.nextLine();
    }

    private void editPrice(Product product) {
        // Display old price
        System.out.println("Current product price.");
        System.out.println(product.price);

        // Get new price
        System.out.println("Input new price.");
        while(true) {
            try {
                product.price = scanner.nextDouble();
                scanner.skip("\n");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Price must be a decimal number. Please try again.");
            }
        }
    }

    private void editAmount(Product product) {
        // Display old amount
        System.out.println("Current product amount.");
        System.out.println(product.stock);

        // Get new amount
        System.out.println("Input new amount.");
        while(true) {
            try {
                product.stock = scanner.nextInt();
                scanner.skip("\n");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Amount must be an integer. Please try again.");
            }
        }
    }
}

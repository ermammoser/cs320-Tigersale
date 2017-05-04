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
            System.out.println("1: Select product");
            if(product != null)
            {
                System.out.println(product);
                System.out.println("2: Edit product description");
                System.out.println("3: Edit product price");
                System.out.println("4: Edit product stock");
                System.out.println("5: Edit product brand");
                System.out.println("6: Edit product category");
                System.out.println("7: Remove product");
                System.out.println("8: Save and execute changes");
            }

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.skip("\n");
            } catch (InputMismatchException e) {
                System.out.println("Please type in an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            if(choice > 1 && product == null) {
                System.out.println("Must select product before editing information");
                continue;
            }

            int result = 0;
            switch(choice) {
                // Go back
                case 0:
                    return;

                // Select product
                case 1:
                    // Get product from user's input criteria
                    System.out.println("Input search criteria for product to update.");
                    while (true) {
                        List<Product> productList = ProductTable.searchForProducts(scanner.next());
                        scanner.skip("\n");

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
                    break;

                // Edit description
                case 2:
                    editDescription(product);
                    break;

                // Edit price
                case 3:
                    editPrice(product);
                    break;

                // Edit amount
                case 4:
                    editAmount(product);
                    break;

                // Edit brand
                case 5:
                    editBrand(product);
                    break;

                // Edit category
                case 6:
                    editCategory(product);
                    break;

                // Remove product
                case 7:
                    product.stock = 0;
                    result = ProductTable.updateProductValues(product);

                    if(result > 0) {
                        System.out.println(product.name + " successfully removed.");
                    } else {
                        System.out.println("There was an error removing " + product.name + ".");
                    }
                    break;

                // Save and execute
                case 8:
                    // Ask for confirmation
                    System.out.println("\t" + oldProduct);
                    System.out.println("will be changed to");
                    System.out.println("\t" + product);
                    System.out.println("Are you sure you want to update (1 for yes, anything else for no)?");
                    String confirm = scanner.nextLine();
                    if(confirm.charAt(0) != '1') {
                        System.out.println("Update aborted.");
                        break;
                    }

                    if(product != null) {
                        // Edit product values
                        result = ProductTable.updateProductValues(product);

                        // Check that values were updated
                        if(result > 0) {

                            // Edit stock value
                            result = ProductTable.updateStockValue(product, product.stock);

                            // Check that stock was updated
                            if(result > 0) {
                                System.out.println("Changes made successfully.");
                            } else {
                                System.out.println("There was a problem making stock value changes.");
                            }
                        } else {
                            System.out.println("There was a problem making changes.");
                        }
                    }
                    return;
                default:
                    System.out.println("Sorry, the option you picked was incorrect");
                    break;

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

    private void editBrand(Product product) {
        // Display old brand
        System.out.println("Current product brand.");
        System.out.println(product.brand);

        // Get new brand
        System.out.println("Input new brand.");
        product.brand = scanner.nextLine();
    }

    private void editCategory(Product product) {
        // Display old category
        System.out.println("Current product category.");
        System.out.println(product.category);

        // Get new category
        System.out.println("Input new category.");
        product.category = scanner.nextLine();
    }
}

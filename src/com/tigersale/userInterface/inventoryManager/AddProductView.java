package com.tigersale.userInterface.inventoryManager;

import com.tigersale.database.ProductTable;
import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author James Haller
 */
public class AddProductView extends AbstractView {

    public AddProductView(Scanner scanner) {
        super(scanner);
    }

    public void runView() {
        String name;
        String description;
        double price;
        int stock;
        String brand;
        String category;

        // Get name and description
        System.out.println("Input product name.");
        name = scanner.next();
        System.out.println("Input product description.");
        description = scanner.next();

        // Get price
        System.out.println("Input product cost.");
        while(true) {
            try {
                price = scanner.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Cost must be a decimal number. Please try again.");
            }
        }

        // Get stock
        System.out.println("Input product stock amount.");
        while(true) {
            try {
                stock = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Stock amount must be an integer. Please try again.");
            }
        }

        // Get brand and category
        System.out.println("Input product brand.");
        brand = scanner.next();
        System.out.println("Input product category.");
        category = scanner.next();

        int result = ProductTable.insertProduct(name, description, price, stock, brand, category);
        if(result > 0) {
            System.out.println(name + " successfully added.");
        } else {
            System.out.println("There was a problem adding " + name + ".");
        }
    }
}

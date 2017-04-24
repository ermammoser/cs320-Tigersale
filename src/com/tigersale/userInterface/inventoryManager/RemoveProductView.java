package com.tigersale.userInterface.inventoryManager;

import com.tigersale.database.ProductTable;
import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.util.Scanner;

/**
 * @author James Haller
 */
public class RemoveProductView extends AbstractView {

    public RemoveProductView(Scanner scanner) {
        super(scanner);
    }

    public void runView() {
        System.out.println("Input name of product to remove.");
        String productName = scanner.nextLine();
        int result = ProductTable.removeProduct(productName);

        if(result > 0) {
            System.out.println(String.format("%d items deleted.", result));
        } else {
            System.out.println("No items deleted.");
        }
    }
}

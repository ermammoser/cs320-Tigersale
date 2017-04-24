package com.tigersale.userInterface.inventoryManager;

import com.tigersale.model.InventoryManager;
import com.tigersale.userInterface.AbstractView;

import java.util.Scanner;

/**
 * @author James Haller
 */
public class RemoveProductView extends AbstractView {
    private InventoryManager user;

    public RemoveProductView(Scanner scanner, InventoryManager user) {
        super(scanner);
        this.user = user;
    }

    public void runView() {

    }
}

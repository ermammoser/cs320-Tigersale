package com.tigersale.userInterface.inventoryManager;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.tigersale.database.OrderTable;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.InventoryManager;
import com.tigersale.model.Order;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;
import javafx.util.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by hamy on 5/5/17.
 */
public class InventoryManagerOrderView extends AbstractView{
    InventoryManager user;

    public InventoryManagerOrderView(Scanner scanner, InventoryManager user){
        super(scanner);
        this.user = user;
    }

    public void runInventoryManagerOrderView() {

        while (true) {
            System.out.println("=========================================================");
            System.out.println("                          Orders                         ");
            System.out.println("=========================================================");

            List<Order> orders = OrderTable.viewOrders();

            int orderNum = 1;

            for (Order order : orders) {
                System.out.println("Order #" + orderNum);
                System.out.println("Date:\t\t" + order.date);
                System.out.println("Price:\t\t" + order.cost);
                System.out.println("Status:\t\t" + order.status);
                System.out.println("Shipped to:\t\t" + order.address);
                System.out.println("Ordered by:\t\t" + order.customerUsername);
                orderNum++;
            }
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go back");
            System.out.println("#: Order to view");

            int orderChoice;

            // Try to get a numeric response from the user
            try {
                orderChoice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error! Input must be an integer.\n");
                scanner.next();
                continue;
            }

            if (orderChoice == 0) {
                return;
            } else if (orderChoice > orders.size() || orderChoice < 0) {
                System.out.println("Error! The number entered is incorrect Please try again.");

            } else {
                while (true)
                {
                    //Show the chosen order
                    Order order = orders.get(orderChoice - 1);
                    System.out.println("Order #" + orderChoice);
                    System.out.println("Status:\t\t" + order.status);
                    System.out.println("List of products:");

                    List<Pair<Product, Integer>> productInts = OrderTable.getProducts(orders.get(orderChoice - 1));
                    for (Pair<Product, Integer> prodInt : productInts) {
                        System.out.println(prodInt.getKey().name);
                        System.out.println("\tAmount:\t\t" + prodInt.getValue());
                        System.out.println("\tIndividual Price:\t\t" + prodInt.getKey().price);
                    }
                    System.out.println();
                    System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                    System.out.println("0: Go back");
                    System.out.println("1: Edit Order Status");

                    int choice;

                    // Try to get a numeric response from the user
                    try {
                        choice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        scanner.next();
                        continue;
                    }

                    if(choice == 0)
                    {
                        break;
                    }
                    else if(choice == 1)
                    {
                        editStatusView(order, choice);
                    }
                    else
                    {
                        System.out.println("Sorry, the option that you chose is invalid.  Please try again.");
                    }
                }
            }

        }
    }

    private void editStatusView (Order order, int chosen)
    {
        Order.Status current = order.status;
        while (true) {
            System.out.println("Order number: #" + chosen);
            System.out.println("This order's current status is: " + current);
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("#: Status to set");
            System.out.println("1: Placed");
            System.out.println("2: Shipping");
            System.out.println("3: Delivered");
            System.out.println("4: Canceled");

            int choice;

            // Try to get a numeric response from the user
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error! Input must be an integer.\n");
                scanner.next();
                continue;
            }

            if (choice == 0)
            {
                return;
            } else if (choice > 4 || choice < 0)
            {
                System.out.println("Error! The number entered is incorrect Please try again.");
            } else
            {
                Order.Status status = Order.Status.getStatus(choice-1);
                OrderTable.changeOrderStatus(order, status);
                current = status;
                System.out.println("The status has been changed to: " + current);
            }
        }
    }


}

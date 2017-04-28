package com.tigersale.userInterface.customerUser;

import com.tigersale.database.OrderTable;
import com.tigersale.model.Address;
import com.tigersale.model.CustomerUser;
import com.tigersale.model.Order;
import com.tigersale.model.Product;
import com.tigersale.userInterface.AbstractView;
import javafx.util.Pair;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.tigersale.database.AddressTable.*;

/**
 * Created by ermam on 4/28/17 for the tigersale.com application.
 */
public class CustomerUserOrdersView extends AbstractView {

    CustomerUser user;

    public CustomerUserOrdersView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }

    /**
     * Provides a view for a Customer User to view their current and past orders
     */
    public void runCustomerUserOrderView(){

        int choice = 0;
        while(true) {
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: View Orders");
            System.out.println("2: Cancel Orders");

            // Try to get a numeric response from the user
            try {

                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {


                // Exit this view
                case 0:
                    return;
                // View all orders
                case 1:
                    while(true) {
                        System.out.println();
                        System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                        System.out.println("0: Go back");
                        System.out.println("#: Order to view");

                        List<Order> orders = OrderTable.getOrders(user);

                        int orderNum = 1;

                        for (Order order : orders) {
                            System.out.println("Order #" + orderNum);
                            System.out.println("Date:\t\t" + order.date);
                            System.out.println("Price:\t\t" + order.cost);
                            System.out.println("Status:\t\t" + order.status);
                            System.out.println("Shipped to:\t\t" + order.address);
                            orderNum++;
                        }

                        int orderChoice = 0;

                        // Try to get a numeric response from the user
                        try {
                            orderChoice = scanner.nextInt();
                            scanner.nextLine();
                        } catch (InputMismatchException e) {
                            System.out.println("Error! Input must be an integer.\n");
                            break;
                        }

                        if (orderChoice == 0) {
                            break;
                        } else if (orderChoice > orders.size() || orderChoice < 0) {
                            System.out.println("Error! The number entered is incorrect Please try again.");
                        } else {
                            List<Pair<Product, Integer>> productInts = OrderTable.getProducts(orders.get(orderChoice - 1));
                            for (Pair<Product, Integer> prodInt : productInts) {
                                System.out.println(prodInt.getKey().name);
                                System.out.println("\tAmount:\t\t" + prodInt.getValue());
                                System.out.println("\tIndividual Price:\t\t" + prodInt.getKey().price);
                            }
                            System.out.println("Press enter to continue.");
                            scanner.nextLine();
                        }
                    }
                    break;

                // Cancel an order
                case 2:
                    System.out.println();
                    System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                    System.out.println("0: Go back");
                    System.out.println("#: Cancel order");

                    // Only get placed orders
                    List<Order> orders = OrderTable.getOrders(user).stream()
                            .filter(order -> order.status == Order.Status.Placed).collect(Collectors.toList());

                    int orderNum = 1;

                    for (Order order : orders) {
                            System.out.println("Order #" + orderNum);
                            System.out.println("Date:\t\t" + order.date);
                            System.out.println("Status:\t\t" + order.status);
                            System.out.println("Shipping to:\t\t" + order.address);
                            orderNum++;
                    }

                    int orderChoice = 0;

                    // Try to get a numeric response from the user
                    try {
                        orderChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        break;
                    }

                    if (orderChoice == 0) {
                        break;
                    } else if (orderChoice > orders.size() || orderChoice < 0) {
                        System.out.println("Error! The number entered is incorrect Please try again.");
                    } else {
                        OrderTable.changeOrderStatus(orders.get(orderChoice-1), Order.Status.Canceled);
                    }
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.\n");
                    break;

            }
        }

    }
}

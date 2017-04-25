package com.tigersale.userInterface.customerUser;


import com.tigersale.model.CustomerUser;
import com.tigersale.model.PaymentMethod;
import com.tigersale.userInterface.AbstractView;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.tigersale.database.PaymentMethodTable.deletePaymentMethod;
import static com.tigersale.database.PaymentMethodTable.getPaymentMethods;
import static com.tigersale.database.PaymentMethodTable.insertPaymentMethod;

/**
 * Created by JimmmerS on 4/24/17.
 */
public class CustomerUserPaymentView extends AbstractView{
    CustomerUser user;

    public CustomerUserPaymentView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }

    public void runCustomerUserPaymentView(){


        int choice = 0;
        while(true) {
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Add Payment Information");
            System.out.println("2: View/Delete Payment Information");
            System.out.println("3: View Current Payment Information");

            // Try to get a numeric response from the user
            try {

                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter an integer corresponding to your preferred option.");
                scanner.next();
                continue;
            }

            switch (choice) {


                // Exit this view
                case 0:
                    return;

                case 1:
                    System.out.println("Please enter the payment method name. Example: Debit Card");
                    System.out.flush();
                    String paymentName = scanner.next();

                    System.out.println("Please enter the name on the card.");
                    System.out.flush();
                    String nameOnCard = scanner.next();

                    System.out.println("Please enter the card number.");
                    System.out.flush();
                    String cardNumber = scanner.next();

                    while(!isInteger(cardNumber) || cardNumber.length() != 16){
                        System.out.println("Error! A credit card number must be 16 digits and contain only numbers.");
                        System.out.println("Please enter the card number.");
                        System.out.flush();
                        cardNumber = scanner.next();
                    }

                    System.out.println("Please enter the CVC security code.");
                    System.out.flush();
                    String cvc = scanner.next();

                    while(!isInteger(cvc) || cvc.length() != 3){
                        System.out.println("Error! A CVC code must be 3 digits and contain only numbers.\n");
                        System.out.println("Please enter the CVC security code.");
                        System.out.flush();
                        cvc = scanner.next();
                    }

                    System.out.println("Please enter the expiration date in the form mm-yyyy.");
                    System.out.flush();
                    String expiration = scanner.next();

                    while(expiration.length() != 7){
                        System.out.println("Error! The expiration date isn't in the correct form.\n");
                        System.out.println("Please enter the expiration date in the form mm-yyyy.");
                        System.out.flush();
                        expiration = scanner.next();
                    }

                    System.out.println("You have successfully added a payment method.\n");
                    insertPaymentMethod(paymentName, nameOnCard, cardNumber, cvc, expiration, user);

                    break;

                // Update an existing address
                case 2:
                    System.out.println("Your current payment methods:");
                    int payNum = 0;
                    List<PaymentMethod> paymentList = getPaymentMethods(user);

                    for(PaymentMethod pay: paymentList){
                        System.out.println("\nPayment Method #" + payNum);
                        System.out.println("Payment Method Name:\t" + pay.paymentMethodName);
                        System.out.println("Name on Card:\t\t" + pay.nameOnCard);
                        System.out.println("Card Number:\t\t" + "XXXX-XXXX-XXXX-"
                                + pay.creditCardNumber.substring(12, 16));

                        System.out.println("Expiration Date:\t" + pay.expirationDate);
                        payNum++;
                    }

                    System.out.println("Enter the address # to remove.");

                    int killChoice = 0;
                    // Try to get a numeric response from the user
                    try {

                        killChoice = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        break;
                    }

                    if(killChoice >= paymentList.size()){
                        System.out.println("Error! The number entered is too large.\n");
                        break;
                    }

                    deletePaymentMethod(paymentList.get(killChoice));

                    System.out.println();
                    break;
                case 3:
                    System.out.println("Your current payment methods:");

                    for(PaymentMethod pay: getPaymentMethods(user)){
                        System.out.println("Payment Method Name:\t" + pay.paymentMethodName);
                        System.out.println("Name on Card:\t\t" + pay.nameOnCard);
                        System.out.println("Card Number:\t\t" + "XXXX-XXXX-XXXX-"
                                + pay.creditCardNumber.substring(12, 16));
                        System.out.println("Expiration Date:\t" + pay.expirationDate);
                    }

                    System.out.println();
                    break;
                default:
                    System.out.println("I am sorry, the option you chose does not exist. Please try again.\n");
                    break;

            }
        }

    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }
}

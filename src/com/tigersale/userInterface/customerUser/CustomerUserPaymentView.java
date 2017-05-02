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
 * Created by JimmmerS on 4/24/17 for the tigersale.com application
 */
public class CustomerUserPaymentView extends AbstractView{
    CustomerUser user;

    public CustomerUserPaymentView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }


    /**
     * Provides a view for a Customer User to edit their payment methods
     */
    public void runCustomerUserPaymentView(){


        int choice = 0;
        while(true) {
            System.out.println();
            System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
            System.out.println("0: Go Back");
            System.out.println("1: Add Payment Information");
            System.out.println("2: View/Delete Payment Information");
            System.out.println("3: View Current Payment Information");

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

                case 1:
                    System.out.println("Please enter the payment method name. Example: Debit Card");
                    String paymentName = scanner.nextLine();

                    while(paymentName.length() < 2){
                        paymentName = scanner.nextLine();
                    }

                    System.out.println("Please enter the name on the card.");
                    String nameOnCard = scanner.nextLine();

                    while(nameOnCard.length() < 2){
                        System.out.println("Sorry, that name is too short. Please try again.");
                        nameOnCard = scanner.nextLine();
                    }

                    System.out.println("Please enter the card number.");
                    String cardNumber = scanner.nextLine().trim();

                    while(cardNumber.length() != 16){
                        System.out.println("Error! A credit card number must be 16 digits and contain only numbers.");
                        System.out.println("Please enter the card number.");
                        cardNumber = scanner.nextLine().trim();
                    }

                    System.out.println("Please enter the CVC security code.");
                    String cvc = scanner.nextLine();

                    while(!isInteger(cvc) || cvc.length() != 3){
                        System.out.println("Error! A CVC code must be 3 digits and contain only numbers.\n");
                        System.out.println("Please enter the CVC security code.");
                        cvc = scanner.nextLine();
                    }

                    System.out.println("Please enter the expiration date in the form mm-yy.");
                    String expiration = scanner.nextLine();

                    while(expiration.length() != 5 || !isInteger(expiration.substring(0,2))
                            || !isInteger(expiration.substring(3, 5))){

                        System.out.println("Error! The expiration date isn't in the correct form.\n");
                        System.out.println("Example: January 2019 would be entered: 01-19");
                        System.out.println("Please enter the expiration date in the form mm-yy.");
                        expiration = scanner.nextLine();
                    }

                    insertPaymentMethod(paymentName, nameOnCard, cardNumber, cvc, expiration, user);
                    System.out.println("You have successfully added a payment method.\n");

                    break;

                // Delete existing payment method
                case 2:
                    System.out.println("Your current payment methods:");
                    int payNum = 1;
                    List<PaymentMethod> paymentList = getPaymentMethods(user);

                    for(PaymentMethod pay: paymentList){
                        System.out.println("\nPayment Method #" + payNum);
                        System.out.println("Payment Method Name:\t" + pay.paymentMethodName);
                        System.out.println("Name on Card:\t\t\t" + pay.nameOnCard);
                        System.out.println("Card Number:\t\t\t" + "XXXX-XXXX-XXXX-"
                                + pay.creditCardNumber.substring(12, 16));

                        System.out.println("Expiration Date:\t\t" + pay.expirationDate);
                        payNum++;
                    }

                    System.out.println();
                    System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
                    System.out.println("0: Go back");
                    System.out.println("#: Payment Method to delete");

                    int killChoice = 0;

                    // Try to get a numeric response from the user
                    try {

                        killChoice = scanner.nextInt();
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Error! Input must be an integer.\n");
                        break;
                    }

                    if(killChoice == 0)
                    {
                        continue;
                    }
                    if(killChoice > paymentList.size() || killChoice < 0){
                        System.out.println("Error! The number entered is incorrect. Please try again.\n");
                        break;
                    }
                    else {
                        deletePaymentMethod(paymentList.get(killChoice - 1));
                        System.out.println("Payment method deleted.");
                    }

                    System.out.println();
                    break;

                // List all the current payment methods
                case 3:
                    System.out.println();
                    System.out.println("Your current payment methods:");

                    for(PaymentMethod pay: getPaymentMethods(user)){
                        System.out.println("\nPayment Method Name:\t" + pay.paymentMethodName);
                        System.out.println("Name on Card:\t\t\t" + pay.nameOnCard);
                        System.out.println("Card Number:\t\t\t" + "XXXX-XXXX-XXXX-"
                                + pay.creditCardNumber.substring(12, 16));
                        System.out.println("Expiration Date:\t\t" + pay.expirationDate);
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
        for(int i = 0; i < s.length(); i++){
            if("0123456789".indexOf(s.charAt(i)) == -1){
                return false;
            }
        }
        return true;
    }

}

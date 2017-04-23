package com.tigersale.userInterface.customerUser;

import com.tigersale.model.CustomerUser;
import com.tigersale.userInterface.AbstractView;

import java.util.Scanner;

/**
 * Created by JimmmerS on 4/22/17.
 */
public class CustomerUserAddressView extends AbstractView {

    CustomerUser user;

    public CustomerUserAddressView(Scanner scanner, CustomerUser user){
        super(scanner);
        this.user = user;
    }

    public void runCustomerUserAddressView(){
        System.out.println("Please choose from the following options (Enter the number corresponding to your choice):");
        System.out.println("0: Go Back");
        System.out.println("1: Login");
        System.out.println("2: Register");
    }
}

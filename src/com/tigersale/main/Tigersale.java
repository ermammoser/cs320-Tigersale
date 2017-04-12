package com.tigersale.main;

import com.tigersale.database.*;
import com.tigersale.userInterface.WelcomeView;

import java.util.Scanner;

/**
 * Created by ermam on 2/6/2017 for the tigersale.com application.
 *
 * The main entrance to the tigersale.com application
 */
public class Tigersale {

    public static void main(String []args)
    {
        boolean createNewDatabase = false;

        // Handle the arguments
        if(args.length > 1)
        {
            usage();
        }
        else if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("-reset"))
            {
                createNewDatabase = true;
            }
            else
            {
                usage();
            }
        }

        // Create the database
        DatabaseConnection.initializeConnection(createNewDatabase);

        // Create the scanner to handle user input
        Scanner scanner = new Scanner(System.in);

        // Run the welcome view
        (new WelcomeView(scanner)).runWelcomeView();
    }

    public static void usage()
    {
        System.out.println("usage: Tigersale [-reset]");
        System.out.println("    -reset   reset the database.");
        System.exit(1);
    }
}

package com.tigersale.userInterface;

import java.util.Scanner;

/**
 * Created by ermam on 4/12/2017. for the tigersale.com application.
 */
public class AbstractView {

    /**
     * The scanner to retrieve user input from
     */
    protected Scanner scanner;

    public AbstractView(Scanner scanner)
    {
        if(scanner == null)
        {
            throw new NullPointerException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }
}

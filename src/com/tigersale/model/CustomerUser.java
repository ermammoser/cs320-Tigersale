package com.tigersale.model;

import java.util.Date;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * An application representation of a Customer User
 */
public class CustomerUser {
    public String customerUsername;
    public String password;
    public Date dateOfBirth;
    public String firstName;
    public String lastName;
    public String middleInitial;

    public CustomerUser(String customerUsername, String password, Date dateOfBirth,
                        String firstName, String lastName, String middleInitial)
    {
        this.customerUsername = customerUsername;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
    }

    @Override
    public String toString()
    {
        return customerUsername + " : " + password + " - " + dateOfBirth.toString();
    }
}

package com.tigersale.model;

import java.util.Date;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * An application representation of an Inventory Manager
 */
public class InventoryManager {
    public String username;
    public String password;
    public Date hireDate;
    public int salary;
    public String firstName;
    public String lastName;
    public String middleInitial;

    public InventoryManager(String username, String password, int salary, Date hireDate,
                            String firstName, String lastName, String middleInitial)
    {
        this.username = username;
        this.password = password;
        this.salary = salary;
        this.hireDate = hireDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
    }

    @Override
    public String toString()
    {
        return username + " : " + password + " - " + hireDate.toString() + " $" + salary;
    }
}

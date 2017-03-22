package com.tigersale.model;

import java.util.Date;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * An application representation of a Customer User
 */
public class Address {
    public int id;
    public String street;
    public String city;
    public String state;
    public String zipCode;

    public Address(int id, String street, String city, String state, String zipCode)
    {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @Override
    public String toString()
    {
        return id + ": " + street + " " + city + " " + state + ", " + zipCode;
    }
}

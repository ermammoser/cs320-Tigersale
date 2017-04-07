package com.tigersale.model;

import java.util.Date;

/**
 * Created by ermam on 3/26/2017 for the tigersale.com application.
 * An application representation of an Order
 *
 */
public class Order {

    public enum Status
    {
        Placed,
        Shipping,
        Delivered,
        Canceled,
        None;

        public static Status getStatus(int statusInt)
        {
            if(statusInt == Placed.ordinal())
            {
                return Placed;
            }
            else if(statusInt == Shipping.ordinal())
            {
                return Shipping;
            }
            else if(statusInt == Delivered.ordinal())
            {
                return Delivered;
            }
            else if(statusInt == Canceled.ordinal())
            {
                return Canceled;
            }
            return None;
        }
    }

    public int transactionId;
    public Date date;
    public double cost;
    public Status status;
    public Address address;
    public String customerUsername;

    public Order(int transactionId, Date date, double cost, Status status, Address address, String customerUsername)
    {
        this.transactionId = transactionId;
        this.date = date;
        this.cost = cost;
        this.status = status;
        this.address = address;
        this.customerUsername = customerUsername;
    }

    @Override
    public String toString()
    {
        return transactionId + "-" + date + ": " + cost + ", " + status + ", " + address + ", " + customerUsername;
    }
}

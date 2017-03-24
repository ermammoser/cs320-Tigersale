package com.tigersale.model;

/**
 * Created by ermam on 3/20/2017 for the tigersale.com application.
 *
 * An application representation of a Payment Method
 */
public class Product {
    public int productId;
    public String name;
    public String description;
    public double price;
    public int stock;
    public String brand;
    public String category;

    public Product(int productId, String name, String description, double price, int stock, String brand, String category)
    {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
    }

    @Override
    public String toString()
    {
        return productId + "-" + name + ": " + description + ", $" + price+ ", " + stock + ", " + brand+ ", " + category;
    }
}

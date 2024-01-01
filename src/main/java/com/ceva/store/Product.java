/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import java.util.Locale;

/**
 * JavaBean Product
 */
public class Product {
    private int id_product;
    private String name;
    private String description;
    private double price;
    
    public Product(int id_product, String name, String description, double price) {
        this.id_product = id_product;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getFormattedPrice() {
        return String.format(Locale.US, "$%.2f", price);
    }
}

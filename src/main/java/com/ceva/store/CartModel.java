/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.database.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * CartModel maneja la lista de los productos y representa la informacion de carrito de compras
 *           como modelo de datos
 * CartModel es la interface entre el modelo de datos y la vista cart.jsp
 * Como CartModel representa la informacion del carrito de compras del usuario debe estar
 * almacenado en el objeto session porque se debe mantener esta lista de productos mientras el 
 * usuario se encuentra en su session.
 */
public class CartModel {
    // items maneja la coleccion de elementos del carrito de compras
   private List<Item> items;
    
   // metodo static porque no utiliza info de CartModel para generarse
    private static Product readProduct(int id_product) {
        Product p = null;
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("select id_product, name, price, description from product where id_product=?");
            pstmt.setInt(1, id_product);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                p = new Product(id_product, rs.getString("name"), rs.getString("description"), rs.getDouble("price"));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            p = null;
        }
        return p;
    }
    
    public CartModel() {
        items = new LinkedList<>();
    }
    
    public Item findItem(int id_product) {
        for (Item item : items) {
            if (item.getProduct().getId_product() == id_product)
                return item;
        }
        return null;
    }
    
    public List<Item> getItems() {
        return items;
    }
    
    public void addItem(Item item) {
        items.add(item);
        // luego de agregar el item ordenamos la lista
        sortItems();
    }
    
    public Item newItem(int id_product) {
        Product p = readProduct(id_product);
        if (p != null) {
            Item item = new Item(p, 1);
            return item;
        }
        return null;
    }
    
    public void removeItem(Item item) {
        items.remove(item);
    }
    
    /**
     * permite conservar el orden de los item que ingreso el usuario al carrito
     */
    private void sortItems() {
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                // ordenamos por id product
                int id1 = o1.getProduct().getId_product();
                int id2 = o2.getProduct().getId_product();
                if (id1 > id2)
                    return 1;
                else if (id1 < id2)
                    return -1;
                return 0;
            }
        });
    }
    
    // calulamos el total del carrito
    public double getCartTotal() {
        double total = 0;
        for (Item i : items) {
            total += i.getQuantity()*i.getProduct().getPrice();
        }
        return total;
    } 
}

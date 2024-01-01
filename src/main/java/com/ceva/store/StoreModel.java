/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.database.DBUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
/**
 * Clase encargada de leer los productos en la BD
 * @author Test
 */
public class StoreModel {
    // 10 productos por pagina
    private int pageSize = 10;
    // lista de productos que se carga con el metodo loadProducts()
    private List<Product> products; 
    private int productCount; // total de productos
    private int curPage; // sabemos que pagina estamos manejando en el momento
    
    /**
     * Metodo que carga productos
     * page -> nro de pagina que vamos a cargar
     */
    public void loadProducts(int page) {
        curPage = page;
        // inicializamos la coleccion
        products = new LinkedList<>();
        // uso de try with resources
        try (Connection conn = DBUtil.getConnection(); Statement stmt=conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery("select count(*) from product")) {
                rs.next();
                productCount = rs.getInt(1);
            }
            
            try (ResultSet rs = stmt.executeQuery(
                    // query con paginacion con codigos de escape
                    // asi informamos a la BD que bloque de registros vamos a leer
                    String.format("select id_product,name,price,description from product order by id_product {LIMIT %d OFFSET %d}",
                    pageSize, page*pageSize))) {
                while (rs.next()) {
                    Product p = new Product(rs.getInt("id_product"), rs.getString("name"), rs.getString("description"), rs.getDouble("price"));
                    products.add(p);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
        }
    }
    
    // retorna la lista de productos
    public List<Product> getProducts() {
        return products;
    }
    
    public int getProductCount() {
        return productCount;
    }
    
    public int getPageSize() {
        return pageSize;
    }
}

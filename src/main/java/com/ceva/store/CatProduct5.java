/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.database.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 * @author Test
 */
public class CatProduct5 extends CatHandler {
    private static final Map<String,Integer> productMapIndex;
    static {
        productMapIndex = new HashMap<>();
        productMapIndex.put("id_product", 0);
        productMapIndex.put("name", 1);
        productMapIndex.put("description", 2);
        productMapIndex.put("price", 3);
    }

    public CatProduct5() {
        super();
        mapIndex = productMapIndex;
    }
    
    @Override
    boolean doSave() throws IOException {
        boolean error = false;
        int id_product = parseInt(request.getParameter("id_product"), -1);
        String name = request.getParameter("name");
        if ((name == null) || (name.trim().length() == 0)) {
            setError("name", "Falta el nombre del producto.");
            error = true;
        }
        String description = request.getParameter("description");
        double price = 0;
        try {
            price = Double.parseDouble(request.getParameter("price"));
        } catch (NumberFormatException e) {
            setError("price", "N&uacute;mero inv&aacute;lido.");
            error = true;
        }

        if (!error) {
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement pstmt;
                if (id_product == -1) {
                    // Insert
                    pstmt = conn.prepareStatement("insert into product (name,description,price) values (?,?,?)");
                } else {
                    // Update
                    pstmt = conn.prepareStatement("update product set name=?, description=?, price=? where id_product=?");
                    pstmt.setInt(4, id_product);
                }
                pstmt.setString(1, name);
                pstmt.setString(2, description);
                pstmt.setDouble(3, price);
                pstmt.executeUpdate();
                pstmt.close();

                response.sendRedirect("abc_productList5.jsp");
                return true;
            } catch (SQLException e) {
                System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            setInteger("id_product", id_product);
            setString("name", nullToEmpty(name));
            setString("description", nullToEmpty(description));
            setDouble("price", price);
        }
        editFlag = true;
        return false;
    }
    
    @Override
    boolean doEdit() {
        editFlag = true;
        int id_product = parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            try (Connection conn = DBUtil.getConnection()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select name, description, price from product where id_product=" +
                        request.getParameter("id_product"));
                if (rs.next()) {
                    String name = nullToEmpty(rs.getString("name"));
                    String description = nullToEmpty(rs.getString("description"));
                    double price = rs.getDouble("price");

                    setInteger("id_product", id_product);
                    setString("name", name);
                    setString("description", description);
                    setDouble("price", price);
                }
            } catch (SQLException e) {
                System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            setInteger("id_product", -1);
            setString("name", "");
            setString("description", "");
            setDouble("price", 0d);
        }
        return false;
    }
    
    @Override
    boolean doDelete() throws IOException {
        int id_product = parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            try (Connection conn = DBUtil.getConnection()) {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("delete from product where id_product=" + id_product);
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
                e.printStackTrace();
            }
        }
        response.sendRedirect("abc_productList5.jsp");
        return true;
    }
    
    @Override
    boolean doList() {
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select id_product, name, description, price from product order by id_product");
            data = new LinkedList<>();
            while (rs.next()) {
                int id_product = rs.getInt("id_product");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                
                curData = null;
                setInteger("id_product", id_product);
                setString("name", name);
                setString("description", description);
                setDouble("price", price);
                data.add(curData);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public String getPrimaryKey() {
        int id = getInteger("id_product");
        return id != -1 ? String.valueOf(id) : "";
    }
    
    public String asMoney(String name) {
        return String.format(Locale.US, "%.2f", getDouble(name));
    }
    
    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.request = request;
        this.response = response;
        return super.handleRequest();
    }
}

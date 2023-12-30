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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que se encarga de la logica de la peticion del Catalogo de productos
 */
public class CatProduct {
    // todo servlet maneja los objetos HttpServletRequest y HttpServletResponse
    HttpServletRequest request;
    HttpServletResponse response;
    
    // Mapa constante que maneja en cada posicion los campos de la tabla product
    // Nombre del campo como llave y el indice de los campos como valor
    private static final Map<String,Integer> mapIndex;
    // objeto que nos permite recorrer la lista de productos
    private List<Object[]> data;
    /**
     * arreglo que apunta al sgte registro de la lista data
     * curData[0] = id_product
     * curData[1] = name
     * curData[2] = description
     * curData[3] = price
     */
    private Object[] curData;
    // objeto a nivel clase que nos ayuda a iterar la coleccion de data
    private Iterator<Object[]> it = null;
    // mostrar o no la pantalla de captura de datos
    private boolean editFlag;
    
    // inicializamos el mapa. el bloque static se ejecuta cuando se cargue la clase
    // para leer un dato del mapa: String name = String()curData[mapIndex.get("name")];
    static {
        mapIndex = new HashMap<>();
        mapIndex.put("id_product", 0);
        mapIndex.put("name", 1);
        mapIndex.put("description", 2);
        mapIndex.put("price", 3);
    }
    
    public CatProduct(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public static String nullToEmpty(String s) {
        if (s == null)
            return "";
        return s;
    }
    
    public static int parseInt(String str, int defaultValue) {
        if ((str == null) || (str.length() == 0))
            return defaultValue;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    public boolean getEditFlag() {
        return editFlag;
    }
// start: getter para obtener datos del mapa    
    public String getString(String name) {
        return (String) curData[mapIndex.get(name)];
    }
    
    public Double getDouble(String name) {
        return (Double) curData[mapIndex.get(name)];
    }
    
    public Integer getInteger(String name) {
        return (Integer) curData[mapIndex.get(name)];
    }
// end: getter para obtener datos del mapa    
    
// start: setter que nos permiten guardar lo datos leidos dede la BD de un producto en curData
    /**
     * 
     * @param name : nombre del campo
     * @param value : valor del campo
     */
    public void setString(String name, String value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
    
    public void setInteger(String name, Integer value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
    
    public void setDouble(String name, Double value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
// end: setter
    
    /**
     * funcion que controla al objeto iterator it
     * next() retorna true si existe el siguiente registro  
     *        retorna false si ya no hay mas informacion que recorrer.
     * al inicio it va ser null por lo que empieza ejecutando el bloque else
     */
    public boolean next() {
        if (it != null) {
            // preguntamos si existe el sgte registro
            if (it.hasNext()) {
                // pasamos el primer valor del iterator a curData
                curData = it.next();
                return true;
            }
        } else {
            if (data != null) {
                // creamos un nuevo iterator   
                it = data.iterator();
                // llamado recursivo. en le segunda llamada it no va a ser null
                return next(); // asigna curData al sgte registro del iterator
            }
        }
        return false;
    }
    
    /**
     * Guarda un producto en el Catalogo 
     */
    private boolean doSave() throws IOException {
        try (Connection conn = DBUtil.getConnection()) {
            boolean error = false;
            int id_product = parseInt(request.getParameter("id_product"), -1);
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = 0.0;
            try {
                price = Double.parseDouble(request.getParameter("price"));
            } catch (NumberFormatException e) {
                System.out.println("price " + request.getParameter("price"));
                error = true;
            }

            if (!error) {
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
                
                response.sendRedirect("abc_product2.jsp");
                return true;
            }
            editFlag = true; // mostramos el html para la captura de datos del producto
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // false si se produce cualquier tipo de error
    }
    
    /**
     * Modifica un producto del Catalogo
     */
    private boolean doEdit() {
        editFlag = true;
        int id_product = parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            try (Connection conn = DBUtil.getConnection()) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select id_product, name, description, price from product where id_product=" +
                        request.getParameter("id_product"));
                if (rs.next()) {
                    // leemos la informacion obtenida de la BD
                    id_product = rs.getInt("id_product");
                    String name = nullToEmpty(rs.getString("name"));
                    String description = nullToEmpty(rs.getString("description"));
                    double price = rs.getDouble("price");

                    // Guardamos en curData los datos para que sea consumida por el jsp
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
            // caso en el que id_product es -1
            setInteger("id_product", -1);
            setString("name", "");
            setString("description", "");
            setDouble("price", 0d);
        }
        return false;
    }
    
    /**
     * Elimina un producto del catalogo
     */
    private boolean doDelete() throws IOException {
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
        response.sendRedirect("abc_product2.jsp"); // retornamos al listado de productos
        return true;
    }
    
    /**
     * Lista todos los productos de Catalogo
     */
    private boolean doList() {
        try (Connection conn = DBUtil.getConnection()) {
            Statement stmt = conn.createStatement();
            // obtenemos los registros de la tabla
            ResultSet rs = stmt.executeQuery("select id_product, name, description, price from product order by id_product");
            data = new LinkedList<>();
            // recorremos la lista
            while (rs.next()) {
                int id_product = rs.getInt("id_product");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                
                // guardamos en memoria la informacion
                curData = null;
                // si curData es null, los metodos setter crean un objeto curData
                setInteger("id_product", id_product);
                setString("name", name);
                setString("description", description);
                setDouble("price", price);
                // adicionamos el registro al array Data
                // al sgte ciclo while empieza con curData null para q se genere sgte registro
                // al terminar el ciclo while curData va a tener en memoria el ultimo 
                // producto leido de la BD
                data.add(curData);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getClass().getName() + " generated: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * administramos toda la logica del catalogo de productos
     * @return true : cuando la peticion ha sido completamente manejada por la clase y
     *                no es necesario continuar como es el caso cuando la pagina se redirecciona
     *                en operaciones como save, delete
     *         false: Significa que la generacion del html debe continuar
     * @throws IOException 
     */
    public boolean handleRequest() throws IOException {
        String action = request.getParameter("action");
        if ("save".equals(action)) {
            return doSave();
        } else if ("edit".equals(action)) {
            return doEdit();
        } else if ("del".equals(action)) {
            return doDelete();
        } else {
            return doList();
        }
    }
}

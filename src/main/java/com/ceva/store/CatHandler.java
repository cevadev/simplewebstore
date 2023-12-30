/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Clase que agrupa toda la funcionalidad comun a todos los catalogos, como por ejemplo:
 * metodos static nullToEmpty() y parseInt()
 * Los objetos HttpServletRequest y HttpServletResponse
 * Todos los getters y setters
 */
public class CatHandler {
    // protected para poder usarlas en las clases derivadas
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    protected Map<String,Integer> mapIndex;
    protected List<Object[]> data;
    protected Object[] curData;
    private Iterator<Object[]> it = null;
    protected boolean editFlag;
    private String[] errors;
    
    public CatHandler() {
    }
    
    public CatHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        
        mapIndex = null;
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
    
    public String getString(String name) {
        return (String) curData[mapIndex.get(name)];
    }
    
    public void setString(String name, String value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
    
    public Integer getInteger(String name) {
        return (Integer) curData[mapIndex.get(name)];
    }
    
    public void setInteger(String name, Integer value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
    
    public Double getDouble(String name) {
        return (Double) curData[mapIndex.get(name)];
    }
    
    public void setDouble(String name, Double value) {
        if (curData == null)
            curData = new Object[mapIndex.size()];
        curData[mapIndex.get(name)] = value;
    }
    
    public boolean next() {
        if (it != null) {
            if (it.hasNext()) {
                curData = it.next();
                return true;
            }
        } else {
            if (data != null) {
                it = data.iterator();
                return next();
            }
        }
        return false;
    }
    
    void setError(String name, String errorMessage) {
        if (errors == null)
            errors = new String[mapIndex.size()];
        errors[mapIndex.get(name)] = errorMessage;
    }
    
    public String getError(String name) {
        if (errors != null) {
            String err = errors[mapIndex.get(name)];
            if ((err != null) && (err.length() > 0)) {
                return String.format("<font color=\"red\">%s</font>", err);
            }
        }
        return "";
    }
    
    boolean doSave() throws IOException {
        return false;
    }
    
    boolean doEdit() {
        return false;
    }
    
    boolean doDelete() throws IOException {
        return false;
    }
    
    boolean doList() {
        return false;
    }
    
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.ceva.store;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro que se encargar de crear una instancia de storeController el cual maneja
 * el modelo storeModel y tiene los datos listos para vista storeMain.jsp.
 * storeFilter se va a activar con todos los archivos *.jsp
 */
public class StoreFilter implements Filter {
    
    public StoreFilter() {
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
        String servletPath = hRequest.getServletPath();
        if ("/storeMain.jsp".equals(servletPath)) {
            // controlador que lee la lista de productos y la pone disponible para la vista
            StoreController controller = new StoreController();
            // si es true significa que hizo un redirect
            if (controller.handleRequest(hRequest, hResponse))
                return;
        } else if ("/cart.jsp".equals(servletPath)) {
            CartController controller = new CartController();
            if (controller.handleRequest(hRequest, hResponse))
                return;
        }
        
        chain.doFilter(request, response);
    }

    public void destroy() {        
    }

    public void init(FilterConfig filterConfig) {
    }
}

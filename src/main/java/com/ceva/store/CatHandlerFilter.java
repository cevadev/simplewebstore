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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Test
 */
public class CatHandlerFilter implements Filter {
    public CatHandlerFilter() {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
        
        String servletPath = hRequest.getServletPath();
        // si cualquiera de las dos paginas, creamos una instancia de CatProduct5
        if ("/abc_product5.jsp".equals(servletPath) || "/abc_productList5.jsp".equals(servletPath)) {
            CatProduct5 cat = new CatProduct5();
            // el filtro va a trata de manejar la solicitud
            // si retorna true es porque hizo un redirect
            if (cat.handleRequest(hRequest, hResponse))
                return;
            // si no manejo la solicitud va a guardar el objeto cat en el atributo cat
            hRequest.setAttribute("cat", cat);
        }
        
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {        
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }
}

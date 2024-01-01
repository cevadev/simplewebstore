/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.Misc;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Controller -> lee los productos de la tienda. La lectura del los productos se realiza por
 *               medio del modelo.
 */
public class StoreController {
    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // determinamos el nro de pagina
        int page = Misc.parseInt(request.getParameter("pg"), 0);
        if (page > 0)
            page--;
        
        StoreModel model = new StoreModel();
        model.loadProducts(page);
        
        // creamos un atributo model para colocar alli el modelo con los productos cargados
        // disponibles para la vista, este modelo se guarda en el request
        request.setAttribute("model", model);
        
        // hacemos disponiblePageBuilderBar para usalos en la vista
        request.setAttribute("pageBar", PageBarBuilder.getPageBar("storeMain.jsp", model.getProductCount(), model.getPageSize(), page));
        // false -> indicamos que continue la ejecucion de jsp
        return false;
    }
}

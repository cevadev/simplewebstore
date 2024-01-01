/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

import com.ceva.Misc;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Calse que maneja la funcionalidad y operaciones necesarias del Carrito de compras
 */
public class CartController {
    // metodo que nos retorna la misma instancia del modelo de datos almacenado en la session
    // la primera vez que se llame al metodo no se va a encontrar en la sesion el model por lo
    // se tendra que crear una nueva instancia de model y lo guardar en el modelo
    public static CartModel getModel(HttpSession session) {
        // recuperamos la instancia del modelo almacenada en la sesion del usuario
        CartModel model = (CartModel) session.getAttribute("model");
        if (model == null) {
            // si no hay un modelo guardado, creamos una nueva instancia
            model = new CartModel();
            // guardamos en la sesion del usuario el modelo
            // esta tecnica podria no ser la mejor si nuestra app va a ser accedidas por millones
            // de personas, ya que no se podra guardar millones de modelo en la memoria del server
            // por lo que se puede obtar por guardar en disco la sesion o en la pc del usuario
            session.setAttribute("model", model);
            session.setAttribute("items", model.getItems());
            /**
             * si model fuera serializable una alternativa seria implementar un listener de sesion
             * y cada vez que se recarga el contexto y el carrito esta vacio entonces se
             * elimine el cartTotal para que no aparezca $0.00
             */
            session.removeAttribute("cartTotal");
        }
        return model;
    }
    
    // agregamos un item al carrito de compras
    private void doAdd(HttpServletRequest request) {
        HttpSession session = request.getSession();
        CartModel model = getModel(session);
        
        // obtener el item(product) a agregar al carrito, si no se obtiene del request retorn -1
        /* TODO aqui se podrian aplicar mas validaciones */
        int id_product = Misc.parseInt(request.getParameter("id_product"), -1);
        // validamos el id_product sea distinto de -1
        // validaciones pentidientes: hay en stock,
        if (id_product != -1) {
            // al agregar un item nuevo, lo busco si esta en la session guardado
            Item editItem = (Item) session.getAttribute("editItem");
            if (editItem != null)
                // aqui significa que estoy agregamos un item que ya esta en el carrito
                // si eso ocurre entonces lo agrego al modelo
                model.addItem(editItem);
            
            // validamos si el producto ya esta en el modelo
            Item iFound = model.findItem(id_product);
            if (iFound != null) {
                // significa que item esta en el carrito y lo quiero editar
                editItem = iFound;
                // removemos item del modelo
                model.removeItem(editItem);
            } else
                // al no encontrarlo en el modelo lo agregamos al modelo de datos del carrito
                editItem = model.newItem(id_product);
            // colocamos el newItem en la propiedad editItem para asi poder modificar su cantidad
            session.setAttribute("editItem", editItem);
            // mientras agregamos un item no mostramos el total
            session.removeAttribute("cartTotal");
        }
    }
    
    // metodo que permite actualizar el total del carrito
    private void setCartTotal(CartModel model, HttpSession session) {
        double t = model.getCartTotal();
        if (t > 0)
            session.setAttribute("cartTotal", String.format(Locale.US, "$%.2f", t));
        else
            // cuando el carrito esta vacio, removemos cartTotal para que no aparezca $0.00
            session.removeAttribute("cartTotal");
    }
    
    private void doSave(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        CartModel model = getModel(session);
        
        int quantity = Misc.parseInt(request.getParameter("quantity"), 0);
        // obtenemos el item desde la session
        Item editItem = (Item) session.getAttribute("editItem");
        if (editItem != null) {
            // validamos la cantidad que ingresa el user
            if (quantity > 0)
                editItem.setQuantity(quantity);
            // una vez modificada la cantidad lo agregamos al model
            model.addItem(editItem);

            // ya agregado al modelo lo removemos de la session
            session.removeAttribute("editItem");
            setCartTotal(model, session);
        }

        response.sendRedirect("cart.jsp");
    }
    
    private boolean doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        CartModel model = getModel(session);
        
        int id_product = Misc.parseInt(request.getParameter("id_product"), -1);
        if (id_product != -1) {
            Item item = model.findItem(id_product);
            if (item != null) {
                model.removeItem(item);
                setCartTotal(model, session);
                // luego de eliminar, lo enviamos a la pagina de carrito con sus productos
                response.sendRedirect("cart.jsp");
                return true;
            }
        }
        return false;
    }
    
    private void defaultAction(HttpServletRequest request) {
        // obtenemos la session
        HttpSession session = request.getSession();
        // obtenemos el model de carrito guardado en session
        CartModel model = getModel(session);

        Item editItem = (Item) session.getAttribute("editItem");
        if (editItem != null)
            model.addItem(editItem);
        request.getSession().removeAttribute("editItem");
    }
    
    // asi como el controller de la lista principal de productos tenia su handleRequest
    // este metodo se encargara de invocar todas las operaciones en el carrito de compras
    public boolean handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            doAdd(request);
        } else if ("save".equals(action)) {
            doSave(request, response);
            return true;
        } else if ("del".equals(action)) {
            return doDelete(request, response);
        } else
            defaultAction(request);
        
        return false;
    }
}

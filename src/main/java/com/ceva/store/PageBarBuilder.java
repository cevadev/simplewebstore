/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ceva.store;

/**
 * PageBarBuilder es parte de la vista, por ello getPageBar se retiro del modelo
 * url -> costruimos el link
 * productCount -> numero total de productos para saber cuantas paginas mostrar
 * pageSize -> para saber cuantos productos por pagina
 * nPage -> indica la pagina activa
 * La primer pagina es la 0 pero para los usuarios comienza en 1
 * Obtener numero de paginas: numero de productos / nro de productos_por_pagina y
 *                            redondearlo al sgte numero
 */
public class PageBarBuilder {
    public static String getPageBar(String url, int productCount, int pageSize, int nPage) {
        StringBuilder sb = new StringBuilder();
        for (int n=0; n<((productCount + (pageSize-1))/pageSize); n++) {
            if (sb.length() > 0)
                sb.append(" ");
            if (n != nPage) // nrp pag diferente pagina actual 
                // construimos el link
                sb.append(String.format("<a href=\"%s?pg=%d\">", url, (n+1)));
            sb.append(String.valueOf(n+1));
            if (n != nPage)
                sb.append("</a>");
        }
        // si no hay elementos retornamos 1
        return sb.length() > 0 ? sb.toString() : "1";
    }
}

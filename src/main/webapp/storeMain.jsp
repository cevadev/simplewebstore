<!-- storeMain representa vista de la tienda. Hace uso de storeController el cual se encarga
     de manejar el storeModel. storeFilter es el encargado de crear una instancia de
     storeController -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Store main</title>
    </head>
    <body>
        <h1 align="center">&iexcl;Bienvenidos a mi tienda!</h1>
        <!-- Lista de productos -->
        <table align="center" border="1">
            <tr><th>Nombre</th><th>Descripci&oacute;n</th><th></th><th></th></tr>
            <!-- recorremos los productos del modelo guaradado en el request 
                 product es un javabean 
                 Al momento de llamar a la expresion model.products hay un orden en el que se
                 buscan las cosas.
                 Primero a nivel pagina, luego a nivel request, luego session
                 Finalmente a nivel application-->
            <c:forEach var="p" items="${model.products}">
                <!-- no es necesario escribir getName, tomcat lo reconoce por lo que basta name -->
                <tr><td>${p.name}</td>
                    <td>${p.description}</td>
                    <td align="right">${p.formattedPrice}</td>
                    <td><a href="cart.jsp?action=add&id_product=${p.id_product}">Agregar a carrito</a></td>
                </tr>
            </c:forEach>
        </table>
        <br>
        <div align="center">${pageBar}</div>
        
        <p>
        <div align="center"><a href="cart.jsp">Ir a carrito de compras</a></div>
    </body>
</html>


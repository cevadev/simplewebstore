<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Carrito de compras</title>
    </head>
    <body>
        <h1 align="center">Carrito de compras</h1>
        
        <form method="post" action="cart.jsp?action=save">
        <table align="center" border="1">
            <tr><th>Producto</th><th>Precio</th><th>Cantidad</th><th>Total</th><td></td></tr>
            <!-- Mostramos la lista de items que ya estan en el carrito -->
            <c:forEach var="i" items="${items}">
                <tr><td>${i.product.name}</td>
                    <td align="right">${i.product.formattedPrice}</td>
                    <!-- cuando seleccione la accion de adicionar se ira al controller 
                         CartController para llavar al accion de agregar al carrito -->
                    <td align="right"><a href="cart.jsp?action=add&id_product=${i.product.id_product}">${i.quantity}</a></td>
                    <td align="right">${i.subTotalFormatted}</td>
                    <td><a href="cart.jsp?action=del&id_product=${i.product.id_product}">Borrar</a></td>
                </tr>
            </c:forEach>
            <!-- Cuando deseemos agregar un elemento al carito en lugar de agregarlo directamente
                 colocamos al producto en un atributo editItem y asi permitimos que el usuario
                 pueda editar la cantidad que desee. -->
            <c:if test="${editItem != null}">
                <tr><td>${editItem.product.name}</td>
                    <td>${editItem.product.formattedPrice}</td>
                    <td><input type="text" name="quantity" size="3" value="${editItem.quantity}"></td>
                    <td>${editItem.subTotalFormatted}</td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="3"></td>
                    <td><input type="submit" value="Guardar"></td>
                    <td></td>
                </tr>
            </c:if>
            <c:if test="${cartTotal != null}">
                <tr>
                    <td colspan="3" align="right"><b>Total:</b></td>
                    <td align="right">${cartTotal}</td>
                    <td></td>
                </tr>
            </c:if>
        </table>
        </form>
        
        <div align="center">
            <p><a href="storeMain.jsp">Checkout</a></p>
            <p><a href="storeMain.jsp">Seguir comprando</a></p>
        </div>
    </body>
</html>

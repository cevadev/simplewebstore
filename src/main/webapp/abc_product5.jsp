<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.ceva.store.CatProduct5"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
        
<div align="center">
<form method="post" action="abc_product5.jsp">
    <input type="hidden" name="action" value="save">
    <!-- como el objeto cat ya se encuentra en el objeto request podemos usar expresiones 
         para acceder a informacion de objeto, asi evitamos escribir el codigo de escape jsp
         como en el archivo abc_product4.jsp 
         el metodo cat.primaryKey hace referencia al metodo getPrimaryKey con tomcat
         ya no es necesario escribir get o set-->
    <input type="hidden" name="id_product" value="${cat.primaryKey}">
    Nombre: <input type="text" name="name" value="${cat.getString("name")}"> ${cat.getError("name")} <br>
    Descripci&oacute;n: <input type="text" name="description" value="${cat.getString("description")}"> ${cat.getError("description")}<br>
    Precio: <input type="text" name="price" value="${cat.asMoney("price")}"> ${cat.getError("price")}<br>
    <input type="submit" value="Guardar"><p>
    
    <a href="abc_productList5.jsp">Cancelar</a>
</form>
</div>

    </body>
</html>
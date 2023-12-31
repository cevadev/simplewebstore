<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.ceva.store.CatProduct4"%>
<%
CatProduct4 cat = new CatProduct4(request, response);
if (cat.handleRequest()) {
    return;
}
int id_product = cat.getInteger("id_product");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
    
<div align="center">
<form method="post" action="abc_product4.jsp">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id_product" value="<%=id_product != -1 ? id_product : ""%>">
    Nombre: <input type="text" name="name" value="<%=cat.getString("name")%>"> <%=cat.getError("name")%> <br>
    Descripci&oacute;n: <input type="text" name="description" value="<%=cat.getString("description")%>"> <%=cat.getError("description")%><br>
    Precio: <input type="text" name="price" value="<%=String.format(Locale.US, "%.2f", cat.getDouble("price"))%>"> <%=cat.getError("price")%><br>
    <input type="submit" value="Guardar"><p>
    
    <a href="abc_productList4.jsp">Cancelar</a>
</form>
</div>

    </body>
</html>
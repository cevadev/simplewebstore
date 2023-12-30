<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.ceva.store.CatProduct"%>
<%
CatProduct cat = new CatProduct(request, response);
// validamos si se logro manejar la solicitud
// si retorna false, si aun necesita retornar html
if (cat.handleRequest()) {
    // si manejo la solicitud termina
    return;
}
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
        
<div align="center"><h3>Cat&aacute;logo de productos versi&oacute;n 2</h3></div>
<!-- getEditFlag() nos ayuda a manejar el modo edicion. si es true mostramos el form para edicion -->
<% if (cat.getEditFlag()) {
       // obtenemos el id_product ya que lo vamos a usar muchas veces.
       int id_product = cat.getInteger("id_product");
%>
<div align="center">
<form method="post" action="abc_product2.jsp">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id_product" value="<%=id_product != -1 ? id_product : ""%>">
    Nombre: <input type="text" name="name" value="<%=cat.getString("name")%>"><br>
    Descripci&oacute;n: <input type="text" name="description" value="<%=cat.getString("description")%>"><br>
    Precio: <input type="text" name="price" value="<%=String.format(Locale.US, "%.2f", cat.getDouble("price"))%>"><br>
    <input type="submit" value="Guardar"><p>
    
    <a href="abc_product2.jsp">Cancelar</a>
</form>
</div>
<% } else { %>
<!-- lista de los productos -->
<table align="center" border="1">
    <tr>
        <th>id_product</th><th>name</th><th>description</th><th>price</th><th>&nbsp;</th>
    </tr>
<% while (cat.next()) {
       int id_product = cat.getInteger("id_product");
%>
<tr>
    <td><%=id_product%></td>
    <td><%=cat.nullToEmpty(cat.getString("name"))%></td>
    <td><%=cat.nullToEmpty(cat.getString("description"))%></td>
    <td align="right"><%=String.format(Locale.US,"%.2f", cat.getDouble("price"))%></td>
    <td><a href="abc_product2.jsp?action=edit&id_product=<%=id_product%>">[Ed]</a>
        <a href="abc_product2.jsp?action=del&id_product=<%=id_product%>">[X]</a></td>
</tr>
<% } %>
</table>

<div align="center">
<p>
<a href="abc_product2.jsp?action=edit">Nuevo</a><p>
<a href="index.html">Regresar</a>
</div>

<% } %>

    </body>
</html>
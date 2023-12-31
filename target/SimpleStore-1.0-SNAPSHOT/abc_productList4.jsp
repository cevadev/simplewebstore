<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.ceva.store.CatProduct3"%>
<%
CatProduct3 cat = new CatProduct3(request, response);
if (cat.handleRequest()) {
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
        
<div align="center"><h3>Cat&aacute;logo de productos versi&oacute;n 4</h3></div>
        
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
    <td align="right"><%=String.format(Locale.US, "%.2f", cat.getDouble("price"))%></td>
    <td><a href="abc_product4.jsp?action=edit&id_product=<%=id_product%>">[Ed]</a>
        <a href="abc_product4.jsp?action=del&id_product=<%=id_product%>">[X]</a></td>
</tr>
<% } %>
</table>

<div align="center">
<p>
<a href="abc_product4.jsp?action=edit">Nuevo</a><p>
<a href="index.html">Regresar</a>
</div>

    </body>
</html>
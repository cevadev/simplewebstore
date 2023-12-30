<!-- 
    Deficion del comportamiento de la pagina 
    1. Si llaman a la pagina abc_produts sin ninguna informacion adicional entonces mostraremos
        la lista de productos registrados
    2. Si se llama a la pagina abc_products con action=edit sabemos que es una edicion por lo tanto
        mostramos la forma edicion.
-->
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.*,com.ceva.database.DBUtil"%>
<!-- La funcion nullToEmpty() estara fuera del metodo processRequest() por lo que colocamos el signo 
     de exclamacion luego del menor que porciente-->
<%!
    private static String nullToEmpty(String s) {
        if (s == null)
            return "";
        return s;
    }
    private static int parseInt(String str, int defaultValue) {
        if ((str == null) || (str.length() == 0))
            return defaultValue;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
%>
<!-- Al inicio de la pagina abrimos conexion con la BD -->
<%
Connection conn = DBUtil.getConnection();
Statement stmt = conn.createStatement();
ResultSet rs = null;

// controlamos la accion de la pagina pudiendo ser save or edit
String action = request.getParameter("action");
boolean editFlag = false;
int id_product = -1; // -1 significa sin informacion
String name = null;
String description = null;
double price = 0;

if ("edit".equals(action)) {
    editFlag = true;
    // solicitamos el id_product en caso de error pasamos -1
    id_product = parseInt(request.getParameter("id_product"), -1);
    if (id_product != -1) {
        // recuperamos el product a editar
        rs = stmt.executeQuery("select id_product, name, description, price from product where id_product=" + id_product);
        if (rs.next()) {
            id_product = rs.getInt("id_product");
            name = nullToEmpty(rs.getString("name"));
            description = nullToEmpty(rs.getString("description"));
            price = rs.getDouble("price");
        }
    } else {
        // si id_product es -1 signfica que no esta registrado en la BD
        name = "";
        description = "";
    }

} else if ("save".equals(action)) {
    boolean error = false;
    // solicitamos el id_product en caso de error pasamos -1
    id_product = parseInt(request.getParameter("id_product"), -1);
    // Leemos los otros datos del producto
    name = request.getParameter("name");
    description = request.getParameter("description");
    price = 0;
    try {
        price = Double.parseDouble(request.getParameter("price"));
    } catch (NumberFormatException e) {
        error = true;
    }
    
    if (!error) {
        if (id_product == -1) {
            // Insert
            PreparedStatement pstmt = conn.prepareStatement("insert into product (name,description,price) values (?,?,?)");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
        } else {
            // Update
            PreparedStatement pstmt = conn.prepareStatement("update product set name=?, description=?, price=? where id_product=?");
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, id_product);
            pstmt.executeUpdate();
        }
        // informamos al navegador que llame la pagina de abs_products 
        // como no tiene argumento mostrara la lista de repuestos
        response.sendRedirect("abc_product.jsp"); // enviamos a la pantalla de lista de productos
        return;
    }
    editFlag = true;
} else if ("del".equals(action)) {
    id_product = parseInt(request.getParameter("id_product"), -1);
    if (id_product != -1) {
        stmt.executeUpdate("delete from product where id_product=" + id_product);
    }
    response.sendRedirect("abc_product.jsp");
    return;
} else {
    // Obtenemos una lista de los productos registrados en la BD
    rs = stmt.executeQuery("select id_product, name, description, price from product order by id_product");
}
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cat&aacute;logo de productos</title>
    </head>
    <body>
        
<div align="center"><h3>Cat&aacute;logo de productos versi&oacute;n 1</h3></div>

<% if (editFlag) { %>
<!-- Mostramos el formulario de edicion -->
    <div align="center">
        <form method="post" action="abc_product.jsp">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id_product" value="<%=id_product != -1 ? id_product : ""%>">
            Nombre: <input type="text" name="name" value="<%=name%>"><br>
            Descripci&oacute;n: <input type="text" name="description" value="<%=description%>"><br>
            Precio: <input type="text" name="price" value="<%=String.format("%.2f", price)%>"><br>

            <!<!-- Cuando se presiona el boton guardar: 
                    1. Se lee el campo action que dice save 
                    2. Los datos que se envian deben ser almacenados en la BD
                    3. La accion se realiza en la misma pagina abc_product entonces
                        el codigo para almacenar esta informacion en la BD estara en esta pagina-->
            <input type="submit" value="Guardar"><p>
            <a href="abc_product.jsp">Cancelar</a>
        </form>
    </div>
<% } 
else { %>
<!-- si editFlag es false construimos la lista de productos -->
<table align="center" border="1">
    <tr>
        <th>id_product</th><th>name</th><th>description</th><th>price</th><th>&nbsp;</th>
    </tr>
<% while (rs.next()) { %>
    <tr>
        <td><%=rs.getInt("id_product")%></td>
        <td><%=nullToEmpty(rs.getString("name"))%></td>
        <td><%=nullToEmpty(rs.getString("description"))%></td>
        <td align="right"><%=String.format("%.2f", rs.getDouble("price"))%></td>
        <!-- Botones para editar el registro y eliminar el registro -->
        <td>
            <a href="abc_product.jsp?action=edit&id_product=<%=rs.getInt("id_product")%>">[Edit]</a>
            <a href="abc_product.jsp?action=del&id_product=<%=rs.getInt("id_product")%>">[Delete]</a>
        </td>
    </tr>
<% } %>
</table>

<div align="center">
<p>
<a href="abc_product.jsp?action=edit">Nuevo</a><p>
<a href="index.html">Regresar</a>
</div>

<% } %>

    </body>
</html>
<!-- Cerramos la conexion a la BD -->
<%
if (rs != null)
    rs.close();
stmt.close();
conn.close();
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>P&aacute;gina de forward</h1>
        <!-- jsp:forward esta directiva le dice a tomcat que deje de usar forwardTest.jsp y procese 
             index.html. El control lo toma la pagina que indiquemos en page
             jsp:include esta directiva toma la salida de index.html e insertarla en
             la linea 13 de este archivo-->
        <jsp:include page="index.html"/>
        
    </body>
</html>

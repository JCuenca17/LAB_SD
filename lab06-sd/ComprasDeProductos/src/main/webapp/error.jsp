<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Error en la aplicación</title>
        <link rel="stylesheet" type="text/css" href="css/error.css">
    </head>
    <body>
        <div class="error-container">
            <h1>Se ha producido un error</h1>

            <%
                if (exception != null) {
            %>
            <p><strong>Mensaje técnico:</strong> <%= exception.getMessage()%></p>
            <%
                }
            %>

            <%
                java.util.List<String> mensajes = (java.util.List<String>) request.getAttribute("mensajes");
                if (mensajes != null) {
            %>
            <h3>Información para el usuario:</h3>
            <ul>
                <% for (String msg : mensajes) {%>
                <li><%= msg%></li>
                    <% } %>
            </ul>
            <%
                }
            %>
            <form action="index.jsp" method="get">
                <button type="submit">Volver a intentar</button>
            </form>
        </div>
    </body>
</html>

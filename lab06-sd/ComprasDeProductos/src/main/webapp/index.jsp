<%@ page errorPage="error.jsp" %>
<html>
    <head>
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/estilos.css">
    </head>
    <body>
        <h2>Iniciar Sesi�n</h2>
        <form action="LoginServlet" method="post">
            Usuario: <input type="text" name="usuario" /><br />
            Contrase�a: <input type="password" name="clave" /><br />
            <input type="submit" value="Ingresar" />
        </form>
    </body>
</html>

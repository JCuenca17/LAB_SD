<%@ page errorPage="error.jsp" %>
<html>
    <head>
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="css/estilos.css">
    </head>
    <body>
        <h2>Iniciar Sesión</h2>
        <form action="LoginServlet" method="post">
            Usuario: <input type="text" name="usuario" /><br />
            Contraseña: <input type="password" name="clave" /><br />
            <input type="submit" value="Ingresar" />
        </form>
    </body>
</html>

package compra.productos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    private final String USUARIO_CORRECTO = "admin";
    private final String CLAVE_CORRECTA = "1234";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        List<String> mensajes = new ArrayList<>();

        if (usuario == null || clave == null || usuario.isEmpty() || clave.isEmpty()) {
            mensajes.add("El usuario o la contraseña no deben estar vacíos.");
        } else if (!usuario.equals(USUARIO_CORRECTO) || !clave.equals(CLAVE_CORRECTA)) {
            mensajes.add("Usuario o contraseña incorrectos.");
        }

        if (!mensajes.isEmpty()) {
            request.setAttribute("mensajes", mensajes);
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<h2>¡Bienvenido, " + usuario + "!</h2>");
    }
}

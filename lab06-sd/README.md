# Lab 6: servicios web en GlassFish

Proyecto Maven `ComprasDeProductos`, para desplegar en GlassFish o Payara desde NetBeans.

- `compra/productos/compraproductos.java`: servicio web SOAP (JAX-WS) con la operación `comprasProductos`, que recibe las cantidades de pan, queso, plátanos y naranjas y devuelve el detalle de la compra.
- `compra/productos/LoginServlet.java`: servlet que valida usuario y contraseña desde el formulario de `index.jsp` y muestra un error si están vacíos o no coinciden.
- `ComprasProductosServer/`: configuración de JAX-RS que genera NetBeans por defecto.

`ComprasDeProductos.zip` es una copia comprimida del mismo proyecto.

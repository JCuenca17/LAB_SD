# Lab 7: servicio SOAP de ventas

Servicio web hecho con JAX-WS que no necesita servidor de aplicaciones: `SoapPublisher` lo publica con `Endpoint.publish` en `http://localhost:8080/VentasService`.

`VentasService` tiene dos operaciones:

- `listarProductos`: devuelve los productos con su precio y stock.
- `comprarProducto`: recibe el nombre y la cantidad, descuenta el stock y devuelve el total.

## Ejecutarlo

Es un proyecto Maven. Al ejecutar `SoapPublisher`, el WSDL queda en `http://localhost:8080/VentasService?wsdl` y se puede probar con SoapUI o Postman.

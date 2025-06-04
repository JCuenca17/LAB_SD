package com.ventas.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.HashMap;
import java.util.Map;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class VentasService {

    // Clase interna para guardar precio y cantidad
    public static class ProductoInfo {
        public double precio;
        public int cantidad;

        public ProductoInfo(double precio, int cantidad) {
            this.precio = precio;
            this.cantidad = cantidad;
        }
    }

    private static final Map<String, ProductoInfo> productos = new HashMap<>();

    static {
        productos.put("Laptop", new ProductoInfo(1200.0, 5));
        productos.put("Mouse", new ProductoInfo(25.5, 10));
        productos.put("Teclado", new ProductoInfo(45.9, 7));
    }

    @WebMethod
    public String listarProductos() {
        StringBuilder sb = new StringBuilder("Productos disponibles:\n");
        productos.forEach((nombre, info) ->
                sb.append(nombre)
                        .append(": $").append(info.precio)
                        .append(" (Cantidad: ").append(info.cantidad)
                        .append(")\n"));
        return sb.toString();
    }

    @WebMethod
    public double comprarProducto(String nombreProducto, int cantidad) {
        if (productos.containsKey(nombreProducto)) {
            ProductoInfo info = productos.get(nombreProducto);
            if (info.cantidad >= cantidad) {
                info.cantidad -= cantidad; // Restar cantidad disponible
                return info.precio * cantidad;
            } else {
                return -2; // No hay stock suficiente
            }
        } else {
            return -1; // Producto no encontrado
        }
    }
}

# Lab 8: JDBC con MySQL y Swing

Ventanas en Swing que trabajan sobre la base `EmpresaMSQL` en MySQL.

- `Ventana.java`: recorre los registros con Primero, Anterior, Siguiente y Último, y permite insertar, modificar y eliminar.
- `Ventana1.java`: un cuadro para escribir una consulta SQL y ver el resultado.
- `BaseDatos2.java`: la versión inicial del navegador de registros, sobre la tabla `Categorias`.

`orden.sql` crea las tablas y `insertar.sql` carga los datos.

## Ejecutarlo

Desde `resuelto/`, con la base creada:

```
javac -cp ".;lib/mysql-connector-j-8.4.0.jar" Ventana.java -d bin
java -cp ".;lib/mysql-connector-j-8.4.0.jar;bin" Ventana
```

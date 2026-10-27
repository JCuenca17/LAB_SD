# Biblioteca con RMI, Swing y MySQL

Versión final del propuesto 2. El servidor crea su propio registro RMI en el puerto 2000 y guarda los libros en MySQL. El cliente es una ventana en Swing para ver los libros disponibles, pedirlos prestados, devolverlos y agregar nuevos con título, autor, stock e imagen.

## Estructura

```
src/interfaces/   LibraryInterface (contrato remoto)
src/server/       Library (acceso a MySQL) y LibraryServer
src/client/       LibraryGUI
lib/              conector de MySQL
```

## Ejecutarlo

1. Crear en MySQL una base llamada `biblioteca`. La conexión está en `src/server/Library.java` (usuario `root`, sin contraseña).
2. Compilar las fuentes en `bin/`.
3. Ejecutar `run_servidor.bat` y, cuando el servidor esté arriba, `run_cliente.bat`.

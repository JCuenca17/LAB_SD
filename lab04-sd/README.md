# Lab 4: Java RMI

Objetos remotos con RMI: el servidor registra un objeto y el cliente lo usa como si fuera local.

| Carpeta | Qué hace |
|---|---|
| `Resuelto` | Calculadora remota. Es el ejemplo de introducción |
| `Propuesto 1` | Farmacia: agregar medicinas, consultar el stock y comprar una cantidad. El servidor se registra como `PHARMACY` |
| `Propuesto 2` | Biblioteca: listar los libros disponibles y pedir uno prestado |
| `propuesto2_final` | La misma biblioteca con interfaz en Swing, devolución y alta de libros, y los datos en MySQL |

## Ejecutar los ejercicios básicos

En cada carpeta, con tres terminales:

```
javac *.java
rmiregistry
java CalculatorServer
java CalculatorClient 5 3
```

En Java 8 o anterior hay que generar el stub con `rmic CalculatorImpl` antes de iniciar el registro. Para los propuestos se cambian los nombres de las clases (`ServerSide` y `ClienteSide`, `LibraryServer` y `LibraryClient`).

El README de [propuesto2_final](propuesto2_final) explica cómo levantar la versión con base de datos.

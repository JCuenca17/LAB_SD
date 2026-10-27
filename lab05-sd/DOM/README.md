# Validación con DOM

`src/ValidarBOE.java` abre `boe.xml` (un sumario del Boletín Oficial del Estado) con `DocumentBuilderFactory` y avisa si el documento no está bien formado. La validación contra un DTD está desactivada (`setValidating(false)`); esa parte se hace en el ejercicio de SAX.

```
javac -d bin src/ValidarBOE.java
java -cp bin ValidarBOE
```

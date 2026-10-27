# Ejemplo de commit y rollback

Basado en un ejemplo de [jc-mouse.net](https://www.jc-mouse.net/). Las dos clases hacen varios `INSERT` en `miTabla` y `miOtraTabla`, en la base `lab9`:

- `JNoRollback` hace los `INSERT` sin manejar la transacción.
- `JNoRollback2` mete a propósito un dato de tipo incorrecto en el cuarto `INSERT` y, al fallar, llama a `rollback()`.

Ojo: ninguna de las dos desactiva el autocommit, así que en ambas los `INSERT` que se ejecutaron antes del error quedan guardados y el `rollback()` no tiene nada que deshacer. Para que la segunda versión funcione como se espera hay que llamar a `connection.setAutoCommit(false)` justo después de abrir la conexión.

La conexión está en `Database.java`.

```
javac -cp ".;lib/mysql-connector-j-8.4.0.jar" Database.java JNoRollback.java JNoRollback2.java
java -cp ".;lib/mysql-connector-j-8.4.0.jar" JNoRollback
java -cp ".;lib/mysql-connector-j-8.4.0.jar" JNoRollback2
```

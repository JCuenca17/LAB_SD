# Transferencias entre usuarios

App de escritorio en Swing sobre la base `banco` en MySQL. Después de iniciar sesión, el usuario ve su saldo y puede transferir dinero a otro.

La transferencia está en `UserDAO.transferir`: desactiva el autocommit, descuenta el saldo de uno, lo suma al otro y confirma con `commit()`. Si algo falla en el medio, hace `rollback()` y ningún saldo cambia.

## Estructura

```
src/DBConnection.java   conexión a MySQL
src/UserDAO.java        consultas y transferencia
src/LoginFrame.java     inicio de sesión (punto de entrada)
src/MainFrame.java      saldo y formulario de transferencia
lib/                    conector de MySQL
```

## Ejecutarlo

```
javac -cp "lib/*" -d bin src/*.java
java -cp "bin;lib/*" LoginFrame
```

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8" />
    <title>Ventas en línea - Cliente SOAP con AJAX</title>
    <link rel="stylesheet" href="estilos.css" />
</head>

<body>

    <h1>Ventas en línea</h1>

    <h2>Productos disponibles</h2>
    <table id="tabla-productos">
        <thead>
            <tr>
                <th>Producto</th>
                <th>Precio (USD)</th>
                <th>Cantidad disponible</th>
            </tr>
        </thead>
        <tbody>
            <!-- Productos cargados dinámicamente -->
        </tbody>
    </table>

    <h2>Comprar producto</h2>
    <form id="form-compra">
        <label for="producto">Seleccione un producto:</label>
        <select name="producto" id="producto" required>
            <option value="" disabled selected>-- Elija un producto --</option>
        </select>

        <label for="cantidad">Cantidad:</label>
        <input type="number" name="cantidad" id="cantidad" min="1" value="1" required />

        <button type="submit">Comprar</button>
    </form>

    <div id="mensaje" class="mensaje" style="display:none;"></div>

    <script>
        // Función para cargar productos y actualizar la tabla y select
        async function cargarProductos() {
            const resp = await fetch('productos.php');
            const data = await resp.json();

            if (data.error) {
                alert("Error cargando productos: " + data.error);
                return;
            }

            const tablaCuerpo = document.querySelector('#tabla-productos tbody');
            const selectProducto = document.getElementById('producto');

            tablaCuerpo.innerHTML = '';
            selectProducto.innerHTML = '<option value="" disabled selected>-- Elija un producto --</option>';

            for (const [nombre, info] of Object.entries(data)) {
                // Tabla
                const fila = document.createElement('tr');
                fila.innerHTML = `<td>${nombre}</td><td>${info.precio}</td><td>${info.cantidad}</td>`;
                tablaCuerpo.appendChild(fila);

                // Select
                const option = document.createElement('option');
                option.value = nombre;
                option.textContent = nombre;
                selectProducto.appendChild(option);
            }
        }

        // Manejar compra por AJAX
        document.getElementById('form-compra').addEventListener('submit', async (e) => {
            e.preventDefault();

            const producto = document.getElementById('producto').value;
            const cantidad = parseInt(document.getElementById('cantidad').value);

            if (!producto || cantidad <= 0) {
                mostrarMensaje('Por favor selecciona un producto y una cantidad válida.', true);
                return;
            }

            try {
                // Llamar el servicio SOAP para comprar producto (usamos archivo PHP para eso)
                const formData = new FormData();
                formData.append('producto', producto);
                formData.append('cantidad', cantidad);

                const resp = await fetch('compra.php', {
                    method: 'POST',
                    body: formData
                });

                const resultado = await resp.json();
                mostrarMensaje(resultado.mensaje, !resultado.exito);

                if (resultado.exito) {
                    cargarProductos(); // Actualizar productos para reflejar stock
                    document.getElementById('cantidad').value = 1;
                }

            } catch (err) {
                mostrarMensaje('Error en la compra: ' + err.message, true);
            }
        });

        // Mostrar mensajes
        function mostrarMensaje(msg, error = false) {
            const div = document.getElementById('mensaje');
            div.style.display = 'block';
            div.textContent = msg;
            div.style.backgroundColor = error ? '#f8d7da' : '#d4edda';
            div.style.color = error ? '#721c24' : '#155724';
        }

        // Cargar productos al cargar la página y cada 10 segundos para "actualización"
        cargarProductos();
        setInterval(cargarProductos, 10000);
    </script>

</body>

</html>
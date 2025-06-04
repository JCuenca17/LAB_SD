<?php
header('Content-Type: application/json');

$wsdl = "http://localhost:8080/VentasService?wsdl";

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['exito' => false, 'mensaje' => 'Método no permitido']);
    exit;
}

$producto = $_POST['producto'] ?? '';
$cantidad = intval($_POST['cantidad'] ?? 0);

if (!$producto || $cantidad <= 0) {
    echo json_encode(['exito' => false, 'mensaje' => 'Producto o cantidad inválidos']);
    exit;
}

try {
    $client = new SoapClient($wsdl);

    $params = [
        "arg0" => $producto,
        "arg1" => $cantidad
    ];

    $response = $client->comprarProducto($params);

    if (is_object($response)) {
        $props = get_object_vars($response);
        $valor = reset($props);

        if ($valor == -1) {
            echo json_encode(['exito' => false, 'mensaje' => 'Producto no encontrado.']);
        } elseif ($valor == -2) {
            echo json_encode(['exito' => false, 'mensaje' => 'No hay suficiente stock.']);
        } else {
            echo json_encode(['exito' => true, 'mensaje' => 'Compra exitosa. Total a pagar: $' . $valor]);
        }
    } else {
        echo json_encode(['exito' => false, 'mensaje' => 'Error en la compra']);
    }
} catch (SoapFault $e) {
    echo json_encode(['exito' => false, 'mensaje' => 'Error SOAP: ' . $e->getMessage()]);
}

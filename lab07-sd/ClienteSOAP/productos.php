<?php
header('Content-Type: application/json');

$wsdl = "http://localhost:8080/VentasService?wsdl";

try {
    $client = new SoapClient($wsdl);
    $result = $client->listarProductos();
    $productosTexto = "";
    if (is_object($result)) {
        $props = get_object_vars($result);
        $productosTexto = reset($props);
    } else {
        $productosTexto = $result;
    }

    $productos = [];
    $lineas = explode("\n", trim($productosTexto));
    foreach ($lineas as $linea) {
        if (strpos($linea, ": $") !== false && strpos($linea, "(Cantidad:") !== false) {
            preg_match('/^(.*): \$([\d\.]+) \(Cantidad: (\d+)\)$/', trim($linea), $matches);
            if (count($matches) == 4) {
                $productos[$matches[1]] = [
                    'precio' => $matches[2],
                    'cantidad' => (int)$matches[3]
                ];
            }
        }
    }
    echo json_encode($productos);
} catch (SoapFault $e) {
    echo json_encode(['error' => $e->getMessage()]);
}

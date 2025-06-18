package com.ventas.soap;

import jakarta.xml.ws.Endpoint;

public class SoapPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/VentasService", new VentasService());
        System.out.println("Servicio SOAP publicado en http://localhost:8080/VentasService?wsdl");
    }   
}

package server;

import interfaces.LibraryInterface;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class LibraryServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(2000);
            LibraryInterface lib = new Library();
            Naming.rebind("rmi://localhost:2000/LibraryService", lib);
            System.out.println("Servidor RMI de biblioteca listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

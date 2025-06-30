import java.rmi.Naming;

public class LibraryServer {
    public static void main(String[] args) {
        try {
            LibraryInterface lib = new Library();
            Naming.rebind("rmi://localhost/LibraryService", lib);
            System.out.println("Servidor de biblioteca listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

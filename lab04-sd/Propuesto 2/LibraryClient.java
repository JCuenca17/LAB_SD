import java.rmi.Naming;
import java.util.Scanner;

public class LibraryClient {
    public static void main(String[] args) {
        try {
            LibraryInterface lib = (LibraryInterface) Naming.lookup("rmi://localhost/LibraryService");
            Scanner sc = new Scanner(System.in);

            System.out.println("Bienvenido a la Biblioteca Remota");
            System.out.println("1. Ver libros disponibles");
            System.out.println("2. Pedir prestado un libro");
            System.out.print("Elige una opción: ");
            int opcion = sc.nextInt();
            sc.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    for (String book : lib.getAvailableBooks()) {
                        System.out.println(book);
                    }
                    break;
                case 2:
                    System.out.print("Ingresa el título del libro: ");
                    String title = sc.nextLine();
                    System.out.println(lib.borrowBook(title));
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

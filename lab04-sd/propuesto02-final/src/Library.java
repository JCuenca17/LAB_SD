import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.*;

public class Library extends UnicastRemoteObject implements LibraryInterface {
    private Map<String, Integer> books;

    public Library() throws RemoteException {
        super();
        books = new HashMap<>();
        books.put("Don Quijote", 2);
        books.put("1984", 3);
        books.put("Cien Años de Soledad", 1);
    }

    @Override
    public List<String> getAvailableBooks() throws RemoteException {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : books.entrySet()) {
            list.add(entry.getKey() + " (disponibles: " + entry.getValue() + ")");
        }
        return list;
    }

    @Override
    public String borrowBook(String title) throws RemoteException {
        if (!books.containsKey(title)) {
            return "El libro no existe.";
        }
        int stock = books.get(title);
        if (stock == 0) {
            return "No hay copias disponibles.";
        }
        books.put(title, stock - 1);
        return "Has tomado prestado: " + title;
    }
}

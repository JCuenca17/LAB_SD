import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LibraryInterface extends Remote {
    List<String> getAvailableBooks() throws RemoteException;
    String borrowBook(String title) throws RemoteException;
}

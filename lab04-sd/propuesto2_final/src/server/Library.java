package server;

import interfaces.LibraryInterface;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;

public class Library extends UnicastRemoteObject implements LibraryInterface {
    private Connection conn;

    public Library() throws RemoteException {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAvailableBooks() throws RemoteException {
        List<String> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM libros")) {
            while (rs.next()) {
                String linea = rs.getString("titulo") + ";" +
                        rs.getString("autor") + ";" +
                        rs.getInt("stock") + ";" +
                        rs.getString("imagen");
                list.add(linea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String borrowBook(String title) throws RemoteException {
        try {
            String checkQuery = "SELECT stock FROM libros WHERE titulo = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, title);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int stock = rs.getInt("stock");
                if (stock > 0) {
                    PreparedStatement update = conn
                            .prepareStatement("UPDATE libros SET stock = stock - 1 WHERE titulo = ?");
                    update.setString(1, title);
                    update.executeUpdate();
                    return "Has tomado prestado: " + title;
                } else {
                    return "No hay copias disponibles.";
                }
            } else {
                return "El libro no existe.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al procesar la solicitud.";
        }
    }

    @Override
    public String returnBook(String title) throws RemoteException {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE libros SET stock = stock + 1 WHERE titulo = ?");
            stmt.setString(1, title);
            int updated = stmt.executeUpdate();
            return updated > 0 ? "Has devuelto: " + title : "El libro no existe.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al devolver el libro.";
        }
    }

    @Override
    public String addBook(String titulo, String autor, int stock, String imagen) throws RemoteException {
        try (PreparedStatement ps = conn
                .prepareStatement("INSERT INTO libros (titulo, autor, stock, imagen) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, titulo);
            ps.setString(2, autor);
            ps.setInt(3, stock);
            ps.setString(4, imagen);
            ps.executeUpdate();
            return "Libro agregado correctamente.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al agregar el libro.";
        }
    }

}

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
                list.add(rs.getString("titulo") + " (disponibles: " + rs.getInt("stock") + ")");
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
    public String addBook(String title, int stock) throws RemoteException {
        try {
            PreparedStatement check = conn.prepareStatement("SELECT * FROM libros WHERE titulo = ?");
            check.setString(1, title);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                return "El libro ya existe.";
            }

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO libros (titulo, stock) VALUES (?, ?)");
            stmt.setString(1, title);
            stmt.setInt(2, stock);
            stmt.executeUpdate();
            return "Libro agregado correctamente.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error al agregar el libro.";
        }
    }
}

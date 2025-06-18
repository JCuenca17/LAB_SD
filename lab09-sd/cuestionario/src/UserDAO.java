import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public List<String> obtenerUsuariosDisponibles(String actual) throws SQLException {
        List<String> usuarios = new ArrayList<>();
        String sql = "SELECT nombre FROM usuarios WHERE nombre <> ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, actual);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usuarios.add(rs.getString("nombre"));
            }
        }
        return usuarios;
    }

    public boolean usuarioExiste(String nombre) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE nombre = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public double obtenerSaldo(String nombre) throws SQLException {
        String sql = "SELECT saldo FROM usuarios WHERE nombre = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
            throw new SQLException("Usuario no encontrado.");
        }
    }

    public boolean transferir(String de, String para, double monto) throws SQLException {
        Connection conn = null;
        PreparedStatement restarStmt = null;
        PreparedStatement sumarStmt = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            double saldoOrigen = obtenerSaldo(de);
            if (saldoOrigen < monto) {
                throw new SQLException("Saldo insuficiente.");
            }

            restarStmt = conn.prepareStatement("UPDATE usuarios SET saldo = saldo - ? WHERE nombre = ?");
            restarStmt.setDouble(1, monto);
            restarStmt.setString(2, de);
            restarStmt.executeUpdate();

            sumarStmt = conn.prepareStatement("UPDATE usuarios SET saldo = saldo + ? WHERE nombre = ?");
            sumarStmt.setDouble(1, monto);
            sumarStmt.setString(2, para);
            sumarStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;
        } finally {
            if (restarStmt != null)
                restarStmt.close();
            if (sumarStmt != null)
                sumarStmt.close();
            if (conn != null)
                conn.setAutoCommit(true);
            if (conn != null)
                conn.close();
        }
    }
}

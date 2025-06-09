import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Ventana extends JFrame {
    JLabel LblId = new JLabel("Id Categoria");
    JLabel LblNombre = new JLabel("Nombre");
    JLabel LblDescripcion = new JLabel("Descripcion");

    JTextField TxtId = new JTextField(20);
    JTextField TxtNombre = new JTextField(20);
    JTextField TxtDescripcion = new JTextField(20);

    JButton BtnPrimero = new JButton("Primero");
    JButton BtnAnterior = new JButton("Anterior");
    JButton BtnSiguiente = new JButton("Siguiente");
    JButton BtnUltimo = new JButton("Ultimo");

    JButton BtnInsertar = new JButton("Insertar");
    JButton BtnModificar = new JButton("Modificar");
    JButton BtnEliminar = new JButton("Eliminar");
    JButton BtnActualizar = new JButton("Actualizar");

    Connection conexion;
    Statement sentencia;
    ResultSet resultado;

    public Ventana(String titulo) {
        super(titulo);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);

        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.add(LblId);
        panelCampos.add(TxtId);
        panelCampos.add(LblNombre);
        panelCampos.add(TxtNombre);
        panelCampos.add(LblDescripcion);
        panelCampos.add(TxtDescripcion);

        JPanel panelCrud = new JPanel(new FlowLayout());
        panelCrud.add(BtnInsertar);
        panelCrud.add(BtnModificar);
        panelCrud.add(BtnEliminar);
        panelCrud.add(BtnActualizar);

        JPanel panelNavegacion = new JPanel(new FlowLayout());
        panelNavegacion.add(BtnPrimero);
        panelNavegacion.add(BtnAnterior);
        panelNavegacion.add(BtnSiguiente);
        panelNavegacion.add(BtnUltimo);

        setLayout(new BorderLayout());
        add(panelCampos, BorderLayout.NORTH);
        add(panelCrud, BorderLayout.CENTER);
        add(panelNavegacion, BorderLayout.SOUTH);

        conectarBD();
        cargarDatos();

        // Eventos
        BtnPrimero.addActionListener(e -> irPrimero());
        BtnAnterior.addActionListener(e -> irAnterior());
        BtnSiguiente.addActionListener(e -> irSiguiente());
        BtnUltimo.addActionListener(e -> irUltimo());

        BtnInsertar.addActionListener(e -> insertar());
        BtnModificar.addActionListener(e -> modificar());
        BtnEliminar.addActionListener(e -> eliminar());
        BtnActualizar.addActionListener(e -> cargarDatos());

        setVisible(true);
    }

    private void conectarBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/EmpresaMSQL", "root", "1234");
            sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        try {
            resultado = sentencia.executeQuery("SELECT * FROM Categorias");
            if (resultado.next()) {
                mostrarRegistro();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void mostrarRegistro() {
        try {
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar datos: " + e.getMessage());
        }
    }

    private void irPrimero() {
        try {
            if (resultado.first()) {
                mostrarRegistro();
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void irAnterior() {
        try {
            if (!resultado.previous()) {
                resultado.first();
            }
            mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void irSiguiente() {
        try {
            if (!resultado.next()) {
                resultado.last();
            }
            mostrarRegistro();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void irUltimo() {
        try {
            if (resultado.last()) {
                mostrarRegistro();
            }
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void insertar() {
        String nombre = TxtNombre.getText();
        String descripcion = TxtDescripcion.getText();

        try (PreparedStatement ps = conexion.prepareStatement(
                "INSERT INTO Categorias(Nombre, Descripcion) VALUES (?, ?)")) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void modificar() {
        String id = TxtId.getText();
        String nombre = TxtNombre.getText();
        String descripcion = TxtDescripcion.getText();

        try (PreparedStatement ps = conexion.prepareStatement(
                "UPDATE Categorias SET Nombre=?, Descripcion=? WHERE IdCategoria=?")) {
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, Integer.parseInt(id));
            ps.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void eliminar() {
        String id = TxtId.getText();

        try (PreparedStatement ps = conexion.prepareStatement(
                "DELETE FROM Categorias WHERE IdCategoria=?")) {
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            cargarDatos();
        } catch (SQLException e) {
            mostrarError(e);
        }
    }

    private void mostrarError(Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ventana("Gestión de Categorías"));
    }
}
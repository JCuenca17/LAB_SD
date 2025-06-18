import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class Ventana1 extends JFrame {

    private JTextField txtSql;
    private JTextArea areaResultados;
    private JButton btnConsulta;

    public Ventana1(String titulo) {
        super(titulo);

        txtSql = new JTextField(40);
        areaResultados = new JTextArea(15, 40);
        btnConsulta = new JButton("Ejecutar SQL");
        JScrollPane scrollPanel = new JScrollPane(areaResultados);

        btnConsulta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verBaseDatos();
            }
        });

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout());
        panelSuperior.add(txtSql, BorderLayout.CENTER);
        panelSuperior.add(btnConsulta, BorderLayout.EAST);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPanel, BorderLayout.CENTER);

        getContentPane().add(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void verBaseDatos() {
        String consulta = txtSql.getText();
        if (consulta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una consulta SQL.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/EmpresaMSQL";
        String usuario = "root";
        String contraseña = "1234";

        try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
             Statement sentencia = conexion.createStatement()) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            boolean tieneResultados = sentencia.execute(consulta);
            if (tieneResultados) {
                ResultSet resultado = sentencia.getResultSet();
                mostrarResultados(resultado);
            } else {
                int filasAfectadas = sentencia.getUpdateCount();
                areaResultados.setText("Filas afectadas: " + filasAfectadas);
            }

        } catch (SQLException | ClassNotFoundException e) {
            areaResultados.setText("Error: " + e.getMessage());
        }
    }

    void mostrarResultados(ResultSet r) throws SQLException {
        ResultSetMetaData meta = r.getMetaData();
        int columnas = meta.getColumnCount();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= columnas; i++) {
            sb.append(meta.getColumnName(i)).append("\t");
        }
        sb.append("\n");

        while (r.next()) {
            for (int i = 1; i <= columnas; i++) {
                sb.append(r.getString(i)).append("\t");
            }
            sb.append("\n");
        }

        areaResultados.setText(sb.toString());
    }

    public static void main(String[] args) {
        new Ventana1("Consulta SQL");
    }
}

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

class Ventana extends JFrame {

    JLabel LblId = new JLabel("Id Categoria");
    JLabel LblNombre = new JLabel("Nombre");
    JLabel LblDescripcion = new JLabel("Descripcion");

    JTextField TxtId = new JTextField(15);
    JTextField TxtNombre = new JTextField(15);
    JTextField TxtDescripcion = new JTextField(15);

    JButton BtnPrimero = new JButton("Primero");
    JButton BtnSiguiente = new JButton("Siguiente");
    JButton BtnAnterior = new JButton("Anterior");
    JButton BtnUltimo = new JButton("Ultimo");

    ResultSet resultado;

    public Ventana(String titulo) {
        super(titulo);

        BtnPrimero.addActionListener(new EventoBoton(this));
        BtnAnterior.addActionListener(new EventoBoton(this));
        BtnSiguiente.addActionListener(new EventoBoton(this));
        BtnUltimo.addActionListener(new EventoBoton(this));

        JPanel Panel1 = new JPanel();
        Panel1.setLayout(new GridLayout(3, 2, 5, 5));
        Panel1.add(LblId); Panel1.add(TxtId);
        Panel1.add(LblNombre); Panel1.add(TxtNombre);
        Panel1.add(LblDescripcion); Panel1.add(TxtDescripcion);

        JPanel Panel2 = new JPanel();
        Panel2.setLayout(new FlowLayout());
        Panel2.add(BtnPrimero);
        Panel2.add(BtnAnterior);
        Panel2.add(BtnSiguiente);
        Panel2.add(BtnUltimo);
        Panel2.setBackground(Color.orange);

        JPanel Principal = new JPanel();
        Principal.setLayout(new BorderLayout(10, 10));
        Principal.add(Panel1, BorderLayout.CENTER);
        Principal.add(Panel2, BorderLayout.SOUTH);
        Principal.setBackground(Color.RED);

        setContentPane(Principal);
        addWindowListener(new EventosVentana(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Conexion();
    }

    private void Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver actualizado
            Connection conexion = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/EmpresaMSQL", "root", "1234");
            Statement sentencia = conexion.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery("SELECT * FROM Categorias");

            if (resultado.next()) {
                mostrarRegistro();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Controlador no encontrado: " + e);
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e);
        }
    }

    private void mostrarRegistro() {
        try {
            TxtId.setText(resultado.getString("IdCategoria"));
            TxtNombre.setText(resultado.getString("Nombre"));
            TxtDescripcion.setText(resultado.getString("Descripcion"));
        } catch (SQLException e) {
            System.out.println("Error al mostrar registro: " + e);
        }
    }

    public void IrPrimero() {
        try {
            if (resultado.first()) {
                mostrarRegistro();
            }
        } catch (SQLException e) {
            System.out.println("Error al ir al primero: " + e);
        }
    }

    public void IrAnterior() {
        try {
            if (!resultado.previous()) {
                resultado.first();
            }
            mostrarRegistro();
        } catch (SQLException e) {
            System.out.println("Error al ir al anterior: " + e);
        }
    }

    public void IrSiguiente() {
        try {
            if (!resultado.next()) {
                resultado.last();
            }
            mostrarRegistro();
        } catch (SQLException e) {
            System.out.println("Error al ir al siguiente: " + e);
        }
    }

    public void IrUltimo() {
        try {
            if (resultado.last()) {
                mostrarRegistro();
            }
        } catch (SQLException e) {
            System.out.println("Error al ir al último: " + e);
        }
    }
}

class EventoBoton implements ActionListener {
    Ventana fuente;

    public EventoBoton(Ventana pWnd) {
        fuente = pWnd;
    }

    public void actionPerformed(ActionEvent evento) {
        String texto = evento.getActionCommand();
        if (texto.equals("Primero")) {
            fuente.IrPrimero();
        } else if (texto.equals("Anterior")) {
            fuente.IrAnterior();
        } else if (texto.equals("Siguiente")) {
            fuente.IrSiguiente();
        } else if (texto.equals("Ultimo")) {
            fuente.IrUltimo();
        }
    }
}

class EventosVentana extends WindowAdapter {
    private Ventana fuente;

    public EventosVentana(Ventana pWnd) {
        this.fuente = pWnd;
    }

    public void windowClosing(WindowEvent evento) {
        System.out.println("Cerrando ventana...");
        fuente.dispose();
        System.exit(0);
    }
}

public class BaseDatos2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Ventana miVentana = new Ventana("Navegador de Categorías");
            miVentana.pack();
            miVentana.setLocationRelativeTo(null);
            miVentana.setVisible(true);
        });
    }
}

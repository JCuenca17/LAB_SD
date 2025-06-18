import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MainFrame extends JFrame {
    private String usuario;
    private JLabel saldoLabel;
    private JComboBox<String> destinatarioCombo;
    private JTextField montoField;
    private JButton transferirButton, salirButton;

    public MainFrame(String usuario) {
        this.usuario = usuario;
        setTitle("Panel de Usuario - " + usuario);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
        cargarSaldo();
        cargarDestinatarios();
    }

    private void initComponents() {
        saldoLabel = new JLabel();
        destinatarioCombo = new JComboBox<>();
        montoField = new JTextField(10);
        transferirButton = new JButton("Transferir");
        salirButton = new JButton("Salir");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Saldo
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(saldoLabel, gbc);

        // Destinatario
        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Destinatario:"), gbc);
        gbc.gridx = 1;
        panel.add(destinatarioCombo, gbc);

        // Monto
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel("Monto a transferir:"), gbc);
        gbc.gridx = 1;
        panel.add(montoField, gbc);

        // Botón Transferir
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        panel.add(transferirButton, gbc);

        // Botón Salir
        gbc.gridy++;
        panel.add(salirButton, gbc);

        add(panel);

        // Acción transferir
        transferirButton.addActionListener(e -> transferir());

        // Acción salir
        salirButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void cargarSaldo() {
        try {
            UserDAO dao = new UserDAO();
            double saldo = dao.obtenerSaldo(usuario);
            saldoLabel.setText("Saldo actual: $" + saldo);
        } catch (SQLException e) {
            saldoLabel.setText("Error al obtener saldo.");
        }
    }

    private void cargarDestinatarios() {
        try {
            UserDAO dao = new UserDAO();
            List<String> usuarios = dao.obtenerUsuariosDisponibles(usuario);
            destinatarioCombo.removeAllItems();
            for (String u : usuarios) {
                destinatarioCombo.addItem(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios.");
        }
    }

    private void transferir() {
        String para = (String) destinatarioCombo.getSelectedItem();
        String montoTexto = montoField.getText().trim();

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "Monto inválido.");
                return;
            }

            UserDAO dao = new UserDAO();
            boolean exito = dao.transferir(usuario, para, monto);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Transferencia exitosa.");
                cargarSaldo();
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Monto no válido.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}

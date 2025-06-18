import javax.swing.*;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField usuarioField;
    private JButton ingresarButton;

    public LoginFrame() {
        setTitle("Login Usuario");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        usuarioField = new JTextField(15);
        ingresarButton = new JButton("Ingresar");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Usuario:"));
        panel.add(usuarioField);
        panel.add(ingresarButton);
        add(panel);

        ingresarButton.addActionListener(e -> {
            String usuario = usuarioField.getText().trim().toLowerCase();
            try {
                UserDAO dao = new UserDAO();
                if (dao.usuarioExiste(usuario)) {
                    dispose();
                    new MainFrame(usuario).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Usuario no existe.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error en BD.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

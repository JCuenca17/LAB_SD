import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class LibraryGUI extends JFrame {
    private LibraryInterface lib;
    private DefaultListModel<String> listModel;
    private JList<String> bookList;
    private JButton refreshButton;
    private JButton borrowButton;

    public LibraryGUI() {
        setTitle("Biblioteca Remota");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        try {
            lib = (LibraryInterface) Naming.lookup("rmi://localhost/LibraryService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor RMI.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1);
        }

        // Panel de libros
        listModel = new DefaultListModel<>();
        bookList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookList);

        // Botones
        refreshButton = new JButton("Actualizar lista");
        borrowButton = new JButton("Pedir prestado");

        refreshButton.addActionListener(e -> cargarLibros());
        borrowButton.addActionListener(e -> pedirPrestado());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(borrowButton);

        // Layout
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Cargar lista al inicio
        cargarLibros();
    }

    private void cargarLibros() {
        try {
            listModel.clear();
            List<String> libros = lib.getAvailableBooks();
            for (String libro : libros) {
                listModel.addElement(libro);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener libros.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pedirPrestado() {
        String seleccionado = bookList.getSelectedValue();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la lista.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtener solo el título (antes del paréntesis)
        String titulo = seleccionado.split(" \\(disponibles")[0];

        try {
            String respuesta = lib.borrowBook(titulo);
            JOptionPane.showMessageDialog(this, respuesta, "Resultado", JOptionPane.INFORMATION_MESSAGE);
            cargarLibros(); // Actualizar lista
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al pedir prestado el libro.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryGUI().setVisible(true);
        });
    }
}

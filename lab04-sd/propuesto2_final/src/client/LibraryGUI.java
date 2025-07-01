package client;

import interfaces.LibraryInterface;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.Naming;
import java.util.List;

public class LibraryGUI extends JFrame {
    private LibraryInterface lib;
    private DefaultTableModel model;
    private JTable table;
    private JTextField titleField, stockField;

    public LibraryGUI() {
        setTitle("Sistema de Biblioteca Remota");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            lib = (LibraryInterface) Naming.lookup("rmi://localhost:2000/LibraryService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor");
            e.printStackTrace();
            return;
        }

        // Tabla de libros
        model = new DefaultTableModel(new String[]{"Título", "Stock disponible"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Gestión de libros"));

        titleField = new JTextField();
        stockField = new JTextField();

        formPanel.add(new JLabel("Título del libro:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Cantidad (stock):"));
        formPanel.add(stockField);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton viewBtn = new JButton("Actualizar Lista");
        JButton borrowBtn = new JButton("Pedir libro");
        JButton returnBtn = new JButton("Devolver libro");
        JButton addBtn = new JButton("Agregar libro");
        JButton exitBtn = new JButton("Salir");

        buttonPanel.add(viewBtn);
        buttonPanel.add(borrowBtn);
        buttonPanel.add(returnBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(exitBtn);

        // Listeners
        viewBtn.addActionListener(e -> actualizarTabla());
        borrowBtn.addActionListener(e -> {
            String titulo = titleField.getText().trim();
            if (!titulo.isEmpty()) {
                try {
                    JOptionPane.showMessageDialog(this, lib.borrowBook(titulo));
                    actualizarTabla();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        returnBtn.addActionListener(e -> {
            String titulo = titleField.getText().trim();
            if (!titulo.isEmpty()) {
                try {
                    JOptionPane.showMessageDialog(this, lib.returnBook(titulo));
                    actualizarTabla();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        addBtn.addActionListener(e -> {
            String titulo = titleField.getText().trim();
            String stockStr = stockField.getText().trim();
            if (!titulo.isEmpty() && !stockStr.isEmpty()) {
                try {
                    int stock = Integer.parseInt(stockStr);
                    JOptionPane.showMessageDialog(this, lib.addBook(titulo, stock));
                    actualizarTabla();
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Ingrese un número válido para el stock.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        // Layout general
        setLayout(new BorderLayout(10, 10));
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        actualizarTabla();
    }

    private void actualizarTabla() {
        try {
            List<String> libros = lib.getAvailableBooks();
            model.setRowCount(0); // limpiar tabla
            for (String libro : libros) {
                String[] partes = libro.split("\\(disponibles: ");
                String titulo = partes[0].trim();
                String stock = partes[1].replace(")", "").trim();
                model.addRow(new Object[]{titulo, stock});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}

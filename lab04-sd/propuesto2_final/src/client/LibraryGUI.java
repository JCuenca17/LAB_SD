package client;

import interfaces.LibraryInterface;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.rmi.Naming;
import java.util.List;

public class LibraryGUI extends JFrame {
    private LibraryInterface lib;
    private JPanel gridPanel;

    public LibraryGUI() {
        setTitle("📚 Biblioteca");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Conexión RMI
        try {
            lib = (LibraryInterface) Naming.lookup("rmi://localhost:2000/LibraryService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo conectar al servidor");
            e.printStackTrace();
            return;
        }

        // Barra superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnActualizar = new JButton("🔄 Actualizar lista");
        JButton btnAgregar = new JButton("➕ Agregar libro");
        JButton btnDevolver = new JButton("📗 Devolver libro");
        JButton btnSalir = new JButton("🚪 Salir");

        topPanel.add(btnActualizar);
        topPanel.add(btnAgregar);
        topPanel.add(btnDevolver);
        topPanel.add(btnSalir);
        add(topPanel, BorderLayout.NORTH);

        // Panel central (grilla)
        // gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 20, 20));
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ajustarColumnas();
            }
        });

        // Eventos de botones
        btnAgregar.addActionListener(e -> mostrarFormularioAgregar());
        btnDevolver.addActionListener(e -> mostrarVentanaDevolverLibro());
        btnSalir.addActionListener(e -> System.exit(0));
        btnActualizar.addActionListener(e -> {
            cargarLibros();
            gridPanel.revalidate();
            gridPanel.repaint();
            ajustarColumnas();
        });

        cargarLibros();
        gridPanel.revalidate();
        gridPanel.repaint();
        ajustarColumnas();

    }

    private void ajustarColumnas() {
        int anchoVentana = getWidth();
        int columnas = Math.max(1, anchoVentana / 280); // 280px por tarjeta aprox
        gridPanel.setLayout(new GridLayout(0, columnas, 20, 20));
        gridPanel.revalidate();
    }

    private void cargarLibros() {
        try {
            List<String> libros = lib.getAvailableBooks();
            gridPanel.removeAll();

            for (String info : libros) {
                String[] partes = info.split(";");
                if (partes.length != 4)
                    continue;

                String titulo = partes[0];
                String autor = partes[1];
                String stock = partes[2];
                String imagenURL = partes[3];

                JPanel tarjeta = crearTarjetaLibro(titulo, autor, stock, imagenURL);
                gridPanel.add(tarjeta);
            }

            gridPanel.revalidate();
            gridPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel crearTarjetaLibro(String titulo, String autor, String stockStr, String imagenURL) {
        int stock = Integer.parseInt(stockStr);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(220, 330));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(stock > 0 ? Color.WHITE : new Color(245, 245, 245));

        // Imagen
        JLabel imgLabel;
        try {
            URL url = new URL(imagenURL);
            Image img = Toolkit.getDefaultToolkit().createImage(url);
            Image scaled = img.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(scaled));
        } catch (Exception e) {
            imgLabel = new JLabel("Imagen no disponible");
        }
        imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(5));
        panel.add(imgLabel);

        // Texto
        JLabel lblTitulo = new JLabel("<html><div style='text-align: center;'><b>" + titulo + "</b></div></html>",
                SwingConstants.CENTER);
        JLabel lblAutor = new JLabel("Autor: " + autor, SwingConstants.CENTER);
        JLabel lblStock = new JLabel("Stock: " + stockStr, SwingConstants.CENTER);

        Font fuenteNormal = new Font("SansSerif", Font.PLAIN, 12);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblAutor.setFont(fuenteNormal);
        lblStock.setFont(fuenteNormal);

        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAutor.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblStock.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(8));
        panel.add(lblTitulo);
        panel.add(lblAutor);
        panel.add(lblStock);

        // Botón
        JButton btnPedir = new JButton("Pedir libro");
        btnPedir.setEnabled(stock > 0);
        btnPedir.setFont(fuenteNormal);
        btnPedir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPedir.addActionListener(e -> {
            try {
                String res = lib.borrowBook(titulo);
                JOptionPane.showMessageDialog(this, res);
                cargarLibros();
                gridPanel.revalidate();
                gridPanel.repaint();
                ajustarColumnas();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(btnPedir);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void mostrarFormularioAgregar() {
        JDialog dialog = new JDialog(this, "📘 Agregar nuevo libro", true);
        dialog.setSize(450, 280);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        Font font = new Font("SansSerif", Font.PLAIN, 13);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblTitulo, gbc);

        JTextField txtTitulo = new JTextField(22);
        txtTitulo.setFont(font);
        gbc.gridx = 1;
        formPanel.add(txtTitulo, gbc);

        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setFont(font);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblAutor, gbc);

        JTextField txtAutor = new JTextField(22);
        txtAutor.setFont(font);
        gbc.gridx = 1;
        formPanel.add(txtAutor, gbc);

        JLabel lblStock = new JLabel("Stock:");
        lblStock.setFont(font);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblStock, gbc);

        JTextField txtStock = new JTextField(10);
        txtStock.setFont(font);
        gbc.gridx = 1;
        formPanel.add(txtStock, gbc);

        JLabel lblImagen = new JLabel("URL de imagen:");
        lblImagen.setFont(font);
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(lblImagen, gbc);

        JTextField txtImagen = new JTextField(22);
        txtImagen.setFont(font);
        gbc.gridx = 1;
        formPanel.add(txtImagen, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setFont(font);
        btnCancelar.setFont(font);

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Eventos
        btnGuardar.addActionListener(e -> {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String stockStr = txtStock.getText().trim();
            String imagen = txtImagen.getText().trim();

            if (titulo.isEmpty() || autor.isEmpty() || stockStr.isEmpty() || imagen.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Complete todos los campos.");
                return;
            }

            try {
                int stock = Integer.parseInt(stockStr);
                String respuesta = lib.addBook(titulo, autor, stock, imagen);
                JOptionPane.showMessageDialog(dialog, respuesta);
                dialog.dispose();
                cargarLibros();
                gridPanel.revalidate();
                gridPanel.repaint();
                ajustarColumnas();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Stock debe ser un número.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void mostrarVentanaDevolverLibro() {
        JDialog dialog = new JDialog(this, "📗 Devolver libro", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel panelTop = new JPanel(new BorderLayout(5, 5));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel lblBuscar = new JLabel("Buscar título:");
        JTextField txtBuscar = new JTextField();
        panelTop.add(lblBuscar, BorderLayout.NORTH);
        panelTop.add(txtBuscar, BorderLayout.CENTER);
        dialog.add(panelTop, BorderLayout.NORTH);

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        JList<String> listaLibros = new JList<>(modeloLista);
        JScrollPane scroll = new JScrollPane(listaLibros);
        scroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        dialog.add(scroll, BorderLayout.CENTER);

        // Cargar todos los libros
        try {
            List<String> libros = lib.getAvailableBooks();
            for (String info : libros) {
                String[] partes = info.split(";");
                if (partes.length >= 1) {
                    modeloLista.addElement(partes[0]); // solo título
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros.");
            return;
        }

        // Buscar dinámico
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            private void filtrar() {
                String filtro = txtBuscar.getText().toLowerCase();
                modeloLista.clear();
                try {
                    List<String> libros = lib.getAvailableBooks();
                    for (String info : libros) {
                        String[] partes = info.split(";");
                        if (partes.length >= 1) {
                            String titulo = partes[0];
                            if (titulo.toLowerCase().contains(filtro)) {
                                modeloLista.addElement(titulo);
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDevolver = new JButton("Devolver");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnDevolver);
        panelBotones.add(btnCancelar);
        dialog.add(panelBotones, BorderLayout.SOUTH);

        btnDevolver.addActionListener(a -> {
            String seleccionado = listaLibros.getSelectedValue();
            if (seleccionado == null) {
                JOptionPane.showMessageDialog(dialog, "Seleccione un libro.");
                return;
            }
            try {
                String res = lib.returnBook(seleccionado);
                JOptionPane.showMessageDialog(dialog, res);
                dialog.dispose();
                cargarLibros();
                gridPanel.revalidate();
                gridPanel.repaint();
                ajustarColumnas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error al devolver el libro.");
            }
        });

        btnCancelar.addActionListener(a -> dialog.dispose());
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI().setVisible(true));
    }
}

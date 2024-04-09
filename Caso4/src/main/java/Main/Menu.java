package Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Menu extends JFrame {
    private JTextArea textArea = new JTextArea();
    private JFileChooser fileChooser = new JFileChooser();
    private JLabel statusLabel = new JLabel("Posición del Ratón");
    private JTextField textFieldNombre = new JTextField(20);
    private JTextField textFieldEmail = new JTextField(20);
    private JTextField textFieldTelefono = new JTextField(20);
    private JLabel labelValidacionEmail = new JLabel("Email no validado");
    private DefaultListModel<Contacto> modeloListaContactos = new DefaultListModel<>();
    private JList<Contacto> listaContactos = new JList<>(modeloListaContactos);
    private JPanel cardPanel = new JPanel(new CardLayout());

    public Menu() {
        inicializarUI();
    }

    private void inicializarUI() {
        setTitle("GESTOR DE PUBLICACIONES");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel de botones principales
        JPanel buttonPanel = new JPanel();
        JButton gestorDeTextosBtn = new JButton("Gestor de Textos");
        JButton dibujarBtn = new JButton("Dibujar");
        JButton contactosBtn = new JButton("Contactos");

        buttonPanel.add(gestorDeTextosBtn);
        buttonPanel.add(dibujarBtn);
        buttonPanel.add(contactosBtn);

        // Agregar paneles al CardLayout
        cardPanel.add(crearPanelGestorTextos(), "Gestor de Textos");
        cardPanel.add(crearPanelDibujo(), "Dibujar");
        cardPanel.add(crearPanelContactos(), "Contactos");

        // Listeners para botones
        gestorDeTextosBtn.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Gestor de Textos"));
        dibujarBtn.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Dibujar"));
        contactosBtn.addActionListener(e -> ((CardLayout) cardPanel.getLayout()).show(cardPanel, "Contactos"));

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel crearPanelGestorTextos() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 5, 5)); // Organiza los botones en una grilla

        JButton btnNuevoTexto = new JButton("Nuevo Texto");
        btnNuevoTexto.addActionListener(e -> nuevoTexto());

        JButton btnAbrirTexto = new JButton("Abrir Texto");
        btnAbrirTexto.addActionListener(e -> abrirArchivo());

        JButton btnCompararTextos = new JButton("Comparar Textos");
        btnCompararTextos.addActionListener(e -> compararArchivos());

        JButton btnBuscarEnTexto = new JButton("Buscar en Texto");
        btnBuscarEnTexto.addActionListener(e -> buscarPalabra());

        JButton btnAnalizarTexto = new JButton("Analizar Texto");
        btnAnalizarTexto.addActionListener(e -> analizarTexto());

        panel.add(btnNuevoTexto);
        panel.add(btnAbrirTexto);
        panel.add(btnCompararTextos);
        panel.add(btnBuscarEnTexto);
        panel.add(btnAnalizarTexto);

        return panel;
    }

    private void nuevoTexto() {
        textArea.setText("");
    }

    private void abrirArchivo() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                textArea.read(reader, null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private JPanel crearPanelDibujo() {
        // Implementa tu panel de dibujo aquí
        JPanel panel = new JPanel();
        panel.add(new JLabel("Área de dibujo"));
        // Agrega funcionalidades de dibujo
        return panel;
    }

    private JPanel crearPanelContactos() {
        JPanel panel = new JPanel(new BorderLayout());

        textFieldEmail.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validar(); }
            public void removeUpdate(DocumentEvent e) { validar(); }
            public void insertUpdate(DocumentEvent e) { validar(); }
            private void validar() {
                if (textFieldEmail.getText().matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                    labelValidacionEmail.setText("Email válido");
                    labelValidacionEmail.setForeground(Color.GREEN);
                } else {
                    labelValidacionEmail.setText("Email no válido");
                    labelValidacionEmail.setForeground(Color.RED);
                }
            }
        });

        JButton btnAgregar = new JButton("Agregar Contacto");
        btnAgregar.addActionListener(e -> agregarContacto());
        JButton btnEliminar = new JButton("Eliminar Contacto");
        btnEliminar.addActionListener(e -> {
            if (!listaContactos.isSelectionEmpty()) {
                modeloListaContactos.remove(listaContactos.getSelectedIndex());
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(0, 2));
        inputPanel.add(new JLabel("Nombre:"));
        inputPanel.add(textFieldNombre);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(textFieldEmail);
        inputPanel.add(new JLabel("Teléfono:"));
        inputPanel.add(textFieldTelefono);
        inputPanel.add(labelValidacionEmail);
        inputPanel.add(new JLabel()); // Placeholder

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAgregar);
        btnPanel.add(btnEliminar);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaContactos), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }
    class Contacto {
        private String nombre;
        private String email;
        private String telefono;

        public Contacto(String nombre, String email, String telefono) {
            this.nombre = nombre;
            this.email = email;
            this.telefono = telefono;
        }

        @Override
        public String toString() {
            return nombre + " - " + email + " - " + telefono;
        }
    }
    private void agregarContacto() {
        // Validación de campos y adición del contacto
        if (!textFieldNombre.getText().isEmpty() && !textFieldEmail.getText().isEmpty() && !textFieldTelefono.getText().isEmpty()) {
            modeloListaContactos.addElement(new Contacto(textFieldNombre.getText(), textFieldEmail.getText(), textFieldTelefono.getText()));
        }
    }

    // Define la clase Contacto y otros métodos necesarios aquí...

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}




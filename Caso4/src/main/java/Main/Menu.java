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
    private void guardarTexto() {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(new FileWriter(archivo))) {
                textArea.write(out);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No se pudo guardar el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void compararArchivos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] archivos = fileChooser.getSelectedFiles();
            if (archivos.length == 2) {
                try {
                    String contenidoArchivo1 = new String(Files.readAllBytes(archivos[0].toPath()));
                    String contenidoArchivo2 = new String(Files.readAllBytes(archivos[1].toPath()));

                    if (contenidoArchivo1.equals(contenidoArchivo2)) {
                        JOptionPane.showMessageDialog(this, "Los archivos son idénticos.", "Comparación de Archivos", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Los archivos son diferentes.", "Comparación de Archivos", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error al leer los archivos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona dos archivos para comparar.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private void analizarTexto() {
        String texto = textArea.getText();
        String[] palabras = texto.split("\\s+");
        int totalPalabras = palabras.length;

        Map<String, Integer> frecuenciaPalabras = new HashMap<>();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                frecuenciaPalabras.put(palabra, frecuenciaPalabras.getOrDefault(palabra, 0) + 1);
            }
        }

        StringBuilder estadisticas = new StringBuilder();
        estadisticas.append("Total de palabras: ").append(totalPalabras).append("\n\nFrecuencia de palabras:\n");
        frecuenciaPalabras.forEach((palabra, frecuencia) -> estadisticas.append(palabra).append(": ").append(frecuencia).append("\n"));

        JOptionPane.showMessageDialog(this, estadisticas.toString(), "Análisis de Texto", JOptionPane.INFORMATION_MESSAGE);
    }
    private void buscarPalabra() {
        String palabraABuscar = JOptionPane.showInputDialog(this, "Introduce la palabra a buscar:", "Buscar Palabra", JOptionPane.QUESTION_MESSAGE);
        if (palabraABuscar != null && !palabraABuscar.isEmpty()) {
            String texto = textArea.getText().toLowerCase();
            String palabra = palabraABuscar.toLowerCase();
            int indice = texto.indexOf(palabra);
            int contador = 0;
            while (indice != -1) {
                contador++;
                try {
                    Highlighter highlighter = textArea.getHighlighter();
                    Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
                    highlighter.addHighlight(indice, indice + palabra.length(), painter);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
                indice = texto.indexOf(palabra, indice + palabra.length());
            }
            JOptionPane.showMessageDialog(this, "La palabra '" + palabraABuscar + "' aparece " + contador + " veces.", "Resultado de la Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        }
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




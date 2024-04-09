package Main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
    private JTabbedPane tabbedPane = new JTabbedPane();

    public Menu() {
        inicializarUI();
    }
    private JButton crearBotonPersonalizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(0, 0, 139)); // Un ejemplo de azul oscuro
        boton.setForeground(Color.YELLOW); // Texto amarillo
        boton.setFocusPainted(false); // Quitar el borde de enfoque
        boton.setFont(new Font("Arial", Font.BOLD, 12)); // Cambiar la fuente, opcional
        return boton;
    }
    private void salirDelPrograma() {
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres salir?", "Salir", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    private void inicializarUI() {
        setTitle("ESTRUCTURA DE DATOS");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(135, 206, 250));

        // Panel de botones principales
        JPanel buttonPanel = new JPanel();
        JButton gestorDeTextosBtn = crearBotonPersonalizado("Gestor de Textos");

        JButton dibujarBtn = crearBotonPersonalizado("Dibujar");

        JButton contactosBtn = crearBotonPersonalizado("Contactos");
        JButton btnSalir = crearBotonPersonalizado("Salir");
        btnSalir.addActionListener(e -> salirDelPrograma());


        buttonPanel.add(gestorDeTextosBtn);
        buttonPanel.add(dibujarBtn);
        buttonPanel.add(contactosBtn);
        buttonPanel.add(btnSalir);

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
    private void establecerSeguimientoRatonYBarra(JTextArea textArea, JScrollPane scrollPane) {
        textArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                statusLabel.setText("Posición del Ratón: X=" + e.getX() + ", Y=" + e.getY());
            }
        });

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
            int maximum = scrollPane.getVerticalScrollBar().getMaximum();
            int value = scrollPane.getVerticalScrollBar().getValue();
            int porcentaje = (int) ((value * 100.0) / (maximum - extent));
            statusLabel.setText("Progreso: " + porcentaje + "%");
        });
    }

    private JPanel crearPanelGestorTextos() {
        // Asegúrate de inicializar el tabbedPane si no lo has hecho antes
        tabbedPane = new JTabbedPane();

        // Panel para contener los botones de acciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton btnNuevoTexto = crearBotonPersonalizado("Nuevo Texto")  ;
        btnNuevoTexto.addActionListener(e -> nuevoTexto());

        JButton btnAbrirTexto = crearBotonPersonalizado("Abrir Texto")  ;
        btnAbrirTexto.addActionListener(e -> abrirArchivo());

        JButton btnGuardarTexto = crearBotonPersonalizado("Guardar Texto")  ;
        btnGuardarTexto.addActionListener(e -> guardarTexto());

        JButton btnCompararTextos = crearBotonPersonalizado("Comparar Textos")  ;
        btnCompararTextos.addActionListener(e -> compararArchivos());

        JButton btnAnalizarTexto = crearBotonPersonalizado("Analizar Texto")  ;
        btnAnalizarTexto.addActionListener(e -> analizarTexto());

        JButton btnBuscarEnTexto =  crearBotonPersonalizado("Buscar en Texto")  ;
        btnBuscarEnTexto.addActionListener(e -> buscarPalabra());

        JButton btnCerrarDocumento = crearBotonPersonalizado("Cerrar Documento")  ;
        btnCerrarDocumento.addActionListener(e -> cerrarDocumentoActual());

        panelBotones.add(btnCerrarDocumento);
        panelBotones.add(btnNuevoTexto);
        panelBotones.add(btnAbrirTexto);
        panelBotones.add(btnGuardarTexto);
        panelBotones.add(btnCompararTextos);
        panelBotones.add(btnAnalizarTexto);
        panelBotones.add(btnBuscarEnTexto);
        panelBotones.add(btnCerrarDocumento);

        // Panel principal que será devuelto y agregado al cardLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(tabbedPane, BorderLayout.CENTER);

        // Agrega un documento inicial al abrir la aplicación (opcional)
        nuevoTexto();

        return panelPrincipal;
    }
    private void cerrarDocumentoActual() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de querer cerrar este documento?", "Cerrar Documento", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(selectedIndex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay ningún documento abierto para cerrar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }


    private void nuevoTexto() {
        JTextArea nuevaAreaTexto = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(nuevaAreaTexto);
        tabbedPane.addTab("Documento " + (tabbedPane.getTabCount() + 1), scrollPane);
        tabbedPane.setSelectedComponent(scrollPane); // Selecciona la nueva pestaña automáticamente
        establecerSeguimientoRatonYBarra(nuevaAreaTexto, scrollPane); // Asegura seguimiento y progreso
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
        if (tabbedPane.getTabCount() > 0) {
            JTextArea textAreaActual = (JTextArea) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView();
            String texto = textAreaActual.getText().toLowerCase();
            String[] palabras = texto.split("\\s+");
            if (palabras.length == 0) {
                JOptionPane.showMessageDialog(this, "No hay palabras para analizar.", "Análisis de Texto", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
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
        } else {
            JOptionPane.showMessageDialog(this, "No hay documento activo para analizar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPalabra() {
        if (tabbedPane.getTabCount() > 0) {
            JTextArea textAreaActual = (JTextArea) ((JScrollPane) tabbedPane.getSelectedComponent()).getViewport().getView();
            String palabraABuscar = JOptionPane.showInputDialog(this, "Introduce la palabra a buscar:", "Buscar Palabra", JOptionPane.QUESTION_MESSAGE);
            if (palabraABuscar != null && !palabraABuscar.isEmpty()) {
                String texto = textAreaActual.getText().toLowerCase();
                String palabra = palabraABuscar.toLowerCase();
                int indice = texto.indexOf(palabra);
                int contador = 0;
                while (indice != -1) {
                    contador++;
                    try {
                        Highlighter highlighter = textAreaActual.getHighlighter();
                        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
                        highlighter.addHighlight(indice, indice + palabra.length(), painter);
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    indice = texto.indexOf(palabra, indice + palabra.length());
                }
                JOptionPane.showMessageDialog(this, "La palabra '" + palabraABuscar + "' aparece " + contador + " veces.", "Resultado de la Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay documento activo para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void abrirArchivo() {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            JTextArea nuevaAreaTexto = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(nuevaAreaTexto);
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                nuevaAreaTexto.read(reader, null);
                tabbedPane.addTab(archivo.getName(), scrollPane);
                establecerSeguimientoRatonYBarra(nuevaAreaTexto, scrollPane); // Llama aquí al método
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No se pudo abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    class PanelDibujo extends JPanel {
        private ArrayList<Point> puntos = new ArrayList<>();

        public PanelDibujo() {
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Comienza un nuevo trazo
                    puntos.clear();
                    puntos.add(e.getPoint());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Finaliza el trazo actual
                    puntos.add(e.getPoint());
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    // Añade puntos al trazo actual mientras el ratón se arrastra
                    puntos.add(e.getPoint());
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int i = 0; i < puntos.size() - 1; ++i) {
                Point puntoInicio = puntos.get(i);
                Point puntoFinal = puntos.get(i + 1);
                g2.drawLine(puntoInicio.x, puntoInicio.y, puntoFinal.x, puntoFinal.y);
            }
        }
    }

    private JPanel crearPanelDibujo() {
        PanelDibujo panelDibujo = new PanelDibujo();
        return panelDibujo;
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}




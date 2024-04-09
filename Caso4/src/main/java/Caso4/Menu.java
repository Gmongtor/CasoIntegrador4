package Caso4;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


    public class Menu extends JFrame {
        private JTextArea textArea;
        private JFileChooser fileChooser;
        private JTabbedPane tabbedPane;
        private JLabel statusLabel;


        public Menu() {
            inicializarUI();
        }

        private void inicializarUI() {
            setTitle("GESTIÓN DE PUBLICACIONES");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            textArea = new JTextArea();
            JScrollPane scrollPane = new JScrollPane(textArea);
            add(scrollPane, BorderLayout.CENTER);

            fileChooser = new JFileChooser();
            crearMenuInicio();
            crearBarraHerramientas();
        }

        private void crearMenuInicio() {
            JPanel panelInicio = new JPanel();
            panelInicio.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JButton btnAbrir = new JButton("Abrir");
            JButton btnGuardar = new JButton("Guardar");
            JButton btnComparar = new JButton("Comparar Archivos");
            JButton btnAnalizar = new JButton("Analizar Texto");
            JButton btnBuscar = new JButton("Buscar Palabra");
            JButton btnAgenda = new JButton("Agenda de Contactos");

            // Añadir funcionalidad a los botones
            btnAbrir.addActionListener(e -> abrirArchivo());
            btnGuardar.addActionListener(e -> guardarTexto());
            btnComparar.addActionListener(e -> compararArchivos());
            btnAnalizar.addActionListener(e -> analizarTexto());
            btnBuscar.addActionListener(e -> buscarPalabra());
            btnAgenda.addActionListener(e -> inicializarAgendaContactos());

            // Añadir botones al panel
            panelInicio.add(btnAbrir);
            panelInicio.add(btnGuardar);
            panelInicio.add(btnComparar);
            panelInicio.add(btnAnalizar);
            panelInicio.add(btnBuscar);
            panelInicio.add(btnAgenda);

            // Añadir panelInicio al JFrame
            getContentPane().add(panelInicio, BorderLayout.NORTH);
        }

        private void crearBarraHerramientas() {
                JToolBar toolBar = new JToolBar();
                URL abrirIconUrl = getClass().getResource("/iconos/abrir.png");
                if (abrirIconUrl != null) {
                    JButton btnAbrir = new JButton(new ImageIcon(abrirIconUrl));
                    btnAbrir.setToolTipText("Abrir archivo");
                    btnAbrir.addActionListener(e -> abrirArchivo());
                    toolBar.add(btnAbrir);
                }

        }

        private void inicializarTabbedPane() {
            tabbedPane = new JTabbedPane();

            // Ejemplo de cómo añadir una pestaña con un nuevo documento
            JTextArea areaTexto1 = new JTextArea();
            JScrollPane scrollPane1 = new JScrollPane(areaTexto1);
            tabbedPane.addTab("Documento 1", null, scrollPane1, "Primer documento");

            // Añadir el JTabbedPane al JFrame
            add(tabbedPane, BorderLayout.CENTER);
        }
        private void inicializarStatusLabel() {
            statusLabel = new JLabel("Posición del Ratón");
            add(statusLabel, BorderLayout.SOUTH);

            textArea.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    statusLabel.setText("Posición X: " + e.getX() + ", Posición Y: " + e.getY());
                }
            });
        }
        private void actualizarBarraDesplazamiento() {
            JScrollPane scrollPane = new JScrollPane(textArea);

            scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
                int valorMax = e.getAdjustable().getMaximum();
                int valorActual = e.getValue();
                // Calcular el porcentaje de desplazamiento
                int porcentaje = (int) (((double) valorActual / (valorMax - scrollPane.getVerticalScrollBar().getVisibleAmount())) * 100);
                statusLabel.setText("Desplazamiento: " + porcentaje + "%");
            });
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
        private JList<Contacto> listaContactos;
        private DefaultListModel<Contacto> modeloListaContactos;

        private void inicializarAgendaContactos() {
            modeloListaContactos = new DefaultListModel<>();
            listaContactos = new JList<>(modeloListaContactos);

            JFrame ventanaContactos = new JFrame("Agenda de Contactos");
            ventanaContactos.setSize(400, 300);
            ventanaContactos.add(new JScrollPane(listaContactos));

            JButton btnAgregarContacto = new JButton("Agregar Contacto");
            btnAgregarContacto.addActionListener(e -> agregarContacto());
            ventanaContactos.add(btnAgregarContacto, BorderLayout.SOUTH);

            ventanaContactos.setVisible(true);
        }

        private void agregarContacto() {
            String nombre = JOptionPane.showInputDialog("Nombre del Contacto:");
            String email = JOptionPane.showInputDialog("Email del Contacto:");
            String telefono = JOptionPane.showInputDialog("Teléfono del Contacto:");

            if (nombre != null && email != null && telefono != null) {
                modeloListaContactos.addElement(new Contacto(nombre, email, telefono));
            }
        }
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                try {
                    // Prueba con Nimbus o el Look and Feel del sistema para ver si soluciona el problema
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Menu().setVisible(true);
            });
        }
}



package Caso4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


    public class EditorTexto extends JFrame {
        private JTextArea textArea;
        private JFileChooser fileChooser;

        public EditorTexto() {
            inicializarUI();
        }

        private void inicializarUI() {
            setTitle("GESTIÓN DE PUBLICACIONES");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            textArea = new JTextArea();
            add(new JScrollPane(textArea));

            fileChooser = new JFileChooser();

            crearMenu();
        }

        private void crearMenu() {
            JMenuBar menuBar = new JMenuBar();

            JMenu menuArchivo = new JMenu("Archivo");
            menuBar.add(menuArchivo);

            JMenuItem itemAbrir = new JMenuItem("Abrir");
            itemAbrir.addActionListener(e -> abrirArchivo());
            menuArchivo.add(itemAbrir);

            JMenuItem itemGuardar = new JMenuItem("Guardar");
            itemGuardar.addActionListener(e -> guardarTexto());
            menuArchivo.add(itemGuardar);

            JMenuItem itemComparar = new JMenuItem("Comparar Archivos");
            itemComparar.addActionListener(e -> compararArchivos());
            menuArchivo.add(itemComparar);

            JMenuItem itemAnalizar = new JMenuItem("Analizar Texto");
            itemAnalizar.addActionListener(e -> analizarTexto());
            menuArchivo.add(itemAnalizar);

            JMenuItem itemSalir = new JMenuItem("Salir");
            itemSalir.addActionListener(e -> System.exit(0));
            menuArchivo.add(itemSalir);

            JMenuItem itemBuscarPalabra = new JMenuItem("Buscar Palabra");
            itemBuscarPalabra.addActionListener(e -> buscarPalabra());
            menuArchivo.add(itemBuscarPalabra);

            JMenuItem itemAgendaContactos = new JMenuItem("Agenda de Contactos");
            itemAgendaContactos.addActionListener(e -> inicializarAgendaContactos());
            menuArchivo.add(itemAgendaContactos);

            setJMenuBar(menuBar);
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
                    indice = texto.indexOf(palabra, indice + 1);
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

            // Métodos getters y setters aquí

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
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new EditorTexto().setVisible(true);
            });
        }
    }
}



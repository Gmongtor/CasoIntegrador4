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
            setTitle("Editor de Texto Avanzado");
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



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EditorTexto().setVisible(true);
        });
    }
}



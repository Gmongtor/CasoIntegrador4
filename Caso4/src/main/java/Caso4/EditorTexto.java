package Caso4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

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

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);

        JMenuItem itemAbrir = new JMenuItem("Abrir");
        itemAbrir.addActionListener(e -> abrirArchivo());
        menuArchivo.add(itemAbrir);

        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.addActionListener(e -> guardarTexto());
        menuArchivo.add(itemGuardar);

        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EditorTexto().setVisible(true);
        });
    }
}



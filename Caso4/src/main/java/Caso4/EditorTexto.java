package Caso4;
import javax.swing.*;
import java.io.*;

public class EditorTexto extends JFrame {
    private JTextArea textArea;

    public EditorTexto() {
        setTitle("Editor de Texto");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        add(new JScrollPane(textArea));

        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemGuardar = new JMenuItem("Guardar");
        itemGuardar.addActionListener(e -> guardarTexto());

        menuArchivo.add(itemGuardar);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }

    private void guardarTexto() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (PrintWriter out = new PrintWriter(archivo)) {
                out.println(textArea.getText());
            } catch (FileNotFoundException e) {
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


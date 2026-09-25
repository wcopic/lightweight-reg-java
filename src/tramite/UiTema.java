package tramite;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

/** Estilos compartidos de la interfaz Swing. */
final class UiTema {
    static final Color FONDO = new Color(245, 247, 251);
    static final Color BLANCO = Color.WHITE;
    static final Color TEXTO = new Color(30, 42, 60);
    static final Color SECUNDARIO = new Color(104, 118, 138);
    static final Color AZUL = new Color(48, 95, 214);
    static final Color AZUL_CLARO = new Color(232, 239, 255);
    static final Color BORDE = new Color(225, 230, 239);
    static final Color NAV = new Color(25, 39, 63);

    private UiTema() {
    }

    static void instalar() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // El aspecto nativo sigue siendo utilizable si Nimbus no está disponible.
        }
        UIManager.put("defaultFont", new Font("SansSerif", Font.PLAIN, 13));
    }

    static JLabel titulo(String texto, int tamano) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, tamano));
        label.setForeground(TEXTO);
        return label;
    }

    static JLabel nota(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(SECUNDARIO);
        return label;
    }

    static JPanel tarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(BLANCO);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), BorderFactory.createEmptyBorder(18, 20, 18, 20)));
        return panel;
    }

    static JButton boton(String texto, boolean principal) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 13));
        boton.setForeground(principal ? BLANCO : TEXTO);
        boton.setBackground(principal ? AZUL : BLANCO);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(10, 16, 10, 16));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(principal ? AZUL : BORDE),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));
        return boton;
    }

    static JTextField campo(int columnas) {
        JTextField campo = new JTextField(columnas);
        prepararEntrada(campo);
        return campo;
    }

    static <T> JComboBox<T> selector(T[] opciones) {
        JComboBox<T> selector = new JComboBox<>(opciones);
        prepararEntrada(selector);
        return selector;
    }

    static void prepararEntrada(JComponent entrada) {
        Border borde = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE), BorderFactory.createEmptyBorder(7, 10, 7, 10));
        entrada.setBorder(borde);
        entrada.setBackground(BLANCO);
        entrada.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }
}

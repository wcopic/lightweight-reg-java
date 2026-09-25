package tramite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

final class FormularioExpedienteDialog extends JDialog {
    private final GestorExpedientes gestor;
    private final Runnable alGuardar;
    private final JTextField identificacion = UiTema.campo(24);
    private final JTextField nombre = UiTema.campo(24);
    private final JTextField asunto = UiTema.campo(24);
    private final JTextField documento = UiTema.campo(24);
    private final JComboBox<Prioridad> prioridad = UiTema.selector(Prioridad.values());
    private final JCheckBox interno = new JCheckBox("Solicitante interno");

    FormularioExpedienteDialog(JFrame padre, GestorExpedientes gestor, Runnable alGuardar) {
        super(padre, "Nuevo expediente", true);
        this.gestor = gestor;
        this.alGuardar = alGuardar;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(520, 565));
        setSize(560, 610);
        setLocationRelativeTo(padre);
        setContentPane(contenido());
    }

    private JPanel contenido() {
        JPanel base = new JPanel(new BorderLayout(0, 16));
        base.setBackground(UiTema.FONDO);
        base.setBorder(BorderFactory.createEmptyBorder(24, 25, 23, 25));
        JPanel cabecera = new JPanel(new BorderLayout(0, 5));
        cabecera.setOpaque(false);
        cabecera.add(UiTema.titulo("Nuevo expediente", 24), BorderLayout.NORTH);
        cabecera.add(UiTema.nota("ID asignado automáticamente: " + gestor.siguienteId()), BorderLayout.SOUTH);
        base.add(cabecera, BorderLayout.NORTH);

        JPanel campos = UiTema.tarjeta();
        campos.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 7, 0);
        c.gridy = 0;
        agregar(campos, c, "Documento de identidad *", identificacion);
        agregar(campos, c, "Nombre del solicitante *", nombre);
        agregar(campos, c, "Asunto *", asunto);
        agregar(campos, c, "Prioridad *", prioridad);
        prioridad.setSelectedItem(Prioridad.MEDIA);
        agregar(campos, c, "Documento de referencia (opcional)", documento);
        c.insets = new Insets(0, 0, 0, 0);
        interno.setOpaque(false);
        interno.setForeground(UiTema.TEXTO);
        campos.add(interno, c);
        JScrollPane desplazamiento = new JScrollPane(campos);
        desplazamiento.setBorder(null);
        desplazamiento.getVerticalScrollBar().setUnitIncrement(16);
        base.add(desplazamiento, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        JButton cancelar = UiTema.boton("Cancelar", false);
        cancelar.addActionListener(e -> dispose());
        JButton guardar = UiTema.boton("Crear expediente", true);
        guardar.addActionListener(e -> guardar());
        acciones.add(cancelar);
        acciones.add(guardar);
        base.add(acciones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(guardar);
        return base;
    }

    private void agregar(JPanel panel, GridBagConstraints c, String texto, java.awt.Component entrada) {
        c.insets = new Insets(0, 0, 7, 0);
        JLabel titulo = UiTema.titulo(texto, 12);
        panel.add(titulo, c);
        c.gridy++;
        c.insets = new Insets(0, 0, 19, 0);
        panel.add(entrada, c);
        c.gridy++;
    }

    private void guardar() {
        try {
            Interesado interesado = new Interesado(identificacion.getText().trim(), nombre.getText().trim(),
                    "", "", interno.isSelected());
            gestor.registrar((Prioridad) prioridad.getSelectedItem(), interesado,
                    asunto.getText(), documento.getText());
            alGuardar.run();
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Revisa el formulario", JOptionPane.WARNING_MESSAGE);
        }
    }
}

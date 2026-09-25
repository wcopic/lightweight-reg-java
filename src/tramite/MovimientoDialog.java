package tramite;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

final class MovimientoDialog extends JDialog {
    private final GestorExpedientes gestor;
    private final Expediente expediente;
    private final boolean soloFinalizar;
    private final Runnable alGuardar;
    private final JComboBox<Etapa> etapa = UiTema.selector(Etapa.values());
    private final JTextField resultado = UiTema.campo(26);

    MovimientoDialog(JFrame padre, GestorExpedientes gestor, Expediente expediente,
            boolean soloFinalizar, Runnable alGuardar) {
        super(padre, soloFinalizar ? "Finalizar expediente" : "Registrar movimiento", true);
        this.gestor = gestor;
        this.expediente = expediente;
        this.soloFinalizar = soloFinalizar;
        this.alGuardar = alGuardar;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(490, soloFinalizar ? 315 : 365));
        setSize(520, soloFinalizar ? 325 : 375);
        setLocationRelativeTo(padre);
        setContentPane(contenido());
    }

    private JPanel contenido() {
        JPanel base = new JPanel(new BorderLayout(0, 18));
        base.setBackground(UiTema.FONDO);
        base.setBorder(BorderFactory.createEmptyBorder(24, 25, 23, 25));
        JPanel cabecera = new JPanel(new BorderLayout(0, 5));
        cabecera.setOpaque(false);
        cabecera.add(UiTema.titulo(soloFinalizar ? "Finalizar expediente" : "Nuevo movimiento", 23), BorderLayout.NORTH);
        cabecera.add(UiTema.nota(expediente.getId() + " · " + expediente.getAsunto()), BorderLayout.SOUTH);
        base.add(cabecera, BorderLayout.NORTH);

        JPanel campos = UiTema.tarjeta();
        campos.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 0;
        if (!soloFinalizar) {
            c.insets = new Insets(0, 0, 8, 0);
            campos.add(UiTema.titulo("Etapa", 12), c);
            c.gridy++;
            c.insets = new Insets(0, 0, 15, 0);
            campos.add(etapa, c);
            c.gridy++;
            etapa.addActionListener(e -> actualizarCampo());
        }
        c.insets = new Insets(0, 0, 8, 0);
        campos.add(UiTema.titulo("Documento de resultado", 12), c);
        c.gridy++;
        c.insets = new Insets(0, 0, 8, 0);
        campos.add(resultado, c);
        c.gridy++;
        campos.add(UiTema.nota(soloFinalizar ? "Indica la referencia del resultado final."
                : "Requerido solo si esta etapa finaliza el expediente."), c);
        actualizarCampo();
        base.add(campos, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        JButton cancelar = UiTema.boton("Cancelar", false);
        cancelar.addActionListener(e -> dispose());
        JButton guardar = UiTema.boton(soloFinalizar ? "Finalizar" : "Guardar movimiento", true);
        guardar.addActionListener(e -> guardar());
        acciones.add(cancelar);
        acciones.add(guardar);
        base.add(acciones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(guardar);
        return base;
    }

    private void actualizarCampo() {
        boolean necesario = soloFinalizar || ((Etapa) etapa.getSelectedItem()).finaliza();
        resultado.setEnabled(necesario);
        if (!necesario) {
            resultado.setText("");
        }
    }

    private void guardar() {
        try {
            if (soloFinalizar) {
                gestor.finalizar(expediente.getId(), resultado.getText());
            } else {
                gestor.mover(expediente.getId(), (Etapa) etapa.getSelectedItem(), resultado.getText());
            }
            alGuardar.run();
            dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo guardar", JOptionPane.WARNING_MESSAGE);
        }
    }
}

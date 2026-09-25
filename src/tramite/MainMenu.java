package tramite;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.text.Normalizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Panel principal. La tabla y el detalle comparten el mismo gestor en memoria. */
public final class MainMenu extends JFrame {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm", Locale.forLanguageTag("es-PE"));
    private final GestorExpedientes gestor;
    private final JTextField busqueda = UiTema.campo(22);
    private final JComboBox<String> estado = UiTema.selector(new String[]{"Todos", "Abiertos", "Finalizados"});
    private final JComboBox<Object> prioridad = UiTema.selector(new Object[]{
        "Todas las prioridades", Prioridad.MUY_ALTA, Prioridad.ALTA, Prioridad.MEDIA, Prioridad.BAJA});
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Asunto", "Solicitante", "Prioridad", "Estado", "Creado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private final JPanel detalle = UiTema.tarjeta();
    private final JLabel totalValor = UiTema.titulo("0", 27);
    private final JLabel abiertosValor = UiTema.titulo("0", 27);
    private final JLabel urgentesValor = UiTema.titulo("0", 27);
    private final JLabel cerradosValor = UiTema.titulo("0", 27);
    private final JLabel resultado = UiTema.nota("0 expedientes");

    public MainMenu(GestorExpedientes gestor) {
        super("Trámite · Panel de expedientes");
        this.gestor = gestor;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1030, 640));
        setSize(1320, 790);
        setLocationRelativeTo(null);
        setContentPane(crearContenido());
        conectarEventos();
        actualizar();
    }

    private JPanel crearContenido() {
        JPanel base = new JPanel(new BorderLayout());
        base.setBackground(UiTema.FONDO);
        base.add(crearNavegacion(), BorderLayout.WEST);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 19));
        cuerpo.setBackground(UiTema.FONDO);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(28, 28, 24, 28));
        JPanel superior = new JPanel();
        superior.setOpaque(false);
        superior.setLayout(new BoxLayout(superior, BoxLayout.Y_AXIS));
        superior.add(crearCabecera());
        superior.add(Box.createVerticalStrut(21));
        superior.add(crearResumen());
        superior.add(Box.createVerticalStrut(20));
        superior.add(crearFiltros());
        cuerpo.add(superior, BorderLayout.NORTH);

        JSplitPane contenido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, crearTabla(), detalle);
        contenido.setBorder(null);
        contenido.setDividerSize(12);
        contenido.setResizeWeight(0.72);
        contenido.setContinuousLayout(true);
        detalle.setLayout(new BorderLayout());
        detalle.setMinimumSize(new Dimension(280, 250));
        cuerpo.add(contenido, BorderLayout.CENTER);
        base.add(cuerpo, BorderLayout.CENTER);
        return base;
    }

    private JPanel crearNavegacion() {
        JPanel lateral = new JPanel();
        lateral.setBackground(UiTema.NAV);
        lateral.setPreferredSize(new Dimension(214, 0));
        lateral.setBorder(BorderFactory.createEmptyBorder(30, 20, 24, 20));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        JLabel marca = UiTema.titulo("●  TRÁMITE", 18);
        marca.setForeground(Color.WHITE);
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(marca);
        lateral.add(Box.createVerticalStrut(8));
        JLabel sub = UiTema.nota("Gestión de expedientes");
        sub.setForeground(new Color(165, 181, 207));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(sub);
        lateral.add(Box.createVerticalStrut(46));
        lateral.add(navegacion("▦  Todos los expedientes", () -> estado.setSelectedIndex(0)));
        lateral.add(Box.createVerticalStrut(9));
        lateral.add(navegacion("◷  Pendientes", () -> estado.setSelectedIndex(1)));
        lateral.add(Box.createVerticalStrut(9));
        lateral.add(navegacion("✓  Finalizados", () -> estado.setSelectedIndex(2)));
        lateral.add(Box.createVerticalGlue());
        JLabel sesion = UiTema.nota("DATOS DE ESTA SESIÓN");
        sesion.setForeground(new Color(165, 181, 207));
        sesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lateral.add(sesion);
        return lateral;
    }

    private JButton navegacion(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 42));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setBackground(new Color(40, 57, 84));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(11, 12, 11, 8));
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    private JPanel crearCabecera() {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        JPanel textos = new JPanel(new BorderLayout(0, 4));
        textos.setOpaque(false);
        textos.add(UiTema.titulo("Expedientes", 29), BorderLayout.NORTH);
        textos.add(UiTema.nota("Consulta, organiza y da seguimiento a cada solicitud."), BorderLayout.SOUTH);
        fila.add(textos, BorderLayout.WEST);
        JButton nuevo = UiTema.boton("+  Nuevo expediente", true);
        nuevo.addActionListener(e -> new FormularioExpedienteDialog(this, gestor, () -> {
            estado.setSelectedIndex(0);
            prioridad.setSelectedIndex(0);
            busqueda.setText("");
            actualizar();
        }).setVisible(true));
        fila.add(nuevo, BorderLayout.EAST);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 74));
        return fila;
    }

    private JPanel crearResumen() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 13, 0));
        panel.setOpaque(false);
        panel.add(indicador("TOTAL", totalValor, UiTema.AZUL));
        panel.add(indicador("ABIERTOS", abiertosValor, new Color(26, 132, 116)));
        panel.add(indicador("ALTA PRIORIDAD", urgentesValor, new Color(196, 100, 25)));
        panel.add(indicador("FINALIZADOS", cerradosValor, new Color(103, 112, 133)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 99));
        return panel;
    }

    private JPanel indicador(String etiqueta, JLabel valor, Color color) {
        JPanel panel = UiTema.tarjeta();
        panel.setLayout(new BorderLayout(0, 7));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTema.BORDE),
                BorderFactory.createEmptyBorder(13, 17, 13, 17)));
        panel.add(UiTema.nota(etiqueta), BorderLayout.NORTH);
        valor.setForeground(color);
        panel.add(valor, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearFiltros() {
        JPanel panel = UiTema.tarjeta();
        panel.setLayout(new BorderLayout(12, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UiTema.BORDE),
                BorderFactory.createEmptyBorder(11, 13, 11, 13)));
        busqueda.putClientProperty("JTextField.placeholderText", "Buscar por ID, asunto, nombre o identificación");
        JPanel buscar = new JPanel(new BorderLayout(8, 0));
        buscar.setOpaque(false);
        buscar.add(UiTema.nota("Buscar:"), BorderLayout.WEST);
        buscar.add(busqueda, BorderLayout.CENTER);
        panel.add(buscar, BorderLayout.CENTER);
        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        opciones.setOpaque(false);
        opciones.add(estado);
        opciones.add(prioridad);
        panel.add(opciones, BorderLayout.EAST);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 69));
        return panel;
    }

    private JPanel crearTabla() {
        JPanel contenedor = UiTema.tarjeta();
        contenedor.setLayout(new BorderLayout(0, 13));
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        encabezado.add(UiTema.titulo("Listado", 17), BorderLayout.WEST);
        encabezado.add(resultado, BorderLayout.EAST);
        contenedor.add(encabezado, BorderLayout.NORTH);

        tabla.setRowHeight(45);
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setForeground(UiTema.TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(UiTema.AZUL_CLARO);
        tabla.setSelectionForeground(UiTema.TEXTO);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.getTableHeader().setPreferredSize(new Dimension(0, 42));
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tabla.getTableHeader().setBackground(UiTema.FONDO);
        tabla.getTableHeader().setForeground(UiTema.SECUNDARIO);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(85);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(115);
        tabla.setDefaultRenderer(Object.class, new Celda());
        tabla.getColumnModel().getColumn(3).setCellRenderer(new PrioridadCelda());
        tabla.getColumnModel().getColumn(4).setCellRenderer(new EstadoCelda());
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(UiTema.BORDE));
        scroll.getViewport().setBackground(Color.WHITE);
        contenedor.add(scroll, BorderLayout.CENTER);
        return contenedor;
    }

    private void conectarEventos() {
        busqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { actualizar(); }
            public void removeUpdate(DocumentEvent e) { actualizar(); }
            public void changedUpdate(DocumentEvent e) { actualizar(); }
        });
        estado.addActionListener(e -> actualizar());
        prioridad.addActionListener(e -> actualizar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle(seleccionado());
            }
        });
    }

    private void actualizar() {
        String idAnterior = seleccionado() == null ? null : seleccionado().getId();
        List<Expediente> todos = gestor.listar();
        totalValor.setText(String.valueOf(todos.size()));
        abiertosValor.setText(String.valueOf(todos.stream().filter(e -> e.getFechaFin() == null).count()));
        urgentesValor.setText(String.valueOf(todos.stream().filter(e -> e.getFechaFin() == null
                && e.getPrioridad().getNivel() >= Prioridad.ALTA.getNivel()).count()));
        cerradosValor.setText(String.valueOf(todos.stream().filter(e -> e.getFechaFin() != null).count()));

        String filtro = normalizar(busqueda.getText().trim());
        modelo.setRowCount(0);
        int seleccionar = -1;
        for (Expediente e : todos) {
            if (!coincide(e, filtro)) {
                continue;
            }
            int fila = modelo.getRowCount();
            modelo.addRow(new Object[]{e.getId(), e.getAsunto(), e.getInteresado().getNombre(),
                e.getPrioridad(), e.getFechaFin() == null ? "Abierto" : "Finalizado",
                e.getFechaInicio().format(FECHA)});
            if (e.getId().equals(idAnterior)) {
                seleccionar = fila;
            }
        }
        resultado.setText(modelo.getRowCount() + (modelo.getRowCount() == 1 ? " expediente" : " expedientes"));
        if (modelo.getRowCount() > 0) {
            tabla.setRowSelectionInterval(seleccionar >= 0 ? seleccionar : 0, seleccionar >= 0 ? seleccionar : 0);
        } else {
            mostrarDetalle(null);
        }
    }

    private boolean coincide(Expediente e, String texto) {
        boolean abierto = e.getFechaFin() == null;
        if ("Abiertos".equals(estado.getSelectedItem()) && !abierto
                || "Finalizados".equals(estado.getSelectedItem()) && abierto) {
            return false;
        }
        Object nivel = prioridad.getSelectedItem();
        if (nivel instanceof Prioridad && nivel != e.getPrioridad()) {
            return false;
        }
        String contenido = normalizar(e.getId() + " " + e.getAsunto() + " "
                + e.getInteresado().getNombre() + " " + e.getInteresado().getIdentificacion());
        return contenido.contains(texto);
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }

    private Expediente seleccionado() {
        int fila = tabla.getSelectedRow();
        return fila < 0 || fila >= modelo.getRowCount()
                ? null : gestor.buscar((String) modelo.getValueAt(fila, 0));
    }

    private void mostrarDetalle(Expediente e) {
        detalle.removeAll();
        if (e == null) {
            JLabel vacio = UiTema.nota(gestor.listar().isEmpty()
                    ? "<html><center>Crea un expediente<br>para comenzar.</center></html>"
                    : "<html><center>No hay resultados.<br>Ajusta la búsqueda o los filtros.</center></html>");
            vacio.setHorizontalAlignment(SwingConstants.CENTER);
            detalle.add(vacio, BorderLayout.CENTER);
        } else {
            JPanel contenido = new JPanel();
            contenido.setOpaque(false);
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            contenido.add(UiTema.nota("DETALLE DEL EXPEDIENTE"));
            contenido.add(Box.createVerticalStrut(10));
            contenido.add(UiTema.titulo(e.getId(), 22));
            contenido.add(Box.createVerticalStrut(15));
            contenido.add(linea("Asunto", e.getAsunto()));
            contenido.add(linea("Solicitante", e.getInteresado().getNombre()));
            contenido.add(linea("Identificación", e.getInteresado().getIdentificacion()));
            contenido.add(linea("Prioridad", e.getPrioridad().toString()));
            contenido.add(linea("Estado", e.getFechaFin() == null ? "Abierto" : "Finalizado"));
            contenido.add(linea("Creado", e.getFechaInicio().format(FECHA)));
            contenido.add(linea("Documento de referencia", e.getDocRef().isBlank() ? "Sin referencia" : e.getDocRef()));
            if (e.getFechaFin() != null) {
                contenido.add(linea("Documento de resultado", e.getDocumentoResultado()));
            }
            contenido.add(Box.createVerticalStrut(16));
            contenido.add(UiTema.titulo("Historial", 15));
            contenido.add(Box.createVerticalStrut(7));
            JTextArea historial = new JTextArea();
            historial.setEditable(false);
            historial.setLineWrap(true);
            historial.setWrapStyleWord(true);
            historial.setOpaque(false);
            historial.setFont(new Font("SansSerif", Font.PLAIN, 12));
            historial.setForeground(UiTema.SECUNDARIO);
            StringBuilder eventos = new StringBuilder();
            for (Movimiento m : e.getSeguimiento()) {
                eventos.append("• ").append(m.getFechaHora().format(FECHA)).append("\n   ")
                        .append(m.getDependencia()).append("\n\n");
            }
            historial.setText(eventos.length() == 0 ? "Aún no hay movimientos." : eventos.toString());
            contenido.add(historial);
            JScrollPane desplazamiento = new JScrollPane(contenido);
            desplazamiento.setBorder(null);
            desplazamiento.getViewport().setBackground(Color.WHITE);
            desplazamiento.getVerticalScrollBar().setUnitIncrement(16);
            detalle.add(desplazamiento, BorderLayout.CENTER);
            if (e.getFechaFin() == null) {
                JPanel acciones = new JPanel(new GridLayout(2, 1, 0, 8));
                acciones.setOpaque(false);
                acciones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
                JButton mover = UiTema.boton("+  Registrar movimiento", true);
                mover.addActionListener(a -> new MovimientoDialog(this, gestor, e, false, this::actualizar).setVisible(true));
                JButton finalizar = UiTema.boton("Finalizar expediente", false);
                finalizar.addActionListener(a -> new MovimientoDialog(this, gestor, e, true, this::actualizar).setVisible(true));
                acciones.add(mover);
                acciones.add(finalizar);
                detalle.add(acciones, BorderLayout.SOUTH);
            }
        }
        detalle.revalidate();
        detalle.repaint();
    }

    private JPanel linea(String etiqueta, String valor) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        panel.add(UiTema.nota(etiqueta.toUpperCase(Locale.ROOT)), BorderLayout.NORTH);
        JTextArea contenido = new JTextArea(valor);
        contenido.setFont(new Font("SansSerif", Font.BOLD, 13));
        contenido.setForeground(UiTema.TEXTO);
        contenido.setOpaque(false);
        contenido.setEditable(false);
        contenido.setLineWrap(true);
        contenido.setWrapStyleWord(true);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    private static class Celda extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
            setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 9));
            setBackground(seleccionada ? UiTema.AZUL_CLARO : fila % 2 == 0 ? Color.WHITE : UiTema.FONDO);
            setForeground(UiTema.TEXTO);
            return this;
        }
    }

    private static final class PrioridadCelda extends Celda {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
            Prioridad p = (Prioridad) valor;
            setHorizontalAlignment(SwingConstants.CENTER);
            setForeground(p == Prioridad.MUY_ALTA ? new Color(172, 49, 55)
                    : p == Prioridad.ALTA ? new Color(172, 99, 24)
                    : p == Prioridad.MEDIA ? UiTema.AZUL : new Color(34, 130, 107));
            setFont(getFont().deriveFont(Font.BOLD));
            return this;
        }
    }

    private static final class EstadoCelda extends Celda {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                boolean foco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionada, foco, fila, columna);
            setForeground("Abierto".equals(valor) ? new Color(26, 132, 116) : UiTema.SECUNDARIO);
            setFont(getFont().deriveFont(Font.BOLD));
            return this;
        }
    }

    public static void main(String[] args) {
        UiTema.instalar();
        SwingUtilities.invokeLater(() -> new MainMenu(new GestorExpedientes()).setVisible(true));
    }
}

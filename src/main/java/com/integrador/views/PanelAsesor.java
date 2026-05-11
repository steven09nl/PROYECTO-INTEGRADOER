package com.integrador.views;

import com.integrador.dao.*;
import com.integrador.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel principal del Asesor.
 * Secciones: Dashboard | Mis Estudiantes | Observaciones | Bitácoras | Historial
 */
public class PanelAsesor extends BasePanel {

    private final ObservacionDAO  observacionDAO = new ObservacionDAO();
    private final BitacoraDAO     bitacoraDAO    = new BitacoraDAO();
    private final UsuarioDAO      usuarioDAO     = new UsuarioDAO();

    public PanelAsesor(Usuario u) {
        super(u, "Panel Docente", UIUtils.COLOR_ASESOR.darker(), UIUtils.COLOR_ASESOR);

        contentArea.add(buildDashboard(),     "dashboard");
        contentArea.add(buildEstudiantes(),   "estudiantes");
        contentArea.add(buildObservaciones(), "observaciones");
        contentArea.add(buildBitacoras(),     "bitacoras");
        contentArea.add(buildHistorial(),     "historial");

        cardLayout.show(contentArea, "dashboard");
    }

    @Override
    protected void buildNavigation(JPanel sidebar) {
        addNavSection(sidebar, "Mis Estudiantes");
        addNavItem(sidebar, "🏠", "Inicio",                "dashboard");
        addNavItem(sidebar, "👥", "Mis Estudiantes",       "estudiantes");
        addNavItem(sidebar, "✏️", "Escribir Observación",  "observaciones");
        addNavSection(sidebar, "Revisión");
        addNavItem(sidebar, "📓", "Revisar Bitácoras",     "bitacoras");
        addNavItem(sidebar, "📋", "Historial Observaciones","historial");
    }

    @Override
    protected JPanel buildTopBar() {
        return buildTopBarBase("Bienvenido, " + usuario.getNombre(),
                               "Docente", UIUtils.COLOR_ASESOR);
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────────

    private JPanel buildDashboard() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Panel del Asesor",
                "Resumen de la actividad de tus estudiantes asignados."));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(UIUtils.COLOR_BG);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton bObs  = UIUtils.btnPrimary("✏️ Escribir Observación", UIUtils.COLOR_ASESOR);
        JButton bBit  = UIUtils.btnPrimary("📓 Revisar Bitácoras",    UIUtils.COLOR_ASESOR);
        JButton bHist = UIUtils.btnPrimary("📋 Ver Historial",        new Color(107, 114, 128));

        bObs.addActionListener(e  -> cardLayout.show(contentArea, "observaciones"));
        bBit.addActionListener(e  -> cardLayout.show(contentArea, "bitacoras"));
        bHist.addActionListener(e -> cardLayout.show(contentArea, "historial"));

        actions.add(bObs);
        actions.add(bBit);
        actions.add(bHist);
        p.add(actions);

        return p;
    }

    // ── MIS ESTUDIANTES ───────────────────────────────────────────────────

    private JPanel buildEstudiantes() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Mis Estudiantes",
                "Lista de estudiantes asignados a tu asesoría pedagógica."));

        String[] cols = {"ID", "Nombre", "Email", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);

        JButton btnRecargar = UIUtils.btnPrimary("🔄 Cargar Estudiantes", UIUtils.COLOR_ASESOR);
        btnRecargar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRecargar.addActionListener(e -> {
            model.setRowCount(0);
            List<Usuario> lista = usuarioDAO.listarTodos();
            for (Usuario u : lista) {
                if ("estudiante".equalsIgnoreCase(u.getRol())) {
                    model.addRow(new Object[]{u.getIdUsuario(), u.getNombre(),
                            u.getEmail(), u.getEstado()});
                }
            }
            if (model.getRowCount() == 0)
                JOptionPane.showMessageDialog(this, "No se encontraron estudiantes.", "Info",
                        JOptionPane.INFORMATION_MESSAGE);
        });

        p.add(btnRecargar);
        p.add(Box.createVerticalStrut(12));
        p.add(tableCard(tabla));
        return p;
    }

    // ── OBSERVACIONES ─────────────────────────────────────────────────────

    private JPanel buildObservaciones() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Escribir Observación",
                "Registra formalmente lo que observaste durante tu visita al estudiante."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 240));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBit = new JLabel("ID Bitácora:");
        lblBit.setFont(UIUtils.FONT_LABEL);
        JTextField txtBitacora = UIUtils.textField("ID de la bitácora a observar");
        txtBitacora.setMaximumSize(new Dimension(200, 36));

        JLabel lblFecha = new JLabel("Fecha (AAAA-MM-DD):");
        lblFecha.setFont(UIUtils.FONT_LABEL);
        JTextField txtFecha = UIUtils.textField(LocalDate.now().toString());
        txtFecha.setText(LocalDate.now().toString());
        txtFecha.setMaximumSize(new Dimension(200, 36));

        JLabel lblObs = new JLabel("Observación:");
        lblObs.setFont(UIUtils.FONT_LABEL);
        JTextArea taObs = UIUtils.textArea(4, 50);

        JButton btnGuardar = UIUtils.btnPrimary("✏️ Guardar Observación", UIUtils.COLOR_ASESOR);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Component c : new Component[]{lblBit, txtBitacora, Box.createVerticalStrut(8),
                lblFecha, txtFecha, Box.createVerticalStrut(8),
                lblObs, new JScrollPane(taObs), Box.createVerticalStrut(10), btnGuardar}) {
            if (c instanceof JComponent jc) jc.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(c);
        }

        btnGuardar.addActionListener(e -> {
            try {
                int idBit = Integer.parseInt(txtBitacora.getText().trim());
                String texto = taObs.getText().trim();
                if (texto.isEmpty()) { showMsg("Escribe la observación.", false); return; }
                Date fecha = Date.valueOf(txtFecha.getText().trim());

                Observacion obs = new Observacion();
                obs.setIdBitacora(idBit);
                obs.setIdAsesor(usuario.getIdUsuario());
                obs.setTexto(texto);
                obs.setFecha(fecha);

                if (observacionDAO.agregarObservacion(obs)) {
                    showMsg("Observación guardada correctamente.", true);
                    taObs.setText("");
                } else {
                    showMsg("No se pudo guardar la observación.", false);
                }
            } catch (Exception ex) {
                showMsg("Verifica los datos ingresados.", false);
            }
        });

        p.add(form);
        return p;
    }

    // ── BITÁCORAS ─────────────────────────────────────────────────────────

    private JPanel buildBitacoras() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Revisar Bitácoras",
                "Consulta las bitácoras enviadas y agrega observaciones."));

        String[] cols = {"ID", "ID Estudiante", "ID Práctica", "Estado", "Modalidad",
                         "Fecha Envío", "Calificación"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);

        JButton btnCargar = UIUtils.btnPrimary("🔄 Cargar Bitácoras", UIUtils.COLOR_ASESOR);
        btnCargar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            List<Bitacora> lista = bitacoraDAO.listarBitacoras();
            for (Bitacora b : lista) {
                model.addRow(new Object[]{b.getIdBitacora(), b.getIdEstudiante(),
                        b.getIdPractica(), b.getEstado(), b.getModalidad(),
                        b.getFechaEnvio(), b.getCalificacion()});
            }
        });

        p.add(btnCargar);
        p.add(Box.createVerticalStrut(12));
        p.add(tableCard(tabla));
        return p;
    }

    // ── HISTORIAL ─────────────────────────────────────────────────────────

    private JPanel buildHistorial() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Historial de Observaciones",
                "Registro de todas las observaciones que has escrito durante las visitas."));

        String[] cols = {"ID", "ID Bitácora", "Fecha", "Observación"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);

        JButton btnCargar = UIUtils.btnPrimary("🔄 Cargar Mi Historial", UIUtils.COLOR_ASESOR);
        btnCargar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            List<Observacion> lista = observacionDAO.listarPorAsesor(usuario.getIdUsuario());
            for (Observacion o : lista) {
                model.addRow(new Object[]{o.getIdObservacion(), o.getIdBitacora(),
                        o.getFecha(), o.getTexto()});
            }
            if (model.getRowCount() == 0)
                JOptionPane.showMessageDialog(this, "Aún no tienes observaciones registradas.",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
        });

        p.add(btnCargar);
        p.add(Box.createVerticalStrut(12));
        p.add(tableCard(tabla));
        return p;
    }
}

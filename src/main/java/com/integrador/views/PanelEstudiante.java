package com.integrador.views;

import com.integrador.dao.*;
import com.integrador.models.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Panel principal del Estudiante.
 * Secciones: Dashboard | Bitácora | Evidencias | Horas | Retroalimentación | Exportar PDF
 */
public class PanelEstudiante extends BasePanel {

    private final BitacoraDAO    bitacoraDAO    = new BitacoraDAO();
    private final PreguntaDAO    preguntaDAO    = new PreguntaDAO();
    private final RespuestaDAO   respuestaDAO   = new RespuestaDAO();
    private final EvidenciaDAO   evidenciaDAO   = new EvidenciaDAO();
    private final ControlHorasDAO horasDAO      = new ControlHorasDAO();
    private final ObservacionDAO  observacionDAO = new ObservacionDAO();

    private static final int ID_BITACORA_DEFAULT = 1; // Se puede parametrizar

    public PanelEstudiante(Usuario u) {
        super(u, "Panel Estudiante", UIUtils.COLOR_PRIMARY.darker(), UIUtils.COLOR_PRIMARY);

        contentArea.add(buildDashboard(),          "dashboard");
        contentArea.add(buildBitacora(),           "bitacora");
        contentArea.add(buildEvidencias(),         "evidencias");
        contentArea.add(buildHoras(),              "horas");
        contentArea.add(buildRetroalimentacion(),  "retroalimentacion");
        contentArea.add(buildExportar(),           "exportar");

        cardLayout.show(contentArea, "dashboard");
    }

    @Override
    protected void buildNavigation(JPanel sidebar) {
        addNavSection(sidebar, "Mi Práctica");
        addNavItem(sidebar, "🏠", "Inicio",                 "dashboard");
        addNavItem(sidebar, "📓", "Diligenciar Bitácora",   "bitacora");
        addNavItem(sidebar, "📎", "Adjuntar Evidencias",    "evidencias");
        addNavItem(sidebar, "⏱", "Registrar Horas",        "horas");
        addNavSection(sidebar, "Resultados");
        addNavItem(sidebar, "💬", "Retroalimentación",      "retroalimentacion");
        addNavItem(sidebar, "📄", "Exportar PDF",           "exportar");
    }

    @Override
    protected JPanel buildTopBar() {
        return buildTopBarBase("Bienvenido, " + usuario.getNombre(),
                               "Estudiante", UIUtils.COLOR_PRIMARY);
    }

    // ── DASHBOARD ─────────────────────────────────────────────────────────

    private JPanel buildDashboard() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Bienvenido", "Aquí tienes un resumen de tu práctica actual."));

        // Stats
        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setBackground(UIUtils.COLOR_BG);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        stats.add(statCard("⏱", "Horas registradas", "—", new Color(224, 242, 254)));
        stats.add(statCard("📓", "Bitácoras enviadas", "—", new Color(220, 252, 231)));
        stats.add(statCard("📎", "Evidencias subidas", "—", new Color(254, 249, 195)));
        stats.add(statCard("⭐", "Calificación promedio", "—", new Color(237, 233, 254)));
        p.add(stats);
        p.add(Box.createVerticalStrut(24));

        // Acciones rápidas
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setBackground(UIUtils.COLOR_BG);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton bHoras    = UIUtils.btnPrimary("⏱ Registrar Horas Hoy", UIUtils.COLOR_PRIMARY);
        JButton bBitacora = UIUtils.btnPrimary("📓 Llenar Bitácora", UIUtils.COLOR_PRIMARY);
        JButton bEvidencia = UIUtils.btnPrimary("📎 Subir Evidencia", new Color(107, 114, 128));
        JButton bRetro     = UIUtils.btnPrimary("💬 Ver Retroalimentación", new Color(107, 114, 128));

        bHoras.addActionListener(e -> cardLayout.show(contentArea, "horas"));
        bBitacora.addActionListener(e -> cardLayout.show(contentArea, "bitacora"));
        bEvidencia.addActionListener(e -> cardLayout.show(contentArea, "evidencias"));
        bRetro.addActionListener(e -> cardLayout.show(contentArea, "retroalimentacion"));

        actions.add(bHoras);
        actions.add(bBitacora);
        actions.add(bEvidencia);
        actions.add(bRetro);
        p.add(actions);

        return p;
    }

    private JPanel statCard(String icon, String label, String value, Color bg) {
        JPanel c = new JPanel();
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)));
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        JLabel ico = new JLabel(icon);
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(UIUtils.COLOR_DARK);
        JLabel lbl = new JLabel(label);
        lbl.setFont(UIUtils.FONT_SMALL);
        lbl.setForeground(UIUtils.COLOR_TEXT_MUTED);
        c.add(ico);
        c.add(Box.createVerticalStrut(4));
        c.add(val);
        c.add(lbl);
        return c;
    }

    // ── BITÁCORA ──────────────────────────────────────────────────────────

    private JPanel buildBitacora() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Diligenciar Bitácora",
                "Responde el cuestionario asignado por tu director para esta práctica."));

        // Selector de práctica
        JPanel selRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        selRow.setBackground(UIUtils.COLOR_BG);
        selRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        selRow.add(new JLabel("ID Práctica:"));
        JTextField txtPractica = UIUtils.textField("ej: 1");
        txtPractica.setPreferredSize(new Dimension(80, 32));
        JButton btnCargar = UIUtils.btnPrimary("Cargar Preguntas", UIUtils.COLOR_PRIMARY);
        selRow.add(txtPractica);
        selRow.add(btnCargar);
        p.add(selRow);
        p.add(Box.createVerticalStrut(16));

        // Área de preguntas (dinámica)
        JPanel preguntasPanel = new JPanel();
        preguntasPanel.setBackground(UIUtils.COLOR_BG);
        preguntasPanel.setLayout(new BoxLayout(preguntasPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(preguntasPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setBackground(UIUtils.COLOR_BG);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        p.add(scroll);
        p.add(Box.createVerticalStrut(12));

        // Botón guardar
        JButton btnGuardar = UIUtils.btnPrimary("💾 Guardar Respuestas", UIUtils.COLOR_SUCCESS);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);
        final java.util.Map<Integer, JTextArea> respuestasMap = new java.util.HashMap<>();
        p.add(btnGuardar);

        btnCargar.addActionListener(e -> {
            try {
                int idPractica = Integer.parseInt(txtPractica.getText().trim());
                List<Pregunta> preguntas = preguntaDAO.listarPorPractica(idPractica);
                preguntasPanel.removeAll();
                respuestasMap.clear();

                if (preguntas.isEmpty()) {
                    preguntasPanel.add(new JLabel("No hay preguntas para esta práctica."));
                } else {
                    for (int i = 0; i < preguntas.size(); i++) {
                        Pregunta pr = preguntas.get(i);
                        JPanel card = UIUtils.cardPanel();
                        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                        card.setAlignmentX(Component.LEFT_ALIGNMENT);

                        JLabel lbl = new JLabel((i+1) + ". " + pr.getEnunciado()
                                + (pr.getObligatoria() == 1 ? " *" : ""));
                        lbl.setFont(UIUtils.FONT_LABEL);
                        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

                        JTextArea ta = UIUtils.textArea(3, 40);
                        ta.setAlignmentX(Component.LEFT_ALIGNMENT);
                        respuestasMap.put(pr.getIdPregunta(), ta);

                        card.add(lbl);
                        card.add(Box.createVerticalStrut(8));
                        card.add(new JScrollPane(ta));
                        preguntasPanel.add(card);
                        preguntasPanel.add(Box.createVerticalStrut(10));
                    }
                }
                preguntasPanel.revalidate();
                preguntasPanel.repaint();
            } catch (NumberFormatException ex) {
                showMsg("Ingresa un ID de práctica válido.", false);
            }
        });

        btnGuardar.addActionListener(e -> {
            if (respuestasMap.isEmpty()) { showMsg("Primero carga las preguntas.", false); return; }
            int guardadas = 0;
            for (java.util.Map.Entry<Integer, JTextArea> entry : respuestasMap.entrySet()) {
                String texto = entry.getValue().getText().trim();
                if (!texto.isEmpty()) {
                    Respuesta r = new Respuesta();
                    r.setIdPregunta(entry.getKey());
                    r.setIdBitacora(ID_BITACORA_DEFAULT);
                    r.setTextoRespuesta(texto);
                    r.setFechaRespuesta(Date.valueOf(LocalDate.now()));
                    if (respuestaDAO.guardarRespuesta(r)) guardadas++;
                }
            }
            showMsg("Se guardaron " + guardadas + " respuesta(s) correctamente.", true);
        });

        return p;
    }

    // ── EVIDENCIAS ────────────────────────────────────────────────────────

    private JPanel buildEvidencias() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Adjuntar Evidencias",
                "Sube archivos, fotos o documentos que demuestren tu proceso de práctica."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBit = new JLabel("ID Bitácora:");
        lblBit.setFont(UIUtils.FONT_LABEL);
        JTextField txtBitacora = UIUtils.textField("ej: 1");
        txtBitacora.setMaximumSize(new Dimension(200, 36));

        JLabel lblUrl = new JLabel("URL / Ruta del archivo:");
        lblUrl.setFont(UIUtils.FONT_LABEL);
        JTextField txtUrl = UIUtils.textField("https://... o ruta local");
        txtUrl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(UIUtils.FONT_LABEL);
        JTextArea taDesc = UIUtils.textArea(3, 40);

        JButton btnGuardar = UIUtils.btnPrimary("📎 Registrar Evidencia", UIUtils.COLOR_PRIMARY);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Component c : new Component[]{lblBit, txtBitacora, Box.createVerticalStrut(8),
                lblUrl, txtUrl, Box.createVerticalStrut(8),
                lblDesc, new JScrollPane(taDesc), Box.createVerticalStrut(10), btnGuardar}) {
            if (c instanceof JComponent jc) jc.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(c);
        }

        btnGuardar.addActionListener(e -> {
            try {
                int idBit = Integer.parseInt(txtBitacora.getText().trim());
                String url = txtUrl.getText().trim();
                String desc = taDesc.getText().trim();
                if (url.isEmpty()) { showMsg("Ingresa la URL o ruta del archivo.", false); return; }
                Evidencia ev = new Evidencia();
                ev.setIdBitacora(idBit);
                ev.setUrlArchivo(url);
                ev.setDescripcion(desc);
                ev.setFechaCarga(Date.valueOf(LocalDate.now()));
                if (evidenciaDAO.guardarEvidencia(ev)) {
                    showMsg("Evidencia registrada correctamente.", true);
                    txtUrl.setText(""); taDesc.setText("");
                } else {
                    showMsg("No se pudo registrar la evidencia.", false);
                }
            } catch (NumberFormatException ex) {
                showMsg("Ingresa un ID de bitácora válido.", false);
            }
        });

        p.add(form);
        return p;
    }

    // ── HORAS ─────────────────────────────────────────────────────────────

    private JPanel buildHoras() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Registrar Horas",
                "Anota tu entrada y salida de cada jornada para el control de horas reglamentarias."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblBit = new JLabel("ID Bitácora:");
        lblBit.setFont(UIUtils.FONT_LABEL);
        JTextField txtBitacora = UIUtils.textField("ej: 1");
        txtBitacora.setMaximumSize(new Dimension(200, 36));

        JLabel lblFecha = new JLabel("Fecha (AAAA-MM-DD):");
        lblFecha.setFont(UIUtils.FONT_LABEL);
        JTextField txtFecha = UIUtils.textField(LocalDate.now().toString());
        txtFecha.setText(LocalDate.now().toString());
        txtFecha.setMaximumSize(new Dimension(200, 36));

        JLabel lblEntrada = new JLabel("Hora Entrada (HH:MM):");
        lblEntrada.setFont(UIUtils.FONT_LABEL);
        JTextField txtEntrada = UIUtils.textField("08:00");
        txtEntrada.setMaximumSize(new Dimension(150, 36));

        JLabel lblSalida = new JLabel("Hora Salida (HH:MM):");
        lblSalida.setFont(UIUtils.FONT_LABEL);
        JTextField txtSalida = UIUtils.textField("17:00");
        txtSalida.setMaximumSize(new Dimension(150, 36));

        JLabel lblHoras = new JLabel("Horas cumplidas:");
        lblHoras.setFont(UIUtils.FONT_LABEL);
        JTextField txtHoras = UIUtils.textField("8");
        txtHoras.setMaximumSize(new Dimension(100, 36));

        JButton btnGuardar = UIUtils.btnPrimary("⏱ Registrar Jornada", UIUtils.COLOR_PRIMARY);
        btnGuardar.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Component c : new Component[]{lblBit, txtBitacora, Box.createVerticalStrut(8),
                lblFecha, txtFecha, Box.createVerticalStrut(8),
                lblEntrada, txtEntrada, Box.createVerticalStrut(8),
                lblSalida, txtSalida, Box.createVerticalStrut(8),
                lblHoras, txtHoras, Box.createVerticalStrut(10), btnGuardar}) {
            if (c instanceof JComponent jc) jc.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(c);
        }

        btnGuardar.addActionListener(e -> {
            try {
                int idBit = Integer.parseInt(txtBitacora.getText().trim());
                Date fecha = Date.valueOf(txtFecha.getText().trim());
                float horas = Float.parseFloat(txtHoras.getText().trim());

                String fechaStr = txtFecha.getText().trim();
                Timestamp entrada = Timestamp.valueOf(fechaStr + " " + txtEntrada.getText().trim() + ":00");
                Timestamp salida  = Timestamp.valueOf(fechaStr + " " + txtSalida.getText().trim() + ":00");

                ControlHoras ch = new ControlHoras();
                ch.setIdBitacora(idBit);
                ch.setFecha(fecha);
                ch.setHoraEntrada(entrada);
                ch.setHoraSalida(salida);
                ch.setHorasCumplidas(horas);

                if (horasDAO.registrarHoras(ch)) {
                    showMsg("Jornada registrada: " + horas + " horas.", true);
                } else {
                    showMsg("No se pudo registrar la jornada.", false);
                }
            } catch (Exception ex) {
                showMsg("Datos inválidos. Verifica el formato de fecha/hora.", false);
            }
        });

        p.add(form);
        return p;
    }

    // ── RETROALIMENTACIÓN ─────────────────────────────────────────────────

    private JPanel buildRetroalimentacion() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Retroalimentación",
                "Revisa los comentarios y notas que tu tutor dejó en tus bitácoras."));

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buscar.setBackground(UIUtils.COLOR_BG);
        buscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buscar.add(new JLabel("ID Bitácora:"));
        JTextField txtBit = UIUtils.textField("ej: 1");
        txtBit.setPreferredSize(new Dimension(80, 32));
        JButton btnBuscar = UIUtils.btnPrimary("Buscar", UIUtils.COLOR_PRIMARY);
        buscar.add(txtBit);
        buscar.add(btnBuscar);
        p.add(buscar);
        p.add(Box.createVerticalStrut(16));

        String[] cols = {"ID Obs.", "ID Asesor", "Fecha", "Observación"};
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        p.add(tableCard(tabla));

        btnBuscar.addActionListener(e -> {
            try {
                int idBit = Integer.parseInt(txtBit.getText().trim());
                List<Observacion> obs = observacionDAO.listarPorBitacora(idBit);
                Object[][] data = new Object[obs.size()][4];
                for (int i = 0; i < obs.size(); i++) {
                    Observacion o = obs.get(i);
                    data[i] = new Object[]{o.getIdObservacion(), o.getIdAsesor(),
                            o.getFecha(), o.getTexto()};
                }
                tabla.setModel(new javax.swing.table.DefaultTableModel(data, cols));
            } catch (NumberFormatException ex) {
                showMsg("Ingresa un ID de bitácora válido.", false);
            }
        });

        return p;
    }

    // ── EXPORTAR PDF ──────────────────────────────────────────────────────

    private JPanel buildExportar() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Exportar Bitácora PDF",
                "Descarga tu bitácora completa como constancia de tu proceso de práctica."));

        JPanel info = UIUtils.cardPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        info.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel("Función de exportación a PDF");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel sub = new JLabel("Para generar el PDF de tu bitácora, ingresa el ID y haz clic en Exportar.");
        sub.setFont(UIUtils.FONT_NORMAL);
        sub.setForeground(UIUtils.COLOR_TEXT_MUTED);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        row.setBackground(Color.WHITE);
        row.setOpaque(true);
        JTextField txtBit = UIUtils.textField("ID Bitácora");
        txtBit.setPreferredSize(new Dimension(100, 32));
        JButton btnExportar = UIUtils.btnPrimary("📄 Exportar PDF", UIUtils.COLOR_DANGER);
        row.add(new JLabel("ID Bitácora:"));
        row.add(txtBit);
        row.add(btnExportar);

        info.add(lbl);
        info.add(Box.createVerticalStrut(4));
        info.add(sub);
        info.add(row);

        btnExportar.addActionListener(e -> {
            showMsg("Exportación PDF: Esta funcionalidad debe implementarse\n"
                  + "con una librería como iText o Apache PDFBox.", true);
        });

        p.add(info);
        return p;
    }
}

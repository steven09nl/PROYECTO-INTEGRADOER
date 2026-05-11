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
 * Panel Director/Administrador — v6 con 7 arreglos:
 *  1. Botón "Crear Usuario" claramente visible
 *  2. Email generado automáticamente desde el nombre
 *  3. Tipo de práctica con ComboBox (docente / comunitaria)
 *  4. Rol "asesor" → "docente"
 *  5. ID Docente seleccionable desde ComboBox
 *  6. Fechas con selector de calendario (JSpinner)
 *  7. ID Práctica seleccionable desde ComboBox
 */
public class PanelDirector extends BasePanel {

    private final PracticaDAO     practicaDAO     = new PracticaDAO();
    private final UsuarioDAO      usuarioDAO      = new UsuarioDAO();
    private final PreguntaDAO     preguntaDAO     = new PreguntaDAO();
    private final BitacoraDAO     bitacoraDAO     = new BitacoraDAO();
    private final CalificacionDAO calificacionDAO = new CalificacionDAO();
    private final InformeDAO      informeDAO      = new InformeDAO();
    private final GrupoDAO        grupoDAO        = new GrupoDAO();

    public PanelDirector(Usuario u) {
        super(u, "Panel Director", UIUtils.COLOR_DIRECTOR.darker(), UIUtils.COLOR_DIRECTOR);
        contentArea.add(buildPracticas(),   "practicas");
        contentArea.add(buildUsuarios(),    "usuarios");
        contentArea.add(buildGrupos(),      "grupos");
        contentArea.add(buildPreguntas(),   "preguntas");
        contentArea.add(buildCalificar(),   "calificar");
        contentArea.add(buildBitacoras(),   "bitacoras");
        contentArea.add(buildSeguimiento(), "seguimiento");
        contentArea.add(buildInformes(),    "informes");
        cardLayout.show(contentArea, "practicas");
    }

    @Override
    protected void buildNavigation(JPanel sidebar) {
        addNavSection(sidebar, "Gestión Académica");
        addNavItem(sidebar, "📚", "Prácticas",  "practicas");
        addNavItem(sidebar, "👤", "Usuarios",   "usuarios");
        addNavItem(sidebar, "👥", "Grupos",     "grupos");
        addNavItem(sidebar, "❓", "Preguntas",  "preguntas");
        addNavSection(sidebar, "Evaluación");
        addNavItem(sidebar, "⭐", "Calificar",  "calificar");
        addNavItem(sidebar, "📓", "Bitácoras",  "bitacoras");
        addNavItem(sidebar, "📊", "Seguimiento","seguimiento");
        addNavSection(sidebar, "Reportes");
        addNavItem(sidebar, "📑", "Informes",   "informes");
    }

    @Override
    protected JPanel buildTopBar() {
        return buildTopBarBase("Bienvenido, " + usuario.getNombre(),
                               "Director / Admin", UIUtils.COLOR_DIRECTOR);
    }

    // ══════════════════════════════════════════════════════════════════════
    // PRÁCTICAS — ARREGLO 3 (ComboBox tipo) + ARREGLO 6 (calendarios)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildPracticas() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Gestión de Prácticas",
                "Registra, edita y organiza las prácticas del programa (máximo 8)."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 360));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        addFormTitle(form, "Nueva Práctica");

        // Nombre
        addLbl(form, "Nombre:");
        JTextField txtNombre = fld("Ej: Práctica I", Integer.MAX_VALUE);
        form.add(txtNombre);
        form.add(gap());

        // ARREGLO 3: tipo con ComboBox
        addLbl(form, "Tipo de Práctica:");
        JComboBox<String> cmbTipo = combo(new String[]{"docente", "comunitaria"});
        form.add(cmbTipo);
        form.add(gap());

        // Horas y semestre
        JPanel r1 = row();
        JTextField txtHoras    = smallFld("160", 80);
        JTextField txtSemestre = smallFld("2026-I", 110);
        r1.add(new JLabel("Horas regl.:")); r1.add(txtHoras);
        r1.add(Box.createHorizontalStrut(10));
        r1.add(new JLabel("Semestre:"));    r1.add(txtSemestre);
        form.add(r1); form.add(gap());

        // ARREGLO 6: fechas con calendario
        JPanel r2 = row();
        JSpinner spIni = dateSpinner(LocalDate.now());
        JSpinner spFin = dateSpinner(LocalDate.now().plusMonths(6));
        spIni.setPreferredSize(new Dimension(160, 32));
        spFin.setPreferredSize(new Dimension(160, 32));
        r2.add(new JLabel("📅 Fecha inicio:")); r2.add(spIni);
        r2.add(Box.createHorizontalStrut(10));
        r2.add(new JLabel("📅 Fecha fin:"));    r2.add(spFin);
        form.add(r2); form.add(gap());

        // ARREGLO 1: botón de crear visible
        JButton btnCrear = UIUtils.btnPrimary("📚  Crear Práctica", UIUtils.COLOR_DIRECTOR);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnCrear);
        p.add(form);
        p.add(Box.createVerticalStrut(20));

        String[] cols = {"ID","Nombre","Tipo","Horas","Estado","Semestre","Inicio","Fin"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);
        JButton btnCargar = UIUtils.btnPrimary("🔄 Listar Prácticas", UIUtils.COLOR_DIRECTOR);
        btnCargar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnCrear.addActionListener(e -> {
            try {
                Practica prac = new Practica();
                prac.setNombre(txtNombre.getText().trim());
                prac.setTipoPractica((String) cmbTipo.getSelectedItem());
                prac.setHorasReglamentarias(Integer.parseInt(txtHoras.getText().trim()));
                prac.setSemestre(txtSemestre.getText().trim());
                prac.setFechaInicio(spinnerDate(spIni));
                prac.setFechaFin(spinnerDate(spFin));
                prac.setEstado("Activa");
                if (practicaDAO.crearPractica(prac)) { showMsg("Práctica creada correctamente.", true); txtNombre.setText(""); }
                else showMsg("No se pudo crear la práctica.", false);
            } catch (Exception ex) { showMsg("Verifica los datos (horas: número).", false); }
        });
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            for (Practica pr : practicaDAO.listarTodas())
                model.addRow(new Object[]{pr.getIdPractica(),pr.getNombre(),pr.getTipoPractica(),
                        pr.getHorasReglamentarias(),pr.getEstado(),pr.getSemestre(),pr.getFechaInicio(),pr.getFechaFin()});
        });
        p.add(btnCargar); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // USUARIOS — ARREGLO 1 (botón crear) + ARREGLO 2 (email auto) + ARREGLO 4 (docente)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildUsuarios() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Gestión de Usuarios",
                "Crea y administra las cuentas. El usuario ID=1 (superusuario) no puede eliminarse."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 420));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        addFormTitle(form, "Nuevo Usuario");

        // Nombre
        addLbl(form, "Nombre completo:");
        JTextField txtNombre = fld("Nombre completo", Integer.MAX_VALUE);
        form.add(txtNombre);
        form.add(gap());

        // ARREGLO 2: Email auto-generado
        addLbl(form, "Email  ✨ se genera automáticamente:");
        JPanel emailRow = new JPanel(new BorderLayout(6, 0));
        emailRow.setOpaque(false);
        emailRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        emailRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField txtEmail = UIUtils.textField("Se genera al escribir el nombre");
        txtEmail.setEditable(false);
        txtEmail.setBackground(new Color(0xEFF6FF));
        txtEmail.setForeground(new Color(0x1D4ED8));
        JButton btnRegen = UIUtils.btnPrimary("⟳", UIUtils.COLOR_DIRECTOR);
        btnRegen.setPreferredSize(new Dimension(42, 32));
        btnRegen.setToolTipText("Regenerar email");
        emailRow.add(txtEmail, BorderLayout.CENTER);
        emailRow.add(btnRegen, BorderLayout.EAST);
        form.add(emailRow);
        JLabel hint = new JLabel("   formato: nombre.apellido@universidad.edu.co");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(UIUtils.COLOR_TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);
        form.add(gap());

        // auto-generación en tiempo real
        txtNombre.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { txtEmail.setText(makeEmail(txtNombre.getText())); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { txtEmail.setText(makeEmail(txtNombre.getText())); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { txtEmail.setText(makeEmail(txtNombre.getText())); }
        });
        btnRegen.addActionListener(e -> txtEmail.setText(makeEmail(txtNombre.getText())));

        // Contraseña
        addLbl(form, "Contraseña:");
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209,213,219),1,true), new EmptyBorder(8,12,8,12)));
        txtPass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(txtPass); form.add(gap());

        // ARREGLO 4: rol "docente" en lugar de "asesor"
        addLbl(form, "Rol:");
        JComboBox<String> cmbRol = combo(new String[]{"estudiante", "docente", "administrador"});
        form.add(cmbRol); form.add(gap());

        addLbl(form, "Estado:");
        JComboBox<String> cmbEstado = combo(new String[]{"activo", "inactivo"});
        form.add(cmbEstado); form.add(gap());

        // ARREGLO 1: botón CREAR prominente
        JButton btnCrear = UIUtils.btnPrimary("👤  Crear Usuario", UIUtils.COLOR_DIRECTOR);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnCrear);
        p.add(form);
        p.add(Box.createVerticalStrut(20));

        String[] cols = {"ID","Nombre","Email","Rol","Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(UIUtils.COLOR_BG);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JButton btnCargar   = UIUtils.btnPrimary("🔄 Listar Usuarios",      UIUtils.COLOR_DIRECTOR);
        JButton btnEliminar = UIUtils.btnPrimary("🗑 Eliminar Seleccionado", UIUtils.COLOR_DANGER);
        btnRow.add(btnCargar); btnRow.add(btnEliminar);

        btnCrear.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String email  = txtEmail.getText().trim();
                if (nombre.isEmpty()) { showMsg("Ingresa el nombre del usuario.", false); return; }
                Usuario u = new Usuario();
                u.setNombre(nombre); u.setEmail(email);
                u.setPassword(new String(txtPass.getPassword()));
                u.setRol((String) cmbRol.getSelectedItem());
                u.setEstado((String) cmbEstado.getSelectedItem());
                if (usuarioDAO.crearUsuario(u)) {
                    showMsg("Usuario creado correctamente.", true);
                    txtNombre.setText(""); txtPass.setText(""); txtEmail.setText("");
                } else showMsg("No se pudo crear el usuario.", false);
            } catch (Exception ex) { showMsg("Verifica los datos ingresados.", false); }
        });
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            for (Usuario u : usuarioDAO.listarTodos())
                model.addRow(new Object[]{u.getIdUsuario(),u.getNombre(),u.getEmail(),u.getRol(),u.getEstado()});
        });
        btnEliminar.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { showMsg("Selecciona un usuario de la tabla.", false); return; }
            int id = (int) model.getValueAt(row, 0);
            if (usuarioDAO.esSuperUsuario(id)) {
                showMsg("⚠️ El superusuario (ID=1) no puede ser eliminado.\nEste usuario es necesario para el funcionamiento del sistema.", false); return;
            }
            int c = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el usuario '" + model.getValueAt(row,1) + "' (ID "+id+")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                if (usuarioDAO.eliminarUsuario(id)) { model.removeRow(row); showMsg("Usuario eliminado correctamente.", true); }
                else showMsg("No se pudo eliminar el usuario.", false);
            }
        });
        p.add(btnRow); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // GRUPOS — ARREGLO 5 (ComboBox docente) + ARREGLO 6 (calendario) + ARREGLO 7 (práctica)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildGrupos() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Gestión de Grupos",
                "Al eliminar un grupo, los estudiantes NO se eliminan — solo se desvinculan."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        addFormTitle(form, "Nuevo Grupo");

        // Nombre + Semestre
        JPanel r1 = row();
        JTextField txtNombre   = smallFld("Nombre del grupo", 200);
        JTextField txtSemestre = smallFld("2026-I", 110);
        r1.add(new JLabel("Nombre:")); r1.add(txtNombre);
        r1.add(Box.createHorizontalStrut(10));
        r1.add(new JLabel("Semestre:")); r1.add(txtSemestre);
        form.add(r1); form.add(gap());

        // ARREGLO 5: ComboBox con docentes
        addLbl(form, "Docente responsable:");
        JPanel docRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        docRow.setOpaque(false);
        docRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        docRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JComboBox<DocenteItem> cmbDocente = new JComboBox<>();
        cmbDocente.setPreferredSize(new Dimension(260, 32));
        cmbDocente.setFont(UIUtils.FONT_NORMAL);
        JButton btnLoadDoc = UIUtils.btnPrimary("↻ Cargar Docentes", UIUtils.COLOR_DIRECTOR);
        btnLoadDoc.addActionListener(e -> loadDocentes(cmbDocente));
        docRow.add(cmbDocente); docRow.add(btnLoadDoc);
        form.add(docRow); form.add(gap());

        // ARREGLO 6: Fecha con calendario
        JPanel r2 = row();
        JSpinner spFecha = dateSpinner(LocalDate.now());
        spFecha.setPreferredSize(new Dimension(180, 32));
        r2.add(new JLabel("📅 Fecha inicio:")); r2.add(spFecha);
        form.add(r2); form.add(gap());

        JButton btnCrear = UIUtils.btnPrimary("👥  Crear Grupo", UIUtils.COLOR_DIRECTOR);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnCrear);
        p.add(form); p.add(Box.createVerticalStrut(16));

        // Tabla grupos
        String[] colsG = {"ID Grupo","Nombre","Semestre","ID Docente","Nombre Docente","Fecha Inicio"};
        DefaultTableModel modelG = new DefaultTableModel(colsG, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaG = UIUtils.styledTable(colsG, new Object[0][]);
        tablaG.setModel(modelG);

        JLabel lblEst = new JLabel("Estudiantes del grupo seleccionado:");
        lblEst.setFont(UIUtils.FONT_LABEL); lblEst.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] colsE = {"ID","Nombre","Email"};
        DefaultTableModel modelE = new DefaultTableModel(colsE, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaE = UIUtils.styledTable(colsE, new Object[0][]);
        tablaE.setModel(modelE);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(UIUtils.COLOR_BG);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        JButton btnCargar   = UIUtils.btnPrimary("🔄 Listar Grupos",   UIUtils.COLOR_DIRECTOR);
        JButton btnEliminar = UIUtils.btnPrimary("🗑 Eliminar Grupo",   UIUtils.COLOR_DANGER);
        JButton btnVerEst   = UIUtils.btnPrimary("👁 Ver Estudiantes",  UIUtils.COLOR_DIRECTOR);
        JTextField txtAddEst = smallFld("ID estudiante", 110);
        JButton btnAddEst   = UIUtils.btnPrimary("➕ Agregar", UIUtils.COLOR_DIRECTOR);
        btnRow.add(btnCargar); btnRow.add(btnEliminar);
        btnRow.add(btnVerEst); btnRow.add(new JLabel(" Add:")); btnRow.add(txtAddEst); btnRow.add(btnAddEst);

        btnCrear.addActionListener(e -> {
            try {
                DocenteItem doc = (DocenteItem) cmbDocente.getSelectedItem();
                if (doc == null) { showMsg("Carga y selecciona un docente primero.", false); return; }
                if (grupoDAO.crearGrupo(txtNombre.getText().trim(), txtSemestre.getText().trim(),
                        doc.id, spinnerDate(spFecha))) {
                    showMsg("Grupo creado correctamente.", true); txtNombre.setText("");
                } else showMsg("No se pudo crear el grupo.", false);
            } catch (Exception ex) { showMsg("Verifica los datos.", false); }
        });
        btnCargar.addActionListener(e -> { modelG.setRowCount(0); modelE.setRowCount(0); for (Object[] g : grupoDAO.listarGrupos()) modelG.addRow(g); });
        btnEliminar.addActionListener(e -> {
            int row = tablaG.getSelectedRow();
            if (row < 0) { showMsg("Selecciona un grupo.", false); return; }
            int idG = (int) modelG.getValueAt(row,0);
            int c = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el grupo '" + modelG.getValueAt(row,1) + "'?\nLos estudiantes NO serán eliminados.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (c==JOptionPane.YES_OPTION) {
                if (grupoDAO.eliminarGrupo(idG)) { modelG.removeRow(row); modelE.setRowCount(0); showMsg("Grupo eliminado. Estudiantes conservados.", true); }
                else showMsg("No se pudo eliminar el grupo.", false);
            }
        });
        btnVerEst.addActionListener(e -> {
            int row = tablaG.getSelectedRow();
            if (row < 0) { showMsg("Selecciona un grupo.", false); return; }
            modelE.setRowCount(0);
            for (Object[] est : grupoDAO.listarEstudiantesDeGrupo((int)modelG.getValueAt(row,0))) modelE.addRow(est);
        });
        btnAddEst.addActionListener(e -> {
            int row = tablaG.getSelectedRow();
            if (row < 0) { showMsg("Selecciona un grupo primero.", false); return; }
            try {
                int idG = (int) modelG.getValueAt(row,0);
                int idE = Integer.parseInt(txtAddEst.getText().trim());
                if (grupoDAO.agregarEstudiante(idG, idE)) {
                    showMsg("Estudiante agregado.", true); modelE.setRowCount(0);
                    for (Object[] est : grupoDAO.listarEstudiantesDeGrupo(idG)) modelE.addRow(est);
                } else showMsg("No se pudo agregar el estudiante.", false);
            } catch (NumberFormatException ex) { showMsg("Ingresa un ID de estudiante válido.", false); }
        });
        p.add(btnRow); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tablaG));
        p.add(Box.createVerticalStrut(12)); p.add(lblEst); p.add(Box.createVerticalStrut(6)); p.add(tableCard(tablaE));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // PREGUNTAS — ARREGLO 7 (ComboBox práctica)
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildPreguntas() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Configurar Preguntas",
                "Arma el banco de preguntas por práctica para evaluar a los estudiantes."));

        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 310));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        addFormTitle(form, "Nueva Pregunta");

        // ARREGLO 7: Selector de práctica con ComboBox
        addLbl(form, "Práctica:");
        JPanel pracRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pracRow.setOpaque(false); pracRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        pracRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        JComboBox<PracticaItem> cmbPrac = new JComboBox<>();
        cmbPrac.setPreferredSize(new Dimension(280, 32));
        cmbPrac.setFont(UIUtils.FONT_NORMAL);
        JButton btnLoadPrac = UIUtils.btnPrimary("↻ Cargar Prácticas", UIUtils.COLOR_DIRECTOR);
        btnLoadPrac.addActionListener(e -> loadPracticas(cmbPrac));
        pracRow.add(cmbPrac); pracRow.add(btnLoadPrac);
        form.add(pracRow); form.add(gap());

        addLbl(form, "Enunciado:");
        JTextArea taEnunc = UIUtils.textArea(3, 50);
        JScrollPane spEnunc = new JScrollPane(taEnunc);
        spEnunc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        spEnunc.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(spEnunc); form.add(gap());

        JPanel r = row();
        JComboBox<String> cmbTipo = combo(new String[]{"Abierta","Cerrada","Selección"});
        JComboBox<String> cmbObl  = combo(new String[]{"Sí (1)","No (0)"});
        JTextField txtOrden = smallFld("1", 60);
        r.add(new JLabel("Tipo:")); r.add(cmbTipo);
        r.add(Box.createHorizontalStrut(8));
        r.add(new JLabel("Obligatoria:")); r.add(cmbObl);
        r.add(Box.createHorizontalStrut(8));
        r.add(new JLabel("Orden:")); r.add(txtOrden);
        form.add(r); form.add(gap());

        JButton btnAgregar = UIUtils.btnPrimary("❓  Agregar Pregunta", UIUtils.COLOR_DIRECTOR);
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnAgregar);
        p.add(form); p.add(Box.createVerticalStrut(20));

        String[] cols = {"ID","ID Práctica","Enunciado","Tipo","Obligatoria","Orden"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(UIUtils.COLOR_BG);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // ARREGLO 7: filtro también con ComboBox
        JComboBox<PracticaItem> cmbFiltro = new JComboBox<>();
        cmbFiltro.setPreferredSize(new Dimension(220, 32));
        cmbFiltro.setFont(UIUtils.FONT_NORMAL);
        JButton btnListar   = UIUtils.btnPrimary("🔄 Listar",                UIUtils.COLOR_DIRECTOR);
        JButton btnEliminar = UIUtils.btnPrimary("🗑 Eliminar Seleccionada", UIUtils.COLOR_DANGER);
        btnRow.add(new JLabel("Filtrar:")); btnRow.add(cmbFiltro);
        btnRow.add(btnListar); btnRow.add(btnEliminar);

        // al cargar prácticas, actualizar ambos combos
        btnLoadPrac.addActionListener(e -> { loadPracticas(cmbPrac); loadPracticas(cmbFiltro); });

        btnAgregar.addActionListener(e -> {
            try {
                PracticaItem pi = (PracticaItem) cmbPrac.getSelectedItem();
                if (pi == null) { showMsg("Carga y selecciona una práctica.", false); return; }
                Pregunta pr = new Pregunta();
                pr.setIdPractica(pi.id);
                pr.setEnunciado(taEnunc.getText().trim());
                pr.setTipoPregunta((String) cmbTipo.getSelectedItem());
                pr.setObligatoria(cmbObl.getSelectedIndex() == 0 ? 1 : 0);
                pr.setOrden(Integer.parseInt(txtOrden.getText().trim()));
                if (preguntaDAO.agregarPregunta(pr)) { showMsg("Pregunta agregada.", true); taEnunc.setText(""); }
                else showMsg("No se pudo agregar la pregunta.", false);
            } catch (Exception ex) { showMsg("Verifica los datos.", false); }
        });
        btnListar.addActionListener(e -> {
            model.setRowCount(0);
            PracticaItem pi = (PracticaItem) cmbFiltro.getSelectedItem();
            List<Pregunta> lista = (pi != null) ? preguntaDAO.listarPorPractica(pi.id) : preguntaDAO.listarTodas();
            for (Pregunta pr : lista)
                model.addRow(new Object[]{pr.getIdPregunta(),pr.getIdPractica(),pr.getEnunciado(),pr.getTipoPregunta(),pr.getObligatoria(),pr.getOrden()});
        });
        btnEliminar.addActionListener(e -> {
            int r2 = tabla.getSelectedRow();
            if (r2 < 0) { showMsg("Selecciona una pregunta.", false); return; }
            int id = (int) model.getValueAt(r2, 0);
            if (preguntaDAO.eliminarPregunta(id)) { model.removeRow(r2); showMsg("Pregunta eliminada.", true); }
            else showMsg("No se pudo eliminar.", false);
        });
        p.add(btnRow); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // CALIFICAR
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildCalificar() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Retroalimentar y Calificar",
                "Selecciona una bitácora de la tabla y asigna una calificación."));

        String[] cols = {"ID Bitácora","ID Estudiante","ID Práctica","Estado","Calificación"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);
        JButton btnCargar = UIUtils.btnPrimary("🔄 Cargar Bitácoras", UIUtils.COLOR_DIRECTOR);
        btnCargar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel formCal = UIUtils.cardPanel();
        formCal.setLayout(new BoxLayout(formCal, BoxLayout.Y_AXIS));
        formCal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        formCal.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lbTitle = new JLabel("Calificar bitácora seleccionada");
        lbTitle.setFont(UIUtils.FONT_LABEL);
        JPanel rCal = row();
        JTextField txtNota = smallFld("0.0 - 5.0", 110);
        JComboBox<String> cmbEst = combo(new String[]{"revisada","pendiente","aprobada","reprobada"});
        JButton btnCal = UIUtils.btnPrimary("⭐ Calificar", UIUtils.COLOR_DIRECTOR);
        rCal.add(new JLabel("Nota:")); rCal.add(txtNota);
        rCal.add(Box.createHorizontalStrut(8));
        rCal.add(new JLabel("Estado:")); rCal.add(cmbEst);
        rCal.add(Box.createHorizontalStrut(8)); rCal.add(btnCal);
        formCal.add(lbTitle); formCal.add(Box.createVerticalStrut(8)); formCal.add(rCal);

        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            for (Bitacora b : bitacoraDAO.listarBitacoras())
                model.addRow(new Object[]{b.getIdBitacora(),b.getIdEstudiante(),b.getIdPractica(),b.getEstado(),b.getCalificacion()});
        });
        btnCal.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { showMsg("Selecciona una bitácora.", false); return; }
            try {
                int idBit = (int) model.getValueAt(row,0);
                double nota = Double.parseDouble(txtNota.getText().trim());
                String est  = (String) cmbEst.getSelectedItem();
                if (nota < 0 || nota > 5) { showMsg("La nota debe estar entre 0.0 y 5.0.", false); return; }
                if (calificacionDAO.calificarBitacora(idBit, nota, est)) {
                    showMsg("Bitácora ID "+idBit+" calificada con "+nota+".", true);
                    model.setValueAt(nota, row, 4); model.setValueAt(est, row, 3);
                } else showMsg("No se pudo calificar.", false);
            } catch (NumberFormatException ex) { showMsg("Ingresa una nota válida.", false); }
        });
        p.add(btnCargar); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        p.add(Box.createVerticalStrut(16)); p.add(formCal);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // BITÁCORAS
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildBitacoras() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Gestión de Bitácoras",
                "Al eliminar una bitácora, también se eliminan sus registros asociados."));
        String[] cols = {"ID Bitácora","ID Estudiante","ID Práctica","Estado","Modalidad","Fecha Envío","Calificación"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]);
        tabla.setModel(model);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnRow.setBackground(UIUtils.COLOR_BG); btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JButton btnCargar   = UIUtils.btnPrimary("🔄 Cargar Bitácoras", UIUtils.COLOR_DIRECTOR);
        JButton btnEliminar = UIUtils.btnPrimary("🗑 Eliminar Bitácora", UIUtils.COLOR_DANGER);
        btnRow.add(btnCargar); btnRow.add(btnEliminar);
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            for (Bitacora b : bitacoraDAO.listarBitacoras())
                model.addRow(new Object[]{b.getIdBitacora(),b.getIdEstudiante(),b.getIdPractica(),b.getEstado(),b.getModalidad(),b.getFechaEnvio(),b.getCalificacion()});
        });
        btnEliminar.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { showMsg("Selecciona una bitácora.", false); return; }
            int id = (int) model.getValueAt(row,0);
            int c = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la bitácora ID "+id+"?\n⚠️ También se eliminarán respuestas, observaciones y evidencias.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c==JOptionPane.YES_OPTION) {
                if (bitacoraDAO.eliminarBitacora(id)) { model.removeRow(row); showMsg("Bitácora eliminada.", true); }
                else showMsg("No se pudo eliminar la bitácora.", false);
            }
        });
        p.add(btnRow); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // SEGUIMIENTO
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildSeguimiento() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Panel de Seguimiento", "Vista general del avance de todos los estudiantes."));
        String[] cols = {"ID","Nombre","Email","Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]); tabla.setModel(model);
        JButton btnCargar = UIUtils.btnPrimary("🔄 Actualizar Seguimiento", UIUtils.COLOR_DIRECTOR);
        btnCargar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCargar.addActionListener(e -> {
            model.setRowCount(0);
            for (Usuario u : usuarioDAO.listarTodos())
                if ("estudiante".equalsIgnoreCase(u.getRol()))
                    model.addRow(new Object[]{u.getIdUsuario(),u.getNombre(),u.getEmail(),u.getEstado()});
        });
        p.add(btnCargar); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // INFORMES
    // ══════════════════════════════════════════════════════════════════════
    private JPanel buildInformes() {
        JPanel p = sectionPanel();
        p.add(sectionHeader("Generar Informes","Exporta reportes consolidados para acreditación."));
        JPanel form = UIUtils.cardPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        form.setAlignmentX(Component.LEFT_ALIGNMENT);
        addFormTitle(form, "Registrar nuevo informe");
        JComboBox<String> cmbTipo = combo(new String[]{"Consolidado","Por práctica","Por estudiante","Acreditación MEN"});
        JTextField txtPeriodo = fld("ej: 2026-I", Integer.MAX_VALUE);
        JTextField txtUrl     = fld("URL o ruta del archivo generado", Integer.MAX_VALUE);
        for (String l : new String[]{"Tipo de informe:","Período:","URL Archivo:"}) {
            addLbl(form, l);
            JComponent comp = l.equals("Tipo de informe:") ? cmbTipo : l.equals("Período:") ? txtPeriodo : txtUrl;
            comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(comp); form.add(gap());
        }
        JButton btnGenerar = UIUtils.btnPrimary("📑 Registrar Informe", UIUtils.COLOR_DIRECTOR);
        btnGenerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(btnGenerar);
        p.add(form); p.add(Box.createVerticalStrut(20));
        String[] cols = {"ID","Tipo","Período","Fecha","URL"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = UIUtils.styledTable(cols, new Object[0][]); tabla.setModel(model);
        JButton btnListar = UIUtils.btnPrimary("🔄 Listar Informes", UIUtils.COLOR_DIRECTOR);
        btnListar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerar.addActionListener(e -> {
            try {
                Informe inf = new Informe();
                inf.setIdUsuarioGen(usuario.getIdUsuario());
                inf.setTipoInforme((String) cmbTipo.getSelectedItem());
                inf.setPeriodo(txtPeriodo.getText().trim());
                inf.setFechaGeneracion(Date.valueOf(LocalDate.now()));
                inf.setUrlArchivo(txtUrl.getText().trim());
                if (informeDAO.registrarInforme(inf)) { showMsg("Informe registrado correctamente.", true); txtPeriodo.setText(""); txtUrl.setText(""); }
                else showMsg("No se pudo registrar el informe.", false);
            } catch (Exception ex) { showMsg("Verifica los datos.", false); }
        });
        btnListar.addActionListener(e -> {
            model.setRowCount(0);
            for (Informe inf : informeDAO.listarInformes())
                model.addRow(new Object[]{inf.getIdInforme(),inf.getTipoInforme(),inf.getPeriodo(),inf.getFechaGeneracion(),inf.getUrlArchivo()});
        });
        p.add(btnListar); p.add(Box.createVerticalStrut(8)); p.add(tableCard(tabla));
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════════════════════════════════════

    /** Genera email: "Juan Pérez González" → "juan.gonzalez@universidad.edu.co" */
    private String makeEmail(String nombre) {
        if (nombre == null || nombre.isBlank()) return "";
        String clean = java.text.Normalizer
            .normalize(nombre.toLowerCase().trim(), java.text.Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
            .replaceAll("[^a-z0-9 ]", "").trim();
        String[] p = clean.split("\\s+");
        String local = p.length >= 2 ? p[0] + "." + p[p.length - 1] : p[0];
        return local + "@universidad.edu.co";
    }

    private void loadDocentes(JComboBox<DocenteItem> cmb) {
        cmb.removeAllItems();
        for (Usuario u : usuarioDAO.listarTodos())
            if ("docente".equalsIgnoreCase(u.getRol()) || "asesor".equalsIgnoreCase(u.getRol())
                    || "administrador".equalsIgnoreCase(u.getRol()))
                cmb.addItem(new DocenteItem(u.getIdUsuario(), u.getNombre()));
        if (cmb.getItemCount() == 0)
            JOptionPane.showMessageDialog(this, "No hay docentes registrados aún.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadPracticas(JComboBox<PracticaItem> cmb) {
        cmb.removeAllItems();
        for (Practica pr : practicaDAO.listarTodas())
            cmb.addItem(new PracticaItem(pr.getIdPractica(), pr.getNombre()));
        if (cmb.getItemCount() == 0)
            JOptionPane.showMessageDialog(this, "No hay prácticas registradas aún.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private JSpinner dateSpinner(LocalDate initial) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(initial.getYear(), initial.getMonthValue()-1, initial.getDayOfMonth());
        SpinnerDateModel mdl = new SpinnerDateModel(cal.getTime(), null, null, java.util.Calendar.DAY_OF_MONTH);
        JSpinner sp = new JSpinner(mdl);
        sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd"));
        sp.setFont(UIUtils.FONT_NORMAL);
        return sp;
    }

    private Date spinnerDate(JSpinner sp) {
        return new Date(((java.util.Date) sp.getValue()).getTime());
    }

    // micro-helpers de layout
    private void addFormTitle(JPanel form, String title) {
        JLabel l = new JLabel(title);
        l.setFont(new Font("Segoe UI", Font.BOLD, 15));
        l.setForeground(UIUtils.COLOR_TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(l); form.add(Box.createVerticalStrut(10));
    }
    private void addLbl(JPanel form, String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIUtils.FONT_LABEL);
        l.setForeground(UIUtils.COLOR_TEXT);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(l);
    }
    private JTextField fld(String ph, int maxW) {
        JTextField tf = UIUtils.textField(ph);
        tf.setMaximumSize(new Dimension(maxW, 36));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }
    private JTextField smallFld(String ph, int w) {
        JTextField tf = UIUtils.textField(ph);
        tf.setPreferredSize(new Dimension(w, 32));
        return tf;
    }
    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = UIUtils.comboBox(items);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cb;
    }
    private Component gap() { return Box.createVerticalStrut(8); }
    private JPanel row() {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        r.setBackground(Color.WHITE); r.setOpaque(true);
        r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        r.setAlignmentX(Component.LEFT_ALIGNMENT);
        return r;
    }

    // inner classes para ComboBox
    private static class DocenteItem {
        final int id; final String nombre;
        DocenteItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return id + " – " + nombre; }
    }
    private static class PracticaItem {
        final int id; final String nombre;
        PracticaItem(int id, String nombre) { this.id = id; this.nombre = nombre; }
        @Override public String toString() { return id + " – " + nombre; }
    }
}

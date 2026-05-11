package com.integrador.views;

import com.integrador.dao.UsuarioDAO;
import com.integrador.models.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class LoginFrame extends JFrame {

    private final JTextField    txtEmail;
    private final JPasswordField txtPassword;
    private final JLabel        lblError;
    private final JButton       btnLogin;
    private final UsuarioDAO    usuarioDAO = new UsuarioDAO();

    public LoginFrame() {
        setTitle("Sistema de Practicas - Iniciar Sesion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // ── Panel raíz ────────────────────────────────────────────────
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // ── Panel izquierdo — ilustración/branding ────────────────────
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fondo degradado
                GradientPaint gp = new GradientPaint(
                    0, 0,        new Color(0x1E1B4B),
                    0, getHeight(), new Color(0x312E81)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Círculo decorativo grande
                g2.setColor(new Color(0x4338CA));
                g2.fillOval(-80, getHeight()/2 - 180, 360, 360);

                // Círculo pequeño superior derecho
                g2.setColor(new Color(0x6D28D9, false));
                g2.fillOval(getWidth()-80, -40, 160, 160);

                g2.dispose();
            }
        };
        left.setPreferredSize(new Dimension(360, 0));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(64, 48, 64, 40));
        left.setOpaque(true);

        JLabel lblIcon = new JLabel("SP");
        lblIcon.setFont(new Font("SansSerif", Font.BOLD, 40));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel iconBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x4F46E5));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconBg.setOpaque(false);
        iconBg.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        iconBg.setMaximumSize(new Dimension(64, 64));
        iconBg.setPreferredSize(new Dimension(64, 64));
        iconBg.add(lblIcon);
        iconBg.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitulo = new JLabel("<html><span style='font-size:22pt;font-weight:bold;'>Sistema de<br>Practicas</span></html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDesc = new JLabel(
            "<html><p style='width:230px;line-height:1.6;font-size:10pt;'>"
            + "Plataforma universitaria para la gestion integral de practicas profesionales, "
            + "bitacoras y seguimiento academico.</p></html>");
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(0xA5B4FC));
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Badges de roles
        JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        badges.setOpaque(false);
        badges.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        badges.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (String role : new String[]{"Estudiante", "Asesor", "Director"}) {
            JLabel badge = new JLabel(role);
            badge.setFont(new Font("SansSerif", Font.BOLD, 11));
            badge.setForeground(new Color(0xC7D2FE));
            badge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x4338CA), 1, true),
                new EmptyBorder(3, 10, 3, 10)
            ));
            badges.add(badge);
        }

        left.add(iconBg);
        left.add(Box.createVerticalStrut(32));
        left.add(lblTitulo);
        left.add(Box.createVerticalStrut(16));
        left.add(lblDesc);
        left.add(Box.createVerticalStrut(24));
        left.add(badges);

        // ── Panel derecho — formulario ────────────────────────────────
        JPanel right = new JPanel();
        right.setBackground(Color.WHITE);
        right.setOpaque(true);
        right.setLayout(new GridBagLayout());

        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setOpaque(true);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(360, 420));

        // Título del formulario
        JLabel lblLoginTitle = new JLabel("Iniciar Sesion");
        lblLoginTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblLoginTitle.setForeground(UIUtils.COLOR_TEXT);
        lblLoginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Ingresa con tu correo institucional");
        lblSub.setFont(UIUtils.FONT_SUBTITLE);
        lblSub.setForeground(UIUtils.COLOR_TEXT_MUTED);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo email
        JLabel lblEmailLbl = new JLabel("Correo Electronico");
        lblEmailLbl.setFont(UIUtils.FONT_LABEL);
        lblEmailLbl.setForeground(UIUtils.COLOR_TEXT);
        lblEmailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtEmail = UIUtils.textField("ejemplo@correo.com");
        txtEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo password
        JLabel lblPassLbl = new JLabel("Contrasena");
        lblPassLbl.setFont(UIUtils.FONT_LABEL);
        lblPassLbl.setForeground(UIUtils.COLOR_TEXT);
        lblPassLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword = UIUtils.passwordField();
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón ingresar
        btnLogin = UIUtils.btnPrimary("  Ingresar al Sistema", UIUtils.COLOR_PRIMARY);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Error label
        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblError.setForeground(UIUtils.COLOR_DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Footer
        JLabel lblCopy = new JLabel("© 2026  Sistema de Practicas - Universidad");
        lblCopy.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblCopy.setForeground(new Color(0xCBD5E1));
        lblCopy.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(lblLoginTitle);
        form.add(Box.createVerticalStrut(6));
        form.add(lblSub);
        form.add(Box.createVerticalStrut(32));
        form.add(lblEmailLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(txtEmail);
        form.add(Box.createVerticalStrut(18));
        form.add(lblPassLbl);
        form.add(Box.createVerticalStrut(6));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(24));
        form.add(btnLogin);
        form.add(Box.createVerticalStrut(12));
        form.add(lblError);
        form.add(Box.createVerticalGlue());

        JSeparator sep = new JSeparator();
        sep.setForeground(UIUtils.COLOR_BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(sep);
        form.add(Box.createVerticalStrut(12));
        form.add(lblCopy);

        right.add(form);

        // ── Acciones login ────────────────────────────────────────────
        ActionListener loginAction = e -> doLogin();
        btnLogin.addActionListener(loginAction);
        txtPassword.addActionListener(loginAction);

        root.add(left,  BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void doLogin() {
        String email    = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            lblError.setText("Ingresa tu correo y contrasena.");
            return;
        }
        btnLogin.setEnabled(false);
        btnLogin.setText("  Verificando...");
        lblError.setText(" ");

        SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
            @Override protected Usuario doInBackground() {
                return usuarioDAO.loginEmail(email, password);
            }
            @Override protected void done() {
                btnLogin.setEnabled(true);
                btnLogin.setText("  Ingresar al Sistema");
                try {
                    Usuario u = get();
                    if (u == null) {
                        lblError.setText("Credenciales invalidas. Verifica tu email y contrasena.");
                        return;
                    }
                    if (!"activo".equalsIgnoreCase(u.getEstado())) {
                        lblError.setText("El usuario se encuentra inactivo.");
                        return;
                    }
                    abrirPanel(u);
                } catch (Exception ex) {
                    lblError.setText("Error al conectar con la base de datos.");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void abrirPanel(Usuario u) {
        dispose();
        String rol = (u.getRol() == null ? "" : u.getRol().trim().toLowerCase());
        switch (rol) {
            case "estudiante"   -> new PanelEstudiante(u).setVisible(true);
            case "asesor", "docente" -> new PanelAsesor(u).setVisible(true);
            case "administrador","director" -> new PanelDirector(u).setVisible(true);
            default -> {
                JOptionPane.showMessageDialog(null,
                    "Rol no reconocido: " + u.getRol(), "Error", JOptionPane.ERROR_MESSAGE);
                new LoginFrame().setVisible(true);
            }
        }
    }
}

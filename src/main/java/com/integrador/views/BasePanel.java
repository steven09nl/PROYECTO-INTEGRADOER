package com.integrador.views;

import com.integrador.models.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana base para todos los paneles de rol.
 * Sidebar fijo izquierdo + área de contenido con CardLayout.
 * Corrección de renderizado: sin colores con alpha en componentes opaque.
 */
public abstract class BasePanel extends JFrame {

    protected final Usuario usuario;
    protected final JPanel  contentArea;
    protected final CardLayout cardLayout;
    protected final Color sidebarColor;
    protected final Color accentColor;

    // Color sólido real del sidebar (sin alpha) para hover de botones
    private Color solidSidebar;

    public BasePanel(Usuario usuario, String title, Color sidebarColor, Color accentColor) {
        this.usuario      = usuario;
        this.sidebarColor = sidebarColor;
        this.accentColor  = accentColor;
        // Color sólido para uso en botones del sidebar
        this.solidSidebar = sidebarColor;

        setTitle(title + " - Sistema de Practicas");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 680));

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        cardLayout  = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIUtils.COLOR_BG);
        contentArea.setOpaque(true);

        JPanel sidebar  = buildSidebar();
        JPanel topBar   = buildTopBar();

        JScrollPane scroll = new JScrollPane(contentArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.setBackground(UIUtils.COLOR_BG);
        scroll.getViewport().setBackground(UIUtils.COLOR_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.add(topBar, BorderLayout.NORTH);
        mainArea.add(scroll,  BorderLayout.CENTER);
        mainArea.setBackground(UIUtils.COLOR_BG);
        mainArea.setOpaque(true);

        JPanel root = new JPanel(new BorderLayout());
        root.add(sidebar,  BorderLayout.WEST);
        root.add(mainArea, BorderLayout.CENTER);
        root.setBackground(UIUtils.COLOR_BG);
        setContentPane(root);
    }

    // ── Sidebar ──────────────────────────────────────────────────────────

    private JPanel buildSidebar() {
        // Panel con fondo sólido oscuro — sin gradiente para evitar problemas
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                // Gradiente sutil de arriba abajo
                GradientPaint gp = new GradientPaint(
                    0, 0, UIUtils.COLOR_SIDEBAR,
                    0, getHeight(), UIUtils.COLOR_SIDEBAR.brighter()
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Línea derecha separadora
                g2.setColor(new Color(0x1E293B));
                g2.fillRect(getWidth()-1, 0, 1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(true);
        sidebar.setBackground(UIUtils.COLOR_SIDEBAR);

        // ── Logo / cabecera ──────────────────────────────────────────
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(24, 20, 16, 20));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblDot = new JLabel("●  Sistema de Practicas");
        lblDot.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDot.setForeground(accentColor.brighter());

        JLabel lblUni = new JLabel("Universidad");
        lblUni.setFont(UIUtils.FONT_SMALL);
        lblUni.setForeground(UIUtils.SIDEBAR_TEXT_MUTED);

        header.add(lblDot);
        header.add(Box.createVerticalStrut(3));
        header.add(lblUni);
        sidebar.add(header);

        // ── Separador ────────────────────────────────────────────────
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(0x1E293B));
        sep1.setBackground(new Color(0x1E293B));
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep1);

        // ── Avatar / Usuario ─────────────────────────────────────────
        JPanel userBox = buildUserBox();
        sidebar.add(userBox);

        // ── Separador ────────────────────────────────────────────────
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(0x1E293B));
        sep2.setBackground(new Color(0x1E293B));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(sep2);
        sidebar.add(Box.createVerticalStrut(8));

        // ── Navegación ───────────────────────────────────────────────
        buildNavigation(sidebar);

        sidebar.add(Box.createVerticalGlue());

        // ── Botón cerrar sesión ──────────────────────────────────────
        sidebar.add(buildLogoutButton());

        return sidebar;
    }

    private JPanel buildUserBox() {
        JPanel box = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        box.setOpaque(false);
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 68));
        box.setBorder(new EmptyBorder(0, 10, 0, 10));

        // Circulo avatar con inicial
        JLabel avatar = new JLabel(String.valueOf(usuario.getNombre().charAt(0)).toUpperCase()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setPreferredSize(new Dimension(38, 38));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);
        avatar.setFont(new Font("SansSerif", Font.BOLD, 16));
        avatar.setForeground(Color.WHITE);
        avatar.setOpaque(false);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel lblNombre = new JLabel(usuario.getNombre());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNombre.setForeground(UIUtils.SIDEBAR_TEXT);

        JLabel lblRol = new JLabel(capitalize(usuario.getRol()));
        lblRol.setFont(UIUtils.FONT_SMALL);
        lblRol.setForeground(UIUtils.SIDEBAR_TEXT_MUTED);

        info.add(lblNombre);
        info.add(Box.createVerticalStrut(2));
        info.add(lblRol);

        box.add(avatar);
        box.add(info);
        return box;
    }

    private JPanel buildLogoutButton() {
        JButton btnLogout = new JButton("  Cerrar Sesion") {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                setOpaque(true);
                setBackground(new Color(0x1A0A0A));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setBackground(new Color(0x7F1D1D));
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setBackground(new Color(0x1A0A0A));
                        repaint();
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLogout.setForeground(new Color(0xFCA5A5));
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setHorizontalAlignment(SwingConstants.LEFT);
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnLogout.setBorder(new EmptyBorder(12, 20, 12, 20));
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x1E293B)));
        footer.add(btnLogout, BorderLayout.CENTER);
        footer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return footer;
    }

    /** Subclases implementan la navegación del sidebar */
    protected abstract void buildNavigation(JPanel sidebar);

    /** Subclases crean la barra superior */
    protected abstract JPanel buildTopBar();

    // ── Helpers para subclases ───────────────────────────────────────────

    protected void addNavSection(JPanel sidebar, String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 10));
        lbl.setForeground(UIUtils.SIDEBAR_SECTION);
        lbl.setBorder(new EmptyBorder(14, 20, 5, 20));
        lbl.setOpaque(false);
        lbl.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        sidebar.add(lbl);
    }

    protected JButton addNavItem(JPanel sidebar, String icon, String text, String cardName) {
        // Color de fondo del hover: versión más clara del sidebar
        Color hoverColor = new Color(0x1E293B);
        Color activeColor = new Color(
            Math.min(255, accentColor.getRed() / 3),
            Math.min(255, accentColor.getGreen() / 3),
            Math.min(255, accentColor.getBlue() / 3 + 50)
        );

        JButton btn = new JButton(icon + "  " + text) {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                setOpaque(true);
                setBackground(UIUtils.COLOR_SIDEBAR);
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        setBackground(hoverColor);
                        repaint();
                    }
                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        setBackground(UIUtils.COLOR_SIDEBAR);
                        repaint();
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Barra lateral izquierda en hover
                if (hovered) {
                    g2.setColor(accentColor);
                    g2.fillRect(0, 6, 3, getHeight()-12);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setForeground(UIUtils.SIDEBAR_TEXT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.addActionListener(e -> cardLayout.show(contentArea, cardName));
        sidebar.add(btn);
        return btn;
    }

    protected JPanel sectionHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setBackground(UIUtils.COLOR_BG);
        p.setOpaque(true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel t = UIUtils.labelTitle(title);
        JLabel s = UIUtils.labelSubtitle(subtitle);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);

        p.add(t);
        p.add(Box.createVerticalStrut(5));
        p.add(s);
        return p;
    }

    protected JPanel sectionPanel() {
        JPanel p = new JPanel();
        p.setBackground(UIUtils.COLOR_BG);
        p.setOpaque(true);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(28, 32, 28, 32));
        return p;
    }

    protected JPanel buildTopBarBase(String welcomeText, String roleText, Color roleColor) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setOpaque(true);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtils.COLOR_BORDER),
            new EmptyBorder(14, 32, 14, 32)));

        JLabel lbl = new JLabel(welcomeText);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(UIUtils.COLOR_TEXT);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rolePanel.setOpaque(false);
        JLabel rolLbl = new JLabel("  " + roleText + "  ");
        rolLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        rolLbl.setForeground(roleColor);
        rolLbl.setBackground(new Color(
            Math.min(255, roleColor.getRed() + 210),
            Math.min(255, roleColor.getGreen() + 210),
            Math.min(255, roleColor.getBlue() + 210)));
        rolLbl.setOpaque(true);
        rolLbl.setBorder(new EmptyBorder(4, 10, 4, 10));
        rolePanel.add(rolLbl);

        bar.add(lbl,        BorderLayout.WEST);
        bar.add(rolePanel,  BorderLayout.EAST);
        return bar;
    }

    protected JScrollPane tableCard(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(UIUtils.COLOR_BORDER, 1));
        sp.setBackground(Color.WHITE);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setOpaque(true);
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        return sp;
    }

    protected void showMsg(String msg, boolean ok) {
        JOptionPane.showMessageDialog(this, msg,
            ok ? "Exito" : "Error",
            ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return "";
        return Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase();
    }
}

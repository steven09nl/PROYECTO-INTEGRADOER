package com.integrador.views;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UIUtils {

    // ── Paleta de colores ────────────────────────────────────────────────

    public static final Color COLOR_BG          = new Color(0xF1F5F9);
    public static final Color COLOR_WHITE        = Color.WHITE;
    public static final Color COLOR_PRIMARY      = new Color(0x4F46E5);
    public static final Color COLOR_DARK         = new Color(0x1E1B4B);
    public static final Color COLOR_SIDEBAR      = new Color(0x0F172A);
    public static final Color COLOR_BORDER       = new Color(0xE2E8F0);
    public static final Color COLOR_TEXT_MUTED   = new Color(0x64748B);
    public static final Color COLOR_TEXT         = new Color(0x1E293B);
    public static final Color COLOR_SUCCESS      = new Color(0x059669);
    public static final Color COLOR_DANGER       = new Color(0xDC2626);
    public static final Color COLOR_WARNING      = new Color(0xD97706);
    public static final Color COLOR_ASESOR       = new Color(0x0891B2);
    public static final Color COLOR_DIRECTOR     = new Color(0x7C3AED);

    static final Color SIDEBAR_ITEM_HOVER  = new Color(0x1E293B);
    static final Color SIDEBAR_ITEM_ACTIVE = new Color(0x312E81);
    static final Color SIDEBAR_TEXT        = new Color(0xCBD5E1);
    static final Color SIDEBAR_TEXT_MUTED  = new Color(0x64748B);
    static final Color SIDEBAR_SECTION     = new Color(0x475569);

    // ── Tipografía ───────────────────────────────────────────────────────

    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_LABEL    = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_NORMAL   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 11);

    // ── Botones ──────────────────────────────────────────────────────────

    public static JButton btnPrimary(String text, Color bg) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                setContentAreaFilled(false);
                setOpaque(false);
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? bg.darker() : bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public static JTextField textField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(FONT_NORMAL);
        tf.setForeground(COLOR_TEXT);
        tf.setBackground(Color.WHITE);
        tf.setToolTipText(placeholder);
        tf.setBorder(new RoundedBorder(COLOR_BORDER, 6, 12, 9));
        return tf;
    }

    public static JPasswordField passwordField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_NORMAL);
        pf.setBorder(new RoundedBorder(COLOR_BORDER, 6, 12, 9));
        return pf;
    }

    public static JLabel labelTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(COLOR_TEXT);
        return lbl;
    }

    public static JLabel labelSubtitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_SUBTITLE);
        lbl.setForeground(COLOR_TEXT_MUTED);
        return lbl;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // sombra suave
                g2.setColor(new Color(15, 23, 42, 14));
                g2.fill(new RoundRectangle2D.Float(2, 3, getWidth()-4, getHeight()-2, 12, 12));
                // fondo blanco
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth()-2, getHeight()-3, 12, 12));
                // borde
                g2.setColor(COLOR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-3, getHeight()-4, 12, 12));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(20, 24, 20, 24));
        return p;
    }

    public static JTable styledTable(String[] cols, Object[][] data) {
        JTable table = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table.setFont(FONT_NORMAL);
        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(0xEDE9FE));
        table.setSelectionForeground(COLOR_TEXT);
        table.setBackground(Color.WHITE);
        table.setForeground(COLOR_TEXT);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8FAFC));
                    setForeground(COLOR_TEXT);
                }
                setBorder(new EmptyBorder(4, 14, 4, 14));
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setBackground(new Color(0xF8FAFC));
        header.setForeground(COLOR_TEXT_MUTED);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BORDER));

        DefaultTableCellRenderer hRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setText(val != null ? val.toString().toUpperCase() : "");
                setFont(new Font("SansSerif", Font.BOLD, 11));
                setForeground(COLOR_TEXT_MUTED);
                setBackground(new Color(0xF8FAFC));
                setBorder(new EmptyBorder(8, 14, 8, 14));
                return this;
            }
        };
        for (int i = 0; i < cols.length; i++)
            table.getColumnModel().getColumn(i).setHeaderRenderer(hRenderer);

        return table;
    }

    public static JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(COLOR_BORDER);
        return sep;
    }

    public static JComboBox<String> comboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_NORMAL);
        cb.setBackground(Color.WHITE);
        cb.setForeground(COLOR_TEXT);
        return cb;
    }

    public static JTextArea textArea(int rows, int cols) {
        JTextArea ta = new JTextArea(rows, cols);
        ta.setFont(FONT_NORMAL);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBackground(Color.WHITE);
        ta.setForeground(COLOR_TEXT);
        ta.setBorder(new EmptyBorder(10, 12, 10, 12));
        return ta;
    }

    // ── Bordes y helpers ─────────────────────────────────────────────────

    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius, hPad, vPad;
        public RoundedBorder(Color color, int radius, int hPad, int vPad) {
            this.color = color; this.radius = radius;
            this.hPad = hPad; this.vPad = vPad;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Float(x+0.75f, y+0.75f, w-1.5f, h-1.5f, radius, radius));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(vPad,hPad,vPad,hPad); }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(vPad,hPad,vPad,hPad); return i; }
    }

    public static JScrollPane cleanScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBackground(Color.WHITE);
        return sp;
    }
}

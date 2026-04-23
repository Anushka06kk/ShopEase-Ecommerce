package ecommerce;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * UITheme - Light Violet / Purple theme.
 * Very pale lavender backgrounds, deep purple/dark text — strain-free reading.
 * MIT Academy of Engineering  |  Prof. Vijaykumar Mantri
 */
public class UITheme {

    // ── Palette ──────────────────────────────────────────────────────────────
    public static final Color BG_PAGE          = new Color(245, 242, 255); // window / page bg
    public static final Color BG_PANEL         = new Color(255, 255, 255); // white cards
    public static final Color BG_FIELD         = new Color(250, 248, 255); // input bg
    public static final Color BG_HEADER        = new Color(100,  60, 180); // top bar (deep violet)
    public static final Color BG_FOOTER        = new Color(237, 231, 255); // footer strip

    // Buttons
    public static final Color BTN_PRIMARY      = new Color(110,  72, 200);
    public static final Color BTN_PRIMARY_HOV  = new Color( 88,  52, 170);
    public static final Color BTN_SECONDARY    = new Color(220, 210, 250);
    public static final Color BTN_SECONDARY_HOV= new Color(200, 185, 240);
    public static final Color BTN_DANGER       = new Color(200,  50,  50);
    public static final Color BTN_DANGER_HOV   = new Color(165,  25,  25);
    public static final Color BTN_SUCCESS      = new Color( 34, 150,  80);
    public static final Color BTN_SUCCESS_HOV  = new Color( 20, 120,  60);

    // Text
    public static final Color TEXT_ON_DARK  = new Color(255, 255, 255);
    public static final Color TEXT_PRIMARY  = new Color( 25,  10,  55); // very dark purple-black
    public static final Color TEXT_MUTED    = new Color(100,  80, 145);
    public static final Color TEXT_ACCENT   = new Color( 88,  50, 170);
    public static final Color TEXT_DANGER   = new Color(180,  30,  30);

    // Borders
    public static final Color BORDER_LIGHT  = new Color(210, 195, 245);
    public static final Color BORDER_FOCUS  = new Color(110,  72, 200);

    // Table
    public static final Color TABLE_EVEN    = new Color(255, 255, 255);
    public static final Color TABLE_ODD     = new Color(244, 240, 255);
    public static final Color TABLE_SEL_BG  = new Color(200, 180, 255);
    public static final Color TABLE_SEL_FG  = new Color( 25,  10,  55);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font FONT_BRAND   = new Font("Georgia",  Font.BOLD,   20);
    public static final Font FONT_TITLE   = new Font("Georgia",  Font.BOLD,   16);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD,   13);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN,  11);
    public static final Font FONT_FOOTER  = new Font("Segoe UI", Font.ITALIC, 11);

    // ── Helpers ───────────────────────────────────────────────────────────────

    public static void styleFrame(JFrame f) {
        f.getContentPane().setBackground(BG_PAGE);
        f.setBackground(BG_PAGE);
    }

    public static JTextField makeField(int cols) {
        JTextField f = new JTextField(cols);
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(BTN_PRIMARY);
        f.setFont(FONT_BODY);
        applyFieldBorder(f, false);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { applyFieldBorder(f, true);  }
            public void focusLost (java.awt.event.FocusEvent e) { applyFieldBorder(f, false); }
        });
        return f;
    }

    public static JPasswordField makePasswordField(int cols) {
        JPasswordField f = new JPasswordField(cols);
        f.setBackground(BG_FIELD);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(BTN_PRIMARY);
        f.setFont(FONT_BODY);
        applyFieldBorder(f, false);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) { applyFieldBorder(f, true);  }
            public void focusLost (java.awt.event.FocusEvent e) { applyFieldBorder(f, false); }
        });
        return f;
    }

    private static void applyFieldBorder(JComponent f, boolean focused) {
        Color border = focused ? BORDER_FOCUS : BORDER_LIGHT;
        int   thick  = focused ? 2 : 1;
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(border, thick),
            BorderFactory.createEmptyBorder(7 - thick + 1, 10, 7 - thick + 1, 10)));
    }

    public static JButton makeButton(String text, Color base, Color hover) {
        final Color bg  = (base  != null) ? base  : BTN_SECONDARY;
        final Color hov = (hover != null) ? hover : BTN_SECONDARY_HOV;
        // White text on dark buttons, dark text on light buttons
        final Color fg  = isDark(bg) ? TEXT_ON_DARK : TEXT_PRIMARY;

        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(FONT_HEADER);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hov); }
            public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(bg);  }
        });
        return btn;
    }

    public static JButton makeButton(String text) {
        return makeButton(text, BTN_SECONDARY, BTN_SECONDARY_HOV);
    }

    public static JLabel makeLabel(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static void styleTable(javax.swing.JTable table) {
        table.setBackground(TABLE_EVEN);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(34);
        table.setGridColor(BORDER_LIGHT);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(TABLE_SEL_BG);
        table.setSelectionForeground(TABLE_SEL_FG);
        table.setIntercellSpacing(new Dimension(0, 1));

        javax.swing.table.JTableHeader h = table.getTableHeader();
        h.setBackground(BG_HEADER);
        h.setForeground(Color.WHITE);
        h.setFont(FONT_HEADER);
        h.setPreferredSize(new Dimension(0, 38));
        h.setReorderingAllowed(false);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_FOCUS));
    }

    public static JScrollPane darkScrollPane(java.awt.Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.getViewport().setBackground(BG_PANEL);
        sp.setBackground(BG_PANEL);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        return sp;
    }

    /**
     * Deep violet top-bar used across every screen.
     * @param pageTitle  shown after the brand name
     * @param rightComponents  buttons/labels placed on the right
     */
    public static JPanel buildTopBar(String pageTitle, java.awt.Component... rightComponents) {
        JPanel bar = new JPanel(new BorderLayout(14, 0));
        bar.setBackground(BG_HEADER);
        bar.setBorder(BorderFactory.createEmptyBorder(13, 24, 13, 24));

        JLabel brand = makeLabel("🛒  ShopEase   |   " + pageTitle, FONT_BRAND, TEXT_ON_DARK);
        bar.add(brand, BorderLayout.WEST);

        if (rightComponents.length > 0) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            right.setBackground(BG_HEADER);
            for (java.awt.Component c : rightComponents) right.add(c);
            bar.add(right, BorderLayout.EAST);
        }
        return bar;
    }

    /**
     * Footer strip shown at the bottom of every screen.
     * Left : MIT Academy of Engineering  |  Prof. Vijaykumar Mantri
     * Right: Developed by: Swejal Jitendra Kawadse
     */
    public static JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_FOOTER);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_LIGHT),
            BorderFactory.createEmptyBorder(8, 24, 8, 24)));

        JLabel left = makeLabel(
            "MIT Academy of Engineering   |   Prof. Asmeeta Mali",
            FONT_FOOTER, TEXT_MUTED);

        JLabel right = makeLabel(
            "Developed by: Anushka Chhagan Kadnar",
            FONT_FOOTER, TEXT_ACCENT);

        footer.add(left,  BorderLayout.WEST);
        footer.add(right, BorderLayout.EAST);
        return footer;
    }

    /** Global Swing defaults — light theme. */
    public static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("OptionPane.background",        BG_PANEL);
        UIManager.put("Panel.background",             BG_PAGE);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background",            BTN_PRIMARY);
        UIManager.put("Button.foreground",            TEXT_ON_DARK);
        UIManager.put("Button.focus",                 BTN_PRIMARY);
        UIManager.put("TextField.background",         BG_FIELD);
        UIManager.put("TextField.foreground",         TEXT_PRIMARY);
    }

    private static boolean isDark(Color c) {
        return (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) < 160;
    }
}

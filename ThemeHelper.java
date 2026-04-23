package ecommerce;


import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ThemeHelper - Centralizes all UI theme constants and helper methods.
 * Light green professional theme throughout the application.
 */
public class ThemeHelper {

    // ─── Color Palette ────────────────────────────────────────────────────────
    public static final Color BG_MAIN       = new Color(0xE8, 0xF5, 0xE9); // Very light green
    public static final Color BG_PANEL      = new Color(0xC8, 0xE6, 0xC9); // Soft green panels
    public static final Color BG_HEADER     = new Color(0xA5, 0xD6, 0xA7); // Header green
    public static final Color BTN_COLOR     = new Color(0x43, 0xA0, 0x47); // Medium green button
    public static final Color BTN_HOVER     = new Color(0x2E, 0x7D, 0x32); // Dark green hover
    public static final Color TEXT_DARK     = new Color(0x1B, 0x5E, 0x20); // Dark green text
    public static final Color TEXT_HEADING  = new Color(0x1A, 0x23, 0x7E); // Deep blue-green heading
    public static final Color FOOTER_BG     = new Color(0xA5, 0xD6, 0xA7);
    public static final Color WHITE         = Color.WHITE;
    public static final Color TABLE_HEADER  = new Color(0x66, 0xBB, 0x6A);

    // ─── Fonts ────────────────────────────────────────────────────────────────
    public static final Font FONT_HEADING   = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUB       = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY      = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_LABEL     = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_SMALL     = new Font("Segoe UI", Font.ITALIC, 13);
    public static final Font FONT_BTN       = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_INSTITUTE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE     = new Font("Segoe UI", Font.BOLD, 26);

    // ─── Factory Methods ──────────────────────────────────────────────────────

    /** Creates a styled green button with hover effect. */
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(BTN_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(BTN_HOVER); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(BTN_COLOR); }
        });
        return btn;
    }

    /** Creates a styled text field. */
    public static JTextField createTextField(int cols) {
        JTextField tf = new JTextField(cols);
        tf.setFont(FONT_BODY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    /** Creates a styled password field. */
    public static JPasswordField createPasswordField(int cols) {
        JPasswordField pf = new JPasswordField(cols);
        pf.setFont(FONT_BODY);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BTN_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return pf;
    }

    /** Creates the college/guide header panel shown on login page. */
    public static JPanel createInstituteHeader() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 0, 2));
        panel.setBackground(BG_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel college = new JLabel("MIT Academy of Engineering, Alandi", SwingConstants.CENTER);
        college.setFont(FONT_INSTITUTE);
        college.setForeground(TEXT_DARK);

        JLabel guide = new JLabel("Under the Guidance of Prof. Asmeeta Mali", SwingConstants.CENTER);
        guide.setFont(FONT_SMALL);
        guide.setForeground(TEXT_DARK);

        JLabel title = new JLabel("Mini E-Commerce System", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_HEADING);

        panel.add(college);
        panel.add(guide);
        panel.add(title);
        return panel;
    }

    /** Creates the footer panel shown on all pages. */
    public static JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(FOOTER_BG);
        footer.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JLabel devLabel = new JLabel("Developed by: Anushka Kadnar", SwingConstants.CENTER);
        devLabel.setFont(FONT_SMALL);
        devLabel.setForeground(TEXT_DARK);

        footer.add(devLabel);
        return footer;
    }

    /** Creates a section heading label. */
    public static JLabel createHeading(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(FONT_HEADING);
        lbl.setForeground(TEXT_HEADING);
        return lbl;
    }

    /** Creates a standard form label. */
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    /** Applies common background to a frame. */
    public static void styleFrame(JFrame frame, int w, int h) {
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(BG_MAIN);
    }
}

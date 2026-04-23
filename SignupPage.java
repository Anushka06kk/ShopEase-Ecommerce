package ecommerce;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * SignupPage – New user registration.
 * Same left branding panel as LoginPage.
 * Inserts a row into the users table on success.
 */
public class SignupPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public SignupPage() {
        setTitle("ShopEase — Create Account");
        setSize(820, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        UITheme.styleFrame(this);
        buildUI();

        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);

        root.add(buildBrandPanel(), BorderLayout.WEST);
        root.add(buildFormPanel(), BorderLayout.CENTER);
        root.add(UITheme.buildFooter(), BorderLayout.SOUTH);

        add(root);
    }

    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(85, 45, 165),
                        0, getHeight(), new Color(120, 80, 210)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setPreferredSize(new Dimension(310, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 30, 40, 30));

        JLabel icon = new JLabel("🎓", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 56));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel institute1 = UITheme.makeLabel(
                "MIT Academy of",
                UITheme.FONT_BRAND,
                Color.WHITE
        );

        JLabel institute2 = UITheme.makeLabel(
                "Engineering",
                UITheme.FONT_BRAND,
                Color.WHITE
        );

        institute1.setAlignmentX(Component.CENTER_ALIGNMENT);
        institute2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 80));
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel guidedBy = UITheme.makeLabel(
                "Guided By",
                UITheme.FONT_SMALL,
                new Color(210, 195, 255)
        );

        JLabel profName = UITheme.makeLabel(
                "Prof. Asmeeta Mali",
                UITheme.FONT_HEADER,
                Color.WHITE
        );

        guidedBy.setAlignmentX(Component.CENTER_ALIGNMENT);
        profName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel appName = UITheme.makeLabel(
                "🛒 ShopEase",
                UITheme.FONT_TITLE,
                new Color(220, 210, 255)
        );

        JLabel tagLine = UITheme.makeLabel(
                "Mini E-Commerce System",
                UITheme.FONT_SMALL,
                new Color(190, 175, 240)
        );

        appName.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagLine.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(institute1);
        panel.add(institute2);
        panel.add(Box.createVerticalStrut(24));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(14));
        panel.add(guidedBy);
        panel.add(Box.createVerticalStrut(6));
        panel.add(profName);
        panel.add(Box.createVerticalGlue());
        panel.add(appName);
        panel.add(Box.createVerticalStrut(4));
        panel.add(tagLine);

        return panel;
    }

    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(UITheme.BG_PANEL);
        outer.setBorder(BorderFactory.createMatteBorder(
                0, 2, 0, 0, UITheme.BORDER_LIGHT
        ));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UITheme.BG_PANEL);
        card.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        JLabel title = UITheme.makeLabel(
                "Create Account",
                UITheme.FONT_TITLE,
                UITheme.TEXT_PRIMARY
        );

        JLabel sub = UITheme.makeLabel(
                "Join ShopEase today",
                UITheme.FONT_SMALL,
                UITheme.TEXT_MUTED
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        // Username
        card.add(fieldLabel("Username"));
        card.add(Box.createVerticalStrut(6));

        usernameField = UITheme.makeField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(usernameField);

        // Password
        card.add(Box.createVerticalStrut(16));
        card.add(fieldLabel("Password"));
        card.add(Box.createVerticalStrut(6));

        passwordField = UITheme.makePasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(passwordField);

        // Confirm Password
        card.add(Box.createVerticalStrut(16));
        card.add(fieldLabel("Confirm Password"));
        card.add(Box.createVerticalStrut(6));

        confirmField = UITheme.makePasswordField(20);
        confirmField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        card.add(confirmField);

        card.add(Box.createVerticalStrut(26));

        JButton signupBtn = UITheme.makeButton(
                "Create Account",
                UITheme.BTN_PRIMARY,
                UITheme.BTN_PRIMARY_HOV
        );

        signupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        signupBtn.addActionListener(e -> handleSignup());
        card.add(signupBtn);

        card.add(Box.createVerticalStrut(12));

        JButton backBtn = UITheme.makeButton(
                "Already have an account? Login",
                UITheme.BG_PANEL,
                new Color(235, 225, 255)
        );

        backBtn.setForeground(UITheme.TEXT_ACCENT);
        backBtn.setFont(UITheme.FONT_SMALL);
        backBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        backBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        card.add(backBtn);

        outer.add(card);
        return outer;
    }

    private JLabel fieldLabel(String text) {
        JLabel lbl = UITheme.makeLabel(
                text,
                UITheme.FONT_SMALL,
                UITheme.TEXT_MUTED
        );

        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void handleSignup() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "All fields are required.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username must be at least 3 characters.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password must be at least 4 characters.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String checkSql =
                "SELECT user_id FROM users WHERE username=?";

        String insertSql =
                "INSERT INTO users(username,password) VALUES(?,?)";

        try (Connection conn = DBConnection.getConnection()) {

            // Check duplicate username
            try (PreparedStatement ps =
                         conn.prepareStatement(checkSql)) {

                ps.setString(1, username);

                if (ps.executeQuery().next()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Username already taken.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            // Insert user
            try (PreparedStatement ps =
                         conn.prepareStatement(insertSql)) {

                ps.setString(1, username);
                ps.setString(2, password);
                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Account Created Successfully ✔",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            new LoginPage();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
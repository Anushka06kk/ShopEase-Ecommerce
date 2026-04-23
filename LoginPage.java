package ecommerce;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * LoginPage – Entry screen.
 * Left panel: College / professor branding.
 * Right panel: Login form.
 * Footer: Student name.
 */
public class LoginPage extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginPage() {
        setTitle("ShopEase — Login");
        setSize(820, 540);
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

    /**
     * Left Branding Panel
     */
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
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setForeground(new Color(255, 255, 255, 90));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel guidedBy = UITheme.makeLabel(
                "Guided By",
                UITheme.FONT_SMALL,
                new Color(220, 210, 255)
        );

        JLabel prof = UITheme.makeLabel(
                "Prof. Asmeeta Mali",
                UITheme.FONT_HEADER,
                Color.WHITE
        );

        guidedBy.setAlignmentX(Component.CENTER_ALIGNMENT);
        prof.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel app = UITheme.makeLabel(
                "🛒 ShopEase",
                UITheme.FONT_TITLE,
                new Color(235, 225, 255)
        );

        JLabel tag = UITheme.makeLabel(
                "Mini E-Commerce System",
                UITheme.FONT_SMALL,
                new Color(210, 200, 255)
        );

        app.setAlignmentX(Component.CENTER_ALIGNMENT);
        tag.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(20));
        panel.add(institute1);
        panel.add(institute2);
        panel.add(Box.createVerticalStrut(25));
        panel.add(sep);
        panel.add(Box.createVerticalStrut(15));
        panel.add(guidedBy);
        panel.add(Box.createVerticalStrut(6));
        panel.add(prof);
        panel.add(Box.createVerticalGlue());
        panel.add(app);
        panel.add(Box.createVerticalStrut(5));
        panel.add(tag);

        return panel;
    }

    /**
     * Right Login Form
     */
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
        card.setMaximumSize(new Dimension(380, 450));

        JLabel title = UITheme.makeLabel(
                "Welcome Back",
                UITheme.FONT_TITLE,
                UITheme.TEXT_PRIMARY
        );

        JLabel sub = UITheme.makeLabel(
                "Sign in to your account",
                UITheme.FONT_SMALL,
                UITheme.TEXT_MUTED
        );

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(28));

        // Role
        card.add(fieldLabel("Login As"));
        card.add(Box.createVerticalStrut(6));

        roleBox = new JComboBox<>(new String[]{"User", "Admin"});
        roleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        roleBox.setFont(UITheme.FONT_BODY);
        card.add(roleBox);

        // Username
        card.add(Box.createVerticalStrut(16));
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

        // Login Button
        card.add(Box.createVerticalStrut(25));

        JButton loginBtn = UITheme.makeButton(
                "Login",
                UITheme.BTN_PRIMARY,
                UITheme.BTN_PRIMARY_HOV
        );

        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.addActionListener(e -> handleLogin());
        card.add(loginBtn);

        // Signup Button
        card.add(Box.createVerticalStrut(12));

        JButton signupBtn = UITheme.makeButton(
                "Don't have an account? Sign Up",
                UITheme.BG_PANEL,
                new Color(235, 225, 255)
        );

        signupBtn.setForeground(UITheme.TEXT_ACCENT);
        signupBtn.setFont(UITheme.FONT_SMALL);
        signupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        signupBtn.addActionListener(e -> {
            dispose();
            new SignupPage();
        });

        card.add(signupBtn);

        passwordField.addActionListener(e -> handleLogin());

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

    /**
     * Login Function
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String table = role.equals("Admin") ? "admins" : "users";
        String idCol = role.equals("Admin") ? "admin_id" : "user_id";

        String sql = "SELECT * FROM " + table +
                     " WHERE username=? AND password=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt(idCol);

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome " + username + " ✔",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                dispose();

                if (role.equals("Admin")) {
                    new AdminPanel();
                } else {
                    new Dashboard(id, username);
                }

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Username or Password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database Error : " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
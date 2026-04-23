
package ecommerce;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * CartPage – User's shopping cart.
 * Light violet theme. Cart data fetched live from DB.
 */
public class CartPage extends JFrame {

    private final int    userId;
    private final String username;

    private JTable            cartTable;
    private DefaultTableModel tableModel;
    private JLabel            totalLabel;

    public CartPage(int userId, String username) {
        this.userId   = userId;
        this.username = username;

        setTitle("ShopEase — My Cart   [" + username + "]");
        setSize(860, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.styleFrame(this);
        buildUI();
        loadCart();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_PAGE);

        // ── Top bar ──────────────────────────────────────────
        JButton backBtn = UITheme.makeButton("← Back to Shop", UITheme.BTN_SECONDARY, UITheme.BTN_SECONDARY_HOV);
        backBtn.setForeground(UITheme.TEXT_ACCENT);
        backBtn.addActionListener(e -> { dispose(); new Dashboard(userId, username); });
        root.add(UITheme.buildTopBar("My Cart", backBtn), BorderLayout.NORTH);

        // ── Cart Table ───────────────────────────────────────
        String[] cols = {"Cart ID", "Product Name", "Unit Price (₹)", "Qty", "Subtotal (₹)"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(tableModel);
        UITheme.styleTable(cartTable);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(130);

        cartTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (sel) {
                    setBackground(UITheme.TABLE_SEL_BG);
                    setForeground(UITheme.TABLE_SEL_FG);
                } else {
                    setBackground(row % 2 == 0 ? UITheme.TABLE_EVEN : UITheme.TABLE_ODD);
                    setForeground(UITheme.TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        JScrollPane sp = UITheme.darkScrollPane(cartTable);
        sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(UITheme.BG_PAGE);
        centre.setBorder(BorderFactory.createEmptyBorder(14, 20, 0, 20));
        centre.add(sp, BorderLayout.CENTER);
        root.add(centre, BorderLayout.CENTER);

        // ── Bottom row ───────────────────────────────────────
        JPanel bottom = new JPanel(new BorderLayout(0, 0));
        bottom.setBackground(UITheme.BG_PAGE);
        bottom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_LIGHT),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        totalLabel = UITheme.makeLabel("Total:  ₹0", UITheme.FONT_TITLE, UITheme.TEXT_ACCENT);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnRow.setBackground(UITheme.BG_PAGE);

        JButton refreshBtn  = UITheme.makeButton("⟳  Refresh",     UITheme.BTN_SECONDARY,  UITheme.BTN_SECONDARY_HOV);
        JButton removeBtn   = UITheme.makeButton("🗑  Remove Item", UITheme.BTN_DANGER,      UITheme.BTN_DANGER_HOV);
        JButton checkoutBtn = UITheme.makeButton("✔  Checkout",    UITheme.BTN_SUCCESS,     UITheme.BTN_SUCCESS_HOV);

        refreshBtn.setForeground(UITheme.TEXT_ACCENT);
        removeBtn.setForeground(Color.WHITE);
        checkoutBtn.setForeground(Color.WHITE);

        refreshBtn.setPreferredSize(new Dimension(130, 40));
        removeBtn.setPreferredSize(new Dimension(150, 40));
        checkoutBtn.setPreferredSize(new Dimension(140, 40));

        refreshBtn.addActionListener(e  -> loadCart());
        removeBtn.addActionListener(e   -> removeItem());
        checkoutBtn.addActionListener(e -> checkout());

        btnRow.add(refreshBtn);
        btnRow.add(removeBtn);
        btnRow.add(checkoutBtn);

        bottom.add(totalLabel, BorderLayout.WEST);
        bottom.add(btnRow,     BorderLayout.EAST);

        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setBackground(UITheme.BG_PAGE);
        southWrapper.add(bottom,                BorderLayout.NORTH);
        southWrapper.add(UITheme.buildFooter(), BorderLayout.SOUTH);
        root.add(southWrapper, BorderLayout.SOUTH);

        add(root);
    }

    private void loadCart() {
        tableModel.setRowCount(0);
        int total = 0;

        String sql = "SELECT c.cart_id, p.name, p.price, c.quantity, " +
                     "(p.price * c.quantity) AS subtotal " +
                     "FROM cart c JOIN products p ON c.product_id = p.product_id " +
                     "WHERE c.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int    cartId   = rs.getInt("cart_id");
                String name     = rs.getString("name");
                int    price    = rs.getInt("price");
                int    qty      = rs.getInt("quantity");
                int    subtotal = rs.getInt("subtotal");
                total += subtotal;
                tableModel.addRow(new Object[]{cartId, name, "₹" + price, qty, "₹" + subtotal});
            }

            totalLabel.setText("Total:  ₹" + total);
            if (tableModel.getRowCount() == 0) {
                tableModel.addRow(new Object[]{"—", "Your cart is empty", "—", "—", "—"});
                totalLabel.setText("Total:  ₹0");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeItem() {
        int row = cartTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object idVal = tableModel.getValueAt(row, 0);
        if ("—".equals(idVal)) return;

        String name   = (String) tableModel.getValueAt(row, 1);
        int    cartId = (int) idVal;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Remove  \"" + name + "\"  from cart?",
                "Confirm Remove", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM cart WHERE cart_id = ?")) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
            loadCart();
            JOptionPane.showMessageDialog(this, "Item removed from cart. ✔",
                    "Removed", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkout() {
        if (tableModel.getRowCount() == 0 || "—".equals(tableModel.getValueAt(0, 0))) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Place order for  " + totalLabel.getText() + " ?\n\nStock will be updated automatically.",
                "Confirm Order", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String totalSql = "SELECT SUM(p.price * c.quantity) AS total FROM cart c " +
                          "JOIN products p ON c.product_id = p.product_id WHERE c.user_id = ?";
        String orderSql = "INSERT INTO orders (user_id, total_amount) VALUES (?, ?)";
        String stockSql = "UPDATE products p JOIN cart c ON p.product_id = c.product_id " +
                          "SET p.stock = p.stock - c.quantity WHERE c.user_id = ?";
        String clearSql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Total
            int total = 0;
            try (PreparedStatement ps = conn.prepareStatement(totalSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) total = rs.getInt("total");
            }

            // 2. Insert order
            try (PreparedStatement ps = conn.prepareStatement(orderSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, total);
                ps.executeUpdate();
            }

            // 3. Reduce stock
            try (PreparedStatement ps = conn.prepareStatement(stockSql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            // 4. Clear cart
            try (PreparedStatement ps = conn.prepareStatement(clearSql)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this,
                    "🎉  Order placed successfully!\n\nTotal Paid:  ₹" + total,
                    "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            loadCart();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Order could not be placed.\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


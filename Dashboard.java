
package ecommerce;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

/**
 * Dashboard – User screen.
 * Light violet theme. Products fetched live from DB.
 */
public class Dashboard extends JFrame {

    private final int    userId;
    private final String username;

    private JTable            productTable;
    private DefaultTableModel tableModel;
    private JTextField        searchField;

    public Dashboard(int userId, String username) {
        this.userId   = userId;
        this.username = username;

        setTitle("ShopEase — Dashboard   [" + username + "]");
        setSize(960, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        UITheme.styleFrame(this);
        buildUI();
        loadProducts(null);
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_PAGE);

        // ── Top bar ──────────────────────────────────────────
        JButton cartBtn   = UITheme.makeButton("🛍  My Cart",  UITheme.BTN_SECONDARY, UITheme.BTN_SECONDARY_HOV);
        JButton logoutBtn = UITheme.makeButton("Logout",       UITheme.BTN_DANGER,    UITheme.BTN_DANGER_HOV);
        cartBtn.setForeground(UITheme.TEXT_ACCENT);
        cartBtn.addActionListener(e -> { dispose(); new CartPage(userId, username); });
        logoutBtn.addActionListener(e -> { dispose(); new LoginPage(); });

        root.add(UITheme.buildTopBar("Welcome, " + username, cartBtn, logoutBtn), BorderLayout.NORTH);

        // ── Search bar ───────────────────────────────────────
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 10));
        searchBar.setBackground(UITheme.BG_PAGE);
        searchBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER_LIGHT));

        JLabel searchLbl = UITheme.makeLabel("Search Products:", UITheme.FONT_BODY, UITheme.TEXT_MUTED);
        searchField = UITheme.makeField(28);
        searchField.setPreferredSize(new Dimension(320, 34));

        JButton searchBtn  = UITheme.makeButton("Search",  UITheme.BTN_PRIMARY,   UITheme.BTN_PRIMARY_HOV);
        JButton refreshBtn = UITheme.makeButton("⟳ Refresh", UITheme.BTN_SECONDARY, UITheme.BTN_SECONDARY_HOV);
        searchBtn.setForeground(Color.WHITE);
        refreshBtn.setForeground(UITheme.TEXT_ACCENT);

        searchBtn.addActionListener(e  -> loadProducts(searchField.getText().trim()));
        refreshBtn.addActionListener(e -> { searchField.setText(""); loadProducts(null); });
        searchField.addActionListener(e -> loadProducts(searchField.getText().trim()));

        searchBar.add(searchLbl);
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(refreshBtn);
        root.add(searchBar, BorderLayout.AFTER_LAST_LINE); // placeholder; will fix

        // ── Centre layout: search + table ────────────────────
        JPanel centre = new JPanel(new BorderLayout(0, 0));
        centre.setBackground(UITheme.BG_PAGE);
        centre.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Product Name", "Price (₹)", "Stock", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        productTable = new JTable(tableModel);
        UITheme.styleTable(productTable);
        productTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(140);

        // Row renderer: alternate colours + red for out-of-stock
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (sel) {
                    setBackground(UITheme.TABLE_SEL_BG);
                    setForeground(UITheme.TABLE_SEL_FG);
                } else {
                    setBackground(row % 2 == 0 ? UITheme.TABLE_EVEN : UITheme.TABLE_ODD);
                    String status = String.valueOf(t.getValueAt(row, 4));
                    setForeground("Out of Stock".equals(status) ? UITheme.TEXT_DANGER : UITheme.TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });

        JScrollPane sp = UITheme.darkScrollPane(productTable);
        sp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        centre.add(sp, BorderLayout.CENTER);

        root.add(centre, BorderLayout.CENTER);

        // ── Bottom action row ────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        bottom.setBackground(UITheme.BG_PAGE);
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_LIGHT));

        JButton addCartBtn = UITheme.makeButton("➕  Add to Cart", UITheme.BTN_PRIMARY, UITheme.BTN_PRIMARY_HOV);
        addCartBtn.setForeground(Color.WHITE);
        addCartBtn.setPreferredSize(new Dimension(170, 40));
        addCartBtn.addActionListener(e -> addToCart());

        JLabel hint = UITheme.makeLabel("Select a product from the list, then click Add to Cart",
                UITheme.FONT_SMALL, UITheme.TEXT_MUTED);

        bottom.add(hint);
        bottom.add(addCartBtn);
        root.add(bottom, BorderLayout.SOUTH);

        // Footer — remove existing then add
        // (buildTopBar already added; now replace south)
        // We need a wrapper for south: bottom + footer
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.setBackground(UITheme.BG_PAGE);
        southWrapper.add(bottom,                 BorderLayout.NORTH);
        southWrapper.add(UITheme.buildFooter(),  BorderLayout.SOUTH);
        root.remove(bottom);
        root.add(southWrapper, BorderLayout.SOUTH);

        add(root);
    }

    private void loadProducts(String keyword) {
        tableModel.setRowCount(0);
        String sql = (keyword == null || keyword.isEmpty())
            ? "SELECT * FROM products ORDER BY name"
            : "SELECT * FROM products WHERE name LIKE ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword != null && !keyword.isEmpty()) ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int    pid   = rs.getInt("product_id");
                String name  = rs.getString("name");
                int    price = rs.getInt("price");
                int    stock = rs.getInt("stock");
                String status = stock > 0 ? "In Stock (" + stock + ")" : "Out of Stock";
                tableModel.addRow(new Object[]{pid, name, "₹" + price, stock, status});
            }
            if (tableModel.getRowCount() == 0)
                tableModel.addRow(new Object[]{"—", "No products found", "—", "—", "—"});

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToCart() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product first.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object idVal = tableModel.getValueAt(row, 0);
        if ("—".equals(idVal)) return;

        int productId = (int) idVal;
        int stock     = (int) tableModel.getValueAt(row, 3);

        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "This product is Out of Stock.", "Unavailable",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this,
                "Enter quantity (available: " + stock + "):", "Add to Cart",
                JOptionPane.QUESTION_MESSAGE);
        if (qtyStr == null) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (qty > stock) {
            JOptionPane.showMessageDialog(this, "Only " + stock + " units available.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String checkSql  = "SELECT cart_id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart SET quantity = ? WHERE cart_id = ?";
        String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, userId);
            check.setInt(2, productId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                int cartId = rs.getInt("cart_id");
                int newQty = rs.getInt("quantity") + qty;
                if (newQty > stock) {
                    JOptionPane.showMessageDialog(this,
                            "Adding this would exceed available stock.", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                PreparedStatement upd = conn.prepareStatement(updateSql);
                upd.setInt(1, newQty);
                upd.setInt(2, cartId);
                upd.executeUpdate();
            } else {
                PreparedStatement ins = conn.prepareStatement(insertSql);
                ins.setInt(1, userId);
                ins.setInt(2, productId);
                ins.setInt(3, qty);
                ins.executeUpdate();
            }

            String name = (String) tableModel.getValueAt(row, 1);
            JOptionPane.showMessageDialog(this,
                    qty + "×  " + name + "  added to cart! ✔",
                    "Cart Updated", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


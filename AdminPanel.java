package ecommerce;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * AdminPanel.java
 * Dark Blue Theme
 */
public class AdminPanel extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;

    public AdminPanel() {

        setTitle("Admin Panel");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initUI();
        loadProducts();
    }

    private void initUI() {

        setLayout(new BorderLayout());

        // Top Panel
        JPanel top = new JPanel();
        top.setBackground(new Color(15, 23, 42));

        JLabel title = new JLabel("ShopEase Admin Panel");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));

        top.add(title);

        add(top, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Price", "Stock"};

        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);

        add(scroll, BorderLayout.CENTER);

        // Right Form Panel
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(10, 1, 10, 10));
        right.setPreferredSize(new Dimension(250, 0));
        right.setBackground(new Color(226, 232, 240));

        nameField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton logoutBtn = new JButton("Logout");

        right.add(new JLabel("Product Name"));
        right.add(nameField);

        right.add(new JLabel("Price"));
        right.add(priceField);

        right.add(new JLabel("Stock"));
        right.add(stockField);

        right.add(addBtn);
        right.add(updateBtn);
        right.add(deleteBtn);
        right.add(clearBtn);
        right.add(logoutBtn);

        add(right, BorderLayout.EAST);

        // Events
        addBtn.addActionListener(e -> addProduct());
        updateBtn.addActionListener(e -> updateProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        clearBtn.addActionListener(e -> clearFields());

        logoutBtn.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });

        table.getSelectionModel().addListSelectionListener(e -> fillFields());
    }

    private void loadProducts() {

        model.setRowCount(0);

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM products";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                });
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void addProduct() {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "INSERT INTO products(name,price,stock) VALUES(?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt(stockField.getText()));

            ps.executeUpdate();

            ps.close();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void updateProduct() {

        int row = table.getSelectedRow();

        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "UPDATE products SET name=?,price=?,stock=? WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setInt(3, Integer.parseInt(stockField.getText()));
            ps.setInt(4, id);

            ps.executeUpdate();

            ps.close();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteProduct() {

        int row = table.getSelectedRow();

        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM products WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void fillFields() {

        int row = table.getSelectedRow();

        if (row == -1) return;

        nameField.setText(model.getValueAt(row, 1).toString());
        priceField.setText(model.getValueAt(row, 2).toString());
        stockField.setText(model.getValueAt(row, 3).toString());
    }

    private void clearFields() {

        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }
}
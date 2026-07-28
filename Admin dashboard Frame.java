package ui;

import dao.ComplaintDAO;
import model.Complaint;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel recurringLabel;

    public AdminDashboardFrame(User admin) {
        setTitle("Hostel Complaint System - Admin");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel heading = new JLabel("Hostel Complaint System - Admin");
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(new Color(37, 99, 235));
        topBar.add(heading, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.setBackground(Color.WHITE);
        rightTop.add(new JLabel("Welcome, " + admin.getName() + "  "));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        rightTop.add(logoutBtn);
        topBar.add(rightTop, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);

        // ---- Recurring issue banner ----
        recurringLabel = new JLabel();
        recurringLabel.setOpaque(true);
        recurringLabel.setBackground(new Color(254, 226, 226));
        recurringLabel.setForeground(new Color(153, 27, 27));
        recurringLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        recurringLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        recurringLabel.setVisible(false);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(recurringLabel, BorderLayout.NORTH);

        // ---- Complaints table ----
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Room", "Resident", "Category", "Description", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        JScrollPane tableScroll = new JScrollPane(table);
        centerPanel.add(tableScroll, BorderLayout.CENTER);

        // ---- Update controls ----
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.add(new JLabel("Update selected complaint status:"));
        String[] statuses = {"Pending", "In Progress", "Resolved"};
        JComboBox<String> statusBox = new JComboBox<>(statuses);
        updatePanel.add(statusBox);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBackground(new Color(37, 99, 235));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updatePanel.add(updateBtn);

        JButton refreshBtn = new JButton("Refresh");
        updatePanel.add(refreshBtn);

        centerPanel.add(updatePanel, BorderLayout.SOUTH);
        root.add(centerPanel, BorderLayout.CENTER);

        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a complaint row first.",
                        "No selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int complaintId = (int) tableModel.getValueAt(row, 0);
            String newStatus = (String) statusBox.getSelectedItem();
            boolean success = complaintDAO.updateStatus(complaintId, newStatus);
            if (success) {
                loadComplaints();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update status.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        refreshBtn.addActionListener(e -> loadComplaints());

        loadComplaints();
    }

    private void loadComplaints() {
        List<Complaint> complaints = complaintDAO.getAllComplaints();
        tableModel.setRowCount(0);
        for (Complaint c : complaints) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getRoomNo(), c.getResidentName(),
                    c.getCategory(), c.getDescription(), c.getStatus()
            });
        }

        // ---- Recurring issue detection ----
        List<String> recurring = complaintDAO.getRecurringIssues();
        if (!recurring.isEmpty()) {
            StringBuilder sb = new StringBuilder("<html>&#9888; Recurring Issues Detected: ");
            sb.append(String.join(" &nbsp;|&nbsp; ", recurring));
            sb.append("</html>");
            recurringLabel.setText(sb.toString());
            recurringLabel.setVisible(true);
        } else {
            recurringLabel.setVisible(false);
        }
    }
}

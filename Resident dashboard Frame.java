package ui;

import dao.ComplaintDAO;
import model.Complaint;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ResidentDashboardFrame extends JFrame {

    private final User currentUser;
    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private DefaultTableModel tableModel;

    public ResidentDashboardFrame(User user) {
        this.currentUser = user;

        setTitle("Hostel Complaint System - Resident");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);

        // ---- Top bar ----
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel heading = new JLabel("Hostel Complaint System - Resident");
        heading.setFont(new Font("SansSerif", Font.BOLD, 16));
        heading.setForeground(new Color(37, 99, 235));
        topBar.add(heading, BorderLayout.WEST);

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.setBackground(Color.WHITE);
        rightTop.add(new JLabel("Welcome, " + currentUser.getName() + "  "));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        rightTop.add(logoutBtn);
        topBar.add(rightTop, BorderLayout.EAST);

        root.add(topBar, BorderLayout.NORTH);

        // ---- Center: raise complaint form + complaint history ----
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Raise Complaint", buildRaiseComplaintPanel());
        tabs.addTab("My Complaints", buildHistoryPanel());

        root.add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildRaiseComplaintPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.WHITE);

        JLabel formTitle = new JLabel("Raise a New Complaint");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        formTitle.setBounds(30, 20, 400, 30);
        panel.add(formTitle);

        JLabel catLabel = new JLabel("Category:");
        catLabel.setBounds(30, 70, 150, 25);
        panel.add(catLabel);

        String[] categories = {"Electrical", "Plumbing", "Internet", "Furniture", "Other"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(30, 95, 300, 30);
        panel.add(categoryBox);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(30, 135, 150, 25);
        panel.add(descLabel);

        JTextArea descArea = new JTextArea();
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(30, 160, 500, 120);
        panel.add(descScroll);

        JButton submitBtn = new JButton("Submit Complaint");
        submitBtn.setBackground(new Color(37, 99, 235));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setBounds(30, 300, 200, 35);
        panel.add(submitBtn);

        submitBtn.addActionListener(e -> {
            String description = descArea.getText().trim();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please describe the issue.",
                        "Missing details", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Complaint c = new Complaint();
            c.setUserId(currentUser.getId());
            c.setRoomNo(currentUser.getRoomNo());
            c.setCategory((String) categoryBox.getSelectedItem());
            c.setDescription(description);

            boolean success = complaintDAO.addComplaint(c);
            if (success) {
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                descArea.setText("");
                refreshHistory();
            } else {
                JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JScrollPane historyScroll;

    private JPanel buildHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        tableModel = new DefaultTableModel(
                new Object[]{"Category", "Description", "Status", "Raised On"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(28);
        historyScroll = new JScrollPane(table);
        panel.add(historyScroll, BorderLayout.CENTER);

        loadHistory();
        return panel;
    }

    private void loadHistory() {
        List<Complaint> complaints = complaintDAO.getComplaintsByUser(currentUser.getId());
        tableModel.setRowCount(0);
        for (Complaint c : complaints) {
            tableModel.addRow(new Object[]{
                    c.getCategory(), c.getDescription(), c.getStatus(),
                    c.getCreatedAt() != null ? c.getCreatedAt().toString() : ""
            });
        }
    }

    private void refreshHistory() {
        if (tableModel != null) {
            loadHistory();
        }
    }
}

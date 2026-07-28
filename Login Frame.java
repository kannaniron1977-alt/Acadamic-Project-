package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Hostel Complaint System - Login");
        setSize(420, 340);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        setContentPane(panel);

        JLabel title = new JLabel("Hostel Complaint System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(37, 99, 235));
        title.setBounds(20, 20, 380, 30);
        panel.add(title);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 80, 100, 25);
        panel.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(40, 105, 320, 30);
        panel.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 145, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(40, 170, 320, 30);
        panel.add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(40, 215, 320, 35);
        loginBtn.setBackground(new Color(37, 99, 235));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        panel.add(loginBtn);

        JButton registerBtn = new JButton("New resident? Create an account");
        registerBtn.setBounds(40, 258, 320, 25);
        registerBtn.setBorderPainted(false);
        registerBtn.setContentAreaFilled(false);
        registerBtn.setForeground(new Color(37, 99, 235));
        panel.add(registerBtn);

        JLabel demo = new JLabel("<html><center>Demo admin: admin@hostel.com / admin123<br>"
                + "Demo resident: student@hostel.com / student123</center></html>", SwingConstants.CENTER);
        demo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        demo.setForeground(Color.GRAY);
        demo.setBounds(20, 285, 380, 40);
        panel.add(demo);

        UserDAO userDAO = new UserDAO();

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both email and password.",
                        "Missing details", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = userDAO.login(email, password);
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Invalid email or password.",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            if ("admin".equalsIgnoreCase(user.getRole())) {
                new AdminDashboardFrame(user).setVisible(true);
            } else {
                new ResidentDashboardFrame(user).setVisible(true);
            }
        });

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
    }
}

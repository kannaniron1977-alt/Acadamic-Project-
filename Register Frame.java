package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Hostel Complaint System - Register");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        setContentPane(panel);

        JLabel title = new JLabel("Create an Account", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(37, 99, 235));
        title.setBounds(20, 15, 380, 30);
        panel.add(title);

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setBounds(40, 60, 150, 25);
        panel.add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBounds(40, 85, 320, 30);
        panel.add(nameField);

        JLabel roomLabel = new JLabel("Room No:");
        roomLabel.setBounds(40, 125, 150, 25);
        panel.add(roomLabel);
        JTextField roomField = new JTextField();
        roomField.setBounds(40, 150, 320, 30);
        panel.add(roomField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(40, 190, 150, 25);
        panel.add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(40, 215, 320, 30);
        panel.add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 255, 150, 25);
        panel.add(passLabel);
        JPasswordField passField = new JPasswordField();
        passField.setBounds(40, 280, 320, 30);
        panel.add(passField);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(40, 325, 320, 35);
        registerBtn.setBackground(new Color(37, 99, 235));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFocusPainted(false);
        panel.add(registerBtn);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setBounds(40, 368, 320, 25);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setForeground(new Color(37, 99, 235));
        panel.add(backBtn);

        UserDAO userDAO = new UserDAO();

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String room = roomField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());

            if (name.isEmpty() || room.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                        "Missing details", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setName(name);
            user.setRoomNo(room);
            user.setEmail(email);
            user.setPassword(password);

            boolean success = userDAO.register(user);
            if (success) {
                JOptionPane.showMessageDialog(this, "Account created! Please login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Email may already be in use.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}

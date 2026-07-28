-- =========================================================
-- Smart Hostel Complaint & Maintenance System - DB Schema
-- =========================================================

CREATE DATABASE IF NOT EXISTS hostel_complaint_system;
USE hostel_complaint_system;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    room_no VARCHAR(20) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'resident'   -- 'resident' or 'admin'
);

CREATE TABLE IF NOT EXISTS complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    room_no VARCHAR(20) NOT NULL,
    category VARCHAR(50) NOT NULL,        -- Electrical, Plumbing, Internet, Furniture, Other
    description TEXT,
    status VARCHAR(20) DEFAULT 'Pending', -- Pending, In Progress, Resolved
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Sample admin login (password is plain text here for demo simplicity — hash it for real use)
INSERT INTO users (name, room_no, email, password, role)
VALUES ('Warden Admin', 'ADMIN', 'admin@hostel.com', 'admin123', 'admin');

-- Sample resident
INSERT INTO users (name, room_no, email, password, role)
VALUES ('Test Student', 'A101', 'student@hostel.com', 'student123', 'resident');

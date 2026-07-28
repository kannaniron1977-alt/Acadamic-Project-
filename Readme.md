# Hostel Complaint System - Desktop App (Java Swing)

This is a **desktop application** version (a real window, not a browser page) of the
Hostel Complaint System. It reuses the same database logic (DAO/model classes) as the
web version, but the UI is built with Java Swing.

## Requirements
- JDK 11 or higher
- Maven
- MySQL Server (same database as before: `hostel_complaint_system`)

## First-time setup
1. If you haven't already, run `database/schema.sql` in MySQL Workbench / phpMyAdmin
   to create the `hostel_complaint_system` database and its tables.
2. Open `src/main/java/util/DBConnection.java` and set your MySQL password:
   ```java
   private static final String PASSWORD = "your_mysql_password_here";
   ```

## Run it (development mode - fastest way)
From a terminal in this project folder:
```
mvn exec:java
```
A window titled "Hostel Complaint System - Login" will pop up directly on your screen.

## Build a double-clickable app (for submission / demo without a terminal)
```
mvn clean package
```
This creates `target/HostelComplaintSystem.jar` — a single file with everything
bundled inside (including the MySQL driver). To run it:
- Double-click the jar (if your OS is set to open .jar files with Java), OR
- Run from terminal: `java -jar target/HostelComplaintSystem.jar`

## Demo logins
- Admin: admin@hostel.com / admin123
- Resident: student@hostel.com / student123

## Features
- Login / Register (residents)
- Raise Complaint + view your own complaint history (resident view)
- All Complaints table with status update dropdown (admin view)
- Recurring Issue alert - automatically flags any room+category combo with
  3 or more complaints logged, shown as a banner on the admin dashboard

package dao;

import model.Complaint;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // Add a new complaint
    public boolean addComplaint(Complaint c) {
        String sql = "INSERT INTO complaints (user_id, room_no, category, description, status) VALUES (?, ?, ?, ?, 'Pending')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getUserId());
            ps.setString(2, c.getRoomNo());
            ps.setString(3, c.getCategory());
            ps.setString(4, c.getDescription());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all complaints raised by one resident
    public List<Complaint> getComplaintsByUser(int userId) {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get all complaints (admin view) - Pending ones first, oldest first
    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.*, u.name AS resident_name FROM complaints c " +
                     "JOIN users u ON c.user_id = u.id " +
                     "ORDER BY (c.status = 'Resolved'), (c.status = 'In Progress'), c.created_at ASC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Complaint c = mapRow(rs);
                c.setResidentName(rs.getString("resident_name"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Admin updates status (Pending -> In Progress -> Resolved)
    public boolean updateStatus(int complaintId, String newStatus) {
        String sql;
        if (newStatus.equalsIgnoreCase("Resolved")) {
            sql = "UPDATE complaints SET status = ?, resolved_at = CURRENT_TIMESTAMP WHERE id = ?";
        } else {
            sql = "UPDATE complaints SET status = ? WHERE id = ?";
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, complaintId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ---------- UNIQUE FEATURE 1: Recurring Issue Detection ----------
    // Flags room+category combos that have 3 or more complaints logged
    public List<String> getRecurringIssues() {
        List<String> results = new ArrayList<>();
        String sql = "SELECT room_no, category, COUNT(*) AS complaint_count " +
                     "FROM complaints " +
                     "GROUP BY room_no, category " +
                     "HAVING COUNT(*) >= 3 " +
                     "ORDER BY complaint_count DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String msg = "Room " + rs.getString("room_no") + " - " + rs.getString("category")
                        + " (" + rs.getInt("complaint_count") + " complaints logged - needs a permanent fix)";
                results.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // ---------- UNIQUE FEATURE 2: Average Resolution Time per Category ----------
    public List<String> getAvgResolutionTime() {
        List<String> results = new ArrayList<>();
        String sql = "SELECT category, ROUND(AVG(TIMESTAMPDIFF(HOUR, created_at, resolved_at)) / 24, 1) AS avg_days " +
                     "FROM complaints " +
                     "WHERE status = 'Resolved' AND resolved_at IS NOT NULL " +
                     "GROUP BY category";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String msg = rs.getString("category") + ": avg " + rs.getDouble("avg_days") + " days to resolve";
                results.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    private Complaint mapRow(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setUserId(rs.getInt("user_id"));
        c.setRoomNo(rs.getString("room_no"));
        c.setCategory(rs.getString("category"));
        c.setDescription(rs.getString("description"));
        c.setStatus(rs.getString("status"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setResolvedAt(rs.getTimestamp("resolved_at"));
        return c;
    }
}

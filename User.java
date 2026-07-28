package model;

public class User {
    private int id;
    private String name;
    private String roomNo;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(int id, String name, String roomNo, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.roomNo = roomNo;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoomNo() { return roomNo; }
    public void setRoomNo(String roomNo) { this.roomNo = roomNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

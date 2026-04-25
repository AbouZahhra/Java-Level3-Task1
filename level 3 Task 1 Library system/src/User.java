import java.sql.*;

public class User {

    public void register(String username, String role) throws Exception {

        String checkSql = "SELECT * FROM users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {

            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                System.out.println("Username already exists!");
                return;
            }
        }

        String sql = "INSERT INTO users (username, role) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, role);

            ps.executeUpdate();
            System.out.println("User registered successfully!");
        }
    }

    // LOGIN
    public String login(String username) throws Exception {

        String sql = "SELECT * FROM users WHERE username=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String role = rs.getString("role");

                System.out.println("Login successful!");
                System.out.println("Welcome " + username);
                System.out.println("Role: " + role);

                return role;
            }

            System.out.println("User not found!");
            return null;
        }
    }
    public int getUserId(String username) throws Exception {

    String sql = "SELECT id FROM users WHERE username=?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }
    }

    return -1;
}
}
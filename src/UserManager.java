import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class UserManager {
    private final Connection conn;
    public UserManager(Connection conn) throws SQLException {
        this.conn = conn;
        initializeUsersTable();
        createDefaultUser();
    }
    private void initializeUsersTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                salt TEXT NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    private void createDefaultUser() throws SQLException {
        // Check if default user exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setString(1, "test");
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                // Create default demo user: test / 12345
                createUser("test", "12345");
            }
        }
    }
    public final boolean createUser(String username, String password) throws SQLException {
        // Generate random salt
        byte[] salt = generateSalt();
        String saltStr = Base64.getEncoder().encodeToString(salt);
        
        // Hash password with salt
        String passwordHash = hashPassword(password, salt);
        
        String sql = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, saltStr);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                return false; // Username already exists
            }
            throw e;
        }
    }
    public int authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT id, password_hash, salt FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int userId = rs.getInt("id");
                String storedHash = rs.getString("password_hash");
                String saltStr = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(saltStr);
                
                // Hash provided password with stored salt
                String providedHash = hashPassword(password, salt);
                
                if (storedHash.equals(providedHash)) {
                    return userId;
                }
            }
        }
        return -1; // Failed Auth
    }
    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }
    public String getUsername(int userId) throws SQLException {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return null;
    }
    
    /**
     * Gets the salt for a specific user (needed for encryption key derivation).
     * 
     * @param username The username
     * @return The user's salt bytes, or null if user not found
     * @throws SQLException If database error occurs
     */
    public byte[] getUserSalt(String username) throws SQLException {
        String sql = "SELECT salt FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String saltStr = rs.getString("salt");
                return Base64.getDecoder().decode(saltStr);
            }
        }
        return null;
    }
    
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    
    private String hashPassword(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}

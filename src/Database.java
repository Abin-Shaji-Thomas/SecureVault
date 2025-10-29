import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:securevault.db";
    private Connection connection;
    //checks JDBC driver and connects to the database
    public Database() throws SQLException {
        try {
            // Explicitly load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found. Make sure sqlite-jdbc jar is in classpath.", e);
        }
        connect();
        createTableIfNotExists();
    }
    private void connect() throws SQLException {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established: " + DB_URL);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }
    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS credentials (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "title TEXT NOT NULL," +
                     "username TEXT NOT NULL," +
                     "password TEXT NOT NULL," +
                     "FOREIGN KEY (user_id) REFERENCES users(id)" +
                     ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }
    // Check if credential with same title and username already exists for this user
    public boolean credentialExists(int userId, String title, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM credentials WHERE user_id = ? AND LOWER(title) = LOWER(?) AND LOWER(username) = LOWER(?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    
    // Check if credential exists excluding a specific ID (for updates)
    public boolean credentialExistsExcludingId(int userId, String title, String username, int excludeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM credentials WHERE user_id = ? AND LOWER(title) = LOWER(?) AND LOWER(username) = LOWER(?) AND id != ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, username);
            pstmt.setInt(4, excludeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    //for checking duplicatess
    public void insertCredential(int userId, String title, String username, String password) throws SQLException {
        
        if (credentialExists(userId, title, username)) {
            throw new SQLException("A credential with this title and username already exists!");
        }
        
        String sql = "INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.executeUpdate();
        }
    }

    public void updateCredential(int id, String title, String username, String password) throws SQLException {
        // Check for duplicates before updating (excluding the current credential)
        String getUserIdSql = "SELECT user_id FROM credentials WHERE id = ?";
        int userId = -1;
        try (PreparedStatement pstmt = connection.prepareStatement(getUserIdSql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        }
        
        if (userId != -1 && credentialExistsExcludingId(userId, title, username, id)) {
            throw new SQLException("A credential with this title and username already exists!");
        }
        
        String sql = "UPDATE credentials SET title = ?, username = ?, password = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public void deleteCredential(int id) throws SQLException {
        String sql = "DELETE FROM credentials WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Credential> getAllCredentials(int userId) throws SQLException {
        List<Credential> credentials = new ArrayList<>();
        String sql = "SELECT id, title, username, password FROM credentials WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String password = rs.getString("password");
                credentials.add(new Credential(id, title, username, password));
            }
        }
        return credentials;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
    public static class Credential {
        public final int id;
        public final String title;
        public final String username;
        public final String password;

        public Credential(int id, String title, String username, String password) {
            this.id = id;
            this.title = title;
            this.username = username;
            this.password = password;
        }
    }
}

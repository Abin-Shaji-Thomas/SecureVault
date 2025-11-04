import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.SecretKey;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:securevault.db";
    private Connection connection;
    private SecretKey encryptionKey; // Encryption key for this session
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
        
        // Upgrade database with new columns and tables
        try {
            DatabaseUpgrade.upgradeDatabase(connection);
        } catch (SQLException e) {
            System.err.println("Warning: Database upgrade failed: " + e.getMessage());
            // Don't throw - allow app to continue with existing schema
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
        insertCredential(userId, title, username, password, "", false, "Other", "", "");
    }
    
    public void insertCredential(int userId, String title, String username, String password, String notes, boolean isFavorite) throws SQLException {
        insertCredential(userId, title, username, password, notes, isFavorite, "Other", "", "");
    }
    
    public void insertCredential(int userId, String title, String username, String password, String notes, 
                                boolean isFavorite, String category, String websiteUrl, String expiryDate) throws SQLException {
        
        if (credentialExists(userId, title, username)) {
            throw new SQLException("A credential with this title and username already exists!");
        }
        
        // Encrypt password before storing
        String encryptedPassword = password;
        if (encryptionKey != null) {
            try {
                encryptedPassword = PasswordEncryption.encrypt(password, encryptionKey);
            } catch (Exception e) {
                throw new SQLException("Failed to encrypt password: " + e.getMessage(), e);
            }
        }
        
        String sql = "INSERT INTO credentials (user_id, title, username, password, notes, is_favorite, category, website_url, expiry_date, created_date, modified_date, last_password_change) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now'), datetime('now'), datetime('now'))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            pstmt.setString(3, username);
            pstmt.setString(4, encryptedPassword); // Store encrypted password
            pstmt.setString(5, notes);
            pstmt.setInt(6, isFavorite ? 1 : 0);
            pstmt.setString(7, category != null ? category : "Other");
            pstmt.setString(8, websiteUrl);
            pstmt.setString(9, expiryDate);
            pstmt.executeUpdate();
        }
    }

    public void updateCredential(int id, String title, String username, String password) throws SQLException {
        updateCredential(id, title, username, password, "", false, "Other", "", "");
    }
    
    public void updateCredential(int id, String title, String username, String password, String notes, boolean isFavorite) throws SQLException {
        updateCredential(id, title, username, password, notes, isFavorite, "Other", "", "");
    }
    
    public void updateCredential(int id, String title, String username, String password, String notes, 
                                boolean isFavorite, String category, String websiteUrl, String expiryDate) throws SQLException {
        // Check for duplicates before updating (excluding the current credential)
        String getUserIdSql = "SELECT user_id, password FROM credentials WHERE id = ?";
        int userId = -1;
        String oldEncryptedPassword = null;
        try (PreparedStatement pstmt = connection.prepareStatement(getUserIdSql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
                oldEncryptedPassword = rs.getString("password");
            }
        }
        
        if (userId != -1 && credentialExistsExcludingId(userId, title, username, id)) {
            throw new SQLException("A credential with this title and username already exists!");
        }
        
        // Encrypt password before storing
        String encryptedPassword = password;
        if (encryptionKey != null) {
            try {
                encryptedPassword = PasswordEncryption.encrypt(password, encryptionKey);
            } catch (Exception e) {
                throw new SQLException("Failed to encrypt password: " + e.getMessage(), e);
            }
        }
        
        // Check if password changed to update last_password_change
        boolean passwordChanged = !encryptedPassword.equals(oldEncryptedPassword);
        
        String sql = "UPDATE credentials SET title = ?, username = ?, password = ?, notes = ?, is_favorite = ?, category = ?, website_url = ?, expiry_date = ?, modified_date = datetime('now')" +
                    (passwordChanged ? ", last_password_change = datetime('now')" : "") + " WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, username);
            pstmt.setString(3, encryptedPassword); // Store encrypted password
            pstmt.setString(4, notes);
            pstmt.setInt(5, isFavorite ? 1 : 0);
            pstmt.setString(6, category != null ? category : "Other");
            pstmt.setString(7, websiteUrl);
            pstmt.setString(8, expiryDate);
            pstmt.setInt(9, id);
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
        String sql = "SELECT id, title, username, password, is_favorite, notes, created_date, modified_date, " +
                     "COALESCE(category, 'Other') as category, " +
                     "COALESCE(website_url, '') as website_url, " +
                     "COALESCE(expiry_date, '') as expiry_date, " +
                     "COALESCE(last_password_change, created_date, datetime('now')) as last_password_change " +
                     "FROM credentials WHERE user_id = ? ORDER BY is_favorite DESC, modified_date DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String username = rs.getString("username");
                String encryptedPassword = rs.getString("password");
                boolean isFavorite = rs.getInt("is_favorite") == 1;
                String notes = rs.getString("notes");
                String createdDate = rs.getString("created_date");
                String modifiedDate = rs.getString("modified_date");
                String category = rs.getString("category");
                String websiteUrl = rs.getString("website_url");
                String expiryDate = rs.getString("expiry_date");
                String lastPasswordChange = rs.getString("last_password_change");
                
                // Decrypt password if encryption key is available
                String password = encryptedPassword;
                if (encryptionKey != null && encryptedPassword != null && !encryptedPassword.isEmpty()) {
                    try {
                        password = PasswordEncryption.decrypt(encryptedPassword, encryptionKey);
                    } catch (Exception e) {
                        // If decryption fails, it might be a plain text password (migration case)
                        // Or wrong encryption key - keep encrypted value to avoid data loss
                        System.err.println("Warning: Failed to decrypt password for credential ID " + id + ": " + e.getMessage());
                        password = encryptedPassword;
                    }
                }
                
                credentials.add(new Credential(id, title, username, password, isFavorite, notes, createdDate, modifiedDate,
                                              category, websiteUrl, expiryDate, lastPasswordChange));
            }
        }
        return credentials;
    }
    
    public void toggleFavorite(int id) throws SQLException {
        String sql = "UPDATE credentials SET is_favorite = CASE WHEN is_favorite = 1 THEN 0 ELSE 1 END WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Sets the encryption key for this database session.
     * This key is used to encrypt/decrypt all passwords.
     * 
     * @param key The encryption key derived from user's master password
     */
    public void setEncryptionKey(SecretKey key) {
        this.encryptionKey = key;
    }
    
    /**
     * Gets the current encryption key.
     * 
     * @return The encryption key, or null if not set
     */
    public SecretKey getEncryptionKey() {
        return encryptionKey;
    }
    
    /**
     * Clears the encryption key from memory for security.
     * Should be called on logout or app close.
     */
    public void clearEncryptionKey() {
        if (encryptionKey != null) {
            PasswordEncryption.clearKey(encryptionKey);
            encryptionKey = null;
        }
    }

    /**
     * Initializes demo data for the default test user.
     * This creates sample credentials across different categories for demonstration purposes.
     * Should only be called once on first run or after database reset.
     * 
     * @param userId The user ID to create demo data for
     * @throws SQLException If database error occurs
     */
    public void initializeDemoData(int userId) throws SQLException {
        // Check if user already has credentials
        String checkSql = "SELECT COUNT(*) FROM credentials WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return; // User already has data, don't initialize
            }
        }
        
        // Calculate expiry date (90 days from now)
        String expiryDate = java.time.LocalDate.now().plusDays(90).toString();
        
        // Demo credentials with various categories
        String[][] demoData = {
            {"GitHub Account", "demo_user", "SecurePass123!", "Social Media", "https://github.com", "Personal GitHub account for open source projects", "true"},
            {"Gmail Account", "demo.user@gmail.com", "MyEmail2024#", "Email", "https://mail.google.com", "Primary email account", "true"},
            {"Netflix Subscription", "demo_user@email.com", "Netflix@Home99", "Entertainment", "https://netflix.com", "Family streaming account", "false"},
            {"Bank Portal", "demo_user_bank", "BankSecure!987", "Banking", "https://bank.example.com", "Online banking access - Demo only", "false"},
            {"Amazon Shopping", "demo_shopper", "AmazonPrime#456", "Shopping", "https://amazon.com", "Prime member account", "false"},
            {"Work VPN", "demo.user", "WorkVPN@2024", "Work", "https://vpn.company.example", "Corporate VPN access", "false"},
            {"LinkedIn Profile", "demo_professional", "LinkedIn!Prof23", "Social Media", "https://linkedin.com", "Professional networking", "false"},
            {"Spotify Premium", "demo_music_lover", "Music4Life@99", "Entertainment", "https://spotify.com", "Premium music streaming", "true"}
        };
        
        for (String[] demo : demoData) {
            try {
                insertCredential(userId, demo[0], demo[1], demo[2], demo[5], 
                               Boolean.parseBoolean(demo[6]), demo[3], demo[4], expiryDate);
            } catch (SQLException e) {
                System.err.println("Warning: Failed to insert demo credential '" + demo[0] + "': " + e.getMessage());
            }
        }
        
        System.out.println("Demo data initialized successfully for user ID: " + userId);
    }
    
    public void close() {
        try {
            // Clear encryption key before closing
            clearEncryptionKey();
            
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
        public final boolean isFavorite;
        public final String notes;
        public final String createdDate;
        public final String modifiedDate;
        public final String category;
        public final String websiteUrl;
        public final String expiryDate;
        public final String lastPasswordChange;

        public Credential(int id, String title, String username, String password, 
                         boolean isFavorite, String notes, String createdDate, String modifiedDate,
                         String category, String websiteUrl, String expiryDate, String lastPasswordChange) {
            this.id = id;
            this.title = title;
            this.username = username;
            this.password = password;
            this.isFavorite = isFavorite;
            this.notes = notes;
            this.createdDate = createdDate;
            this.modifiedDate = modifiedDate;
            this.category = category != null ? category : "Other";
            this.websiteUrl = websiteUrl != null ? websiteUrl : "";
            this.expiryDate = expiryDate != null ? expiryDate : "";
            this.lastPasswordChange = lastPasswordChange != null ? lastPasswordChange : createdDate;
        }
        
        // Legacy constructor for backward compatibility
        public Credential(int id, String title, String username, String password, 
                         boolean isFavorite, String notes, String createdDate, String modifiedDate) {
            this(id, title, username, password, isFavorite, notes, createdDate, modifiedDate,
                 "Other", "", "", createdDate);
        }
        
        // Legacy constructor for backward compatibility
        public Credential(int id, String title, String username, String password) {
            this(id, title, username, password, false, "", "", "", "Other", "", "", "");
        }
    }
}

import java.sql.*;

/**
 * Database upgrade utility to add new columns for enhanced features
 */
public class DatabaseUpgrade {
    
    public static void upgradeDatabase(Connection conn) throws SQLException {
        // Add new columns to credentials table if they don't exist
        addColumnIfNotExists(conn, "credentials", "category", "TEXT");
        addColumnIfNotExists(conn, "credentials", "website_url", "TEXT");
        addColumnIfNotExists(conn, "credentials", "expiry_date", "TEXT");
        addColumnIfNotExists(conn, "credentials", "last_password_change", "TEXT");
        
        // Set default values for existing NULL entries
        setDefaultValues(conn);
        
        // Create categories table for custom categories
        createCategoriesTable(conn);
        
        // Create attachments table
        createAttachmentsTable(conn);
        
        System.out.println("✅ Database upgraded successfully!");
    }
    
    private static void setDefaultValues(Connection conn) throws SQLException {
        // Set default category
        String sql1 = "UPDATE credentials SET category = 'Other' WHERE category IS NULL";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql1);
        }
        
        // Set default last_password_change to created_date or current time
        String sql2 = "UPDATE credentials SET last_password_change = COALESCE(created_date, datetime('now')) WHERE last_password_change IS NULL";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql2);
        }
    }
    
    private static void addColumnIfNotExists(Connection conn, String tableName, String columnName, String columnDef) throws SQLException {
        // Check if column exists
        DatabaseMetaData metadata = conn.getMetaData();
        ResultSet rs = metadata.getColumns(null, null, tableName, columnName);
        
        if (!rs.next()) {
            // Column doesn't exist, add it
            String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", tableName, columnName, columnDef);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("✅ Added column: " + tableName + "." + columnName);
            }
        } else {
            System.out.println("ℹ️  Column already exists: " + tableName + "." + columnName);
        }
        rs.close();
    }
    
    private static void createCategoriesTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS custom_categories (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "user_id INTEGER NOT NULL," +
                     "category_name TEXT NOT NULL," +
                     "color TEXT," +
                     "FOREIGN KEY (user_id) REFERENCES users(id)," +
                     "UNIQUE(user_id, category_name)" +
                     ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Created custom_categories table");
        }
    }
    
    private static void createAttachmentsTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS attachments (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "credential_id INTEGER NOT NULL," +
                     "filename TEXT NOT NULL," +
                     "file_data BLOB NOT NULL," +
                     "file_size INTEGER NOT NULL," +
                     "encrypted BOOLEAN DEFAULT 1," +
                     "upload_date TEXT DEFAULT (datetime('now'))," +
                     "FOREIGN KEY (credential_id) REFERENCES credentials(id) ON DELETE CASCADE" +
                     ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Created attachments table");
        }
    }
}

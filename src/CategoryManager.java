import java.sql.*;
import java.util.*;

/**
 * Manages credential categories including default and custom categories
 */
public class CategoryManager {
    
    // Default categories available to all users
    public static final String[] DEFAULT_CATEGORIES = {
        "Social Media",
        "Banking",
        "Email",
        "Work",
        "Shopping",
        "Entertainment",
        "Other"
    };
    
    // Category colors for visual identification
    private static final Map<String, String> CATEGORY_COLORS = new HashMap<>();
    static {
        CATEGORY_COLORS.put("Social Media", "#3b5998");    // Facebook blue
        CATEGORY_COLORS.put("Banking", "#28a745");          // Green
        CATEGORY_COLORS.put("Email", "#dc3545");            // Red
        CATEGORY_COLORS.put("Work", "#ffc107");             // Amber
        CATEGORY_COLORS.put("Shopping", "#e91e63");         // Pink
        CATEGORY_COLORS.put("Entertainment", "#9c27b0");    // Purple
        CATEGORY_COLORS.put("Other", "#6c757d");            // Gray
    }
    
    private Connection connection;
    
    public CategoryManager(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Get all categories for a user (default + custom)
     */
    public List<String> getAllCategories(int userId) throws SQLException {
        List<String> categories = new ArrayList<>();
        
        // Add default categories
        categories.addAll(Arrays.asList(DEFAULT_CATEGORIES));
        
        // Add custom categories
        String sql = "SELECT category_name FROM custom_categories WHERE user_id = ? ORDER BY category_name";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String customCategory = rs.getString("category_name");
                if (!categories.contains(customCategory)) {
                    categories.add(customCategory);
                }
            }
        }
        
        return categories;
    }
    
    /**
     * Add a custom category for a user
     */
    public void addCustomCategory(int userId, String categoryName, String color) throws SQLException {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new SQLException("Category name cannot be empty");
        }
        
        // Check if it's a default category
        for (String defaultCat : DEFAULT_CATEGORIES) {
            if (defaultCat.equalsIgnoreCase(categoryName.trim())) {
                throw new SQLException("Cannot add default category as custom");
            }
        }
        
        String sql = "INSERT INTO custom_categories (user_id, category_name, color) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, categoryName.trim());
            pstmt.setString(3, color);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new SQLException("Category already exists");
            }
            throw e;
        }
    }
    
    /**
     * Delete a custom category
     */
    public void deleteCustomCategory(int userId, String categoryName) throws SQLException {
        String sql = "DELETE FROM custom_categories WHERE user_id = ? AND category_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, categoryName);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Get color for a category
     */
    public String getCategoryColor(String category) {
        return CATEGORY_COLORS.getOrDefault(category, "#6c757d");
    }
    
    /**
     * Check if category is a default category
     */
    public static boolean isDefaultCategory(String category) {
        for (String defaultCat : DEFAULT_CATEGORIES) {
            if (defaultCat.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }
}

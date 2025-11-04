import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Health Dashboard for password security analysis
 */
public class HealthDashboard {
    
    private List<Database.Credential> credentials;
    
    public HealthDashboard(List<Database.Credential> credentials) {
        this.credentials = credentials;
    }
    
    /**
     * Dashboard statistics
     */
    public static class Stats {
        public int total;
        public int weak;
        public int medium;
        public int strong;
        public int reused;
        public int expired;
        public int expiringSoon; // Within 7 days
        public int noExpiry;
        public double securityScore; // 0-100
        
        @Override
        public String toString() {
            return String.format("Total: %d, Weak: %d, Medium: %d, Strong: %d, Reused: %d, Expired: %d, Score: %.1f",
                               total, weak, medium, strong, reused, expired, securityScore);
        }
    }
    
    /**
     * Calculate overall security statistics
     */
    public Stats calculateStats() {
        Stats stats = new Stats();
        stats.total = credentials.size();
        
        if (stats.total == 0) {
            stats.securityScore = 100.0;
            return stats;
        }
        
        Map<String, Integer> passwordCounts = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        for (Database.Credential cred : credentials) {
            // Password strength - Use StrengthChecker.Strength enum for accurate classification
            StrengthChecker.Strength strength = StrengthChecker.checkStrength(cred.password);
            
            if (strength == StrengthChecker.Strength.WEAK) stats.weak++;
            else if (strength == StrengthChecker.Strength.MEDIUM) stats.medium++;
            else if (strength == StrengthChecker.Strength.STRONG) stats.strong++;
            
            // Count password reuse
            passwordCounts.put(cred.password, passwordCounts.getOrDefault(cred.password, 0) + 1);
            
            // Check expiry
            if (cred.expiryDate != null && !cred.expiryDate.isEmpty()) {
                try {
                    LocalDate expiryDate = parseDate(cred.expiryDate);
                    if (expiryDate.isBefore(today)) {
                        stats.expired++;
                    } else if (expiryDate.isBefore(today.plusDays(7))) {
                        stats.expiringSoon++;
                    }
                } catch (Exception e) {
                    // Invalid date format, ignore
                }
            } else {
                stats.noExpiry++;
            }
        }
        
        // Count reused passwords
        for (int count : passwordCounts.values()) {
            if (count > 1) {
                stats.reused += count;
            }
        }
        
        // Calculate security score (0-100)
        // Start with base score based on password strength distribution
        double strengthScore = 0;
        if (stats.total > 0) {
            strengthScore = ((stats.strong * 100.0) + (stats.medium * 60.0) + (stats.weak * 20.0)) / stats.total;
        } else {
            strengthScore = 100.0;
        }
        
        // Apply penalties for security issues
        double penalties = 0;
        if (stats.total > 0) {
            // Penalty for reused passwords (up to -20 points)
            penalties += Math.min(20, (stats.reused * 100.0 / stats.total) * 0.5);
            
            // Penalty for expired passwords (up to -20 points)
            penalties += Math.min(20, (stats.expired * 100.0 / stats.total) * 0.5);
            
            // Penalty for expiring soon (up to -10 points)
            penalties += Math.min(10, (stats.expiringSoon * 100.0 / stats.total) * 0.3);
        }
        
        double score = Math.max(0, Math.min(100, strengthScore - penalties));
        stats.securityScore = score;
        
        return stats;
    }
    
    /**
     * Get passwords that need attention (weak, reused, expired)
     */
    public List<Database.Credential> getPasswordsNeedingAttention() {
        List<Database.Credential> needsAttention = new ArrayList<>();
        Map<String, Integer> passwordCounts = new HashMap<>();
        LocalDate today = LocalDate.now();
        
        // Count password usage
        for (Database.Credential cred : credentials) {
            passwordCounts.put(cred.password, passwordCounts.getOrDefault(cred.password, 0) + 1);
        }
        
        for (Database.Credential cred : credentials) {
            boolean needsHelp = false;
            
            // Weak password - Use the Strength enum check
            if (StrengthChecker.checkStrength(cred.password) == StrengthChecker.Strength.WEAK) {
                needsHelp = true;
            }
            
            // Reused password
            if (passwordCounts.get(cred.password) > 1) {
                needsHelp = true;
            }
            
            // Expired password
            if (cred.expiryDate != null && !cred.expiryDate.isEmpty()) {
                try {
                    LocalDate expiryDate = parseDate(cred.expiryDate);
                    if (expiryDate.isBefore(today)) {
                        needsHelp = true;
                    }
                } catch (Exception e) {
                    // Invalid date, ignore
                }
            }
            
            if (needsHelp) {
                needsAttention.add(cred);
            }
        }
        
        return needsAttention;
    }
    
    /**
     * Check if a password is expired
     */
    public static boolean isExpired(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        
        try {
            LocalDate expiry = parseDate(expiryDate);
            return expiry.isBefore(LocalDate.now());
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if password is expiring soon (within 7 days)
     */
    public static boolean isExpiringSoon(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return false;
        }
        
        try {
            LocalDate expiry = parseDate(expiryDate);
            LocalDate today = LocalDate.now();
            LocalDate weekFromNow = today.plusDays(7);
            return !expiry.isBefore(today) && expiry.isBefore(weekFromNow);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get days until expiry
     */
    public static long getDaysUntilExpiry(String expiryDate) {
        if (expiryDate == null || expiryDate.isEmpty()) {
            return Long.MAX_VALUE;
        }
        
        try {
            LocalDate expiry = parseDate(expiryDate);
            return ChronoUnit.DAYS.between(LocalDate.now(), expiry);
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }
    
    /**
     * Get password age in days
     */
    public static long getPasswordAge(String lastPasswordChange) {
        if (lastPasswordChange == null || lastPasswordChange.isEmpty()) {
            return 0;
        }
        
        try {
            LocalDate changeDate = parseDate(lastPasswordChange);
            return ChronoUnit.DAYS.between(changeDate, LocalDate.now());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Calculate suggested expiry date (90 days from now)
     */
    public static String calculateExpiryDate(int daysFromNow) {
        LocalDate expiry = LocalDate.now().plusDays(daysFromNow);
        return expiry.toString(); // ISO format: yyyy-MM-dd
    }
    
    /**
     * Parse date from various formats
     */
    private static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now();
        }
        
        // Try ISO format first (yyyy-MM-dd)
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e1) {
            // Try datetime format (yyyy-MM-dd HH:mm:ss)
            try {
                if (dateStr.contains(" ")) {
                    dateStr = dateStr.split(" ")[0];
                    return LocalDate.parse(dateStr);
                }
            } catch (Exception e2) {
                // Default to today
                return LocalDate.now();
            }
        }
        return LocalDate.now();
    }
    
    /**
     * Format date for display
     */
    public static String formatDate(String dateStr) {
        try {
            LocalDate date = parseDate(dateStr);
            return date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
    }
}

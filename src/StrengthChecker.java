public class StrengthChecker 
{    
    public enum Strength { WEAK, MEDIUM, STRONG }
    public static Strength checkStrength(String password) {
        if (password == null || password.isEmpty()) {
            return Strength.WEAK;
        }  
        int score = computeScore(password);
        if (score >= 5) return Strength.STRONG;
        if (score >= 3) return Strength.MEDIUM;
        return Strength.WEAK;
    }
    public static int computeScore(String password) {
        if (password == null || password.isEmpty()) return 0;   
        int score = 0;
        if (password.length() >= 8) score++;
        if (password.length() >= 12) score++;
        if (password.chars().anyMatch(Character::isUpperCase)) score++;
        if (password.chars().anyMatch(Character::isLowerCase)) score++;
        if (password.chars().anyMatch(Character::isDigit)) score++;
        if (password.chars().anyMatch(ch -> "!@#$%^&*()-_=+[]{};:,.<>?/\\|~`".indexOf(ch) >= 0)) score++;
        return score;
    }
    public static String[] getSuggestions(String password) {
        java.util.List<String> suggestions = new java.util.ArrayList<>();   
        if (password.length() < 8) {
            suggestions.add("• Increase length to at least 8 characters.");
        } else if (password.length() < 12) {
            suggestions.add("• Consider using 12+ characters for better security.");
        }
        if (!password.chars().anyMatch(Character::isUpperCase)) {
            suggestions.add("• Add uppercase letters.");
        }
        if (!password.chars().anyMatch(Character::isLowerCase)) {
            suggestions.add("• Add lowercase letters.");
        }
        if (!password.chars().anyMatch(Character::isDigit)) {
            suggestions.add("• Add digits.");
        }
        if (!password.chars().anyMatch(ch -> "!@#$%^&*()-_=+[]{};:,.<>?/\\|~`".indexOf(ch) >= 0)) {
            suggestions.add("• Add symbols like !@#$% to increase entropy.");
        }
        if (checkStrength(password) == Strength.STRONG) {
            suggestions.add("Looks good — your password is strong.");
        }
        return suggestions.toArray(new String[0]);
    }
}

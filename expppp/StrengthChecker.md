# StrengthChecker.java - Line-by-Line Explanation

This file contains the password strength checking algorithm. It analyzes passwords based on multiple criteria and provides a strength rating (WEAK, MEDIUM, or STRONG) along with suggestions for improvement.

---

## **Import Statements**

```java
import java.util.ArrayList;
import java.util.List;
```

- **`import java.util.ArrayList;`** - Imports the `ArrayList` class to store a dynamic list of suggestions
- **`import java.util.List;`** - Imports the `List` interface used as the return type for suggestion collection

---

## **Class Declaration**

```java
public class StrengthChecker {
```

- **`public class StrengthChecker`** - Declares a public utility class with static methods for password strength analysis
- No instance variables needed - all methods are static and stateless

---

## **Strength Enum**

```java
    public enum Strength {
        WEAK, MEDIUM, STRONG
    }
```

- **`public enum Strength`** - Defines three possible strength levels as an enumeration
- **`WEAK`** - Password score 0-1 out of 6 criteria (very poor password)
- **`MEDIUM`** - Password score 2-3 out of 6 criteria (adequate but improvable)
- **`STRONG`** - Password score 4+ out of 6 criteria (excellent password)
- Used instead of strings/integers to provide type-safe strength representation

---

## **checkStrength() Method**

```java
    public static Strength checkStrength(String password) {
```

- **`public static Strength checkStrength(String password)`** - Main entry point for strength checking
- **`static`** - Can be called without creating StrengthChecker instance: `StrengthChecker.checkStrength("mypass")`
- **Returns:** `Strength` enum value (WEAK, MEDIUM, or STRONG)
- **Parameter:** `password` - the password string to analyze

```java
        int score = computeScore(password);
```

- **`int score = computeScore(password);`** - Calls the scoring algorithm to get 0-6 score
- This score represents how many of 6 criteria the password meets
- Example: password "Abc123!@#" might score 6/6 (has length, uppercase, lowercase, digits, symbols, and good length)

```java
        if (score >= 4) return Strength.STRONG;
```

- **`if (score >= 4) return Strength.STRONG;`** - Passwords meeting 4+ criteria are considered strong
- Example: "Password123!" would score 5 (length >=12, upper, lower, digits, symbols) → STRONG

```java
        if (score >= 2) return Strength.MEDIUM;
```

- **`if (score >= 2) return Strength.MEDIUM;`** - Passwords meeting 2-3 criteria are adequate
- Example: "password123" would score 2 (length >=8, lower, digits) → MEDIUM

```java
        return Strength.WEAK;
    }
```

- **`return Strength.WEAK;`** - Passwords meeting 0-1 criteria are weak
- Example: "abc" would score 1 (only lowercase) → WEAK

---

## **computeScore() Method**

This is the core scoring algorithm that evaluates 6 different password criteria.

```java
    public static int computeScore(String password) {
```

- **`public static int computeScore(String password)`** - Calculates numerical strength score
- **Returns:** integer from 0 to 6 (number of criteria met)
- Each criterion adds 1 point to the score

```java
        int score = 0;
```

- **`int score = 0;`** - Initialize score counter to zero
- Will be incremented for each criterion the password satisfies

### **Criterion 1: Minimum Length (8 characters)**

```java
        if (password.length() >= 8) score++;
```

- **`if (password.length() >= 8) score++;`** - Award 1 point if password has at least 8 characters
- **Why 8?** - NIST recommends minimum 8 characters for basic security
- Example: "Pass123!" has 8 characters → +1 point

### **Criterion 2: Good Length (12+ characters)**

```java
        if (password.length() >= 12) score++;
```

- **`if (password.length() >= 12) score++;`** - Award additional point for 12+ character passwords
- Longer passwords exponentially increase brute-force difficulty
- Example: "Password123!" has 12 characters → +1 point (in addition to the first point)
- Note: A 12-character password can score 2 points total from length alone

### **Criterion 3: Contains Uppercase Letters**

```java
        if (password.matches(".*[A-Z].*")) score++;
```

- **`if (password.matches(".*[A-Z].*")) score++;`** - Award point if password contains at least one uppercase letter
- **Regex breakdown:**
  - `.*` - any characters before
  - `[A-Z]` - one uppercase letter (A through Z)
  - `.*` - any characters after
- Example: "Password123" contains 'P' → +1 point

### **Criterion 4: Contains Lowercase Letters**

```java
        if (password.matches(".*[a-z].*")) score++;
```

- **`if (password.matches(".*[a-z].*")) score++;`** - Award point if password contains at least one lowercase letter
- **Regex:** `[a-z]` matches any lowercase letter (a through z)
- Example: "Password123" contains 'assword' → +1 point

### **Criterion 5: Contains Digits**

```java
        if (password.matches(".*\\d.*")) score++;
```

- **`if (password.matches(".*\\d.*")) score++;`** - Award point if password contains at least one digit
- **Regex:** `\\d` matches any digit 0-9 (escaped because Java string)
- Example: "Password123" contains '123' → +1 point

### **Criterion 6: Contains Special Symbols**

```java
        if (password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/`~].*")) score++;
```

- **`if (password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/`~].*")) score++;`** - Award point for special characters
- **Regex character class:** Matches any of these symbols:
  - `!@#$%^&*()` - common symbols
  - `\\-_=+` - math/punctuation (dash escaped)
  - `\\[\\]{}` - brackets (square brackets escaped)
  - `|;:'\",.<>?/` - various punctuation
  - `` `~ `` - backtick and tilde
- Example: "Password123!" contains '!' → +1 point

```java
        return score;
    }
```

- **`return score;`** - Return the total score (0-6)

**Example Calculation:**
- Password: `"MyP@ss2024"`
- Length 10 → +1 (≥8)
- Not 12+ → +0
- Has 'M', 'P' → +1 (uppercase)
- Has 'y', 's', 's' → +1 (lowercase)
- Has '2', '0', '2', '4' → +1 (digits)
- Has '@' → +1 (symbols)
- **Total score: 5/6** → STRONG

---

## **getSuggestions() Method**

This method generates a list of actionable recommendations for improving weak passwords.

```java
    public static String[] getSuggestions(String password) {
```

- **`public static String[] getSuggestions(String password)`** - Generates improvement recommendations
- **Returns:** Array of suggestion strings to display to user
- **Parameter:** `password` - the password to analyze

```java
        List<String> suggestions = new ArrayList<>();
```

- **`List<String> suggestions = new ArrayList<>();`** - Create dynamic list to hold suggestions
- Using `List` instead of array because we don't know how many suggestions we'll need
- Will convert to array at the end

### **Suggestion 1: Length Check**

```java
        if (password.length() < 8) {
            suggestions.add("❌ Use at least 8 characters");
        } else if (password.length() < 12) {
            suggestions.add("⚠️ Consider using 12+ characters for better security");
        } else {
            suggestions.add("✓ Good length!");
        }
```

- **First `if`** - If password is too short (< 8), show error message with ❌
  - Example: Password "abc123" (6 chars) → "❌ Use at least 8 characters"
- **`else if`** - If password is adequate but not ideal (8-11 chars), show warning with ⚠️
  - Example: Password "Password1" (9 chars) → "⚠️ Consider using 12+ characters for better security"
- **`else`** - If password is 12+ characters, show success with ✓
  - Example: Password "MySecurePass123" (15 chars) → "✓ Good length!"

### **Suggestion 2: Uppercase Check**

```java
        if (!password.matches(".*[A-Z].*")) {
            suggestions.add("❌ Add uppercase letters (A-Z)");
        }
```

- **`if (!password.matches(".*[A-Z].*"))`** - Check if password is missing uppercase letters
- **`!`** - Negation: true if password does NOT contain uppercase
- If missing, add suggestion to include uppercase letters
- Example: Password "password123" → "❌ Add uppercase letters (A-Z)"

### **Suggestion 3: Lowercase Check**

```java
        if (!password.matches(".*[a-z].*")) {
            suggestions.add("❌ Add lowercase letters (a-z)");
        }
```

- **`if (!password.matches(".*[a-z].*"))`** - Check if password is missing lowercase letters
- Example: Password "PASSWORD123" → "❌ Add lowercase letters (a-z)"

### **Suggestion 4: Digit Check**

```java
        if (!password.matches(".*\\d.*")) {
            suggestions.add("❌ Add numbers (0-9)");
        }
```

- **`if (!password.matches(".*\\d.*"))`** - Check if password is missing digits
- Example: Password "Password" → "❌ Add numbers (0-9)"

### **Suggestion 5: Symbol Check**

```java
        if (!password.matches(".*[!@#$%^&*()\\-_=+\\[\\]{}|;:'\",.<>?/`~].*")) {
            suggestions.add("❌ Add special characters (!@#$%^&* etc.)");
        }
```

- **`if (!password.matches(...))`** - Check if password is missing special characters
- Example: Password "Password123" → "❌ Add special characters (!@#$%^&* etc.)"

### **Return Suggestions Array**

```java
        return suggestions.toArray(new String[0]);
    }
}
```

- **`return suggestions.toArray(new String[0]);`** - Convert List to String array
- **`new String[0]`** - Template array (Java will create appropriately sized array automatically)
- Returns empty array if password meets all criteria (no suggestions needed)

**Example Output:**
- Password: `"abc123"`
- Suggestions:
  1. "❌ Use at least 8 characters"
  2. "❌ Add uppercase letters (A-Z)"
  3. "❌ Add special characters (!@#$%^&* etc.)"

---

## **How It's Used in the Application**

### **In CredentialDialog (Live Feedback):**
```java
StrengthChecker.Strength s = StrengthChecker.checkStrength(password);
if (s == StrengthChecker.Strength.STRONG) {
    // Show green circle
} else if (s == StrengthChecker.Strength.MEDIUM) {
    // Show yellow circle
} else {
    // Show red circle
}
```

### **In StrengthCheckerDialog (Detailed Analysis):**
```java
StrengthChecker.Strength s = StrengthChecker.checkStrength(pwd);
int score = StrengthChecker.computeScore(pwd);
String[] suggestions = StrengthChecker.getSuggestions(pwd);
// Display strength meter, score, and suggestions
```

---

## **Algorithm Summary**

1. **Scoring System**: Password evaluated on 6 criteria (0-6 points)
2. **Criteria**:
   - Length ≥ 8 characters
   - Length ≥ 12 characters
   - Contains uppercase letters
   - Contains lowercase letters
   - Contains digits
   - Contains special symbols
3. **Classification**:
   - 0-1 points → WEAK (red)
   - 2-3 points → MEDIUM (yellow)
   - 4-6 points → STRONG (green)
4. **Suggestions**: Specific actionable recommendations for each missing criterion
5. **Real-time**: Used in UI to provide instant feedback as user types

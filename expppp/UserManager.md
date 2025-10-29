# UserManager.java - Complete Line-by-Line Explanation

## File Purpose
Manages user authentication with secure password hashing using SHA-256 + salt. Handles user creation, login verification, and password security.

---

## Import Statements (Lines 1-5)

```java
import java.security.MessageDigest;
```
**Line 1**: For SHA-256 hashing algorithm

```java
import java.security.NoSuchAlgorithmException;
```
**Line 2**: Exception if SHA-256 not available

```java
import java.security.SecureRandom;
```
**Line 3**: Cryptographically strong random number generator (for salt)

```java
import java.sql.*;
```
**Line 4**: Database classes (Connection, PreparedStatement, ResultSet)

```java
import java.util.Base64;
```
**Line 5**: For encoding binary data (hash, salt) as text

---

## Class Documentation & Declaration (Lines 7-11)

```java
/**
 * Manages user authentication and master password storage with SHA-256 hashing
 */
public class UserManager {
```
**Lines 7-10**: JavaDoc comment and class declaration

```java
private final Connection conn;
```
**Line 11**: Database connection (passed from Database class)
- `final` - Connection set in constructor, never changes

---

## Constructor (Lines 13-17)

```java
public UserManager(Connection conn) throws SQLException {
```
**Line 13**: Constructor takes database connection

```java
this.conn = conn;
```
**Line 14**: Store connection for later use

```java
initializeUsersTable();
```
**Line 15**: Create users table if it doesn't exist

```java
createDefaultUser();
```
**Line 16**: Create default "Abin" user if no users exist

---

## Initialize Users Table (Lines 19-32)

```java
private void initializeUsersTable() throws SQLException {
```
**Line 19**: Create users table in database

```java
String sql = """
    CREATE TABLE IF NOT EXISTS users (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT UNIQUE NOT NULL,
        password_hash TEXT NOT NULL,
        salt TEXT NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )
    """;
```
**Lines 20-28**: SQL CREATE TABLE statement (using text block `"""`)
- **`id`** - Auto-incrementing user ID
- **`username TEXT UNIQUE`** - Username must be unique (no duplicates)
- **`password_hash TEXT NOT NULL`** - SHA-256 hash (Base64 encoded)
- **`salt TEXT NOT NULL`** - Random salt (Base64 encoded)
- **`created_at TIMESTAMP`** - Auto-set to current time

```java
try (Statement stmt = conn.createStatement()) {
    stmt.execute(sql);
}
```
**Lines 29-31**: Execute SQL statement
- Try-with-resources automatically closes statement

---

## Create Default User (Lines 34-46)

```java
private void createDefaultUser() throws SQLException {
```
**Line 34**: Create default "Abin" user for testing

```java
String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
```
**Line 36**: SQL to check if user exists

```java
try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
    pstmt.setString(1, "Abin");
    ResultSet rs = pstmt.executeQuery();
```
**Lines 37-39**: Check if "Abin" user exists

```java
if (rs.next() && rs.getInt(1) == 0) {
```
**Line 40**: If COUNT(*) is 0, user doesn't exist

```java
createUser("Abin", "Abin@2006");
```
**Line 42**: Create default user with password "Abin@2006"

---

## Create User Method (Lines 48-71)

```java
/**
 * Create a new user with username and master password
 */
public boolean createUser(String username, String password) throws SQLException {
```
**Lines 48-51**: Public method to register new user
- Returns `true` if successful, `false` if username taken

```java
byte[] salt = generateSalt();
```
**Line 53**: Generate random 16-byte salt

```java
String saltStr = Base64.getEncoder().encodeToString(salt);
```
**Line 54**: Convert binary salt to text (Base64)
- Binary: `[0x3A, 0x7B, ...]`
- Base64: `"OnsvL3Q8..."`

```java
String passwordHash = hashPassword(password, salt);
```
**Line 57**: Hash password with salt

```java
String sql = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
```
**Line 59**: SQL to insert new user

```java
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, username);
    pstmt.setString(2, passwordHash);
    pstmt.setString(3, saltStr);
```
**Lines 60-63**: Set parameters (username, hash, salt)

```java
pstmt.executeUpdate();
return true;
```
**Lines 64-65**: Execute INSERT, return success

```java
} catch (SQLException e) {
    if (e.getMessage().contains("UNIQUE constraint failed")) {
        return false; // Username already exists
    }
    throw e;
}
```
**Lines 66-70**: Catch errors
- If UNIQUE constraint failed = username taken
- Return false instead of throwing exception
- Other errors are re-thrown

---

## Authenticate User Method (Lines 73-99)

```java
/**
 * Authenticate user with username and password
 * Returns user ID if successful, -1 if failed
 */
public int authenticateUser(String username, String password) throws SQLException {
```
**Lines 73-77**: Verify login credentials
- Returns user ID on success
- Returns -1 on failure

```java
String sql = "SELECT id, password_hash, salt FROM users WHERE username = ?";
```
**Line 78**: SQL to get user's hash and salt

```java
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, username);
    ResultSet rs = pstmt.executeQuery();
```
**Lines 79-81**: Query database for username

```java
if (rs.next()) {
```
**Line 83**: If user exists (query returned a row)

```java
int userId = rs.getInt("id");
String storedHash = rs.getString("password_hash");
String saltStr = rs.getString("salt");
byte[] salt = Base64.getDecoder().decode(saltStr);
```
**Lines 84-87**: Extract data from database
- Get user ID, stored hash, and salt
- Decode salt from Base64 to binary

```java
String providedHash = hashPassword(password, salt);
```
**Line 90**: Hash provided password with stored salt

```java
if (storedHash.equals(providedHash)) {
    return userId;
}
```
**Lines 92-94**: Compare hashes
- If match: Password correct, return user ID
- If no match: Continue to return -1

```java
}
}
return -1; // Authentication failed
```
**Lines 96-98**: If user not found or password wrong, return -1

---

## User Exists Method (Lines 101-110)

```java
/**
 * Check if username exists
 */
public boolean userExists(String username) throws SQLException {
```
**Lines 101-104**: Check if username is taken

```java
String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setString(1, username);
    ResultSet rs = pstmt.executeQuery();
    return rs.next() && rs.getInt(1) > 0;
}
```
**Lines 105-109**: Query and return result
- COUNT(*) returns number of matching users (0 or 1)
- Returns true if count > 0

---

## Get Username Method (Lines 112-123)

```java
/**
 * Get username by user ID
 */
public String getUsername(int userId) throws SQLException {
```
**Lines 112-115**: Get username from user ID

```java
String sql = "SELECT username FROM users WHERE id = ?";
try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    pstmt.setInt(1, userId);
    ResultSet rs = pstmt.executeQuery();
    if (rs.next()) {
        return rs.getString("username");
    }
}
return null;
```
**Lines 116-122**: Query and return username, or null if not found

---

## Generate Salt Method (Lines 125-130)

```java
private byte[] generateSalt() {
```
**Line 125**: Create random salt for password hashing

```java
SecureRandom random = new SecureRandom();
```
**Line 126**: Create cryptographically strong random generator
- **NOT** `new Random()` - that's predictable!
- **SecureRandom** is cryptographically secure

```java
byte[] salt = new byte[16];
```
**Line 127**: Create array for 16 bytes (128 bits)
- 16 bytes = 2^128 possible salts = 340,282,366,920,938,463,463,374,607,431,768,211,456 possibilities!

```java
random.nextBytes(salt);
return salt;
```
**Lines 128-129**: Fill array with random bytes and return

---

## Hash Password Method (Lines 132-141)

```java
private String hashPassword(String password, byte[] salt) {
```
**Line 132**: Hash password with salt using SHA-256

```java
try {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
```
**Lines 133-134**: Get SHA-256 hash algorithm
- SHA-256 = Secure Hash Algorithm, 256-bit output

```java
md.update(salt);
```
**Line 135**: Add salt to hash calculation
- Hash = SHA-256(salt + password)

```java
byte[] hashedPassword = md.digest(password.getBytes());
```
**Line 136**: Hash the password
- `password.getBytes()` converts string to bytes
- `digest()` computes final hash (32 bytes)

```java
return Base64.getEncoder().encodeToString(hashedPassword);
```
**Line 137**: Convert binary hash to text
- Binary: 32 bytes like `[0xA3, 0x7F, ...]`
- Base64: 44 characters like `"pL4mnQ8x..."`

```java
} catch (NoSuchAlgorithmException e) {
    throw new RuntimeException("SHA-256 algorithm not available", e);
}
```
**Lines 138-140**: Handle error if SHA-256 not available
- Should never happen on modern Java

---

## How Authentication Works: Complete Example

### Registration Flow

**User registers with username "John" and password "Secret123"**

```java
createUser("John", "Secret123");
```

**Step 1: Generate Salt**
```java
byte[] salt = generateSalt();
// salt = [0x3A, 0x7B, 0x2F, ..., 0xC4] (16 random bytes)
String saltStr = Base64.getEncoder().encodeToString(salt);
// saltStr = "OnsvL3Q8PV9eX0BCQ0RERU=="
```

**Step 2: Hash Password**
```java
String passwordHash = hashPassword("Secret123", salt);
// SHA-256([0x3A, 0x7B, ..., 0xC4] + "Secret123")
// = [0xA3, 0x7F, ..., 0x2B] (32 bytes)
// Base64 encode:
// passwordHash = "pL4mnQ8xY3zB1vC6hN4kM0pO9iU8tA7sE1D2gF3="
```

**Step 3: Store in Database**
```sql
INSERT INTO users (username, password_hash, salt) 
VALUES ('John', 'pL4mnQ8xY3zB1vC6hN4kM0pO9iU8tA7sE1D2gF3=', 'OnsvL3Q8PV9eX0BCQ0RERU==');
```

### Login Flow

**User tries to login with "John" / "Secret123"**

```java
int userId = authenticateUser("John", "Secret123");
```

**Step 1: Get Stored Data**
```sql
SELECT id, password_hash, salt FROM users WHERE username = 'John';
-- Returns:
-- id = 1
-- password_hash = "pL4mnQ8xY3zB1vC6hN4kM0pO9iU8tA7sE1D2gF3="
-- salt = "OnsvL3Q8PV9eX0BCQ0RERU=="
```

**Step 2: Hash Provided Password**
```java
byte[] salt = Base64.getDecoder().decode("OnsvL3Q8PV9eX0BCQ0RERU==");
// salt = [0x3A, 0x7B, 0x2F, ..., 0xC4] (same as registration)

String providedHash = hashPassword("Secret123", salt);
// SHA-256([0x3A, 0x7B, ..., 0xC4] + "Secret123")
// providedHash = "pL4mnQ8xY3zB1vC6hN4kM0pO9iU8tA7sE1D2gF3="
```

**Step 3: Compare Hashes**
```java
if (storedHash.equals(providedHash)) {
    return userId; // Success! Return 1
}
```

**If wrong password:**
```java
authenticateUser("John", "WrongPass");
// providedHash = "X7jK9mP2wQrT5nL8xY3zB1vC..." (different!)
// storedHash   = "pL4mnQ8xY3zB1vC..."
// NOT equal → return -1
```

---

## Security Features Explained

### Why Salt?

**Without Salt (BAD):**
```
Password "hello" → SHA-256 → Always "2cf24dba..."
Password "hello" → SHA-256 → Always "2cf24dba..."
```
- Same password = Same hash
- Attacker can use rainbow tables (pre-computed hashes)

**With Salt (GOOD):**
```
Password "hello" + Salt1 → SHA-256 → "X7jK9..."
Password "hello" + Salt2 → SHA-256 → "pL4mn..." (different!)
```
- Same password + different salts = different hashes
- Rainbow tables useless
- Must brute-force each password individually

### Why SHA-256?

- **One-way function**: Can't reverse hash to get password
- **Deterministic**: Same input always gives same output
- **Fast but not too fast**: Hard to brute-force
- **Collision-resistant**: Very hard to find two inputs with same hash

### Why SecureRandom?

**Regular Random (BAD):**
```java
Random random = new Random();
// Predictable if you know the seed!
```

**SecureRandom (GOOD):**
```java
SecureRandom random = new SecureRandom();
// Uses OS entropy sources (mouse movements, keyboard timing, etc.)
// Cryptographically unpredictable
```

---

## Summary

This class provides **secure authentication**:

1. **Registration:**
   - Generate random salt (16 bytes)
   - Hash password with salt using SHA-256
   - Store username, hash, and salt in database

2. **Login:**
   - Retrieve stored hash and salt for username
   - Hash provided password with stored salt
   - Compare hashes (match = success, no match = failure)

3. **Security Features:**
   - Passwords never stored in plain text
   - Each user has unique salt
   - SHA-256 is cryptographically secure
   - SecureRandom for unpredictable salts
   - PreparedStatement prevents SQL injection

4. **Helper Methods:**
   - `userExists()` - Check username availability
   - `getUsername()` - Get username from ID
   - `generateSalt()` - Create random salt
   - `hashPassword()` - SHA-256 hash with salt

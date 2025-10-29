# Database.java - Complete Line-by-Line Explanation

## File Purpose
Handles all database operations - connecting to SQLite, creating tables, and CRUD (Create, Read, Update, Delete) operations for credentials.

---

## Import Statements (Lines 1-3)

```java
import java.sql.*;
```
**Line 1**: Imports all SQL-related classes
- `Connection` - Database connection
- `Statement` - Execute SQL commands
- `PreparedStatement` - Secure SQL with parameters
- `ResultSet` - Query results
- `SQLException` - Database errors

```java
import java.util.ArrayList;
import java.util.List;
```
**Lines 2-3**: Import list classes for storing multiple credentials

---

## Class Documentation (Lines 5-8)

```java
/**
 * Database handler for SecureVault using SQLite.
 * Manages CRUD operations for credentials.
 */
```
**Lines 5-8**: JavaDoc comment explaining class purpose

---

## Class Declaration & Constants (Lines 9-11)

```java
public class Database {
```
**Line 9**: Public class - anyone can use it

```java
private static final String DB_URL = "jdbc:sqlite:securevault.db";
```
**Line 10**: Database connection string
- `jdbc:sqlite:` - Use SQLite driver
- `securevault.db` - Database file name (created in current directory)
- `static final` - Constant that never changes

```java
private Connection connection;
```
**Line 11**: Stores active database connection

---

## Constructor (Lines 13-22)

```java
public Database() throws SQLException {
```
**Line 13**: Constructor that can throw SQLException if database fails

```java
try {
    Class.forName("org.sqlite.JDBC");
} catch (ClassNotFoundException e) {
    throw new SQLException("SQLite JDBC driver not found. Make sure sqlite-jdbc jar is in classpath.", e);
}
```
**Lines 14-18**: Try to load SQLite driver
- **Line 15**: `Class.forName()` loads the driver class
- **Line 16**: Catch if driver JAR file is missing
- **Line 17**: Convert to SQLException with helpful message

```java
connect();
```
**Line 19**: Call method to establish connection

```java
createTableIfNotExists();
```
**Line 20**: Call method to create tables if they don't exist

---

## Connect Method (Lines 24-32)

```java
private void connect() throws SQLException {
```
**Line 24**: Private method - only used internally

```java
try {
    connection = DriverManager.getConnection(DB_URL);
```
**Lines 25-26**: Try to connect to database
- `DriverManager.getConnection()` - Establishes connection
- Uses the DB_URL constant defined earlier

```java
System.out.println("Database connection established: " + DB_URL);
```
**Line 27**: Print success message to console

```java
} catch (SQLException e) {
    System.err.println("Failed to connect to database: " + e.getMessage());
    throw e;
}
```
**Lines 28-31**: If connection fails
- Print error to console (System.err for errors)
- Re-throw exception so caller knows it failed

---

## Create Table Method (Lines 34-43)

```java
private void createTableIfNotExists() throws SQLException {
```
**Line 34**: Create credentials table if it doesn't already exist

```java
String sql = "CREATE TABLE IF NOT EXISTS credentials (" +
             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "user_id INTEGER NOT NULL," +
             "title TEXT NOT NULL," +
             "username TEXT NOT NULL," +
             "password TEXT NOT NULL," +
             "FOREIGN KEY (user_id) REFERENCES users(id)" +
             ")";
```
**Lines 35-41**: SQL CREATE TABLE statement
- **`CREATE TABLE IF NOT EXISTS`** - Only create if doesn't exist
- **`id INTEGER PRIMARY KEY AUTOINCREMENT`** - Auto-incrementing ID
- **`user_id INTEGER NOT NULL`** - Links to users table, required
- **`title TEXT NOT NULL`** - Service name (e.g., "Gmail"), required
- **`username TEXT NOT NULL`** - Username for service, required
- **`password TEXT NOT NULL`** - Password for service, required
- **`FOREIGN KEY`** - Ensures user_id matches an existing user

```java
try (Statement stmt = connection.createStatement()) {
    stmt.execute(sql);
}
```
**Lines 42-44**: Execute the SQL statement
- **`try (...)`** - Try-with-resources, auto-closes statement
- **`createStatement()`** - Create SQL statement object
- **`execute(sql)`** - Run the SQL command

---

## Insert Credential Method (Lines 46-55)

```java
public void insertCredential(int userId, String title, String username, String password) throws SQLException {
```
**Line 46**: Public method to add new credential
- Parameters: userId (owner), title (service), username, password

```java
String sql = "INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)";
```
**Line 47**: SQL INSERT statement
- **`?`** - Placeholders for parameters (prevents SQL injection)

```java
try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
```
**Line 48**: Create prepared statement (auto-closes)

```java
pstmt.setInt(1, userId);
pstmt.setString(2, title);
pstmt.setString(3, username);
pstmt.setString(4, password);
```
**Lines 49-52**: Fill in the `?` placeholders
- **`setInt(1, userId)`** - First `?` = userId (integer)
- **`setString(2, title)`** - Second `?` = title (string)
- **`setString(3, username)`** - Third `?` = username
- **`setString(4, password)`** - Fourth `?` = password

**Why use `?` and set methods?**
- **SQL Injection Prevention**: If user types `"; DROP TABLE credentials;--` it's treated as literal text, not SQL code
- **Type Safety**: setInt ensures it's an integer, setString handles string escaping

```java
pstmt.executeUpdate();
```
**Line 53**: Execute the INSERT command
- `executeUpdate()` for INSERT, UPDATE, DELETE
- `executeQuery()` for SELECT

---

## Update Credential Method (Lines 57-66)

```java
public void updateCredential(int id, String title, String username, String password) throws SQLException {
```
**Line 57**: Update existing credential

```java
String sql = "UPDATE credentials SET title = ?, username = ?, password = ? WHERE id = ?";
```
**Line 58**: SQL UPDATE statement
- **`SET title = ?, username = ?, password = ?`** - New values
- **`WHERE id = ?`** - Which credential to update

```java
try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setString(1, title);
    pstmt.setString(2, username);
    pstmt.setString(3, password);
    pstmt.setInt(4, id);
```
**Lines 59-63**: Set parameters
- First 3 `?` are new values
- Last `?` is the credential ID to update

```java
pstmt.executeUpdate();
```
**Line 64**: Execute UPDATE

---

## Delete Credential Method (Lines 68-74)

```java
public void deleteCredential(int id) throws SQLException {
```
**Line 68**: Delete credential by ID

```java
String sql = "DELETE FROM credentials WHERE id = ?";
```
**Line 69**: SQL DELETE statement
- **`WHERE id = ?`** - Only delete this specific credential

```java
try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setInt(1, id);
    pstmt.executeUpdate();
}
```
**Lines 70-73**: Set ID parameter and execute DELETE

---

## Get All Credentials Method (Lines 76-91)

```java
public List<Credential> getAllCredentials(int userId) throws SQLException {
```
**Line 76**: Get all credentials for a specific user
- Returns List of Credential objects

```java
List<Credential> credentials = new ArrayList<>();
```
**Line 77**: Create empty list to store results

```java
String sql = "SELECT id, title, username, password FROM credentials WHERE user_id = ?";
```
**Line 78**: SQL SELECT statement
- **`SELECT id, title, username, password`** - Columns to retrieve
- **`WHERE user_id = ?`** - Only this user's credentials

```java
try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    pstmt.setInt(1, userId);
```
**Lines 79-80**: Set userId parameter

```java
ResultSet rs = pstmt.executeQuery();
```
**Line 81**: Execute query, get results
- **`ResultSet`** - Like a cursor pointing to rows

```java
while (rs.next()) {
```
**Line 82**: Loop through each row
- **`rs.next()`** - Move to next row, returns false when done

```java
int id = rs.getInt("id");
String title = rs.getString("title");
String username = rs.getString("username");
String password = rs.getString("password");
```
**Lines 83-86**: Extract data from current row
- **`rs.getInt("id")`** - Get value from "id" column as integer
- **`rs.getString("title")`** - Get value from "title" column as string

```java
credentials.add(new Credential(id, title, username, password));
```
**Line 87**: Create Credential object and add to list

```java
}
}
return credentials;
```
**Lines 88-90**: End loop, return list

---

## Get Connection Method (Lines 93-95)

```java
public Connection getConnection() {
    return connection;
}
```
**Lines 93-95**: Return database connection
- Used by UserManager to access users table

---

## Close Method (Lines 97-105)

```java
public void close() {
```
**Line 97**: Close database connection

```java
try {
    if (connection != null && !connection.isClosed()) {
```
**Lines 98-99**: Check if connection exists and is open

```java
connection.close();
```
**Line 100**: Close the connection

```java
}
} catch (SQLException e) {
    System.err.println("Error closing database: " + e.getMessage());
}
```
**Lines 101-104**: Catch and print any errors during close
- Not re-throwing because close() is often called during cleanup

---

## Inner Class: Credential (Lines 107-121)

```java
/**
 * Credential model class with database ID
 */
public static class Credential {
```
**Lines 107-110**: Inner class to store credential data
- **`static`** - Can be used without Database instance
- **`public`** - Other classes can use it

```java
public final int id;
public final String title;
public final String username;
public final String password;
```
**Lines 111-114**: Fields to store credential data
- **`final`** - Cannot be changed after creation (immutable)
- **`public`** - Directly accessible (simple data container)

```java
public Credential(int id, String title, String username, String password) {
    this.id = id;
    this.title = title;
    this.username = username;
    this.password = password;
}
```
**Lines 116-121**: Constructor to create Credential object
- **`this.id = id`** - Store parameter in field
- All 4 fields set from parameters

---

## How It All Works Together

### Example: Adding a Credential

```java
// In SecureVaultSwing.onAdd():
database.insertCredential(1, "Gmail", "john@gmail.com", "MyPassword123");
```

**What happens:**
1. Method called with userId=1, title="Gmail", etc.
2. SQL string created: `INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)`
3. PreparedStatement created
4. Parameters set: 1, "Gmail", "john@gmail.com", "MyPassword123"
5. SQL executed: `INSERT INTO credentials (user_id, title, username, password) VALUES (1, 'Gmail', 'john@gmail.com', 'MyPassword123')`
6. Row added to database

### Example: Loading Credentials

```java
// In SecureVaultSwing.loadCredentials():
List<Database.Credential> creds = database.getAllCredentials(1);
```

**What happens:**
1. Method called with userId=1
2. SQL: `SELECT id, title, username, password FROM credentials WHERE user_id = 1`
3. Query executed, ResultSet returned
4. Loop through ResultSet:
   - Row 1: id=1, title="Gmail", username="john@gmail.com", password="MyPassword123"
   - Create Credential object, add to list
   - Row 2: id=2, title="Facebook"...
   - Continue for all rows
5. Return list of Credential objects

### Security: SQL Injection Prevention

**BAD (Vulnerable to SQL Injection):**
```java
String sql = "INSERT INTO credentials VALUES ('" + title + "', '" + username + "')";
// If title = "'; DROP TABLE credentials;--" the entire table is deleted!
```

**GOOD (Using PreparedStatement):**
```java
String sql = "INSERT INTO credentials VALUES (?, ?)";
pstmt.setString(1, title);
// Even if title = "'; DROP TABLE credentials;--" it's treated as literal text
```

---

## Summary

This class is the **Data Access Layer**:
- **Manages connection** to SQLite database file
- **Creates tables** if they don't exist
- **Provides CRUD methods**:
  - CREATE: `insertCredential()`
  - READ: `getAllCredentials()`
  - UPDATE: `updateCredential()`
  - DELETE: `deleteCredential()`
- **Uses PreparedStatement** for security (prevents SQL injection)
- **Auto-closes resources** with try-with-resources
- **Defines Credential class** as simple data container

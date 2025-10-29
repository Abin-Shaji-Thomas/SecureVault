# PROGRAM_EXECUTION_FLOW.md

This document explains how all the components of SecureVault work together from startup to shutdown. It traces the complete execution flow, showing how each class is called, when methods execute, and how data flows through the application.

---

## **Table of Contents**

1. [Application Startup](#application-startup)
2. [Login/Registration Flow](#loginregistration-flow)
3. [Main Application Initialization](#main-application-initialization)
4. [User Interactions](#user-interactions)
5. [Database Operations](#database-operations)
6. [Security Layer](#security-layer)
7. [UI Update Cycle](#ui-update-cycle)
8. [Shutdown Flow](#shutdown-flow)
9. [Complete Call Graph](#complete-call-graph)

---

## **Application Startup**

### **Entry Point: main() Method**

```
SecureVaultSwing.main()  ← JVM starts here
```

**File:** `SecureVaultSwing.java`, line 24

**What happens:**
1. JVM calls `main(String[] args)`
2. Sets look-and-feel to system default
3. Creates SecureVaultSwing instance: `new SecureVaultSwing()`
4. Constructor takes over execution

**Code:**
```java
public static void main(String[] args) {
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    new SecureVaultSwing();  // ← Constructor executes immediately
}
```

---

## **Login/Registration Flow**

### **Phase 1: Show Login Dialog**

```
SecureVaultSwing constructor (line 24)
  ↓
showLogin() method (line 45)
  ↓
new LoginDialog(null) (line 46)
  ↓
loginDialog.setVisible(true) (line 47)  ← Blocks here waiting for user
```

**File:** `SecureVaultSwing.java`, lines 45-47

**What happens:**
1. Constructor calls `showLogin()` immediately
2. `showLogin()` creates `LoginDialog` instance
3. Dialog is displayed and BLOCKS program execution
4. User sees login window (cannot proceed until they interact)

**Visual:**
```
┌─────────────────────────┐
│  SecureVault - Login    │
├─────────────────────────┤
│ Username: [_________]   │
│ Password: [_________]   │
│ [ ] Show Password       │
│                         │
│ [Login] [Create] [Exit] │  ← Program waits here
└─────────────────────────┘
```

---

### **Phase 2: User Action Decision Tree**

**Three possible outcomes:**

#### **Outcome A: User Clicks "Exit"**

```
User clicks Exit button
  ↓
LoginDialog: succeeded = false (line 127)
  ↓
LoginDialog: dispose() (line 128)
  ↓
SecureVaultSwing: if (!loginDialog.isSucceeded()) (line 49)
  ↓
System.exit(0) (line 50)
  ↓
Application terminates  ← JVM exits
```

**Result:** Application quits immediately, no window appears.

---

#### **Outcome B: User Clicks "Login"**

```
User clicks Login button
  ↓
LoginDialog: username = usernameField.getText().trim() (line 82)
  ↓
LoginDialog: password = new String(passwordField.getPassword()) (line 83)
  ↓
LoginDialog: succeeded = true (line 84)
  ↓
LoginDialog: createNewUser = false (line 85)
  ↓
LoginDialog: dispose() (line 86)
  ↓
SecureVaultSwing: String username = loginDialog.getUsername() (line 53)
  ↓
SecureVaultSwing: String password = loginDialog.getPassword() (line 54)
  ↓
SecureVaultSwing: if (loginDialog.isCreateNewUser()) → FALSE
  ↓
SecureVaultSwing: else block executes (line 66)
  ↓
UserManager: currentUserId = userManager.authenticateUser(username, password) (line 68)
```

**Then goes to:** [Authentication Process](#authentication-process)

---

#### **Outcome C: User Clicks "Create User"**

```
User clicks Create User button
  ↓
LoginDialog: username = usernameField.getText().trim() (line 90)
  ↓
LoginDialog: password = new String(passwordField.getPassword()) (line 91)
  ↓
LoginDialog: Validation checks (lines 93-108)
  ├─ if (username.isEmpty() || password.isEmpty()) → Show error, return
  ├─ if (password.length() < 6) → Show error, return
  └─ All valid → Continue
  ↓
LoginDialog: succeeded = true (line 110)
  ↓
LoginDialog: createNewUser = true (line 111)
  ↓
LoginDialog: dispose() (line 112)
  ↓
SecureVaultSwing: if (loginDialog.isCreateNewUser()) → TRUE (line 56)
  ↓
UserManager: boolean created = userManager.createUser(username, password) (line 58)
```

**Then goes to:** [User Creation Process](#user-creation-process)

---

### **Authentication Process (Login Attempt)**

```
UserManager.authenticateUser(username, password) (line 68)
  ↓
UserManager: SELECT password_hash, salt FROM users WHERE username = ? (line 71)
  ↓
Database Query Executes
  ├─ User exists → rs.next() returns true
  │   ↓
  │   Retrieve stored hash and salt (lines 74-75)
  │   ↓
  │   hashPassword(password, salt) (line 76)
  │   ↓
  │   Compare computed hash with stored hash (line 77)
  │   ├─ Match → return userId (line 79)
  │   └─ No match → return -1 (line 84)
  │
  └─ User doesn't exist → rs.next() returns false → return -1 (line 84)
  ↓
SecureVaultSwing: if (currentUserId == -1) → TRUE (failed) (line 69)
  ↓
Show error message (lines 70-72)
  ↓
continue; (line 73) → Loop back to showLogin()
  ↓
User sees login dialog again  ← Infinite loop until success or exit
```

**Success Path:**
```
currentUserId != -1 (valid user ID received)
  ↓
break; (line 75) → Exit login loop
  ↓
buildUI() is called (line 40)
  ↓
Proceed to Main Application Initialization
```

---

### **User Creation Process (Registration)**

```
UserManager.createUser(username, password) (line 58)
  ↓
UserManager: Check if username exists (lines 22-29)
  ├─ Username taken → return false
  └─ Username available → Continue
  ↓
UserManager: byte[] salt = generateSalt() (line 33)
  ├─ SecureRandom random = new SecureRandom() (line 108)
  ├─ byte[] salt = new byte[16] (line 109)
  ├─ random.nextBytes(salt) (line 110)
  └─ return salt → 16 random bytes
  ↓
UserManager: String hash = hashPassword(password, salt) (line 34)
  ├─ MessageDigest digest = MessageDigest.getInstance("SHA-256") (line 96)
  ├─ digest.update(salt) → Add salt to digest (line 97)
  ├─ byte[] hashBytes = digest.digest(password.getBytes(UTF_8)) (line 98)
  ├─ return Base64.getEncoder().encodeToString(hashBytes) (line 99)
  └─ return base64-encoded hash
  ↓
UserManager: INSERT INTO users VALUES(null, ?, ?, ?) (line 37)
  ├─ setString(1, username)
  ├─ setString(2, hash)
  └─ setString(3, Base64.encode(salt))
  ↓
User record created in database
  ↓
UserManager.createUser() returns true (line 39)
  ↓
SecureVaultSwing: if (created) → TRUE (line 59)
  ↓
Show success message (lines 60-61)
  ↓
SecureVaultSwing: currentUserId = userManager.authenticateUser(username, password) (line 62)
  ↓
Authentication succeeds (user just created)
  ↓
break; (line 64) → Exit login loop
  ↓
buildUI() is called (line 40)
```

---

## **Main Application Initialization**

### **buildUI() Execution**

```
SecureVaultSwing.buildUI() (line 80)
  ↓
setTitle("SecureVault") (line 81)
  ↓
setSize(1000, 600) (line 82)
  ↓
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) (line 83)
  ↓
setLocationRelativeTo(null) (line 84) → Center on screen
```

**Create Table Model and Table:**
```
buildUI() continued...
  ↓
tableModel = new DefaultTableModel(...) (lines 87-91)
  ├─ Column names: ["Title", "Username", "Password", "Actions", "Strength"]
  └─ isCellEditable() overridden to return false
  ↓
table = new JTable(tableModel) (line 93)
  ↓
table.setRowHeight(40) (line 94)
  ↓
table.getColumnModel().getColumn(4).setCellRenderer(new StrengthRenderer()) (line 97)
  └─ Custom renderer for "Strength" column
```

**Create Toolbar:**
```
buildUI() continued...
  ↓
JToolBar toolbar = new JToolBar() (line 102)
  ↓
Create buttons:
  ├─ addBtn ("Add Credential")
  ├─ refreshBtn ("Refresh")
  ├─ deleteBtn ("Delete")
  ├─ genBtn ("Generate Password")
  ├─ strengthBtn ("Check Strength")
  ├─ copyUserBtn ("Copy Username")
  ├─ copyPassBtn ("Copy Password")
  ├─ logoutBtn ("Logout")
  └─ themeBtn ("Toggle Theme")
  ↓
Add action listeners to all buttons (lines 106-231)
```

**Assemble Layout:**
```
buildUI() continued...
  ↓
setLayout(new BorderLayout()) (line 233)
  ↓
add(toolbar, BorderLayout.NORTH) (line 234)
  ↓
add(new JScrollPane(table), BorderLayout.CENTER) (line 235)
  ↓
applyTheme(currentTheme) (line 237) → Apply default LIGHT theme
  ↓
loadCredentials() (line 239) → Fetch user's credentials from database
  ↓
setVisible(true) (line 240) → Show main window
```

**Result:** User sees main application window with their credentials.

---

### **Initial Data Load**

```
loadCredentials() (line 253)
  ↓
tableModel.setRowCount(0) (line 254) → Clear existing rows
  ↓
Database.getAllCredentials(currentUserId) (line 255)
  ↓
Database: SELECT * FROM credentials WHERE user_id = ? (line 53)
  ↓
Database: ResultSet rs = stmt.executeQuery() (line 56)
  ↓
Database: while (rs.next()) loop (lines 58-63)
  ├─ Create Credential objects from each row
  └─ Add to credentials list
  ↓
Database: return credentials list (line 67)
  ↓
SecureVaultSwing: for (Credential c : credentials) loop (lines 256-261)
  ├─ StrengthChecker.checkStrength(c.password) → Get strength
  └─ tableModel.addRow([c.title, c.username, "••••••••", "Edit | Delete", strength])
  ↓
Table displays all credentials
```

---

## **User Interactions**

### **Add Credential Flow**

```
User clicks "Add Credential" button
  ↓
addBtn ActionListener executes (line 106)
  ↓
new CredentialDialog(this, null).showDialog() (line 107)
  ├─ CredentialDialog constructor (line 14)
  ├─ showDialog() creates input fields (lines 19-24)
  ├─ Live strength meter attached (lines 33-67)
  └─ JOptionPane.showConfirmDialog() blocks (lines 69-70)
  ↓
User fills in fields:
  ├─ Title: "Gmail"
  ├─ Username: "user@gmail.com"
  └─ Password: "MyPass123!" (strength meter updates live)
  ↓
User clicks OK
  ↓
CredentialDialog: Validate fields (lines 77-81)
  ├─ All fields filled? → Yes, continue
  └─ Any empty? → Show error, return null
  ↓
CredentialDialog: result = new Credential(0, title, username, password) (line 85)
  ↓
CredentialDialog: return result (line 88)
  ↓
SecureVaultSwing: if (newCred != null) → TRUE (line 108)
  ↓
Database.insertCredential(currentUserId, newCred.title, newCred.username, newCred.password) (line 109)
  ↓
Database: INSERT INTO credentials VALUES(null, ?, ?, ?, ?) (line 32)
  ├─ setInt(1, userId)
  ├─ setString(2, title)
  ├─ setString(3, username)
  └─ setString(4, password)
  ↓
Database: stmt.executeUpdate() → Insert row
  ↓
SecureVaultSwing: loadCredentials() (line 110)
  ↓
Table refreshes with new credential
```

---

### **Edit Credential Flow**

```
User double-clicks a table row
  ↓
table MouseListener.mouseClicked() executes (line 242)
  ↓
if (e.getClickCount() == 2) → TRUE (line 243)
  ↓
int row = table.getSelectedRow() (line 244)
  ↓
int id = (int) tableModel.getValueAt(row, 0) → Wait, this is wrong...
  ↓
Actually: Credentials are loaded in order, so row index maps to credential
  ↓
Retrieve credential from database by reconstructing from table
  ↓
OR: Alternative flow via "Actions" column click
```

**Via "Edit" Link in Actions Column:**
```
User clicks "Edit" in Actions column
  ↓
table MouseListener.mouseClicked() executes (line 264)
  ↓
int col = table.columnAtPoint(e.getPoint()) (line 265)
  ↓
if (col == 3) → TRUE (Actions column) (line 266)
  ↓
int row = table.rowAtPoint(e.getPoint()) (line 267)
  ↓
String actions = (String) tableModel.getValueAt(row, 3) (line 268)
  ↓
Rectangle cellRect = table.getCellRect(row, col, false) (line 269)
  ↓
int x = e.getX() - cellRect.x (line 270)
  ↓
if (x < 50) → TRUE (clicked "Edit") (line 272)
  ↓
Retrieve credential data from table:
  ├─ title = (String) tableModel.getValueAt(row, 0)
  ├─ username = (String) tableModel.getValueAt(row, 1)
  └─ password = actual password (need to retrieve from database)
  ↓
Problem: Table doesn't show actual password (shows bullets)
  ↓
Solution: Re-fetch credential from database using title+username
  ↓
Database.getAllCredentials(currentUserId) → Get all
  ↓
Filter to find matching credential
  ↓
Credential found = matching credential
  ↓
new CredentialDialog(this, found).showDialog() (line 277)
  ↓
User edits fields
  ↓
User clicks OK
  ↓
CredentialDialog returns edited Credential
  ↓
Database.updateCredential(found.id, edited.title, edited.username, edited.password) (line 280)
  ↓
Database: UPDATE credentials SET title=?, username=?, password=? WHERE id=? (line 72)
  ↓
Database: stmt.executeUpdate() → Update row
  ↓
loadCredentials() (line 281) → Refresh table
```

---

### **Delete Credential Flow**

```
User clicks "Delete" in Actions column
  ↓
table MouseListener.mouseClicked() executes (line 264)
  ↓
int col = table.columnAtPoint(e.getPoint()) (line 265)
  ↓
if (col == 3) → TRUE (Actions column) (line 266)
  ↓
int x = e.getX() - cellRect.x (line 270)
  ↓
if (x >= 50) → TRUE (clicked "Delete", right side of cell) (line 286)
  ↓
int confirm = JOptionPane.showConfirmDialog(...) (line 287)
  ↓
if (confirm == JOptionPane.YES_OPTION) → User clicked Yes (line 292)
  ↓
Retrieve credential ID (same lookup as Edit)
  ↓
Database.deleteCredential(credential.id) (line 293)
  ↓
Database: DELETE FROM credentials WHERE id=? (line 80)
  ↓
Database: stmt.executeUpdate() → Delete row
  ↓
loadCredentials() (line 294) → Refresh table
```

---

### **Password Generation Flow**

```
User clicks "Generate Password" button
  ↓
genBtn ActionListener executes (line 155)
  ↓
new PasswordGeneratorDialog(this) (line 156)
  ↓
PasswordGeneratorDialog constructor (line 20)
  ↓
Create configuration panel:
  ├─ JSpinner length (default 12)
  ├─ JCheckBox uppercase (checked)
  ├─ JCheckBox lowercase (checked)
  ├─ JCheckBox digits (checked)
  └─ JCheckBox symbols (unchecked)
  ↓
JOptionPane.showConfirmDialog() → Show config dialog (lines 36-37)
  ↓
User adjusts settings (e.g., length 16, check symbols)
  ↓
User clicks OK
  ↓
generatePassword(16, true, true, true, true) (line 40)
  ├─ StringBuilder pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$..." (lines 53-57)
  ├─ SecureRandom random = new SecureRandom() (line 63)
  ├─ for (int i = 0; i < 16; i++) (line 65)
  │   └─ password.append(pool.charAt(random.nextInt(pool.length())))
  └─ return password.toString() (line 68)
  ↓
Generated password: "A7$mK2pQx9!zB3Rt"
  ↓
copyToClipboard(pwd) (line 43) → Copy to clipboard
  ├─ Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(pwd), null) (line 73-74)
  └─ Password now in clipboard
  ↓
showGeneratedPassword(parent, pwd) (line 46)
  ↓
Display dialog showing password with buttons:
  ├─ [ Copy Again ] → copyToClipboard(pwd) again
  ├─ [ Check Strength ] → new StrengthCheckerDialog(parent, pwd).setVisible(true)
  └─ [ Close ] → d.dispose()
  ↓
User can now paste password into other applications
```

---

### **Password Strength Checker Flow**

```
User clicks "Check Strength" button
  ↓
strengthBtn ActionListener executes (line 164)
  ↓
new StrengthCheckerDialog(this, null).setVisible(true) (line 165)
  ↓
StrengthCheckerDialog constructor (line 13)
  ↓
Create UI:
  ├─ JPasswordField pf (empty)
  ├─ JCheckBox show (show/hide password)
  ├─ 5-segment meter (all gray)
  ├─ JLabel status ("Strength: ")
  └─ JTextArea suggestions (empty)
  ↓
Initialize timer and updater (lines 62-107)
  ↓
Add DocumentListener to password field (lines 110-131)
  ↓
updater.run() → Initialize display (line 134)
  ↓
Dialog is shown (lines 136-137)
  ↓
User types password: "Pass"
  ↓
DocumentListener.insertUpdate() fires (line 117)
  ↓
SwingUtilities.invokeLater(updater) (line 112)
  ↓
updater.run() (line 97)
  ├─ StrengthChecker.checkStrength("Pass") → WEAK (line 99)
  ├─ StrengthChecker.computeScore("Pass") → 2 (line 100)
  ├─ filled = round((2/6) * 5) = 2 segments (line 101)
  ├─ target[0] = 2 (line 102)
  ├─ anim.start() → Begin animation (line 103)
  ├─ status.setText("Strength: WEAK") (line 104)
  └─ Update suggestions (lines 106-112)
  ↓
Timer fires every 50ms (line 62)
  ├─ displayed[0]++ (0 → 1 → 2) (line 68)
  ├─ Update segment colors (lines 72-85)
  │   ├─ Segments 0-1: Red (target[0] < 2)
  │   └─ Segments 2-4: Gray (not filled)
  └─ if (displayed[0] == target[0]) anim.stop() (line 87)
  ↓
Meter shows: [🔴][🔴][   ][   ][   ]
  ↓
User continues typing: "Password123!"
  ↓
Each keystroke triggers updater
  ↓
Final state:
  ├─ Strength: STRONG
  ├─ Score: 5/6
  ├─ Filled: 4 segments
  └─ Meter: [🟢][🟢][🟢][🟢][   ]
```

---

### **Copy Username/Password Flow**

```
User clicks "Copy Username" button
  ↓
copyUserBtn ActionListener executes (line 172)
  ↓
int row = table.getSelectedRow() (line 173)
  ↓
if (row != -1) → TRUE (row is selected) (line 174)
  ↓
String username = (String) tableModel.getValueAt(row, 1) (line 175)
  ↓
StringSelection selection = new StringSelection(username) (line 176)
  ↓
Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null) (line 177)
  ↓
Username copied to clipboard
  ↓
Show success message (lines 178-179)
```

**Copy Password is similar but needs to retrieve actual password:**
```
User clicks "Copy Password" button
  ↓
copyPassBtn ActionListener executes (line 184)
  ↓
int row = table.getSelectedRow() (line 185)
  ↓
if (row != -1) → TRUE (line 186)
  ↓
Retrieve credential from database (need ID or unique identifier)
  ↓
Copy credential.password to clipboard (line 188)
  ↓
Show success message (lines 192-193)
```

---

### **Theme Toggle Flow**

```
User clicks "Toggle Theme" button
  ↓
themeBtn ActionListener executes (line 213)
  ↓
currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT (line 214)
  ↓
If was LIGHT → now DARK
If was DARK → now LIGHT
  ↓
applyTheme(currentTheme) (line 215)
  ↓
applyTheme() method executes (line 304)
  ├─ switch (theme) (line 305)
  ├─ case LIGHT:
  │   ├─ bgColor = Color.WHITE (line 307)
  │   ├─ fgColor = Color.BLACK (line 308)
  │   └─ ...
  ├─ case DARK:
  │   ├─ bgColor = new Color(45, 45, 48) (line 312)
  │   ├─ fgColor = Color.WHITE (line 313)
  │   └─ ...
  ├─ Apply colors to all components:
  │   ├─ getContentPane().setBackground(bgColor) (line 318)
  │   ├─ table.setBackground(bgColor) (line 319)
  │   ├─ table.setForeground(fgColor) (line 320)
  │   ├─ toolbar components (lines 323-328)
  │   └─ header (lines 332-333)
  └─ repaint() (line 335) → Redraw entire frame
  ↓
UI updates with new theme colors
```

---

### **Logout Flow**

```
User clicks "Logout" button
  ↓
logoutBtn ActionListener executes (line 201)
  ↓
currentUserId = -1 (line 202) → Clear user session
  ↓
dispose() (line 203) → Close main window
  ↓
showLogin() (line 204) → Show login dialog again
  ↓
Loop back to [Login/Registration Flow](#loginregistration-flow)
```

---

## **Database Operations**

### **Connection Management**

Every database operation follows this pattern:

```
Database method called (e.g., getAllCredentials(userId))
  ↓
Database.getConnection() (line 16)
  ├─ Class.forName("org.sqlite.JDBC") (line 107)
  ├─ Connection conn = DriverManager.getConnection("jdbc:sqlite:securevalut.db") (line 108)
  └─ return conn
  ↓
Method: try-with-resources block
  ├─ Connection conn = Database.getConnection() (line 52)
  ├─ PreparedStatement stmt = conn.prepareStatement(SQL) (line 53)
  ├─ Set parameters (stmt.setInt(), stmt.setString())
  ├─ Execute query/update (stmt.executeQuery() or stmt.executeUpdate())
  ├─ Process results (if query)
  └─ try-with-resources auto-closes stmt and conn
  ↓
Connection and statement automatically closed
```

**Why try-with-resources?**
- Automatically closes resources even if exception occurs
- Prevents database locks and memory leaks
- No need for explicit `finally` block

---

### **PreparedStatement Security**

**BAD (SQL Injection Vulnerable):**
```java
String sql = "SELECT * FROM credentials WHERE user_id = " + userId;
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);
```

**GOOD (Injection-Proof):**
```java
String sql = "SELECT * FROM credentials WHERE user_id = ?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setInt(1, userId);  // Parameter is safely escaped
ResultSet rs = stmt.executeQuery();
```

**How it works:**
1. SQL is compiled with placeholder `?`
2. Parameters are set separately using typed methods (`setInt`, `setString`)
3. Database driver escapes special characters automatically
4. Injected SQL code is treated as literal data, not executable code

---

## **Security Layer**

### **Password Hashing (Registration)**

```
User registers with username "alice" and password "SecurePass123!"
  ↓
UserManager.createUser("alice", "SecurePass123!")
  ↓
generateSalt() (line 33)
  ├─ SecureRandom random = new SecureRandom() (line 108)
  │   └─ Uses OS entropy sources: /dev/random, mouse movements, hardware noise
  ├─ byte[] salt = new byte[16] (line 109)
  │   └─ 16 bytes = 128 bits
  ├─ random.nextBytes(salt) (line 110)
  │   └─ salt = [0x3A, 0x7F, 0x9B, 0x2C, 0xE4, 0x51, 0xA8, 0xD6, 0x14, 0xC7, 0x5E, 0x82, 0xF0, 0x69, 0x3D, 0xB1]
  └─ return salt
  ↓
hashPassword("SecurePass123!", salt) (line 34)
  ├─ MessageDigest digest = MessageDigest.getInstance("SHA-256") (line 96)
  │   └─ Get SHA-256 algorithm implementation
  ├─ digest.update(salt) (line 97)
  │   └─ Add salt bytes to digest
  ├─ byte[] hashBytes = digest.digest(password.getBytes(UTF_8)) (line 98)
  │   ├─ Convert password string to bytes: [0x53, 0x65, 0x63, 0x75, 0x72, 0x65, ...]
  │   ├─ SHA-256 computes: hash(salt + password)
  │   └─ hashBytes = [0xA2, 0x8F, 0x3C, 0xD4, 0xE5, 0x76, 0x91, 0xB8, ...] (32 bytes)
  ├─ String encoded = Base64.getEncoder().encodeToString(hashBytes) (line 99)
  │   └─ encoded = "oo88zORXkbi..." (44 characters)
  └─ return encoded
  ↓
INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?) (line 37)
  ├─ setString(1, "alice")
  ├─ setString(2, "oo88zORXkbi...") → Base64-encoded hash
  └─ setString(3, "On+bLORRqNY...") → Base64-encoded salt
  ↓
Database stores:
  | id | username | password_hash                     | salt                         |
  |----|----------|-----------------------------------|------------------------------|
  | 5  | alice    | oo88zORXkbi...                    | On+bLORRqNY...               |
```

---

### **Password Verification (Login)**

```
User logs in with username "alice" and password "SecurePass123!"
  ↓
UserManager.authenticateUser("alice", "SecurePass123!")
  ↓
SELECT id, password_hash, salt FROM users WHERE username = ? (line 71)
  ├─ setString(1, "alice")
  └─ executeQuery()
  ↓
ResultSet rs:
  | id | password_hash     | salt          |
  |----|-------------------|---------------|
  | 5  | oo88zORXkbi...    | On+bLORRqNY...|
  ↓
Retrieve from database (lines 74-75):
  ├─ String storedHash = rs.getString("password_hash") → "oo88zORXkbi..."
  ├─ String saltStr = rs.getString("salt") → "On+bLORRqNY..."
  └─ byte[] salt = Base64.getDecoder().decode(saltStr) → [0x3A, 0x7F, 0x9B, ...]
  ↓
hashPassword("SecurePass123!", salt) (line 76)
  ├─ Uses SAME salt from database
  ├─ Computes: hash(salt + provided_password)
  └─ Returns: "oo88zORXkbi..." (same as stored)
  ↓
String computedHash = "oo88zORXkbi..."
  ↓
if (computedHash.equals(storedHash)) → TRUE (line 77)
  ├─ Passwords match
  ├─ return userId (line 79)
  └─ Login successful
  ↓
SecureVaultSwing: currentUserId = 5
  ↓
User authenticated, proceed to main window
```

**Wrong Password Example:**
```
User enters wrong password: "WrongPass456"
  ↓
hashPassword("WrongPass456", salt)
  └─ Returns: "xY7aB3cD9..." (completely different hash)
  ↓
if ("xY7aB3cD9...".equals("oo88zORXkbi...")) → FALSE
  ↓
return -1 (line 84) → Authentication failed
  ↓
SecureVaultSwing: currentUserId = -1
  ↓
Show error message, loop back to login
```

---

### **Why Salt + SHA-256?**

**Without Salt (Vulnerable):**
```
User A password: "password123" → hash: "ABC123..."
User B password: "password123" → hash: "ABC123..." (SAME!)
```
- Attacker uses rainbow table (precomputed hashes)
- Finds "ABC123..." → password is "password123"
- Cracks ALL users with that password at once

**With Salt (Secure):**
```
User A: salt1 + "password123" → hash: "DEF456..."
User B: salt2 + "password123" → hash: "XYZ789..." (DIFFERENT!)
```
- Attacker needs unique rainbow table for EACH salt
- 16-byte salt = 2^128 possible values = 340 undecillion variations
- Rainbow tables become impossible (storage required > atoms in universe)

---

## **UI Update Cycle**

### **Table Refresh Pattern**

```
Any credential modification (add/edit/delete)
  ↓
Database operation completes
  ↓
loadCredentials() called (line 253)
  ↓
tableModel.setRowCount(0) (line 254)
  ├─ Remove all rows from table model
  └─ Table displays empty
  ↓
Database.getAllCredentials(currentUserId) (line 255)
  ├─ Fetch fresh data from database
  └─ Return List<Credential>
  ↓
for (Credential c : credentials) loop (lines 256-261)
  ├─ StrengthChecker.checkStrength(c.password) → Get strength
  ├─ String strengthText = strength.name() → "WEAK", "MEDIUM", or "STRONG"
  └─ tableModel.addRow(new Object[]{
        c.title,           // Column 0
        c.username,        // Column 1
        "••••••••",        // Column 2 (masked password)
        "Edit | Delete",   // Column 3 (action links)
        strength           // Column 4 (strength enum for custom renderer)
      })
  ↓
Table repaints automatically (JTable detects model change)
  ↓
User sees updated table
```

---

### **Strength Renderer**

```
JTable needs to render a cell in the "Strength" column
  ↓
table.getColumnModel().getColumn(4).getCellRenderer() → StrengthRenderer (line 97)
  ↓
StrengthRenderer.getTableCellRendererComponent() called (line 342)
  ├─ value = table.getValueAt(row, column) → StrengthChecker.Strength enum
  ├─ JPanel panel = new JPanel(new BorderLayout()) (line 343)
  ├─ if (value instanceof StrengthChecker.Strength) (line 344)
  │   ├─ StrengthChecker.Strength s = (StrengthChecker.Strength) value (line 345)
  │   ├─ JPanel barPanel = new JPanel(new GridLayout(1, 10, 2, 0)) (line 346)
  │   ├─ int filled = (s == STRONG) ? 10 : (s == MEDIUM) ? 6 : 3 (lines 347-348)
  │   ├─ for (int i = 0; i < 10; i++) (line 349)
  │   │   ├─ JPanel seg = new JPanel() (line 350)
  │   │   ├─ if (i < filled) (line 351)
  │   │   │   ├─ Color col = (s == STRONG) ? green : (s == MEDIUM) ? yellow : red
  │   │   │   └─ seg.setBackground(col) (line 357)
  │   │   └─ else seg.setBackground(lightGray) (line 359)
  │   ├─ panel.add(barPanel, BorderLayout.CENTER) (line 361)
  │   └─ return panel (line 365)
  └─ JTable displays colored strength bar in cell
```

**Visual Result:**
```
┌────────┬──────────────┬──────────┬──────────────┬──────────────────┐
│ Title  │ Username     │ Password │ Actions      │ Strength         │
├────────┼──────────────┼──────────┼──────────────┼──────────────────┤
│ Gmail  │ user@...     │ ••••••••  │ Edit │Delete│ [🟢🟢🟢🟢🟢🟢🟢🟢🟢🟢]│ ← STRONG
│ GitHub │ alice        │ ••••••••  │ Edit │Delete│ [🟡🟡🟡🟡🟡🟡      ]│ ← MEDIUM
│ WiFi   │ admin        │ ••••••••  │ Edit │Delete│ [🔴🔴🔴          ]│ ← WEAK
└────────┴──────────────┴──────────┴──────────────┴──────────────────┘
```

---

## **Shutdown Flow**

### **Normal Exit (Close Button)**

```
User clicks window close button (X)
  ↓
WindowListener.windowClosing() event (handled by default)
  ↓
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE) (line 83)
  ↓
frame.dispose() → Release window resources
  ↓
System.exit(0) → Terminate JVM
  ↓
Database connections closed (try-with-resources)
  ↓
Application terminates
```

---

### **Logout and Re-login**

```
User clicks "Logout" button
  ↓
currentUserId = -1 (line 202) → Session cleared
  ↓
dispose() (line 203) → Main window closes
  ↓
showLogin() (line 204) → New login loop starts
  ↓
Previous window destroyed, new LoginDialog shown
  ↓
User can log in as different user
```

---

## **Complete Call Graph**

### **Startup Sequence**

```
main()
  └─ new SecureVaultSwing()
       └─ constructor
            ├─ db = new Database()
            │    └─ Database.initializeTables()
            │         └─ CREATE TABLE IF NOT EXISTS...
            ├─ userManager = new UserManager(db)
            ├─ showLogin() ◄──────────┐
            │    ├─ new LoginDialog()   │
            │    ├─ setVisible(true)    │
            │    ├─ [User interacts]    │
            │    └─ getUsername(), getPassword()
            │                           │
            ├─ while (!loginSuccess) ───┘
            │    ├─ if createNewUser:
            │    │    ├─ UserManager.createUser()
            │    │    │    ├─ generateSalt()
            │    │    │    ├─ hashPassword()
            │    │    │    └─ Database.insert()
            │    │    └─ UserManager.authenticateUser()
            │    │         ├─ Database.select()
            │    │         ├─ hashPassword()
            │    │         └─ compare hashes
            │    └─ else:
            │         └─ UserManager.authenticateUser()
            │
            └─ buildUI()
                 ├─ createTableModel()
                 ├─ createTable()
                 │    └─ setRenderer(StrengthRenderer)
                 ├─ createToolbar()
                 │    ├─ addBtn → CredentialDialog
                 │    ├─ deleteBtn → Database.delete()
                 │    ├─ genBtn → PasswordGeneratorDialog
                 │    ├─ strengthBtn → StrengthCheckerDialog
                 │    ├─ copyUserBtn → clipboard
                 │    ├─ copyPassBtn → clipboard
                 │    ├─ logoutBtn → showLogin()
                 │    └─ themeBtn → applyTheme()
                 ├─ setLayout()
                 ├─ applyTheme()
                 ├─ loadCredentials()
                 │    ├─ Database.getAllCredentials()
                 │    └─ StrengthChecker.checkStrength()
                 └─ setVisible(true)
```

---

### **User Operation Flows**

**Add Credential:**
```
addBtn.click
  └─ CredentialDialog.showDialog()
       ├─ create UI with live strength meter
       ├─ DocumentListener → StrengthChecker
       ├─ user submits
       └─ return Credential
            └─ Database.insertCredential()
                 └─ loadCredentials()
```

**Edit Credential:**
```
table.doubleClick / Edit link click
  └─ CredentialDialog.showDialog(existing)
       ├─ pre-fill fields
       ├─ user edits
       └─ return edited Credential
            └─ Database.updateCredential()
                 └─ loadCredentials()
```

**Delete Credential:**
```
Delete link click
  └─ JOptionPane.showConfirmDialog()
       ├─ user confirms
       └─ Database.deleteCredential()
            └─ loadCredentials()
```

**Generate Password:**
```
genBtn.click
  └─ PasswordGeneratorDialog
       ├─ show config dialog
       ├─ generatePassword()
       │    ├─ SecureRandom
       │    └─ build password
       ├─ copyToClipboard()
       └─ showGeneratedPassword()
            └─ [Check Strength] → StrengthCheckerDialog
```

**Check Strength:**
```
strengthBtn.click
  └─ StrengthCheckerDialog
       ├─ create UI with meter
       ├─ DocumentListener
       │    └─ updater
       │         ├─ StrengthChecker.checkStrength()
       │         ├─ StrengthChecker.computeScore()
       │         └─ StrengthChecker.getSuggestions()
       └─ Timer animation
            └─ update segments
```

---

## **Data Flow Diagram**

```
┌──────────┐
│   USER   │
└────┬─────┘
     │
     │ types password
     ▼
┌─────────────────────────┐
│  LoginDialog /          │
│  CredentialDialog /     │
│  StrengthCheckerDialog  │
└────┬────────────────────┘
     │
     │ plaintext password
     ▼
┌─────────────────────────┐
│  UserManager /          │
│  StrengthChecker        │
└────┬─────────┬──────────┘
     │         │
     │ hash    │ strength analysis
     │         │
     ▼         ▼
┌─────────────────────────┐
│  Database (SQLite)      │
│  securevalut.db         │
│                         │
│  users table:           │
│  - id                   │
│  - username             │
│  - password_hash        │
│  - salt                 │
│                         │
│  credentials table:     │
│  - id                   │
│  - user_id (FK)         │
│  - title                │
│  - username             │
│  - password (plaintext) │
└────┬────────────────────┘
     │
     │ query results
     ▼
┌─────────────────────────┐
│  SecureVaultSwing       │
│  - JTable               │
│  - StrengthRenderer     │
└────┬────────────────────┘
     │
     │ visual display
     ▼
┌──────────┐
│   USER   │
└──────────┘
```

---

## **Thread and Timing Analysis**

### **Main Thread (Event Dispatch Thread)**

All UI operations run on the EDT:
- Button clicks
- Table updates
- Dialog displays
- Painting/rendering

### **DocumentListener Thread**

DocumentListener callbacks run on the document's thread (not EDT), so we use `SwingUtilities.invokeLater()` to safely update UI:

```
User types character
  ↓
[Document Thread] DocumentListener.insertUpdate()
  ↓
SwingUtilities.invokeLater(() -> {
    [EDT] Update UI components
    [EDT] Call StrengthChecker
    [EDT] Repaint meter
})
```

### **Animation Timer**

`javax.swing.Timer` automatically runs on EDT:

```
Timer.start()
  ↓
[Timer Thread] Wait 50ms
  ↓
[EDT] Timer.actionPerformed()
  ├─ Update segment colors
  └─ repaint()
```

### **Database Operations**

Database operations run synchronously on EDT (blocking):

```
[EDT] User clicks Add button
  ↓
[EDT] Show CredentialDialog (blocks EDT)
  ↓
[EDT] User clicks OK
  ↓
[EDT] Database.insertCredential() (blocks while writing)
  ↓
[EDT] loadCredentials() (blocks while reading)
  ↓
[EDT] Update table model
  ↓
[EDT] Repaint table
```

**Note:** For large databases, this could freeze UI. Production apps use background threads (SwingWorker) for database operations.

---

## **Summary of Execution Flow**

1. **Application starts** → `main()` creates `SecureVaultSwing` instance
2. **Login loop** → User authenticates or creates account (infinite loop until success)
3. **Database initialization** → Tables created if not exists
4. **Authentication** → SHA-256 hash with 16-byte salt
5. **Main window builds** → Toolbar, table, initial data load
6. **User interactions** → Button clicks trigger operations
7. **Database operations** → All use PreparedStatement for security
8. **UI updates** → loadCredentials() + table repaint
9. **Real-time feedback** → DocumentListener + Timer animation
10. **Logout** → Clear session, loop back to login
11. **Exit** → Close window, terminate JVM

**Total Classes:** 8
**Total Methods:** ~50+
**LOC:** ~1800 lines
**Database Tables:** 2 (users, credentials)
**Security:** SHA-256 + salt, PreparedStatement, masked passwords
**UI Components:** JFrame, JDialog, JTable, JToolBar, custom renderers
**Animation:** Timer-based 5-segment strength meter

---

## **Key Design Patterns**

1. **MVC (Model-View-Controller):**
   - Model: Database + Credential data
   - View: JTable + Dialogs
   - Controller: SecureVaultSwing + listeners

2. **Singleton-ish Database:** One Database instance shared via UserManager

3. **Factory Methods:** `Database.getConnection()` creates connections on-demand

4. **Observer Pattern:** DocumentListener observes text changes, triggers updates

5. **Strategy Pattern:** Different authentication strategies (login vs create)

6. **Command Pattern:** Button listeners encapsulate actions

7. **Template Method:** All database operations follow try-with-resources pattern

8. **Decorator Pattern:** StrengthRenderer decorates table cells with visual bars

This completes the comprehensive execution flow documentation!

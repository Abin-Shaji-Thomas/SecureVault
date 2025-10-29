# 🚀 SecureVault Production Roadmap

## **Complete Guide to Transform SecureVault into a Production-Ready Product**

---

## 📊 **Current Project Analysis**

### ✅ **What You Have (Strengths):**
1. **Solid Core Functionality:**
   - Multi-user authentication with SHA-256 hashing
   - Full CRUD operations for credentials
   - Password strength checker
   - Password generator
   - Duplicate prevention validation
   - SQLite database with proper schema

2. **Good UI/UX:**
   - Clean Swing GUI
   - Dark/Light theme support
   - Interactive table with sorting
   - Context menus
   - Real-time password strength visualization

3. **Security Features:**
   - Password hashing with salt
   - Prepared statements (SQL injection prevention)
   - Secure random password generation
   - Password masking

4. **Documentation:**
   - Comprehensive code explanations
   - Architecture documentation
   - Future enhancements roadmap
   - Quick start guides

### ⚠️ **Critical Gaps (What's Missing for Production):**

#### **1. Security Vulnerabilities:**
- ❌ Passwords stored in **PLAIN TEXT** in database (CRITICAL!)
- ❌ No encryption at rest
- ❌ No master password concept
- ❌ No session timeout
- ❌ No protection against memory dumps
- ❌ No secure clipboard clearing
- ❌ Database file easily readable by anyone

#### **2. Production Infrastructure:**
- ❌ No proper build system (Maven/Gradle)
- ❌ No executable JAR packaging
- ❌ No installer (Windows .exe, macOS .dmg, Linux .deb/.rpm)
- ❌ No auto-update mechanism
- ❌ No crash reporting
- ❌ No analytics/telemetry

#### **3. Quality Assurance:**
- ❌ No unit tests
- ❌ No integration tests
- ❌ No automated testing
- ❌ No code coverage reports
- ❌ No CI/CD pipeline

#### **4. User Experience:**
- ❌ No backup/restore functionality
- ❌ No import/export (CSV, JSON)
- ❌ No search functionality
- ❌ No password history
- ❌ No categories/tags
- ❌ No favorites/pinning
- ❌ No keyboard shortcuts

#### **5. Business/Legal:**
- ❌ No license file
- ❌ No terms of service
- ❌ No privacy policy
- ❌ No GDPR compliance
- ❌ No security audit
- ❌ No user manual

#### **6. Deployment:**
- ❌ No versioning system
- ❌ No update notifications
- ❌ No database migration strategy
- ❌ No configuration management
- ❌ No logging system

---

## 🎯 **Production Roadmap (Phases)**

---

## **PHASE 1: Critical Security Fixes** ⚡ (MUST DO FIRST - 1-2 Weeks)

### **Priority: CRITICAL 🔴**

### **1.1 Implement Password Encryption in Database**

**Problem:** Currently passwords are stored as plain text in SQLite. If someone accesses `securevault.db`, they can read all passwords!

**Solution:** Encrypt passwords using AES-256 before storing them.

#### **Implementation Steps:**

```java
// Add to Database.java
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class PasswordEncryption {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Derive encryption key from user's master password
    public static SecretKey deriveKey(String masterPassword, byte[] salt) throws Exception {
        // Use PBKDF2 to derive strong key from master password
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(
            masterPassword.toCharArray(), 
            salt, 
            100000,  // iterations
            256      // key length
        );
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
    
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        
        // Generate random IV
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));
        
        // Combine IV + encrypted data
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
        
        return Base64.getEncoder().encodeToString(combined);
    }
    
    public static String decrypt(String ciphertext, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(ciphertext);
        
        // Extract IV and encrypted data
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[combined.length - 16];
        System.arraycopy(combined, 0, iv, 0, 16);
        System.arraycopy(combined, 16, encrypted, 0, encrypted.length);
        
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(encrypted);
        
        return new String(decrypted, "UTF-8");
    }
}
```

**Modify Database.java:**
```java
public void insertCredential(int userId, String title, String username, String password) throws SQLException {
    // ... duplicate check ...
    
    // ENCRYPT password before storing
    String encryptedPassword = PasswordEncryption.encrypt(password, userEncryptionKey);
    
    String sql = "INSERT INTO credentials (user_id, title, username, password) VALUES (?, ?, ?, ?)";
    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        pstmt.setInt(1, userId);
        pstmt.setString(2, title);
        pstmt.setString(3, username);
        pstmt.setString(4, encryptedPassword);  // Store encrypted
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
            String encryptedPassword = rs.getString("password");
            // DECRYPT password when retrieving
            String decryptedPassword = PasswordEncryption.decrypt(encryptedPassword, userEncryptionKey);
            
            credentials.add(new Credential(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("username"),
                decryptedPassword  // Return decrypted password
            ));
        }
    }
    return credentials;
}
```

**Master Password Flow:**
```java
// Add to LoginDialog.java
public class LoginDialog extends JDialog {
    private JPasswordField masterPasswordField;
    
    private void onLoginSuccess(String username, String password) {
        // Derive encryption key from user's password
        byte[] salt = getUserSalt(username);
        SecretKey encryptionKey = PasswordEncryption.deriveKey(password, salt);
        
        // Store key in session for encrypting/decrypting passwords
        SecureVaultSwing.setUserEncryptionKey(encryptionKey);
        
        // Continue with normal login
    }
}
```

**Checklist:**
- [ ] Create `PasswordEncryption.java` utility class
- [ ] Modify `insertCredential()` to encrypt passwords
- [ ] Modify `updateCredential()` to encrypt passwords
- [ ] Modify `getAllCredentials()` to decrypt passwords
- [ ] Add master password derivation in login flow
- [ ] Test with existing and new credentials
- [ ] Create database migration script to encrypt existing passwords

---

### **1.2 Implement Session Timeout**

**Problem:** App stays unlocked indefinitely. If user walks away, anyone can access passwords.

**Solution:** Auto-lock after 5 minutes of inactivity.

```java
// Add to SecureVaultSwing.java
private Timer sessionTimer;
private static final int SESSION_TIMEOUT = 5 * 60 * 1000; // 5 minutes

private void initSessionTimeout() {
    sessionTimer = new Timer(SESSION_TIMEOUT, e -> {
        lockVault();
    });
    sessionTimer.setRepeats(false);
    sessionTimer.start();
    
    // Reset timer on any user activity
    addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            resetSessionTimer();
        }
    });
    addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            resetSessionTimer();
        }
    });
}

private void resetSessionTimer() {
    sessionTimer.restart();
}

private void lockVault() {
    // Clear encryption key from memory
    clearEncryptionKey();
    
    // Show login dialog again
    dispose();
    new LoginDialog(null).setVisible(true);
}
```

**Checklist:**
- [ ] Add session timeout timer
- [ ] Reset timer on mouse/keyboard activity
- [ ] Clear sensitive data from memory on lock
- [ ] Show lock icon in UI
- [ ] Add manual lock button
- [ ] Add settings to configure timeout duration

---

### **1.3 Secure Clipboard Management**

**Problem:** Copied passwords stay in clipboard forever. Anyone can paste them later.

**Solution:** Auto-clear clipboard after 30 seconds.

```java
// Add to SecureVaultSwing.java
private void copyToClipboard(String text, boolean sensitive) {
    StringSelection selection = new StringSelection(text);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(selection, null);
    
    if (sensitive) {
        // Clear clipboard after 30 seconds
        Timer clearTimer = new Timer(30000, e -> {
            try {
                // Only clear if our content is still there
                String current = (String) clipboard.getData(DataFlavor.stringFlavor);
                if (text.equals(current)) {
                    clipboard.setContents(new StringSelection(""), null);
                    showMessage("Clipboard cleared for security");
                }
            } catch (Exception ex) {
                // Ignore errors
            }
        });
        clearTimer.setRepeats(false);
        clearTimer.start();
        
        showMessage("Copied! Will clear in 30 seconds");
    }
}
```

**Checklist:**
- [ ] Implement auto-clear clipboard
- [ ] Add visual countdown timer
- [ ] Add settings to configure clear time
- [ ] Show notification when cleared
- [ ] Clear on app close/lock

---

### **1.4 Database Encryption at Rest**

**Problem:** `securevault.db` file is readable by anyone with file access.

**Solution:** Use SQLCipher (encrypted SQLite).

```bash
# Replace sqlite-jdbc with sqlcipher in lib/
wget https://repo1.maven.org/maven2/net/zetetic/android-database-sqlcipher/4.5.4/android-database-sqlcipher-4.5.4.jar
```

```java
// Modify Database.java
private static final String DB_URL = "jdbc:sqlite:securevault.db";

private void connect(String password) throws SQLException {
    connection = DriverManager.getConnection(DB_URL);
    
    // Enable SQLCipher encryption
    try (Statement stmt = connection.createStatement()) {
        stmt.execute("PRAGMA key = '" + password + "';");
        stmt.execute("PRAGMA cipher_page_size = 4096;");
        stmt.execute("PRAGMA kdf_iter = 256000;");
    }
}
```

**Checklist:**
- [ ] Add SQLCipher dependency
- [ ] Encrypt database with master password
- [ ] Test database encryption
- [ ] Create migration script for existing databases
- [ ] Handle wrong password errors

---

## **PHASE 2: Build & Distribution** 📦 (Week 3-4)

### **2.1 Setup Maven/Gradle Build System**

**Currently:** Using basic `javac` and shell scripts. Not scalable.

**Solution:** Use Maven for dependency management and building.

Create `pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.securevault</groupId>
    <artifactId>securevault</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>SecureVault</name>
    <description>Secure Password Manager</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
    </properties>

    <dependencies>
        <!-- SQLite JDBC -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.44.1.0</version>
        </dependency>

        <!-- SLF4J -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.9</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>2.0.9</version>
        </dependency>

        <!-- JUnit for testing -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <sourceDirectory>src</sourceDirectory>
        <plugins>
            <!-- Compile plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>

            <!-- Create executable JAR with dependencies -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>SecureVaultSwing</mainClass>
                                </transformer>
                            </transformers>
                            <finalName>SecureVault-${project.version}</finalName>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

**Restructure project:**
```
SecureVault/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── securevault/
│   │   │           ├── SecureVaultSwing.java
│   │   │           ├── Database.java
│   │   │           ├── UserManager.java
│   │   │           └── ...
│   │   └── resources/
│   │       ├── icons/
│   │       ├── images/
│   │       └── config/
│   └── test/
│       └── java/
│           └── com/
│               └── securevault/
│                   ├── DatabaseTest.java
│                   ├── UserManagerTest.java
│                   └── ...
├── target/
├── docs/
└── README.md
```

**Build commands:**
```bash
# Build
mvn clean package

# Run
java -jar target/SecureVault-1.0.0.jar

# Run tests
mvn test

# Generate documentation
mvn javadoc:javadoc
```

**Checklist:**
- [ ] Create `pom.xml`
- [ ] Restructure to Maven standard layout
- [ ] Move source files to `src/main/java/com/securevault/`
- [ ] Add package declarations to all Java files
- [ ] Test Maven build
- [ ] Create executable JAR
- [ ] Update documentation with new build process

---

### **2.2 Create Native Installers**

**Solution:** Use jpackage (Java 14+) to create platform-specific installers.

**Windows Installer (.exe):**
```bash
jpackage \
  --input target/ \
  --name SecureVault \
  --main-jar SecureVault-1.0.0.jar \
  --main-class com.securevault.SecureVaultSwing \
  --type exe \
  --icon resources/icons/securevault.ico \
  --win-menu \
  --win-shortcut \
  --win-dir-chooser \
  --app-version 1.0.0 \
  --vendor "SecureVault" \
  --description "Secure Password Manager" \
  --copyright "Copyright © 2025 SecureVault"
```

**macOS Installer (.dmg):**
```bash
jpackage \
  --input target/ \
  --name SecureVault \
  --main-jar SecureVault-1.0.0.jar \
  --main-class com.securevault.SecureVaultSwing \
  --type dmg \
  --icon resources/icons/securevault.icns \
  --mac-package-name SecureVault \
  --app-version 1.0.0
```

**Linux Installer (.deb):**
```bash
jpackage \
  --input target/ \
  --name securevault \
  --main-jar SecureVault-1.0.0.jar \
  --main-class com.securevault.SecureVaultSwing \
  --type deb \
  --icon resources/icons/securevault.png \
  --linux-menu-group "Utilities" \
  --app-version 1.0.0
```

**Checklist:**
- [ ] Create icons for all platforms (.ico, .icns, .png)
- [ ] Test jpackage on Windows
- [ ] Test jpackage on macOS
- [ ] Test jpackage on Linux
- [ ] Add installer to GitHub releases
- [ ] Create installation guide

---

### **2.3 Version Management & Auto-Update**

**Add version checking:**
```java
// Version.java
public class Version {
    public static final String CURRENT = "1.0.0";
    public static final String UPDATE_URL = "https://api.securevault.com/version";
    
    public static boolean checkForUpdates() {
        try {
            URL url = new URL(UPDATE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String latestVersion = in.readLine();
            in.close();
            
            return !CURRENT.equals(latestVersion);
        } catch (Exception e) {
            return false;
        }
    }
}

// In SecureVaultSwing.java
private void checkForUpdates() {
    SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
        protected Boolean doInBackground() {
            return Version.checkForUpdates();
        }
        
        protected void done() {
            try {
                if (get()) {
                    int response = JOptionPane.showConfirmDialog(
                        this,
                        "A new version of SecureVault is available. Download now?",
                        "Update Available",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().browse(new URI("https://securevault.com/download"));
                    }
                }
            } catch (Exception e) {
                // Ignore update check failures
            }
        }
    };
    worker.execute();
}
```

**Checklist:**
- [ ] Add version tracking
- [ ] Implement update checker
- [ ] Create update server/API
- [ ] Add "Check for Updates" menu item
- [ ] Show update notifications
- [ ] Add release notes display

---

## **PHASE 3: Testing & Quality** 🧪 (Week 5-6)

### **3.1 Unit Tests**

Create test files:

```java
// src/test/java/com/securevault/UserManagerTest.java
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager userManager;
    private Connection testConn;
    
    @BeforeEach
    void setUp() throws Exception {
        // Create in-memory test database
        testConn = DriverManager.getConnection("jdbc:sqlite::memory:");
        userManager = new UserManager(testConn);
    }
    
    @Test
    void testCreateUser() throws Exception {
        boolean created = userManager.createUser("testuser", "TestPass123!");
        assertTrue(created);
        assertTrue(userManager.authenticateUser("testuser", "TestPass123!"));
    }
    
    @Test
    void testDuplicateUsername() throws Exception {
        userManager.createUser("testuser", "Pass123!");
        assertThrows(SQLException.class, () -> {
            userManager.createUser("testuser", "DifferentPass");
        });
    }
    
    @Test
    void testWrongPassword() throws Exception {
        userManager.createUser("testuser", "CorrectPass");
        assertFalse(userManager.authenticateUser("testuser", "WrongPass"));
    }
    
    @AfterEach
    void tearDown() throws Exception {
        testConn.close();
    }
}
```

```java
// DatabaseTest.java
public class DatabaseTest {
    @Test
    void testInsertCredential() throws Exception {
        // Test credential insertion
    }
    
    @Test
    void testDuplicatePrevention() throws Exception {
        // Test duplicate blocking
    }
    
    @Test
    void testEncryption() throws Exception {
        // Test password encryption/decryption
    }
}
```

```java
// StrengthCheckerTest.java
public class StrengthCheckerTest {
    @Test
    void testWeakPassword() {
        assertEquals(0, StrengthChecker.checkStrength("abc"));
    }
    
    @Test
    void testStrongPassword() {
        assertTrue(StrengthChecker.checkStrength("MyP@ssw0rd123!") >= 4);
    }
}
```

**Checklist:**
- [ ] Write tests for all core classes
- [ ] Achieve 80%+ code coverage
- [ ] Add CI/CD to run tests automatically
- [ ] Fix all failing tests
- [ ] Add integration tests

---

### **3.2 Continuous Integration (GitHub Actions)**

Create `.github/workflows/build.yml`:

```yaml
name: Build and Test

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ${{ matrix.os }}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest, macos-latest]
        java: [21]

    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK ${{ matrix.java }}
      uses: actions/setup-java@v3
      with:
        java-version: ${{ matrix.java }}
        distribution: 'temurin'
    
    - name: Cache Maven packages
      uses: actions/cache@v3
      with:
        path: ~/.m2
        key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
    
    - name: Build with Maven
      run: mvn clean package
    
    - name: Run tests
      run: mvn test
    
    - name: Generate coverage report
      run: mvn jacoco:report
    
    - name: Upload artifacts
      uses: actions/upload-artifact@v3
      with:
        name: SecureVault-${{ matrix.os }}
        path: target/*.jar
```

**Checklist:**
- [ ] Create GitHub Actions workflow
- [ ] Test on all platforms (Windows, macOS, Linux)
- [ ] Add code coverage reporting
- [ ] Add automated releases
- [ ] Add security scanning

---

## **PHASE 4: Enhanced Features** ✨ (Week 7-10)

### **4.1 Search & Filter**

```java
// Add to SecureVaultSwing.java
private JTextField searchField;

private void initSearchBar() {
    searchField = new JTextField(20);
    searchField.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { filterTable(); }
        public void removeUpdate(DocumentEvent e) { filterTable(); }
        public void changedUpdate(DocumentEvent e) { filterTable(); }
    });
}

private void filterTable() {
    String searchText = searchField.getText().toLowerCase();
    TableRowSorter<VaultTableModel> sorter = new TableRowSorter<>(tableModel);
    table.setRowSorter(sorter);
    
    if (searchText.isEmpty()) {
        sorter.setRowFilter(null);
    } else {
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
    }
}
```

---

### **4.2 Backup & Restore**

```java
public class BackupManager {
    public static void createBackup(String dbPath, String backupPath) throws IOException {
        Files.copy(
            Paths.get(dbPath),
            Paths.get(backupPath),
            StandardCopyOption.REPLACE_EXISTING
        );
    }
    
    public static void autoBackup() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String backupPath = "backups/securevault_" + timestamp + ".db";
        createBackup("securevault.db", backupPath);
    }
}
```

---

### **4.3 Import/Export**

```java
public class ImportExport {
    public static void exportToCSV(List<Credential> credentials, String path) {
        try (PrintWriter writer = new PrintWriter(path)) {
            writer.println("Title,Username,Password,Strength");
            for (Credential c : credentials) {
                writer.printf("%s,%s,%s,%d%n",
                    c.title, c.username, c.password, c.strength);
            }
        }
    }
    
    public static List<Credential> importFromCSV(String path) {
        // Parse CSV and return credentials
    }
}
```

---

### **4.4 Categories & Tags**

Modify database schema:
```sql
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    color TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE credentials ADD COLUMN category_id INTEGER REFERENCES categories(id);

CREATE TABLE tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT UNIQUE NOT NULL
);

CREATE TABLE credential_tags (
    credential_id INTEGER,
    tag_id INTEGER,
    PRIMARY KEY (credential_id, tag_id),
    FOREIGN KEY (credential_id) REFERENCES credentials(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);
```

---

### **4.5 Password History**

```sql
CREATE TABLE password_history (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    credential_id INTEGER NOT NULL,
    old_password TEXT NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (credential_id) REFERENCES credentials(id)
);
```

```java
public void updateCredential(int id, String title, String username, String newPassword) {
    // Save old password to history
    String oldPassword = getCredentialById(id).password;
    saveToHistory(id, oldPassword);
    
    // Update with new password
    // ...
}
```

---

## **PHASE 5: Advanced Security** 🔒 (Week 11-12)

### **5.1 Two-Factor Authentication (2FA)**

Use TOTP (Time-based One-Time Password):

```java
// Add dependency
<dependency>
    <groupId>dev.samstevens.totp</groupId>
    <artifactId>totp</artifactId>
    <version>1.7.1</version>
</dependency>

// 2FA Setup
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.qr.*;
import dev.samstevens.totp.secret.*;

public class TwoFactorAuth {
    public static String generateSecret() {
        SecretGenerator generator = new DefaultSecretGenerator();
        return generator.generate();
    }
    
    public static String generateQRCode(String secret, String username) {
        QrDataFactory qrDataFactory = new QrDataFactory();
        String qrData = qrDataFactory.newBuilder()
            .label(username)
            .secret(secret)
            .issuer("SecureVault")
            .build()
            .getUri();
        return qrData;
    }
    
    public static boolean verifyCode(String secret, String code) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }
}
```

---

### **5.2 Breach Detection**

Use Have I Been Pwned API:

```java
public class BreachChecker {
    public static boolean isPasswordBreached(String password) throws Exception {
        // Hash password with SHA-1
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        String sha1 = bytesToHex(hash).toUpperCase();
        
        // Use k-anonymity: send only first 5 chars
        String prefix = sha1.substring(0, 5);
        String suffix = sha1.substring(5);
        
        // Query HIBP API
        URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith(suffix)) {
                in.close();
                return true; // Password found in breach
            }
        }
        in.close();
        return false; // Password not found
    }
}
```

---

## **PHASE 6: Distribution & Marketing** 📢 (Week 13-14)

### **6.1 Website & Landing Page**

Create marketing website:
- Home page with features
- Download page for all platforms
- Documentation & tutorials
- Blog with security tips
- FAQ section

### **6.2 App Store Distribution**

**Microsoft Store:**
- Create developer account
- Package as MSIX
- Submit for review

**Mac App Store:**
- Apple Developer Account ($99/year)
- Code signing certificate
- Submit via App Store Connect

**Snap Store (Linux):**
```bash
snapcraft
snapcraft upload --release=stable securevault_1.0.0_amd64.snap
```

### **6.3 Open Source Strategy**

**GitHub Repository:**
- Clean up code
- Add comprehensive README
- Create CONTRIBUTING.md
- Add CODE_OF_CONDUCT.md
- Choose license (MIT, GPL, Apache 2.0)
- Create issue templates
- Add GitHub Discussions

**Marketing:**
- Post on Reddit (r/privacy, r/selfhosted)
- Product Hunt launch
- Hacker News Show HN
- Twitter/X announcement
- YouTube demo video

---

## **PHASE 7: Legal & Compliance** ⚖️ (Week 15)

### **7.1 Legal Documents**

Create these files:

**LICENSE.txt:**
```
MIT License

Copyright (c) 2025 SecureVault

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

**PRIVACY_POLICY.md:**
- What data is collected (none, local-only)
- How data is stored (encrypted locally)
- No cloud sync, no telemetry
- User rights (GDPR compliance)

**TERMS_OF_SERVICE.md:**
- Acceptable use policy
- Disclaimer of warranties
- Limitation of liability
- User responsibilities

**SECURITY.md:**
- Responsible disclosure policy
- How to report vulnerabilities
- Security best practices
- Encryption details

---

## **Quick Implementation Priority List**

### **🔴 Critical (Do Immediately):**
1. ✅ Encrypt passwords in database (AES-256)
2. ✅ Implement session timeout
3. ✅ Secure clipboard clearing
4. ✅ Database encryption at rest (SQLCipher)

### **🟡 High Priority (Within 2 weeks):**
5. ✅ Maven/Gradle build system
6. ✅ Unit tests
7. ✅ Native installers (jpackage)
8. ✅ Version management
9. ✅ Backup/restore functionality

### **🟢 Medium Priority (Within 1 month):**
10. ✅ Search & filter
11. ✅ Import/Export (CSV, JSON)
12. ✅ Categories & tags
13. ✅ Password history
14. ✅ CI/CD pipeline

### **🔵 Nice to Have (Future):**
15. ✅ 2FA support
16. ✅ Breach detection
17. ✅ Browser extension
18. ✅ Mobile app
19. ✅ Cloud sync

---

## **Resources & Tools You'll Need**

### **Development:**
- ✅ Maven or Gradle
- ✅ JUnit 5 for testing
- ✅ JaCoCo for code coverage
- ✅ SonarQube for code quality
- ✅ GitHub Actions for CI/CD

### **Security:**
- ✅ SQLCipher for database encryption
- ✅ Bouncy Castle for advanced crypto
- ✅ OWASP Dependency Check
- ✅ Snyk for vulnerability scanning

### **Distribution:**
- ✅ jpackage (Java 14+)
- ✅ Install4j (commercial installer tool)
- ✅ Docker for containerization
- ✅ Chocolatey (Windows package manager)
- ✅ Homebrew (macOS package manager)

### **Monitoring:**
- ✅ Sentry for crash reporting
- ✅ Mixpanel/Plausible for analytics (optional)
- ✅ GitHub Insights for usage stats

---

## **Budget Estimate**

### **Free / Open Source Route:**
- Development: $0 (your time)
- GitHub: Free (public repos)
- CI/CD: Free (GitHub Actions)
- Documentation: Free (GitHub Pages)
- **Total: $0**

### **Professional Route:**
- Domain: $15/year
- Hosting: $10/month ($120/year)
- Apple Developer: $99/year
- Microsoft Store: $19 one-time
- Code signing certificate: $100/year
- Marketing: $500-1000
- **Total: ~$1000 first year, ~$350/year after**

---

## **Timeline Summary**

| Phase | Duration | Key Deliverables |
|-------|----------|------------------|
| Phase 1: Security | 2 weeks | Encrypted passwords, session timeout |
| Phase 2: Build | 2 weeks | Maven, installers, auto-update |
| Phase 3: Testing | 2 weeks | Unit tests, CI/CD, 80% coverage |
| Phase 4: Features | 4 weeks | Search, backup, import/export, tags |
| Phase 5: Advanced Security | 2 weeks | 2FA, breach detection |
| Phase 6: Distribution | 2 weeks | Website, app stores, marketing |
| Phase 7: Legal | 1 week | License, privacy policy, ToS |
| **TOTAL** | **15 weeks** | **Production-ready product** |

---

## **Success Metrics**

After completing this roadmap, you'll have:

✅ **Security:**
- Passwords encrypted with AES-256
- Database encrypted at rest
- Session timeout protection
- Secure clipboard management
- No plain text storage

✅ **Quality:**
- 80%+ test coverage
- Automated testing on every commit
- Cross-platform compatibility
- Zero critical bugs

✅ **Distribution:**
- Native installers for Windows, macOS, Linux
- Auto-update mechanism
- Professional branding
- App store presence

✅ **Features:**
- All current features preserved
- Search & filter
- Backup/restore
- Import/export
- Categories & tags
- Password history

✅ **Legal:**
- Open source license
- Privacy policy
- Terms of service
- Security policy

---

## **Next Steps - Let's Start! 🚀**

I'm ready to help you implement this roadmap step by step. Let's start with the most critical security fixes.

**Which phase would you like to tackle first?**

1. **Phase 1** - Critical Security (password encryption) ← I recommend starting here
2. **Phase 2** - Build System (Maven setup)
3. **Phase 3** - Testing (unit tests)
4. Something else?

Just tell me and I'll guide you through the implementation!

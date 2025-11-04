# Architecture Documentation - SecureVault Pro

Technical architecture and design documentation for developers.

---

## 📋 Table of Contents

- [System Overview](#system-overview)
- [Architecture Diagram](#architecture-diagram)
- [Core Components](#core-components)
- [Database Schema](#database-schema)
- [Security Architecture](#security-architecture)
- [Data Flow](#data-flow)
- [Design Patterns](#design-patterns)
- [Extension Points](#extension-points)

---

## 🏗️ System Overview

SecureVault Pro is a desktop password manager built with:
- **Language:** Java 17+
- **UI Framework:** Java Swing
- **Database:** SQLite 3.44+
- **Architecture:** Layered MVC-style architecture

### Key Characteristics

- **Offline-First:** No network dependencies
- **Single-User Desktop:** Not designed for concurrent access
- **Encryption-at-Rest:** All passwords encrypted in database
- **Session-Based:** Encryption keys exist only in memory during session

---

## 📊 Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                     Presentation Layer                       │
│  ┌──────────────────────────────────────────────────────┐  │
│  │        SecureVaultSwingEnhanced (Main GUI)           │  │
│  │  - JTable with credential list                       │  │
│  │  - Search, Filter, Sort controls                     │  │
│  │  - Add/Edit/Delete buttons                           │  │
│  │  - Theme management                                  │  │
│  └──────────────────────────────────────────────────────┘  │
│         │                    │                    │          │
│         ▼                    ▼                    ▼          │
│  ┌──────────┐      ┌──────────────┐      ┌─────────────┐  │
│  │ Login    │      │  Credential  │      │  Generator  │  │
│  │ Dialog   │      │  Dialog      │      │  Dialog     │  │
│  └──────────┘      └──────────────┘      └─────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Business Logic Layer                     │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │  UserManager │  │   Category   │  │   Attachment    │  │
│  │              │  │   Manager    │  │   Manager       │  │
│  │ - Auth       │  │ - Categories │  │ - File storage  │  │
│  │ - Users      │  │ - CRUD ops   │  │ - Encryption    │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
│                                                              │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │   Password   │  │   Health     │  │   Import/Export │  │
│  │  Encryption  │  │  Dashboard   │  │   Manager       │  │
│  │ - AES-256    │  │ - Analytics  │  │ - Backup        │  │
│  │ - PBKDF2     │  │ - Scoring    │  │ - Restore       │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                       Data Access Layer                      │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                   Database.java                       │  │
│  │  - Connection management                              │  │
│  │  - CRUD operations for credentials                    │  │
│  │  - Encryption key management                          │  │
│  │  - Transaction handling                               │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      Persistence Layer                       │
│                    SQLite Database                           │
│  ┌────────────┐  ┌────────────────┐  ┌─────────────────┐  │
│  │   users    │  │  credentials   │  │   categories    │  │
│  │            │  │  (encrypted)   │  │                 │  │
│  └────────────┘  └────────────────┘  └─────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔧 Core Components

### 1. SecureVaultSwingEnhanced.java
**Primary GUI and Application Controller**

- **Lines:** ~1,160
- **Responsibilities:**
  - Main application window and UI components
  - Event handling for user interactions
  - Credential table management
  - Search, filter, and sort logic
  - Session timeout management
  - Theme switching

**Key Methods:**
```java
- showLogin() - Authentication flow
- loadCredentials() - Load and display credentials
- onAdd() - Add new credential
- onEdit() - Edit existing credential
- onDelete() - Delete credential
- lockVault() - Lock application
```

### 2. Database.java
**Data Access Layer**

- **Lines:** ~370
- **Responsibilities:**
  - SQLite connection management
  - CRUD operations for credentials
  - Encryption key storage (in-memory)
  - Database schema creation/upgrade
  - Demo data initialization

**Key Methods:**
```java
- insertCredential() - Add encrypted credential
- updateCredential() - Update existing credential
- getAllCredentials() - Retrieve all user credentials
- setEncryptionKey() - Set session encryption key
- initializeDemoData() - Create sample data
```

**Inner Class:**
```java
Credential - Data model for password entries
```

### 3. UserManager.java
**Authentication and User Management**

- **Lines:** ~150
- **Responsibilities:**
  - User creation and authentication
  - Password hashing with SHA-256
  - Salt generation and management
  - Default user creation

**Key Methods:**
```java
- createUser() - Register new user
- authenticateUser() - Verify login credentials
- getUserSalt() - Retrieve user's salt for key derivation
```

### 4. PasswordEncryption.java
**Cryptography Implementation**

- **Lines:** ~100 (estimated)
- **Responsibilities:**
  - AES-256-CBC encryption/decryption
  - PBKDF2 key derivation
  - IV generation and management
  - Key clearing for security

**Key Methods:**
```java
- encrypt() - Encrypt password with AES-256-CBC
- decrypt() - Decrypt password
- deriveKey() - PBKDF2 key derivation
- clearKey() - Securely clear key from memory
```

### 5. CategoryManager.java
**Category Management**

- **Lines:** ~130
- **Responsibilities:**
  - Category CRUD operations
  - Default category initialization
  - Category listing and retrieval

**Default Categories:**
- Social Media
- Banking
- Email
- Work
- Shopping
- Entertainment
- Other

### 6. AttachmentManager.java
**File Attachment Handling**

- **Lines:** ~200 (estimated)
- **Responsibilities:**
  - Encrypted file storage
  - File size validation (10MB limit)
  - Attachment CRUD operations
  - File encryption/decryption

### 7. HealthDashboard.java & HealthDashboardDialog.java
**Security Analytics**

- **Responsibilities:**
  - Password strength analysis
  - Reused password detection
  - Overall security score calculation
  - Visual dashboard display

**Metrics Calculated:**
- Weak/Medium/Strong password counts
- Reused passwords
- Expired passwords
- Overall security score (0-100)

### 8. Dialog Classes

**LoginDialog.java**
- User authentication interface
- Create new user option
- Show/hide password toggle

**EnhancedCredentialDialog.java**
- Add/edit credential form
- Password generator integration
- Category selection
- Website URL and expiry date fields

**PasswordGeneratorDialog.java**
- Secure password generation
- Configurable length and character sets
- Strength indicator
- Copy to clipboard

**StrengthCheckerDialog.java**
- Real-time password strength analysis
- Visual strength indicator
- Improvement suggestions

### 9. Supporting Classes

**StrengthChecker.java**
- Password strength calculation algorithm
- Returns: WEAK, MEDIUM, STRONG

**ImportExportManager.java**
- Encrypted backup export
- Restore from backup
- CSV export/import

**DatabaseUpgrade.java**
- Schema migration handling
- Add new columns/tables
- Backward compatibility

---

## 🗄️ Database Schema

### users Table
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    salt TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### credentials Table
```sql
CREATE TABLE credentials (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    username TEXT NOT NULL,
    password TEXT NOT NULL,              -- Encrypted
    notes TEXT,
    is_favorite INTEGER DEFAULT 0,
    category TEXT DEFAULT 'Other',
    website_url TEXT,
    expiry_date TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_password_change TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### categories Table
```sql
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    is_default INTEGER DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### attachments Table
```sql
CREATE TABLE attachments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    credential_id INTEGER NOT NULL,
    filename TEXT NOT NULL,
    file_data BLOB NOT NULL,             -- Encrypted
    file_size INTEGER NOT NULL,
    mime_type TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (credential_id) REFERENCES credentials(id)
);
```

---

## 🔐 Security Architecture

### Encryption Flow

```
User Master Password
        │
        ▼
    [PBKDF2]  ← User's Salt (16 bytes)
        │
        ▼
   256-bit Key (stored in memory only)
        │
        ▼
    [AES-256-CBC]  ← Random IV per password
        │
        ▼
  Encrypted Password (stored in database)
```

### Authentication Flow

```
User Login
    │
    ▼
Username + Password
    │
    ▼
Retrieve salt from users table
    │
    ▼
Hash password with SHA-256 + salt
    │
    ▼
Compare with stored password_hash
    │
    ├─ Match → Authenticate
    │           │
    │           ▼
    │       Derive encryption key (PBKDF2)
    │           │
    │           ▼
    │       Store key in memory
    │           │
    │           ▼
    │       Load encrypted credentials
    │           │
    │           ▼
    │       Decrypt for display
    │
    └─ No Match → Authentication Failed
```

### Session Management

```
Login Success
    │
    ▼
Start 5-minute timer
    │
    ├─ User Activity → Reset timer
    │
    └─ Timer Expires → Lock vault
                        │
                        ▼
                    Clear encryption key
                        │
                        ▼
                    Show login dialog
```

---

## 🔄 Data Flow

### Adding a Credential

```
User Input (Add Dialog)
    │
    ├─ Title, Username, Password, Notes
    ├─ Category, Website URL, Expiry Date
    │
    ▼
Validate Input
    │
    ├─ Check for duplicates
    ├─ Validate required fields
    │
    ▼
Encrypt Password
    │
    ├─ Generate random IV
    ├─ AES-256-CBC encrypt
    │
    ▼
Insert into Database
    │
    ├─ INSERT INTO credentials
    ├─ Store encrypted password
    │
    ▼
Refresh UI
    │
    └─ Reload credential list
```

### Retrieving Credentials

```
Database Query
    │
    ▼
SELECT * FROM credentials WHERE user_id = ?
    │
    ▼
For each credential:
    │
    ├─ Extract encrypted password
    │
    ▼
Decrypt Password
    │
    ├─ Extract IV from encrypted data
    ├─ AES-256-CBC decrypt
    │
    ▼
Create Credential Object
    │
    ▼
Add to List
    │
    ▼
Display in Table
```

---

## 🎨 Design Patterns

### 1. MVC (Model-View-Controller)
- **Model:** `Database.Credential`, manager classes
- **View:** Swing components, dialog classes
- **Controller:** `SecureVaultSwingEnhanced`

### 2. Singleton (Implicit)
- Database connection per session
- UserManager per application instance

### 3. Factory Pattern
- Key derivation factory
- Cipher instance creation

### 4. Strategy Pattern
- Different sort strategies (by title, date, etc.)
- Filter strategies (all, favorites, strength-based)

### 5. Observer Pattern (Swing)
- Event listeners for UI components
- Table model updates

---

## 🔌 Extension Points

### Adding New Features

#### 1. New Credential Field
```java
// 1. Add column to credentials table (DatabaseUpgrade.java)
// 2. Update Database.Credential class
// 3. Update insert/update methods in Database.java
// 4. Update EnhancedCredentialDialog.java UI
// 5. Update table model in SecureVaultSwingEnhanced.java
```

#### 2. New Category
```java
// CategoryManager.java
public void addDefaultCategory(String categoryName) {
    // Insert into categories table
}
```

#### 3. New Encryption Algorithm
```java
// PasswordEncryption.java
public static String encryptWithAlgorithm(
    String plaintext, 
    SecretKey key,
    String algorithm
) {
    // Implement alternative encryption
}
```

#### 4. Import from Other Password Managers
```java
// ImportExportManager.java
public void importFromLastPass(File csvFile) {
    // Parse CSV
    // Map to Credential objects
    // Insert into database
}
```

---

## 📝 Code Organization

### Package Structure (Flat)
All classes in default package (root). For larger projects, consider:

```
com.securevault.pro/
├── ui/           - GUI components
├── core/         - Business logic
├── crypto/       - Encryption
├── data/         - Database access
└── util/         - Utilities
```

### File Sizes
- Small: < 200 lines
- Medium: 200-500 lines
- Large: 500-1000 lines
- Very Large: > 1000 lines (refactor candidate)

---

## 🧪 Testing Considerations

### Unit Testing Targets
- `PasswordEncryption` - Encryption/decryption
- `StrengthChecker` - Password strength logic
- `UserManager` - Authentication logic
- `CategoryManager` - CRUD operations

### Integration Testing
- Database operations with encryption
- Full credential lifecycle (add, edit, delete)
- Import/export functionality

### Security Testing
- Verify encryption strength
- Check key derivation parameters
- Test session timeout
- Verify clipboard clearing

---

## 🚀 Performance Considerations

### Optimization Points

1. **Database Queries**
   - Use prepared statements (already implemented)
   - Index on user_id for faster queries
   - Batch operations when possible

2. **Encryption**
   - Encrypt only once on save
   - Cache decrypted values during session
   - Use hardware acceleration when available

3. **UI Rendering**
   - Lazy loading for large credential lists
   - Virtual scrolling for table
   - Debounce search input

### Memory Management

- Clear sensitive data promptly
- Use char[] for passwords (not String)
- Nullify references to encryption keys

---

## 📚 Dependencies

### Runtime Dependencies

```
lib/
├── sqlite-jdbc-3.44.1.0.jar    - SQLite database driver
├── slf4j-api-2.0.9.jar         - Logging API
└── slf4j-simple-2.0.9.jar      - Simple logging implementation
```

### Standard Library Usage

- `javax.crypto.*` - Encryption (AES, PBKDF2)
- `javax.swing.*` - GUI components
- `java.sql.*` - Database connectivity
- `java.security.*` - Security utilities

---

## 🔮 Future Architecture Improvements

### Potential Enhancements

1. **Modularization**
   - Split into proper Java modules
   - Separate concerns more clearly

2. **Dependency Injection**
   - Use DI framework for better testability
   - Reduce coupling between components

3. **Event Bus**
   - Decouple components with event system
   - Better notification handling

4. **Plugin System**
   - Allow third-party extensions
   - Custom import/export formats

5. **Database Abstraction**
   - Support multiple database backends
   - Consider JPA/Hibernate

---

## 📞 For Developers

### Getting Started with Codebase

1. Start with `SecureVaultSwingEnhanced.java` - main entry point
2. Understand `Database.java` - data layer
3. Review `PasswordEncryption.java` - security core
4. Explore dialog classes - UI components

### Key Files to Understand

1. **Core Logic:** `SecureVaultSwingEnhanced.java`
2. **Data Access:** `Database.java`
3. **Security:** `PasswordEncryption.java`, `UserManager.java`
4. **UI:** Dialog classes

---

**Questions?** Open an issue or discussion on GitHub!

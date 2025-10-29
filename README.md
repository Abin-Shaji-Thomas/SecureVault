# SecureVault - Password Manager# SecureVault - Multi-User Password Manager# SecureVault - Password Manager## Getting Started



## 📋 Project Overview



**SecureVault** is a secure, multi-user password management application built with Java Swing and SQLite. It provides a modern GUI for storing, managing, and organizing credentials with advanced security features including SHA-256 password hashing, password strength analysis, and secure password generation.A secure, feature-rich password manager built with Java Swing and SQLite database. Supports multiple users, each with their own encrypted password vault.



## ✨ Key Features



### 🔐 Security Features## FeaturesA modular, database-backed password manager with Swing GUI.Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

- **Multi-User Authentication**: Secure login system with user isolation

- **SHA-256 Hashing**: All user passwords are hashed with unique 16-byte salts

- **Password Masking**: Credentials displayed with asterisks (••••••)

- **Secure Password Generation**: Cryptographically secure random password generator### 🔐 Multi-User System

- **Password Strength Meter**: Real-time visual strength analysis with animated UI

- **User Authentication**: Secure SHA-256 hashed passwords with salt

### 🎨 User Interface

- **Modern Design**: Clean, intuitive Java Swing interface- **Multiple Users**: Each user has a separate password vault## Project Structure## Folder Structure

- **Dual Theme Support**: Light and Dark themes with proper contrast

- **Interactive Table**: Sortable credential table with visual strength indicators- **Default User**: Pre-configured user `Abin` with password `Abin@2006`

- **Context Menu**: Right-click menu for quick actions

- **Live Strength Feedback**: Real-time password strength visualization- **Create New Users**: Easy user registration from login screen



### 💼 Credential Management- **Logout**: Switch between users without closing the application

- **Add/Edit/Delete**: Full CRUD operations for credentials

- **Copy to Clipboard**: One-click copy for passwords and usernames```The workspace contains two folders by default, where:

- **Password Generator**: Customizable length and character sets### 🔑 Password Management

- **Strength Checker**: Detailed password analysis with recommendations

- **Add/Edit/Delete** credentials with title, username, and passwordPRoejct/

## 🛠️ Technical Stack

- **Copy to Clipboard** with auto-clear (12 seconds for security)

| Component | Technology |

|-----------|-----------|- **Right-click menu** for quick password copy├── src/- `src`: the folder to maintain sources

| **Language** | Java 25 (with preview features) |

| **GUI Framework** | Java Swing |- **Persistent Storage** in SQLite database

| **Database** | SQLite 3 |

| **JDBC Driver** | sqlite-jdbc-3.44.1.0.jar |│   ├── SecureVaultSwing.java       # Main UI application- `lib`: the folder to maintain dependencies

| **Logging** | SLF4J 2.0.9 |

| **Security** | java.security.MessageDigest (SHA-256) |### 🎨 User Interface

| **Build Tool** | javac (manual compilation) |

- **Clean Modern Design** with themed interface│   ├── Database.java                # SQLite database handler (CRUD operations)

## 📁 Project Structure

- **Password Strength Meter**: 5-segment visual bars showing strength

```

PRoejct/- **Live Strength Indicator**: Real-time strength check while typing│   ├── PasswordGenerator.java      # Password generation utilityMeanwhile, the compiled output files will be generated in the `bin` folder by default.

├── src/

│   ├── SecureVaultSwing.java         # Main application & UI- **Show Password Toggle**: View passwords in login screen

│   ├── Database.java                 # SQLite CRUD operations

│   ├── UserManager.java              # Authentication & user management- **Themes**: Light, Dark, and Custom color schemes│   ├── StrengthChecker.java        # Password strength checking

│   ├── LoginDialog.java              # Login/registration dialog

│   ├── CredentialDialog.java         # Add/edit credential dialog

│   ├── PasswordGeneratorDialog.java  # Password generator UI

│   ├── StrengthChecker.java          # Password strength algorithm### 🛠️ Password Tools│   ├── ThemeManager.java           # UI theme management> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

│   └── StrengthCheckerDialog.java    # Animated strength display

├── lib/- **Password Generator**: Create strong passwords with customizable options

│   ├── sqlite-jdbc-3.44.1.0.jar     # SQLite JDBC driver

│   ├── slf4j-api-2.0.9.jar          # SLF4J API  - Length control (8-32 characters)│   ├── ClipboardHelper.java        # Clipboard operations with auto-clear

│   └── slf4j-simple-2.0.9.jar       # SLF4J implementation

├── bin/                              # Compiled .class files  - Character types: uppercase, lowercase, digits, symbols

├── securevault.db                    # SQLite database

├── run.sh                            # Launch script  - Live strength preview│   ├── CredentialDialog.java       # Add/Edit credential dialog## Dependency Management

└── README.md                         # This file

```  



## 🚀 Getting Started- **Password Strength Checker**: Analyze password security│   ├── PasswordGeneratorDialog.java # Password generator UI



### Prerequisites  - Animated strength meter

- **Java 25+** (OpenJDK or Oracle JDK)

- **SQLite3** (for database management, optional)  - Real-time suggestions for improvement│   └── StrengthCheckerDialog.java  # Strength checker UI with animationsThe `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

- **Linux/Unix** environment (tested on Kali Linux)

  - Show/hide password toggle

### Installation

├── lib/

1. **Clone/Download** the project to your system

## Getting Started│   └── sqlite-jdbc-3.44.1.0.jar   # SQLite JDBC driver

2. **Verify Java Installation**

   ```bash├── bin/                            # Compiled classes

   java --version

   # Should show Java 25 or higher### Prerequisites└── securevault.db                  # SQLite database (created at runtime)

   ```

- Java 17 or higher```

3. **Run the Application**

   ```bash- Linux/Unix-based system (tested on Kali Linux)

   chmod +x run.sh

   ./run.sh## Features

   ```

   ### Installation

   The `run.sh` script automatically:

   - Downloads required JAR dependencies if missing### ✅ Implemented

   - Compiles all Java source files

   - Launches the application with proper classpath1. Clone or extract the project to your system- **SQLite Database Persistence**: All credentials stored in local database



### Manual Compilation (Alternative)- **Password Strength Meter**: 



```bash2. Navigate to the project directory:  - Live 5-segment bar in table

# Compile all source files

javac -cp "lib/*:." -d bin src/*.java```bash  - Animated strength checker dialog



# Run the applicationcd /home/abin/Documents/Secure_Valut/PRoejct  - Suggestions for improvement

java -cp "lib/*:bin" SecureVaultSwing

``````- **Password Generator**: Customizable length and character sets



## 👤 Default Users & Login- **Clipboard Auto-Clear**: Copies passwords with automatic clearing after timeout



The system comes with two pre-configured users:3. Make the run script executable:- **Theming**: Light, Dark, and Custom themes



| Username | Password | Credentials |```bash- **Modular Architecture**: Clean separation of concerns

|----------|----------|-------------|

| **Abin** | Abin@2006 | 55 sample credentials |chmod +x run.sh

| **Advaith** | Advaith@2006 | 29 sample credentials |

```### Database Operations

### First Time Login

1. Launch the application- **Insert**: Add new credentials (Title, Username, Password)

2. Enter username and password

3. Click **Login** to access your vault### Running the Application- **Update**: Edit existing credentials

4. Or click **Create User** to register a new account

- **Delete**: Remove credentials from database

## 📖 User Guide

Simply run:- **Select**: Load all credentials on login

### Managing Credentials

```bash

#### Add New Credential

1. Click **Add** button in toolbar./run.sh## Compilation

2. Fill in the form:

   - **Service**: Website/app name (e.g., "GitHub")```

   - **Username**: Your username/email

   - **Password**: Your password```bash

   - **Category**: Optional grouping (e.g., "Social Media")

3. Watch the live strength meter as you typeOr manually:javac -cp "lib/*:." -d bin src/*.java

4. Click **Save**

```bash```

#### Edit Credential

1. Select a credential row in the tablejavac -cp "lib/*:." -d bin src/*.java

2. Click **Edit** button

3. Modify the fieldsjava -cp "lib/*:bin" SecureVaultSwing## Running

4. Click **Save**

```

#### Delete Credential

1. Select a credential row```bash

2. Click **Delete** button

3. Confirm the deletion### First Time Loginjava -cp "lib/*:bin" SecureVaultSwing



#### Copy Credentials```

- **Copy Password**: Select row → Click "Copy Password" button

- **Copy Username**: Select row → Click "Copy Username" buttonWhen you first run the application, you can:

- **Right-Click Menu**: Right-click any row for quick copy options

## Usage

### Password Generator

1. **Login as default user**:

1. Click **Generate** button

2. Configure options:   - Username: `Abin`1. **First Launch**: Set a master password (in-memory for this version)

   - **Length**: 8-64 characters

   - **Include Uppercase**: A-Z   - Password: `Abin@2006`2. **Add Credentials**: Click "Add" button, fill in Title/Username/Password

   - **Include Lowercase**: a-z

   - **Include Digits**: 0-9   - ✓ Use "Show Password" checkbox to verify your input3. **View Strength**: Each row shows a 5-segment strength bar

   - **Include Symbols**: !@#$%^&*

3. Click **Generate**4. **Copy**: Select a row and click "Copy Username" or "Copy Password"

4. Password is automatically copied to clipboard

5. Check strength or generate new password2. **Create a new user**:5. **Generate Password**: Click "Generate Password" for secure random passwords



### Password Strength Checker   - Enter your desired username and password6. **Check Strength**: Use "Strength Checker" to analyze any password



1. Select a credential row   - Click "Create User"7. **Themes**: Click "Theme" to switch between Light/Dark/Custom

2. Click **Check Strength** button

3. View detailed analysis:   - Password must be at least 6 characters long

   - **Score**: 0-100 (with color indicator)

   - **Strength Level**: Weak/Fair/Good/Strong/Very Strong## Database Schema

   - **Character Analysis**: Length, uppercase, lowercase, digits, symbols

   - **Recommendations**: Specific tips to improve strength## Usage Guide



### Theme Switching```sql



1. Click **Theme** button in toolbar### Managing CredentialsCREATE TABLE credentials (

2. Toggles between Light and Dark modes

3. Theme preference persists during session    id INTEGER PRIMARY KEY AUTOINCREMENT,



### Logout- **Add**: Click "Add" button, enter details, click OK    title TEXT NOT NULL,



1. Click **Logout** button- **Edit**: Select a row, click "Edit", modify details    username TEXT NOT NULL,

2. Returns to login screen

3. All data is saved automatically- **Delete**: Select a row, click "Delete", confirm    password TEXT NOT NULL



## 🔒 Security Architecture- **Copy Password**: Select a row, click "Copy Password" or right-click);



### Password Hashing (SHA-256 + Salt)```



```### User Session

User Password → SHA-256 + Random 16-byte Salt → Stored Hash

                                                    ↓## Security Notes

                                         Database (users table)

```- **Current User**: Displayed in top-right corner



**Implementation**:- **Logout**: Click "Logout" to return to login screen⚠️ **Current Limitations** (In-Memory Master Password):

- Each user has a unique 16-byte random salt

- Password + Salt → SHA-256 hash → Base64 encoded- **Switch Users**: Logout and login with different credentials- Master password is not persisted

- Original passwords are **never** stored

- Brute-force attacks are computationally infeasible- Database is **NOT encrypted** (plaintext storage)



### Database Schema### Password Tools- Suitable for learning/development only



#### Users Table

```sql

CREATE TABLE users (- **Generate Password**: Creates strong random passwords### Recommended Future Enhancements:

    id INTEGER PRIMARY KEY AUTOINCREMENT,

    username TEXT UNIQUE NOT NULL,- **Check Strength**: Analyzes any password and provides suggestions1. **Encrypt database** using AES-256-GCM with key derived from master password (PBKDF2 or Argon2)

    password_hash TEXT NOT NULL,  -- SHA-256 hash (Base64)

    salt TEXT NOT NULL,           -- 16-byte salt (Base64)- **Theme**: Choose between Light, Dark, or Custom color schemes2. **Persist master password hash** securely

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);3. **Add salt/IV** per credential

```

## Security Features4. **Memory-wipe** sensitive char[] arrays after use

#### Credentials Table

```sql5. **Add biometric/OS authentication** integration

CREATE TABLE credentials (

    id INTEGER PRIMARY KEY AUTOINCREMENT,- ✓ **SHA-256 Hashing**: Master passwords hashed with unique salts

    user_id INTEGER NOT NULL,     -- Foreign key to users.id

    service TEXT NOT NULL,- ✓ **Database Encryption**: SQLite database for secure storage## Class Responsibilities

    username TEXT NOT NULL,

    password TEXT NOT NULL,       -- Stored in plain text (encrypted storage could be added)- ✓ **Auto-Clear Clipboard**: Passwords cleared after 12 seconds

    category TEXT,

    strength INTEGER,             -- 0-100 score- ✓ **Session Management**: Proper logout and user isolation### SecureVaultSwing.java

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE- ✓ **Strong Password Enforcement**: Minimum 6 characters for master passwords- Main UI frame and event handling

);

```- Table model and renderers



### User Isolation## Project Structure- Coordinates between dialogs and database

- Each credential is linked to a specific user via `user_id`

- Users can only view/modify their own credentials

- SQL queries always filter by `user_id`

```### Database.java

## 🧮 Password Strength Algorithm

SecureVault/- SQLite connection management

The strength checker analyzes passwords based on:

├── src/- CRUD operations (insert, update, delete, select)

### Scoring System (0-100)

│   ├── SecureVaultSwing.java      # Main UI (370 lines - optimized)- Credential model class

| Criteria | Points | Description |

|----------|--------|-------------|│   ├── UserManager.java            # User authentication & management

| **Length** | Up to 30 | 5 points per character (min 6) |

| **Uppercase** | 15 | Contains A-Z |│   ├── LoginDialog.java            # Login/registration UI### PasswordGenerator.java

| **Lowercase** | 15 | Contains a-z |

| **Digits** | 10 | Contains 0-9 |│   ├── Database.java               # SQLite database operations- Secure random password generation

| **Symbols** | 15 | Contains !@#$%^&* etc. |

| **Mixed Types** | 15 | Uses multiple character types |│   ├── PasswordGenerator.java      # Password generation logic- Configurable character sets



### Strength Levels│   ├── StrengthChecker.java        # Password strength analysis



- **0-20**: ⚠️ Very Weak (Red)│   ├── ThemeManager.java           # UI theme management### StrengthChecker.java

- **21-40**: 🔶 Weak (Orange)

- **41-60**: 🟡 Fair (Yellow)│   ├── ClipboardHelper.java        # Secure clipboard operations- Password strength classification (WEAK/MEDIUM/STRONG)

- **61-80**: 🟢 Good (Light Green)

- **81-100**: ✅ Strong/Very Strong (Dark Green)│   ├── CredentialDialog.java       # Add/Edit credential UI- Score computation (0-6 based on length & character variety)



### Visual Indicators│   ├── PasswordGeneratorDialog.java # Password generator UI- Suggestions generator

- **Table View**: Color-coded bars in "Strength" column

- **Strength Dialog**: Animated circular progress meter│   └── StrengthCheckerDialog.java  # Strength checker UI

- **Live Feedback**: Real-time updates while typing

├── lib/### ThemeManager.java

## 🎨 Theme System

│   ├── sqlite-jdbc-3.44.1.0.jar   # SQLite JDBC driver- Theme definitions (Light, Dark, Custom)

### Light Theme

- Background: White/Light Gray│   ├── slf4j-api-2.0.9.jar         # Logging API- Theme application to UI components

- Foreground: Black

- Buttons: Light Gray with hover effects│   └── slf4j-simple-2.0.9.jar      # Logging implementation

- Table: White cells, light gray headers

├── bin/                            # Compiled .class files### ClipboardHelper.java

### Dark Theme (Default)

- Background: Dark Gray (#2b2b2b)├── securevault.db                  # SQLite database (auto-created)- Copy to clipboard with ClipboardOwner

- Foreground: White

- Buttons: Gray with proper contrast├── run.sh                          # Launch script- Auto-clear after timeout using Swing Timer

- Table: Dark cells, dark headers

└── README.md                       # This file

**Toggle**: Click "Theme" button in toolbar

```### CredentialDialog.java

## 📊 Database Management

- Add/Edit credential UI

### View Database (Command Line)

## Database Schema- Live strength indicator

```bash

# Open database

sqlite3 securevault.db

### Users Table### PasswordGeneratorDialog.java

# View users

SELECT id, username, created_at FROM users;```sql- Password generation UI



# View credentials for userCREATE TABLE users (- Integration with strength checker

SELECT service, username, category, strength 

FROM credentials     id INTEGER PRIMARY KEY AUTOINCREMENT,

WHERE user_id = 1;

    username TEXT UNIQUE NOT NULL,### StrengthCheckerDialog.java

# Count credentials per user

SELECT u.username, COUNT(c.id) as credential_count    password_hash TEXT NOT NULL,- Password strength analysis UI

FROM users u

LEFT JOIN credentials c ON u.id = c.user_id    salt TEXT NOT NULL,- Animated 5-segment meter

GROUP BY u.id;

```    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP- Show/hide password toggle



### Database Statistics);- Real-time suggestions



Current database contains:```

- **2 Users**: Abin, Advaith

- **84 Total Credentials**: 55 (Abin) + 29 (Advaith)## Dependencies

- **Auto-backup**: SQLite journal for crash recovery

### Credentials Table

## 🐛 Troubleshooting

```sql- **Java 17+** (uses switch expressions)

### Application Won't Start

CREATE TABLE credentials (- **SQLite JDBC 3.44.1.0** (included in `lib/`)

**Problem**: Missing dependencies

```bash    id INTEGER PRIMARY KEY AUTOINCREMENT,

# Solution: Run setup script

./run.sh    user_id INTEGER NOT NULL,## Backup

# Or manually download JARs

cd lib    title TEXT NOT NULL,

wget https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar

wget https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar    username TEXT NOT NULL,The original monolithic code is saved in:

wget https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar

```    password TEXT NOT NULL,- `src/SecureVaultSwing.java.backup`



### Compilation Errors    FOREIGN KEY (user_id) REFERENCES users(id)



**Problem**: Wrong Java version);## Next Steps

```bash

# Check version```

java --version

Recommended improvements (in priority order):

# Must be Java 21+ (project uses Java 25)

# Install correct version:## Code Optimizations

sudo apt update

sudo apt install openjdk-25-jdk1. **Encryption**: Add AES-256 encryption for database

```

✓ **Removed**: Redundant login/master password screen (replaced with proper user authentication)2. **Settings Persistence**: Save theme preference, window size

### Login Issues

✓ **Removed**: CardLayout switching (simplified single-view design)3. **Search/Filter**: Add search bar above table

**Problem**: Wrong credentials

- **Solution**: Use default credentials (Abin/Abin@2006 or Advaith/Advaith@2006)✓ **Removed**: Unnecessary UI components and event handlers4. **Keyboard Shortcuts**: Ctrl+N (Add), Ctrl+E (Edit), etc.

- **Reset**: Delete `securevault.db` and restart (creates fresh database)

✓ **Added**: Proper user session management5. **Export/Import**: Encrypted backup functionality

### Database Locked

✓ **Added**: Secure password hashing with SHA-2566. **Password History**: Track password changes

**Problem**: Multiple instances running

```bash✓ **Optimized**: Code reduced from 714 lines to 370 lines in main file7. **Tags/Categories**: Organize credentials

# Solution: Kill all Java processes

pkill -9 java✓ **Improved**: Modular architecture with single-responsibility classes8. **zxcvbn Integration**: Better strength checking

# Restart application

./run.sh9. **Automated Tests**: Unit tests for crypto and business logic

```

## Keyboard Shortcuts10. **Build System**: Maven or Gradle for dependency management

## 🔧 Development & Customization



### Adding New Features- **Enter** in login screen: Submit login

- **Enter** in password field: Submit form

1. **Backup Before Changes**- **Right-click** on table row: Show context menu

   ```bash

   cp securevault.db securevault.db.backup## Troubleshooting

   ```

### Database Initialization Failed

2. **Modify Source Files** in `src/` directory- Ensure you have write permissions in the project directory

- Check that SQLite JDBC and SLF4J libraries are in `lib/` folder

3. **Recompile**- Run: `ls -lh lib/` to verify files exist

   ```bash

   javac -cp "lib/*:." -d bin src/*.java### Java Version Issues

   ```- Requires Java 17+ for switch expressions

- Check version: `java --version`

4. **Test Thoroughly** with sample data- Update if needed: `sudo apt install openjdk-17-jdk`



### Code Organization### Permission Denied on run.sh

```bash

- **SecureVaultSwing.java**: Main application logic, UI components, event handlerschmod +x run.sh

- **Database.java**: All SQL operations, connection management```

- **UserManager.java**: Authentication, user CRUD, SHA-256 hashing

- **Dialogs**: Modular UI components for specific tasks## Future Enhancements

- **StrengthChecker.java**: Pure algorithm (no UI dependencies)

- [ ] Password encryption at rest (AES-256)

### Best Practices Followed- [ ] Password expiry and rotation reminders

- [ ] Import/Export functionality

✅ **Single Responsibility**: Each class has one clear purpose  - [ ] Two-factor authentication

✅ **No Code Duplication**: Utilities merged into callers  - [ ] Password history tracking

✅ **Clean Code**: No unused imports, no unused lambda parameters  - [ ] Secure notes and documents

✅ **Security First**: Passwords hashed, salted, never logged  - [ ] Browser extension integration

✅ **User Isolation**: Foreign keys ensure data privacy  - [ ] Mobile app companion

✅ **Error Handling**: Try-catch blocks, user-friendly messages  

## License

## 📝 License & Credits

Educational project for learning Java Swing and database integration.

**Project**: SecureVault Password Manager  

**Author**: Abin  ## Author

**Date**: October 2025  

**Purpose**: Academic project demonstrating secure password management  Created with focus on security, usability, and clean code architecture.



### Technologies Used---

- Java Swing (GUI)

- SQLite (Database)**Default Login**: Username: `Abin` | Password: `Abin@2006`

- SHA-256 (Cryptography)
- Maven Central Repository (Dependencies)

## 🎯 Future Enhancements (Suggestions)

- 🔐 **AES-256 Encryption** for password storage
- ☁️ **Cloud Sync** via encrypted backup files
- 📱 **Export/Import** to JSON/CSV
- 🔍 **Advanced Search** with filters
- 📊 **Analytics Dashboard** showing weak passwords
- 🌐 **Browser Extension** for auto-fill
- 2️⃣ **Two-Factor Authentication** for extra security
- 📧 **Password Sharing** with encrypted links

## 📞 Support

For issues or questions:
1. Check **Troubleshooting** section above
2. Review source code comments
3. Check database schema with `sqlite3 securevault.db ".schema"`

---

**Last Updated**: October 28, 2025  
**Version**: 1.0.0 (Final Release)  
**Status**: ✅ Production Ready

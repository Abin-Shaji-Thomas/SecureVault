# 🔐 SecureVault Pro - Enhanced Password Manager# 🔐 SecureVault Pro - Enhanced Password Manager# 🔐 SecureVault Pro - Enhanced Password Manager



**Version 3.0** - A modern, secure password management application with military-grade encryption



![Status](https://img.shields.io/badge/status-production%20ready-brightgreen)**Version 3.0** - A beautiful, feature-rich password management application with military-grade security, Samsung-inspired UI, and advanced password management features.**Version 3.0** - A beautiful, feature-rich password management application with military-grade security, Samsung-inspired UI, and advanced password management features.

![Java](https://img.shields.io/badge/java-17+-orange)

![License](https://img.shields.io/badge/license-MIT-blue)



---![Status](https://img.shields.io/badge/status-production%20ready-brightgreen)![Status](https://img.shields.io/badge/status-production%20ready-brightgreen)



## 📋 Overview![Java](https://img.shields.io/badge/java-25-orange)![Java](https://img.shields.io/badge/java-25-orange)



**SecureVault Pro** is a feature-rich desktop password manager built with Java Swing and SQLite. It provides:![License](https://img.shields.io/badge/license-personal%20use-blue)![License](https://img.shields.io/badge/license-personal%20use-blue)



- 🔒 **Military-Grade Security** - AES-256 encryption with PBKDF2 key derivation

- 👥 **Multi-User Support** - Each user has their own encrypted vault

- 📊 **Health Dashboard** - Monitor password strength and security score------

- 📂 **Category Management** - Organize credentials with custom categories

- 📎 **Encrypted Attachments** - Store files up to 10MB securely

- 📥 **Import/Export** - Backup and restore with encrypted archives

- 🎨 **Beautiful UI** - Dark/Light themes with Samsung-inspired design## 📋 Overview## � Overview



---



## 🚀 Quick Start**SecureVault Pro** is a secure, feature-rich password manager built with Java Swing and SQLite. It provides military-grade AES-256 encryption for password storage with automatic session management, clipboard security, and a modern, intuitive user interface.**SecureVault Pro** is a secure, feature-rich password manager built with Java Swing and SQLite. It provides military-grade AES-256 encryption for password storage with automatic session management, clipboard security, and a modern, intuitive user interface.



### Prerequisites



- **Java 17 or higher** (OpenJDK or Oracle JDK)### Default Login Credentials### Default Login Credentials

- Linux, macOS, or Windows

- **Username:** `Abin`- **Username:** `Abin`

### Installation & Running

- **Password:** `Abin@2006`- **Password:** `Abin@2006`

1. **Clone the repository:**

   ```bash

   git clone <your-repo-url>

   cd PRoejct------

   ```



2. **Make the run script executable** (first time only):

   ```bash## 🚀 Quick Start## � Quick Start

   chmod +x run.sh

   ```



3. **Run the application:**```bash```bash

   ```bash

   ./run.sh# Make executable (first time only)# Make executable (first time only)

   ```

chmod +x run.shchmod +x run.sh

   The script will automatically:

   - Compile the Java sources if needed

   - Include all required libraries

   - Launch the application# Launch SecureVault Pro# Launch SecureVault Pro



### Manual Compilation (Optional)./run.sh./run.sh



If you prefer to compile manually:``````



```bash

# Create bin directory

mkdir -p bin------



# Compile all sources

javac -Xlint:all -cp "lib/*:." -d bin src/*.java

## ✨ Key Features## ✨ Key Features

# Run the application

java -cp "bin:lib/*" SecureVaultSwingEnhanced

```

### 🔒 Security Features

### Default Login Credentials

- ✅ **AES-256-CBC Encryption** - Military-grade password encryption with PBKDF2 key derivation (100,000 iterations)

For testing purposes, the database includes a default user:

- ✅ **SHA-256 Authentication** - Salted password hashing for user accounts#### 🌐 **Website Integration**

- **Username:** `Abin`

- **Password:** `Abin@2006`- ✅ **Session Timeout** - Auto-locks after 5 minutes of inactivity



⚠️ **Important:** Change this password or create your own user for production use!- ✅ **Secure Clipboard** - Auto-clear copied passwords after 30 seconds- Store website URLs with credentials



---- ✅ **Manual Lock** - Instant vault locking (Ctrl+L)



## ✨ Key Features- ✅ **Password Strength Analysis** - Real-time strength checking with visual indicators- "🌐 Open" button launches browser directly### 🎨 Theme Support- **Manual Lock Button** - Instant vault locking ✅



### 🔐 Security Features

- **AES-256-CBC Encryption** - Military-grade password encryption

- **PBKDF2 Key Derivation** - 100,000 iterations for enhanced security### 💎 Password Management- Auto-prepends https:// if missing

- **SHA-256 Authentication** - Salted password hashing for user accounts

- **Session Timeout** - Auto-locks after 5 minutes of inactivity- ⭐ **Favorites System** - Mark important credentials with golden stars

- **Secure Clipboard** - Auto-clears copied passwords after 30 seconds

- **Manual Lock** - Instant vault locking with Ctrl+L- 🔍 **Real-Time Search** - Instant filtering as you type (Ctrl+F)- Perfect for quick access to login pages- **Light Mode** - Clean purple theme with white backgrounds



### 💎 Password Management- 🎯 **Smart Filters** - View all, favorites only, or filter by password strength (Weak/Medium/Strong)

- ⭐ **Favorites System** - Mark important credentials

- 🔍 **Real-Time Search** - Instant filtering (Ctrl+F)- 📊 **6 Sort Options** - Sort by title, username, created date, modified date, or favorites

- 🎯 **Smart Filters** - View by strength (Weak/Medium/Strong)

- 📊 **6 Sort Options** - Sort by title, username, dates, favorites- 📝 **Notes Field** - Add detailed notes to each credential

- 📝 **Notes Field** - Add detailed notes to credentials

- 📅 **Password Expiry** - Track and manage password expiration- 👤 **Copy Username/Password** - Quick copying with separate buttons#### 📅 **Password Expiry Management**- **Dark Mode** - Comfortable blue theme with dark backgrounds- **SHA-256 Authentication** - Salted password hashing ✅

- 🌐 **Website URLs** - Quick launch with browser integration

- 📅 **Timestamps** - Track when credentials were created and last modified

### 🆕 Advanced Features

- **📂 Categories** - 7 default categories + custom category support- Set expiry dates for passwords

- **📎 Attachments** - Upload encrypted files (up to 10MB)

- **📥 Import** - From Chrome, Firefox, Edge, Opera CSV exports### 🆕 Advanced Features (v3.0)

- **📤 Export** - Encrypted ZIP archives with all data

- **📊 Health Dashboard** - Security score (0-100) and statistics- Default 90-day expiry for new credentials- **Gradient Headers** - Smooth purple-to-blue gradient banner

- **🎨 Themes** - Beautiful Dark/Light mode with gradients

#### 📂 Categories System

### ⌨️ Keyboard Shortcuts

- `Ctrl+N` - Add new credential- 7 default categories: Social Media, Banking, Email, Work, Shopping, Entertainment, Other- Visual warnings in table:

- `Ctrl+F` - Focus search field

- `Ctrl+L` - Lock vault- Custom category support (add your own categories)

- `Ctrl+Shift+C` - Copy password

- `Del` - Delete selected credential- Color-coded category badges in table view  - ⛔ **EXPIRED** (red) - Password has expired- **Hover Effects** - Interactive button highlighting

- `Enter` - Edit selected credential

- Category dropdown in Add/Edit dialog

---

  - ⚠️ **X days** (amber) - Expiring within 7 days

## 📁 Project Structure

#### 🌐 Website Integration

```

PRoejct/- Website URL field for each credential  - Date (green) - Valid expiry date- **Color-Coded Strength** - Visual password strength indicators

├── src/                          # Java source files (15 files)

│   ├── SecureVaultSwingEnhanced.java    # Main application- Quick "Open in Browser" button (🌐) to launch URLs directly

│   ├── Database.java                     # Database operations

│   ├── PasswordEncryption.java           # AES-256 encryption- Automatic URL validation- "📅 +90 Days" button for quick expiry setting

│   ├── UserManager.java                  # Authentication

│   ├── CategoryManager.java              # Category management

│   ├── AttachmentManager.java            # File attachments

│   ├── ImportExportManager.java          # Import/Export#### ⏰ Password Expiry Tracking### 💼 **Features**### 🔐 Security Features## FeaturesA modular, database-backed password manager with Swing GUI.Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

│   ├── HealthDashboard.java              # Security analysis

│   └── ... (other UI and utility classes)- Set expiry dates for credentials

│

├── lib/                          # Dependencies- Visual expiry warnings:#### 📊 **Security Health Dashboard**

│   ├── sqlite-jdbc-3.44.1.0.jar         # SQLite JDBC driver

│   ├── slf4j-api-2.0.9.jar              # Logging API  - 🔴 **Red** - Expired

│   └── slf4j-simple-2.0.9.jar           # Logging implementation

│  - 🟡 **Amber** - Expiring within 30 days- **Overall Security Score** (0-100) with color coding## 🚀 Quick Start

├── docs/                         # Documentation

│   ├── FEATURES.md                       # Detailed feature list  - 🟢 **Green** - Valid

│   ├── CHANGELOG.md                      # Version history

│   ├── FIXES_APPLIED.md                  # Bug fixes log- Quick "+90 Days" button to set expiry date- Password strength distribution (Strong/Medium/Weak)

│   ├── BUILD_STATUS.md                   # Build information

│   └── PROJECT_SUMMARY.md                # Project overview- Automatic expiry notifications in Health Dashboard

│

├── bin/                          # Compiled classes (auto-generated)- **Reused passwords** detection- Multi-user support with isolated vaults

├── securevault.db                # SQLite database (included for convenience)

├── run.sh                        # Launch script#### 📥 Import/Export Manager

├── .gitignore                    # Git ignore rules

└── README.md                     # This file- **Import from:**- **Expired passwords** count

```

  - CSV files (comma-separated)

---

  - Encrypted ZIP archives- **Expiring soon** warnings (within 7 days)### Prerequisites

## 📚 Documentation

  - Automatic field mapping

- **[FEATURES.md](docs/FEATURES.md)** - Complete feature list with technical details

- **[CHANGELOG.md](docs/CHANGELOG.md)** - Version history and updates  - Duplicate detection and prevention- Credentials needing attention list

- **[FIXES_APPLIED.md](docs/FIXES_APPLIED.md)** - Bug fixes and improvements

- **[BUILD_STATUS.md](docs/BUILD_STATUS.md)** - Build verification and quality metrics- **Export to:**

- **[PROJECT_SUMMARY.md](docs/PROJECT_SUMMARY.md)** - Comprehensive project overview

  - Encrypted ZIP archive containing:- Beautiful visual report with charts- Java 25 or higher- Add, edit, delete, view credentials- **Multi-User Authentication**: Secure login system with user isolation

---

    - credentials.csv (all credential data)

## 🔧 Technical Details

    - attachments/ folder (encrypted files)

### Technology Stack

- **Language:** Java 17+  - Organized and sorted structure

- **GUI Framework:** Swing

- **Database:** SQLite 3  - Secure password-protected archives#### 📤 **Import/Export**- Linux/macOS/Windows

- **Encryption:** AES-256-CBC, SHA-256, PBKDF2

- **Dependencies:** sqlite-jdbc, slf4j



### Database Schema#### 📎 Encrypted Attachments- **Import from:**

- `users` - User accounts with hashed passwords

- `credentials` - Encrypted password storage- Upload files up to **10 MB** per credential

- `custom_categories` - User-defined categories

- `attachments` - Encrypted file storage- Encrypted storage in database as BLOBs  - Google Chrome CSV export- Password strength meter & analyzer



### Security Implementation- Download/view attachments anytime

- **Password Encryption:** AES-256-CBC with PKCS5 padding

- **Key Derivation:** PBKDF2 with SHA-256 (100,000 iterations)- Delete individual attachments  - Mozilla Firefox CSV export

- **Authentication:** SHA-256 with 16-byte random salt

- **Session Management:** In-memory key with auto-clear- Attachment count tracking



---- Perfect for recovery codes, 2FA backup codes, IDs, documents  - Microsoft Edge CSV export### Running the Application



## 🛠️ Development



### Building from Source#### 📊 Health Dashboard  - Opera CSV export



```bash- **Security Score** (0-100) calculated from:

# Clean build

rm -rf bin && mkdir bin  - Password strength distribution  - Generic CSV files- Secure password generator- **SHA-256 Hashing**: All user passwords are hashed with unique 16-byte salts



# Compile with all warnings enabled  - Expiry status

javac -Xlint:all -cp "lib/*:." -d bin src/*.java

  - Duplicate password detection  - SecureVault encrypted archives

# Verify compilation

echo $?  # Should output: 0- **Visual Statistics:**

```

  - Total credentials count- **Export to:**```bash

### Running Tests

  - Weak/Medium/Strong password breakdown

The application has been thoroughly tested with:

- ✅ Zero compiler warnings  - Expired/Expiring soon alerts  - Encrypted ZIP archive with:

- ✅ Clean code quality checks

- ✅ Security audit passed- **Action Items:**

- ✅ UI/UX testing completed

  - List of credentials needing attention    - credentials.csv (all data)# Make the script executable (first time only)- Duplicate prevention (title + username)

### Code Quality

- **Total Lines:** ~3,900 lines  - Direct links to update weak/expired passwords

- **Source Files:** 15 Java files

- **Warnings:** 0 (with `-Xlint:all`)    - attachments/ folder (encrypted files)

- **Dependencies:** 3 JAR files

### 🎨 User Interface

---

  - Organized and sorted structurechmod +x run.sh

## 🤝 Contributing

#### Enhanced Table View

Contributions are welcome! Please:

- **8 Columns**: ⭐ Favorite | Title | Username | Password | Category | Strength | Expiry | Modified- Smart format detection

1. Fork the repository

2. Create a feature branch (`git checkout -b feature/amazing-feature`)- Color-coded categories with subtle backgrounds

3. Commit your changes (`git commit -m 'Add amazing feature'`)

4. Push to the branch (`git push origin feature/amazing-feature`)- Visual expiry warnings (red/amber/green)- Duplicate prevention during import- One-click copy to clipboard- **Password Masking**: Credentials displayed with asterisks (••••••)

5. Open a Pull Request

- Password strength bars (5-segment visual indicator)

---

- Golden star favorites

## 📝 License

- Alternating row colors for better readability

This project is provided for personal and educational use. Please add a LICENSE file if you plan to distribute this software.

- Hover effects on rows#### 📎 **Encrypted Attachments**# Launch SecureVault Pro

---



## 🙏 Acknowledgments

#### Toolbar (13 Buttons)- Upload files up to **10 MB** per credential

- SQLite team for the excellent embedded database

- SLF4J team for the logging framework- **➕ Add** - Create new credential (Ctrl+N)

- Java Swing community for UI components

- **✏️ Edit** - Modify selected credential- Encrypted storage in database as BLOBs./run.sh- Dark/Light theme support

---

- **🗑️ Delete** - Remove credential (Del)

## 📞 Support

- **🔑 Copy Pass** - Copy password to clipboard (Ctrl+Shift+C)- Download/view attachments anytime

If you encounter any issues or have questions:

- Check the [FEATURES.md](docs/FEATURES.md) documentation- **👤 Copy User** - Copy username to clipboard

- Review [FIXES_APPLIED.md](docs/FIXES_APPLIED.md) for known issues

- Open an issue on the GitHub repository- **⭐ Favorite** - Toggle favorite status- Delete individual attachments```



---- **🎲 Generate** - Random password generator



## 🗺️ Roadmap- **🔍 Strength** - Check password strength- Attachment count tracking



Future enhancements being considered:- **📥 Import** - Import credentials from CSV/ZIP

- [ ] Cloud sync support

- [ ] Browser extension integration- **📤 Export** - Export to encrypted archive- Perfect for recovery codes, IDs, documents- **Secure Password Generation**: Cryptographically secure random password generator### 🔐 Multi-User System

- [ ] Mobile companion app

- [ ] Biometric authentication- **📊 Health** - View security dashboard

- [ ] Password breach checking

- [ ] Two-factor authentication- **🔒 Lock** - Lock vault (Ctrl+L)



---- **🎨 Theme** - Toggle dark/light mode



**Made with ❤️ by Abin | Version 3.0 | Last Updated: November 4, 2025**- **🚪 Logout** - Logout and exit## 🎨 User Interface### Default Login




#### Enhanced Dialog (9 Fields)

- **Title** - Credential name

- **Username** - Account username### Enhanced Table View- **Username**: `Abin`---

- **Password** - With "👁 Show/Hide" and "🎲 Generate" buttons

- **Category** - Dropdown with custom input support- **8 Columns**: ⭐ Favorite | Title | Username | Password | Category | Strength | Expiry | Modified

- **Website** - URL field with "🌐 Open" button

- **Expiry** - Date field with "📅 +90 Days" button- Color-coded categories with subtle backgrounds- **Password**: `Abin@2006`

- **Notes** - Multi-line text area for detailed information

- **Attachments** - Upload and manage encrypted files- Visual expiry warnings (red/amber/green)

- **Favorite** - Checkbox to mark as favorite

- Password strength bars (5-bar visual indicator)- **Password Strength Meter**: Real-time visual strength analysis with animated UI

#### Theme Support

- 🌙 **Dark Mode** - Professional dark theme with blue accents- Golden star favorites

- ☀️ **Light Mode** - Clean light theme with gradient headers

- Samsung-inspired gradients and modern design- Alternating row colors## 📁 Project Structure

- Smooth theme transitions

- Hover effects on rows

### ⌨️ Keyboard Shortcuts

- **Ctrl+N** - Add new credential## 🚀 Quick Start

- **Ctrl+F** - Focus search field

- **Ctrl+L** - Lock vault### Toolbar (13 Buttons)

- **Ctrl+Shift+C** - Copy password

- **Del** - Delete selected credential- **➕ Add** - Create new credential (Ctrl+N)```

- **Enter** - Edit selected credential

- **✏️ Edit** - Modify selected credential

---

- **🗑️ Delete** - Remove credential (Del)PRoejct/- **User Authentication**: Secure SHA-256 hashed passwords with salt

## 📁 Project Structure

- **🔑 Copy Pass** - Copy password to clipboard (Ctrl+Shift+C)

```

PRoejct/- **👤 Copy User** - Copy username to clipboard├── src/

├── src/

│   ├── SecureVaultSwingEnhanced.java  # Main application (1100+ lines)- **⭐ Favorite** - Toggle favorite status

│   ├── EnhancedCredentialDialog.java  # Add/Edit dialog

│   ├── Database.java                  # Database operations with encryption- **🎲 Generate** - Random password generator│   ├── SecureVaultSwingEnhanced.java  # Main application (955 lines)### **Run Application**

│   ├── DatabaseUpgrade.java           # Schema migration utility

│   ├── PasswordEncryption.java        # AES-256 encryption- **🔍 Strength** - Check password strength

│   ├── UserManager.java               # User authentication

│   ├── LoginDialog.java               # Login UI- **📥 Import** - Import credentials from CSV/ZIP│   ├── EnhancedCredentialDialog.java  # Add/Edit dialog with notes

│   ├── PasswordGeneratorDialog.java   # Password generator

│   ├── StrengthChecker.java           # Password strength analysis- **📤 Export** - Export to encrypted archive

│   ├── StrengthCheckerDialog.java     # Strength checker UI

│   ├── CategoryManager.java           # Category management- **📊 Health** - View security dashboard│   ├── Database.java                  # Database operations with encryption```bash### 🎨 User Interface

│   ├── AttachmentManager.java         # File attachment handling

│   ├── HealthDashboard.java           # Security score calculator- **🔒 Lock** - Lock vault (Ctrl+L)

│   ├── HealthDashboardDialog.java     # Health dashboard UI

│   └── ImportExportManager.java       # CSV/ZIP import/export- **🎨 Theme** - Toggle dark/light mode│   ├── PasswordEncryption.java        # AES-256 encryption

├── lib/

│   └── sqlite-jdbc-3.47.0.0.jar       # SQLite JDBC driver- **🚪 Logout** - Logout and exit

├── bin/                                # Compiled classes

├── securevault.db                      # SQLite database│   ├── UserManager.java               # User authenticationcd /home/abin/Documents/Secure_Valut/PRoejct

├── run.sh                              # Launch script

├── README.md                           # This file### Enhanced Dialog (9 Fields)

├── FEATURES.md                         # Detailed feature documentation

└── FIXES_APPLIED.md                    # Development changelog- **Title** - Credential name│   ├── LoginDialog.java               # Login UI

```

- **Username** - Account username

---

- **Password** - With "👁 Show/Hide" and "🎲 Generate" buttons│   ├── PasswordGeneratorDialog.java   # Password generator./run.sh- **Modern Design**: Clean, intuitive Java Swing interface- **Multiple Users**: Each user has a separate password vault## Project Structure## Folder Structure

## 🔧 Technical Details

- **Category** - Dropdown with custom input support

### Database Schema

- **users** - User accounts with hashed passwords- **Website** - URL field with "🌐 Open" button│   ├── StrengthChecker.java           # Password strength analysis

- **credentials** - Encrypted password storage with categories, expiry, timestamps

- **custom_categories** - User-defined categories- **Expiry** - Date field with "📅 +90 Days" button

- **attachments** - Encrypted file storage (BLOB)

- **Notes** - Multi-line notes (4 rows)│   └── StrengthCheckerDialog.java     # Strength checker UI```

### Security Implementation

- **Encryption**: AES-256-CBC with PKCS5 padding- **Favorite** - ⭐ checkbox

- **Key Derivation**: PBKDF2WithHmacSHA256 (100,000 iterations)

- **Authentication**: SHA-256 with 16-byte random salt- **Attachments** - (Manage after saving)├── lib/

- **Session Management**: In-memory SecretKey cleared on logout/timeout

- **Clipboard Security**: Automatic clipboard clearing after 30 seconds



### Requirements## 📊 Technical Details│   ├── sqlite-jdbc-3.44.1.0.jar       # SQLite driver- **Dual Theme Support**: Light and Dark themes with proper contrast

- **Java**: JDK 25 or higher

- **Operating System**: Linux, macOS, Windows

- **Dependencies**: SQLite JDBC 3.47.0.0 (included in lib/)

### Database Schema│   ├── slf4j-api-2.0.9.jar            # Logging API

---

```sql

## 📖 Usage Guide

credentials (│   └── slf4j-simple-2.0.9.jar         # Logging implementationOr manually:

### First Time Setup

1. Launch the application using `./run.sh`    id, user_id, title, username, 

2. Login with default credentials (Abin / Abin@2006)

3. Start adding your credentials!    password [encrypted], is_favorite,├── bin/                                # Compiled classes



### Adding a Credential    notes, category, website_url,

1. Click **➕ Add** button or press **Ctrl+N**

2. Fill in the required fields (Title, Username, Password)    expiry_date, created_date, modified_date,├── securevault.db                      # SQLite database (encrypted passwords)```bash- **Interactive Table**: Sortable credential table with visual strength indicators- **Default User**: Pre-configured user `Abin` with password `Abin@2006`

3. Optionally add:

   - Category    last_password_change

   - Website URL

   - Expiry date (or use +90 Days button))├── run.sh                              # Launch script

   - Notes

   - Attachments

4. Click **Save**

custom_categories (└── README.md                           # This filejavac -cp "lib/*:." -d bin src/*.java

### Using Categories

- Select from 7 default categories or create custom ones    id, user_id, category_name, color

- Categories are color-coded in the table view

- Filter credentials by category using the filter dropdown)```



### Managing Expiry Dates

- Set expiry dates to track password age

- Use the "+90 Days" button for quick 3-month expiryattachments (java -cp "lib/*:bin" SecureVaultSwing- **Context Menu**: Right-click menu for quick actions

- Visual indicators show expiry status (red/amber/green)

- Health Dashboard shows expired and expiring passwords    id, credential_id, filename,



### Import/Export    file_data [encrypted BLOB], file_size,## 🎯 Usage Guide

- **Import**: Tools → Import, select CSV or ZIP file

- **Export**: Tools → Export, creates encrypted ZIP with all data    encrypted, upload_date

- Duplicate detection prevents importing existing credentials

)```

### Health Dashboard

- Click **📊 Health** button to view security overview```

- Shows security score (0-100)

- Lists credentials needing attention:### Main Toolbar (11 Buttons)

  - Weak passwords

  - Expired passwords### Architecture

  - Expiring soon passwords

- Click any item to edit it directly- **Java 25** with Swing GUI1. **➕ Add** - Create new credential- **Live Strength Feedback**: Real-time password strength visualization- **Create New Users**: Easy user registration from login screen



### Attachments- **SQLite** database with JDBC driver

- In Add/Edit dialog, click "Upload Attachment" button

- Select files up to 10 MB- **AES-256-CBC** for password encryption2. **✏️ Edit** - Modify selected credential

- Files are encrypted and stored in database

- Download or delete attachments as needed- **PBKDF2** key derivation (100,000 iterations)



---- **SHA-256** for user authentication3. **🗑️ Delete** - Remove selected credential### **Default Login**



## 🛠️ Development- **15 Source Files** (~3,200 lines of code)



### Building from Source4. **📋 Copy Password** - Copy password to clipboard (auto-clears in 30s)

```bash

# Compile all Java files### New Classes (v3.0)

javac -cp "lib/*:." -d bin src/*.java

1. `DatabaseUpgrade.java` - Schema migration5. **👤 Copy Username** - Copy username to clipboard- **Username**: `Abin`

# Run the application

java -cp "lib/*:bin" SecureVaultSwingEnhanced2. `CategoryManager.java` - Category management

```

3. `AttachmentManager.java` - File handling6. **⭐ Favorite** - Toggle favorite status (golden star)

### Running with Script

```bash4. `HealthDashboard.java` - Security analysis

./run.sh

```5. `HealthDashboardDialog.java` - Visual report7. **🎲 Generate** - Open random password generator- **Password**: `Abin@2006`



---6. `ImportExportManager.java` - Import/Export



## 🐛 Known Issues & Limitations8. **📊 Strength** - Check password strength



- Maximum attachment size: 10 MB per file## ⌨️ Keyboard Shortcuts

- Session timeout: Fixed at 5 minutes

- Single database file (no cloud sync)9. **🔒 Lock** - Lock vault immediately### 💼 Credential Management- **Logout**: Switch between users without closing the application

- Platform-dependent URL opening behavior

- **Ctrl+F** - Focus search box

---

- **Ctrl+N** - Add new credential10. **🎨 Theme** - Toggle light/dark mode

## 📝 Changelog

- **Ctrl+Shift+C** - Copy password

### Version 3.0 (October 2025)

- ✅ Added Categories system with 7 defaults + custom support- **Ctrl+L** - Lock vault11. **🚪 Logout** - Logout and return to login screen---

- ✅ Added Website URL field with browser integration

- ✅ Added Password Expiry tracking with visual indicators- **Delete** - Delete selected credential

- ✅ Added Import/Export Manager (CSV/ZIP)

- ✅ Added Encrypted Attachments (up to 10 MB)- **Enter** - Edit selected credential

- ✅ Added Health Dashboard with security scoring

- ✅ Enhanced table view with 8 columns- **Escape** - Cancel/Close dialog

- ✅ Improved UI with color-coded categories

- ✅ Fixed all compiler warnings### Search & Filter Panel- **Add/Edit/Delete**: Full CRUD operations for credentials

- ✅ Code cleanup and optimization

## 📁 Project Structure

### Version 2.0

- Added Dark/Light theme support- **Search Box** - Type to filter credentials in real-time

- Added Favorites system

- Added Password strength analysis```

- Enhanced search and filter capabilities

- Added keyboard shortcutsSecureVault/- **Filter Dropdown** - All | Favorites | Weak | Medium | Strong passwords## 📖 Usage



### Version 1.0├── src/                                # Source code

- Initial release

- Basic password management│   ├── SecureVaultSwingEnhanced.java  # Main app (1157 lines)- **Sort Dropdown** - Sort by title, username, created date, modified date, favorites, or last modified

- AES-256 encryption

- Multi-user support│   ├── EnhancedCredentialDialog.java  # Add/Edit dialog



---│   ├── HealthDashboardDialog.java     # Security report UI- **Copy to Clipboard**: One-click copy for passwords and usernames```The workspace contains two folders by default, where:



## 📄 License│   ├── Database.java                  # Database operations



This project is for personal use only. All rights reserved.│   ├── DatabaseUpgrade.java           # Schema migration### Keyboard Shortcuts



---│   ├── CategoryManager.java           # Category handling



## 👤 Author│   ├── AttachmentManager.java         # File attachments- **Ctrl+F** - Focus search box| Action | Steps |



**Abin**│   ├── HealthDashboard.java           # Security analysis

- Built with ❤️ using Java Swing

- Secure by design, beautiful by choice│   ├── ImportExportManager.java       # Import/Export- **Ctrl+N** - Add new credential



---│   ├── PasswordEncryption.java        # AES-256 encryption



## 🙏 Acknowledgments│   ├── PasswordGeneratorDialog.java   # Password generator- **Delete** - Delete selected credential|--------|-------|- **Password Generator**: Customizable length and character sets### 🔑 Password Management



- SQLite JDBC Driver by Xerial│   ├── UserManager.java               # User authentication

- Samsung UI inspiration for gradient headers

- Java Swing community for UI patterns│   ├── LoginDialog.java               # Login UI- **Enter** - Edit selected credential



---│   ├── StrengthChecker.java           # Password strength



**Stay Secure! 🔐**│   └── StrengthCheckerDialog.java     # Strength UI| **Add Password** | Click "Add" → Enter title, username, password → OK |


├── lib/                                # Dependencies

│   ├── sqlite-jdbc-3.44.1.0.jar       # SQLite driver## 🔐 Security Implementation

│   ├── slf4j-api-2.0.9.jar            # Logging API

│   └── slf4j-simple-2.0.9.jar         # Logging impl| **Copy Password** | Select row → "Copy Password" (auto-clears in 30s) |- **Strength Checker**: Detailed password analysis with recommendations

├── bin/                                # Compiled classes

├── securevault.db                      # SQLite database### Password Encryption

├── run.sh                              # Launch script

├── README.md                           # This file- **Algorithm**: AES-256-CBC| **Edit Password** | Select row → "Edit" → Modify → OK |

└── FEATURES.md                         # Detailed features

- **Key Derivation**: PBKDF2WithHmacSHA256

Total: 15 source files, ~3,200 lines of code

```- **Iterations**: 100,000| **Delete Password** | Select row → "Delete" → Confirm |- **Add/Edit/Delete** credentials with title, username, and passwordPRoejct/



## 🛠️ Development- **Salt**: Unique 16-byte random salt per user



### Compile- **IV**: Random 16-byte initialization vector per password| **Lock Vault** | Click "🔒 Lock" button (top-right) |

```bash

javac -cp "lib/*:." -d bin src/*.java

```

### User Authentication| **Change Theme** | Click "Theme" button |## 🛠️ Technical Stack

### Run

```bash- **Hashing**: SHA-256

java -cp "lib/*:bin" SecureVaultSwingEnhanced

```- **Salt**: Unique 16-byte random salt per user| **Generate Password** | Click "Generate" in credential dialog |



### Or use the script- **Storage**: Only hashed passwords stored, never plaintext

```bash

./run.sh- **Copy to Clipboard** with auto-clear (12 seconds for security)

```

### Session Management

## 🔒 Security Best Practices

- **Timeout**: 5 minutes of inactivity---

1. ✅ Use a **strong master password** (min 12 characters)

2. ✅ Set **expiry dates** for important passwords- **Lock Button**: Instant manual locking

3. ✅ Use the **password generator** for new accounts

4. ✅ Check **Health Dashboard** regularly- **Clipboard Security**: Auto-clear after 30 seconds| Component | Technology |

5. ✅ Update **weak or reused** passwords

6. ✅ **Export backups** regularly

7. ✅ **Lock vault** when stepping away

8. ✅ Never share your master password## 📊 Database Schema## 🔐 Security Features



## 📈 Statistics



- **Total Features**: 40+ features### users table|-----------|-----------|- **Right-click menu** for quick password copy├── src/- `src`: the folder to maintain sources

- **Lines of Code**: ~3,200 lines

- **New Features (v3.0)**: 6 major additions- `id` - Primary key

- **New Code**: ~1,100 lines

- **Database Tables**: 4 tables- `username` - Unique username### **Encryption**

- **Security**: Triple-layered (AES-256 + SHA-256 + PBKDF2)

- `password` - SHA-256 hashed password

## 🎯 Use Cases

- `salt` - Random 16-byte salt (hex encoded)- **Algorithm**: AES-256-CBC| **Language** | Java 25 (with preview features) |

### Personal Use

- Store all your passwords securely

- Organize by categories

- Track password expiry### credentials table- **Key Derivation**: PBKDF2 (100,000 iterations)

- Store recovery codes as attachments

- `id` - Primary key

### Work/Business

- Organize work credentials- `user_id` - Foreign key to users table- **Storage**: Base64 encoded ciphertext in database| **GUI Framework** | Java Swing |- **Persistent Storage** in SQLite database

- Track password changes

- Export reports for compliance- `title` - Credential title

- Share credentials securely (export feature)

- `username` - Account username- **Protection**: Database file useless without user password

### Migration

- Import from Chrome, Firefox, Edge, Opera- `password` - AES-256 encrypted password

- Export to encrypted backups

- Transfer between devices- `is_favorite` - Boolean (0 or 1)| **Database** | SQLite 3 |



## 📋 Requirements- `notes` - Optional text notes



- **Java**: Version 25 or higher- `created_date` - ISO 8601 timestamp### **Session Management**

- **OS**: Linux, macOS, Windows

- **RAM**: 512 MB minimum- `modified_date` - ISO 8601 timestamp

- **Disk**: 50 MB for app + database

- Auto-locks after 5 minutes of inactivity| **JDBC Driver** | sqlite-jdbc-3.44.1.0.jar |│   ├── SecureVaultSwing.java       # Main UI application- `lib`: the folder to maintain dependencies

## 📄 License

## 🎨 UI Design

This project is for **personal use and educational purposes**.

- Manual lock button available

## 👤 Author

### Color Scheme

Created by **Abin** - A secure password manager with beautiful UI and advanced features.

- **Light Mode**: Purple (#6554C0) primary with white backgrounds- Encryption keys cleared on logout/lock| **Logging** | SLF4J 2.0.9 |

---

- **Dark Mode**: Blue (#5865F2) primary with dark backgrounds

## 🎉 What's New in v3.0

- **Favorites**: Golden (#FFC107) stars- Activity tracking (mouse, keyboard)

### ✨ Major Features Added

1. **📂 Categories** - 7 defaults + custom- **Gradient**: Purple to blue smooth gradient

2. **🌐 Website URLs** - Open in browser

3. **📅 Password Expiry** - Visual warnings| **Security** | java.security.MessageDigest (SHA-256) |### 🎨 User Interface

4. **📊 Health Dashboard** - Security analysis

5. **📤 Import/Export** - Multiple formats### Visual Elements

6. **📎 Attachments** - Encrypted files

- Gradient header banner (60px height)### **Clipboard Security**

### 🎨 UI Enhancements

- Enhanced table with Category and Expiry columns- Alternating row colors in table

- Color-coded category badges

- Visual expiry warnings (red/amber/green)- Hover effects on buttons (20% brightness increase)- Passwords auto-clear after 30 seconds| **Build Tool** | javac (manual compilation) |

- Beautiful Health Dashboard dialog

- 13 toolbar buttons (was 11)- Color-coded password strength bars

- 9-field Add/Edit dialog (was 5)

- Modern rounded buttons with emoji icons- Usernames don't auto-clear (less sensitive)

### 🔧 Technical Improvements

- Database schema upgraded (4 new columns)

- 6 new classes added

- Import/Export functionality## 🛠️ Development- Smart clearing (won't interfere with other apps)- **Clean Modern Design** with themed interface│   ├── Database.java                # SQLite database handler (CRUD operations)

- Attachment encryption

- NULL value handling

- Enhanced error messages

### Compilation

---

```bash

**Version**: 3.0 - Ultimate Edition  

**Last Updated**: October 29, 2025  javac -cp "lib/*:." -d bin src/*.java---## 📁 Project Structure

**Status**: Production Ready ✅  

**Total Development Time**: Full-stack implementation completed  ```

**Code Quality**: Professional, documented, optimized



🔐 **Stay Secure!**

### Manual Execution

```bash## 📊 Project Status- **Password Strength Meter**: 5-segment visual bars showing strength

java -cp "lib/*:bin" SecureVaultSwingEnhanced

```



## 📝 Notes**Version**: 1.0.0  ```



- All passwords are encrypted using AES-256 before storage**Security Score**: 75/100  

- Session automatically locks after 5 minutes of inactivity

- Copied passwords are automatically cleared from clipboard after 30 seconds**Phase**: 1 of 7 Complete ✅PRoejct/- **Live Strength Indicator**: Real-time strength check while typing│   ├── PasswordGenerator.java      # Password generation utilityMeanwhile, the compiled output files will be generated in the `bin` folder by default.

- Duplicate credentials (same title + username) are prevented

- Database file is stored in the project root directory

- Compiled classes are stored in the `bin/` directory

- [x] Phase 1: Critical Security (Encryption, session, clipboard)├── src/

## 🔒 Security Best Practices

- [ ] Phase 2: Build System (Maven, installers)

1. **Strong Master Password** - Use a strong password for your user account

2. **Regular Updates** - Keep your credentials updated- [ ] Phase 3: Testing (Unit tests, CI/CD)│   ├── SecureVaultSwing.java         # Main application & UI- **Show Password Toggle**: View passwords in login screen

3. **Use Password Generator** - Generate strong random passwords

4. **Check Password Strength** - Use the strength checker for existing passwords- [ ] Phase 4: Enhanced Features

5. **Lock When Away** - Always lock the vault when stepping away

- [ ] Phase 5: Advanced Security (2FA)│   ├── Database.java                 # SQLite CRUD operations

## 📄 License

- [ ] Phase 6: Distribution

This project is for personal use and educational purposes.

- [ ] Phase 7: Legal & Compliance│   ├── UserManager.java              # Authentication & user management- **Themes**: Light, Dark, and Custom color schemes│   ├── StrengthChecker.java        # Password strength checking

## 👤 Author



Created by Abin - A secure password manager with beautiful UI and advanced features.

---│   ├── LoginDialog.java              # Login/registration dialog

---



**Version**: 2.0 Enhanced Edition  

**Last Updated**: October 2025  ## 📁 Project Structure│   ├── CredentialDialog.java         # Add/edit credential dialog

**Status**: Production Ready ✅



```│   ├── PasswordGeneratorDialog.java  # Password generator UI

SecureVault/

├── src/                    # Java source code (10 classes)│   ├── StrengthChecker.java          # Password strength algorithm### 🛠️ Password Tools│   ├── ThemeManager.java           # UI theme management> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

├── lib/                    # Dependencies (SQLite JDBC, SLF4J)

├── bin/                    # Compiled classes│   └── StrengthCheckerDialog.java    # Animated strength display

├── docs/                   # Documentation

│   ├── phase1/            # Phase 1 implementation docs├── lib/- **Password Generator**: Create strong passwords with customizable options

│   └── archive/           # Archived documentation

├── expppp/                # Code explanations│   ├── sqlite-jdbc-3.44.1.0.jar     # SQLite JDBC driver

├── securevault.db         # Main database (encrypted)

└── run.sh                 # Launch script│   ├── slf4j-api-2.0.9.jar          # SLF4J API  - Length control (8-32 characters)│   ├── ClipboardHelper.java        # Clipboard operations with auto-clear

```

│   └── slf4j-simple-2.0.9.jar       # SLF4J implementation

---

├── bin/                              # Compiled .class files  - Character types: uppercase, lowercase, digits, symbols

## 🔄 Migration (Existing Users)

├── securevault.db                    # SQLite database

If you have plain-text passwords (before encryption was added):

├── run.sh                            # Launch script  - Live strength preview│   ├── CredentialDialog.java       # Add/Edit credential dialog## Dependency Management

```bash

java -cp "lib/*:bin" DatabaseMigration└── README.md                         # This file

```

```  

Follow prompts to enter password for each user. See `docs/phase1/MIGRATION_GUIDE.md` for details.



---

## 🚀 Getting Started- **Password Strength Checker**: Analyze password security│   ├── PasswordGeneratorDialog.java # Password generator UI

## 📚 Documentation



- **README.md** - This file (quick start)

- **PRODUCTION_ROADMAP.md** - 7-phase production plan### Prerequisites  - Animated strength meter

- **FUTURE_ENHANCEMENTS.md** - Planned features (20+ enhancements)

- **PROJECT_ARCHITECTURE_AND_METHODOLOGY.md** - Architecture details- **Java 25+** (OpenJDK or Oracle JDK)

- **docs/phase1/** - Phase 1 implementation documentation

- **expppp/** - Line-by-line code explanations- **SQLite3** (for database management, optional)  - Real-time suggestions for improvement│   └── StrengthCheckerDialog.java  # Strength checker UI with animationsThe `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).



---- **Linux/Unix** environment (tested on Kali Linux)



## 🛠️ Technical Details  - Show/hide password toggle



### **Dependencies**### Installation

- Java 21+

- SQLite JDBC 3.44.1.0├── lib/

- SLF4J 2.0.9

1. **Clone/Download** the project to your system

### **Database**

```sql## Getting Started│   └── sqlite-jdbc-3.44.1.0.jar   # SQLite JDBC driver

users (id, username, password_hash, salt, created_at)

credentials (id, user_id, title, username, password)2. **Verify Java Installation**

```

   ```bash├── bin/                            # Compiled classes

### **Encryption Specs**

- AES/CBC/PKCS5Padding, 256-bit key   java --version

- PBKDF2WithHmacSHA256, 100,000 iterations

- Random 128-bit IV per password   # Should show Java 25 or higher### Prerequisites└── securevault.db                  # SQLite database (created at runtime)



---   ```



## 🧪 Testing- Java 17 or higher```



### **Verify Encryption**3. **Run the Application**

```bash

sqlite3 securevault.db "SELECT password FROM credentials LIMIT 1;"   ```bash- Linux/Unix-based system (tested on Kali Linux)

# Should see: 5BiYOKyP53aAfzSbSfsbhyXxsdA93rKat1UCR+ZPfZ0=

# NOT plain text!   chmod +x run.sh

```

   ./run.sh## Features

### **Test Features**

- ✅ Session timeout (wait 5 min)   ```

- ✅ Clipboard auto-clear (wait 31 sec after copy)

- ✅ Manual lock button   ### Installation

- ✅ Add/edit/delete credentials

- ✅ Password generator   The `run.sh` script automatically:

- ✅ Strength checker

   - Downloads required JAR dependencies if missing### ✅ Implemented

---

   - Compiles all Java source files

## 🎯 Future Enhancements

   - Launches the application with proper classpath1. Clone or extract the project to your system- **SQLite Database Persistence**: All credentials stored in local database

See `FUTURE_ENHANCEMENTS.md` for complete list (20+ features):



- Browser extension (Chrome, Firefox, Edge)

- File vault (encrypt photos, videos, documents)### Manual Compilation (Alternative)- **Password Strength Meter**: 

- Two-Factor Authentication (TOTP)

- Password breach detection

- Cloud sync with E2E encryption

- Mobile app (Android/iOS)```bash2. Navigate to the project directory:  - Live 5-segment bar in table

- Import/Export (CSV, 1Password, LastPass)

- And more...# Compile all source files



---javac -cp "lib/*:." -d bin src/*.java```bash  - Animated strength checker dialog



## 🚨 Important Notes



⚠️ **Lost Password = Lost Data** (by design!)  # Run the applicationcd /home/abin/Documents/Secure_Valut/PRoejct  - Suggestions for improvement

⚠️ **Always backup**: `cp securevault.db securevault_backup.db`  

⚠️ **Keep passwords safe** - no recovery mechanismjava -cp "lib/*:bin" SecureVaultSwing



---``````- **Password Generator**: Customizable length and character sets



## 📞 Support



### **Common Issues**## 👤 Default Users & Login- **Clipboard Auto-Clear**: Copies passwords with automatic clearing after timeout



**Q: Passwords still plain text?**  

A: Run migration: `java -cp "lib/*:bin" DatabaseMigration`

The system comes with two pre-configured users:3. Make the run script executable:- **Theming**: Light, Dark, and Custom themes

**Q: Can't decrypt passwords?**  

A: Make sure you entered correct login password



**Q: Session timeout too fast?**  | Username | Password | Credentials |```bash- **Modular Architecture**: Clean separation of concerns

A: Edit `SecureVaultSwing.java`, change `SESSION_TIMEOUT` (default 5 min)

|----------|----------|-------------|

**Q: Clipboard not clearing?**  

A: Normal if another app used clipboard after copy| **Abin** | Abin@2006 | 55 sample credentials |chmod +x run.sh



---| **Advaith** | Advaith@2006 | 29 sample credentials |



## 📈 Metrics```### Database Operations



- **Code**: 2,300+ lines of Java### First Time Login

- **Classes**: 10 Java source files

- **Documentation**: 8,000+ lines1. Launch the application- **Insert**: Add new credentials (Title, Username, Password)

- **Security Score**: 75/100

- **Users**: Multi-user support (3 users currently)2. Enter username and password

- **Credentials**: Unlimited encrypted storage

3. Click **Login** to access your vault### Running the Application- **Update**: Edit existing credentials

---

4. Or click **Create User** to register a new account

## 🎉 Status

- **Delete**: Remove credentials from database

✅ **Ready for Daily Use!**

- Core features: Complete## 📖 User Guide

- Security: AES-256 encryption active

- Documentation: ComprehensiveSimply run:- **Select**: Load all credentials on login

- Testing: Verified and working

### Managing Credentials

**56 credentials successfully encrypted! 🔒**

```bash

---

#### Add New Credential

**Version**: 1.0.0  

**Author**: Abin  1. Click **Add** button in toolbar./run.sh## Compilation

**Date**: October 29, 2025  

**Status**: Production Ready (Phase 1 Complete)2. Fill in the form:



---   - **Service**: Website/app name (e.g., "GitHub")```



## 📋 Quick Commands   - **Username**: Your username/email



```bash   - **Password**: Your password```bash

# Compile

javac -cp "lib/*:." -d bin src/*.java   - **Category**: Optional grouping (e.g., "Social Media")



# Run3. Watch the live strength meter as you typeOr manually:javac -cp "lib/*:." -d bin src/*.java

java -cp "lib/*:bin" SecureVaultSwing

4. Click **Save**

# Or use script

./run.sh```bash```



# Migration#### Edit Credential

java -cp "lib/*:bin" DatabaseMigration

1. Select a credential row in the tablejavac -cp "lib/*:." -d bin src/*.java

# Backup

cp securevault.db securevault_backup_$(date +%Y%m%d).db2. Click **Edit** button



# Check encryption3. Modify the fieldsjava -cp "lib/*:bin" SecureVaultSwing## Running

sqlite3 securevault.db "SELECT password FROM credentials LIMIT 3;"

```4. Click **Save**



---```



**🚀 Ready to secure your passwords!**#### Delete Credential


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

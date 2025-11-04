# Installation Guide - SecureVault Pro

Complete installation instructions for all platforms.

---

## 📋 Table of Contents

- [System Requirements](#system-requirements)
- [Linux Installation](#linux-installation)
- [macOS Installation](#macos-installation)
- [Windows Installation](#windows-installation)
- [IDE Setup](#ide-setup)
- [Troubleshooting](#troubleshooting)

---

## 📦 System Requirements

### Minimum Requirements

- **Java:** JDK 17 or higher (OpenJDK or Oracle JDK)
- **RAM:** 512 MB minimum, 1 GB recommended
- **Disk Space:** 50 MB for application + dependencies
- **Display:** 1024x768 minimum resolution

### Recommended System

- **Java:** OpenJDK 21 or latest LTS version
- **RAM:** 2 GB or more
- **Disk Space:** 100 MB for comfortable usage
- **Display:** 1920x1080 or higher

---

## 🐧 Linux Installation

### Quick Install (Recommended)

```bash
# Clone the repository
git clone https://github.com/yourusername/SecureVault-Pro.git
cd SecureVault-Pro

# Make run script executable
chmod +x run.sh

# Run the application
./run.sh
```

The script automatically:
- Downloads required libraries (SQLite JDBC, SLF4J)
- Compiles Java sources
- Launches the application

### Step-by-Step Installation

#### 1. Install Java (if not already installed)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-17-jdk
```

**Fedora/RHEL:**
```bash
sudo dnf install java-17-openjdk-devel
```

**Arch Linux:**
```bash
sudo pacman -S jdk-openjdk
```

#### 2. Verify Java Installation

```bash
java -version
javac -version
```

Should show Java 17 or higher.

#### 3. Clone Repository

```bash
git clone https://github.com/yourusername/SecureVault-Pro.git
cd SecureVault-Pro
```

#### 4. Run the Application

```bash
chmod +x run.sh
./run.sh
```

### Manual Compilation (Linux)

If you prefer manual control:

```bash
# Create output directory
mkdir -p bin

# Ensure dependencies are present
ls lib/*.jar

# Compile all Java sources
javac -Xlint:all -cp "lib/*:." -d bin src/*.java

# Run the application
java -cp "bin:lib/*" SecureVaultSwingEnhanced
```

### Creating Desktop Launcher (Linux)

Create `~/.local/share/applications/securevault.desktop`:

```desktop
[Desktop Entry]
Version=1.0
Type=Application
Name=SecureVault Pro
Comment=Secure Password Manager
Exec=/path/to/SecureVault-Pro/run.sh
Icon=/path/to/SecureVault-Pro/icon.png
Terminal=false
Categories=Utility;Security;
```

Replace `/path/to/SecureVault-Pro` with actual path.

---

## 🍎 macOS Installation

### Quick Install (Recommended)

```bash
# Clone the repository
git clone https://github.com/yourusername/SecureVault-Pro.git
cd SecureVault-Pro

# Make run script executable
chmod +x run.sh

# Run the application
./run.sh
```

### Step-by-Step Installation

#### 1. Install Java

**Option A: Using Homebrew (Recommended)**
```bash
# Install Homebrew if not already installed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install OpenJDK
brew install openjdk@17

# Link Java
sudo ln -sfn $(brew --prefix)/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

**Option B: Download from Oracle**
- Visit [Oracle Java Downloads](https://www.oracle.com/java/technologies/downloads/)
- Download macOS installer
- Run the installer

#### 2. Verify Java Installation

```bash
java -version
javac -version
```

#### 3. Clone and Run

```bash
git clone https://github.com/yourusername/SecureVault-Pro.git
cd SecureVault-Pro
chmod +x run.sh
./run.sh
```

### Manual Compilation (macOS)

```bash
# Create output directory
mkdir -p bin

# Compile
javac -Xlint:all -cp "lib/*:." -d bin src/*.java

# Run
java -cp "bin:lib/*" SecureVaultSwingEnhanced
```

### Creating App Bundle (macOS)

For a native macOS experience, you can create an app bundle:

```bash
# Create app structure
mkdir -p SecureVaultPro.app/Contents/MacOS
mkdir -p SecureVaultPro.app/Contents/Resources

# Create launch script
cat > SecureVaultPro.app/Contents/MacOS/SecureVault <<'EOF'
#!/bin/bash
cd "$(dirname "$0")/../../.."
./run.sh
EOF

chmod +x SecureVaultPro.app/Contents/MacOS/SecureVault

# Create Info.plist
cat > SecureVaultPro.app/Contents/Info.plist <<'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleName</key>
    <string>SecureVault Pro</string>
    <key>CFBundleExecutable</key>
    <string>SecureVault</string>
    <key>CFBundleIdentifier</key>
    <string>com.securevault.pro</string>
    <key>CFBundleVersion</key>
    <string>3.0</string>
</dict>
</plist>
EOF
```

---

## 🪟 Windows Installation

### Prerequisites

#### 1. Install Java

**Option A: Using Winget (Windows 10/11)**
```powershell
winget install Microsoft.OpenJDK.17
```

**Option B: Manual Download**
- Visit [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/)
- Download Windows installer (MSI or EXE)
- Run installer with default options
- Verify "Add to PATH" is checked

#### 2. Verify Java Installation

Open Command Prompt or PowerShell:
```powershell
java -version
javac -version
```

### Installation Steps

#### 1. Install Git (if not already installed)

Download from [git-scm.com](https://git-scm.com/download/win)

#### 2. Clone Repository

```powershell
git clone https://github.com/yourusername/SecureVault-Pro.git
cd SecureVault-Pro
```

#### 3. Create Run Script for Windows

Create `run.bat`:

```batch
@echo off
echo Starting SecureVault Pro...

REM Check if bin directory exists
if not exist bin mkdir bin

REM Check for dependencies
if not exist lib\sqlite-jdbc-3.44.1.0.jar (
    echo Downloading SQLite JDBC driver...
    curl -L -o lib\sqlite-jdbc-3.44.1.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.44.1.0/sqlite-jdbc-3.44.1.0.jar
)

if not exist lib\slf4j-api-2.0.9.jar (
    echo Downloading SLF4J API...
    curl -L -o lib\slf4j-api-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar
)

if not exist lib\slf4j-simple-2.0.9.jar (
    echo Downloading SLF4J Simple...
    curl -L -o lib\slf4j-simple-2.0.9.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar
)

REM Compile if needed
echo Compiling sources...
javac -Xlint:all -cp "lib/*;." -d bin src/*.java

REM Run application
echo Launching SecureVault Pro...
java -cp "bin;lib/*" SecureVaultSwingEnhanced
```

#### 4. Run the Application

```powershell
.\run.bat
```

### Manual Compilation (Windows)

```powershell
# Create output directory
mkdir bin

# Compile
javac -Xlint:all -cp "lib/*;." -d bin src/*.java

# Run
java -cp "bin;lib/*" SecureVaultSwingEnhanced
```

### Creating Windows Shortcut

1. Right-click on desktop → New → Shortcut
2. Location: `C:\path\to\SecureVault-Pro\run.bat`
3. Name: SecureVault Pro
4. Right-click shortcut → Properties
5. Change icon (optional)
6. Set "Start in" to SecureVault-Pro directory

---

## 🛠️ IDE Setup

### IntelliJ IDEA

1. **Open Project:**
   - File → Open
   - Select SecureVault-Pro folder

2. **Configure JDK:**
   - File → Project Structure → Project
   - SDK: Select Java 17 or higher

3. **Add Libraries:**
   - File → Project Structure → Libraries
   - Add → Java
   - Select all JAR files in `lib/` folder

4. **Run Configuration:**
   - Run → Edit Configurations
   - Add → Application
   - Main class: `SecureVaultSwingEnhanced`
   - Classpath: Select main module

### Eclipse

1. **Import Project:**
   - File → Import → General → Existing Projects into Workspace
   - Select SecureVault-Pro folder

2. **Configure Build Path:**
   - Right-click project → Properties
   - Java Build Path → Libraries
   - Add External JARs → Select lib/*.jar

3. **Run:**
   - Right-click `SecureVaultSwingEnhanced.java`
   - Run As → Java Application

### VS Code

1. **Install Extensions:**
   - Java Extension Pack (Microsoft)

2. **Open Folder:**
   - File → Open Folder → Select SecureVault-Pro

3. **Configure:**
   - VS Code auto-detects Java project
   - Ensure `lib/*.jar` in classpath

4. **Run:**
   - Open `SecureVaultSwingEnhanced.java`
   - Click Run button or F5

---

## 🔧 Troubleshooting

### Common Issues

#### Java Not Found

**Problem:** `java: command not found`

**Solution:**
- Ensure Java is installed
- Add Java to PATH environment variable
- Restart terminal/command prompt

**Linux/macOS:**
```bash
export JAVA_HOME=/path/to/java
export PATH=$JAVA_HOME/bin:$PATH
```

**Windows:**
- System Properties → Environment Variables
- Add `C:\Program Files\Java\jdk-17\bin` to PATH

#### Compilation Errors

**Problem:** `javac: package javax.swing does not exist`

**Solution:**
- Ensure you're using JDK, not JRE
- Install full JDK with development tools

#### Library Not Found

**Problem:** `ClassNotFoundException: org.sqlite.JDBC`

**Solution:**
- Ensure lib/*.jar files are present
- Run `run.sh` to auto-download
- Or manually download from Maven Central

#### Permission Denied

**Problem:** `Permission denied: ./run.sh`

**Solution:**
```bash
chmod +x run.sh
```

#### Display Issues

**Problem:** UI appears too small/large

**Solution:**
```bash
# Force specific DPI scaling
java -Dsun.java2d.uiScale=1.5 -cp "bin:lib/*" SecureVaultSwingEnhanced
```

### Getting More Help

If you encounter issues not covered here:

1. Check [GitHub Issues](https://github.com/yourusername/SecureVault-Pro/issues)
2. Search existing issues
3. Create new issue with:
   - Operating system and version
   - Java version (`java -version`)
   - Complete error message
   - Steps to reproduce

---

## ✅ Verification

After installation, verify everything works:

1. **Launch application** - No errors on startup
2. **Login** with demo credentials (`test` / `12345`)
3. **View credentials** - 8 demo credentials visible
4. **Add credential** - Can add new entry
5. **Search** - Search functionality works
6. **Lock/Unlock** - Ctrl+L locks, login unlocks

---

## 🔄 Updating

To update to the latest version:

```bash
cd SecureVault-Pro
git pull origin main
./run.sh
```

Your database (`securevault.db`) is preserved during updates.

---

## 🗑️ Uninstallation

To remove SecureVault Pro:

```bash
# Backup your database first!
cp securevault.db ~/securevault-backup.db

# Remove application
cd ..
rm -rf SecureVault-Pro

# Optional: Remove database
rm ~/securevault-backup.db
```

---

## 📞 Support

Need help? 

- 📖 Check [README.md](../README.md)
- 🔍 Search [existing issues](https://github.com/yourusername/SecureVault-Pro/issues)
- 💬 Open [new issue](https://github.com/yourusername/SecureVault-Pro/issues/new)

---

**Happy Installing! 🎉**

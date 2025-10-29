# SecureVault - Quick Start Guide

## 🚀 5-Minute Setup

### Step 1: Verify Requirements
```bash
# Check Java version (need Java 21+)
java --version

# If not installed:
sudo apt update
sudo apt install openjdk-25-jdk
```

### Step 2: Run the Application
```bash
cd /path/to/PRoejct
chmod +x run.sh
./run.sh
```

That's it! The script automatically:
- ✅ Downloads required libraries
- ✅ Compiles source code
- ✅ Launches the application

### Step 3: Login
Use one of the default accounts:

| Username | Password |
|----------|----------|
| Abin | Abin@2006 |
| Advaith | Advaith@2006 |

---

## 📱 Basic Usage

### Add a Password
1. Click **Add** button
2. Fill in the form:
   - Service: "Gmail"
   - Username: "myemail@gmail.com"
   - Password: "MySecurePass123!"
   - Category: "Email"
3. Click **Save**

### Copy a Password
1. Select the row
2. Click **Copy Password**
3. Paste anywhere (Ctrl+V)

### Generate Random Password
1. Click **Generate**
2. Choose length and character types
3. Click **Generate**
4. Password copied automatically!

### Check Password Strength
1. Select a row
2. Click **Check Strength**
3. View detailed analysis

### Switch Theme
- Click **Theme** button to toggle Light/Dark mode

### Logout
- Click **Logout** to return to login screen

---

## 🎯 Common Tasks

### Create Your Own Account
1. Click **Create User** on login screen
2. Enter username and password (min 6 chars)
3. Start adding your passwords!

### Edit Existing Password
1. Select the row
2. Click **Edit**
3. Modify fields
4. Click **Save**

### Delete Password
1. Select the row
2. Click **Delete**
3. Confirm deletion

### Right-Click Menu
- Right-click any row for quick copy options

---

## ⚡ Keyboard Shortcuts

- **Enter** in password field → Login
- **Ctrl+C** (after copying) → Paste password
- **Double-click** row → Edit credential

---

## 🆘 Troubleshooting

### "JDBC Driver not found"
```bash
# Run this once:
./run.sh
# It will download missing libraries
```

### "Wrong password"
- Double-check: Username = "Abin", Password = "Abin@2006" (case-sensitive)
- Try other account: "Advaith" / "Advaith@2006"

### Application won't start
```bash
# Check Java:
java --version

# Should show Java 21 or higher
# If not, install:
sudo apt install openjdk-25-jdk
```

### Database locked
```bash
# Close all instances:
pkill -9 java

# Restart:
./run.sh
```

---

## 📖 Next Steps

- Read **README.md** for full feature list
- Read **TECHNICAL_DOCUMENTATION.md** for implementation details
- Explore the application and add your passwords!

---

**Quick Links**:
- 📝 Full Documentation: README.md
- 🔧 Technical Details: TECHNICAL_DOCUMENTATION.md
- 💾 Database: securevault.db
- 📂 Source Code: src/ directory

**Support**: Check README.md troubleshooting section for detailed help

---

**Last Updated**: October 28, 2025

# Quick Reference - SecureVault Pro

## 🚀 One-Line Quick Start

```bash
chmod +x run.sh && ./run.sh
```

## 📝 Demo Login Credentials
- **Username:** `test`
- **Password:** `12345`

> 💡 The demo account includes 8 sample credentials to showcase all features!

## ⌨️ Essential Shortcuts
| Shortcut | Action |
|----------|--------|
| `Ctrl+N` | Add new credential |
| `Ctrl+F` | Search |
| `Ctrl+L` | Lock vault |
| `Ctrl+Shift+C` | Copy password |
| `Del` | Delete selected |
| `Enter` | Edit selected |

## 🔒 Security Features
- AES-256-CBC encryption
- PBKDF2 (100,000 iterations)
- SHA-256 authentication
- Auto-lock after 5 minutes
- Clipboard auto-clear (30s)

## 📂 Key Files
- `src/` - Java source code
- `lib/` - Dependencies (SQLite, SLF4J)
- `docs/` - Full documentation
- `securevault.db` - Your encrypted vault
- `run.sh` - Launch script

## 🛠️ Manual Commands

### Compile
```bash
mkdir -p bin
javac -Xlint:all -cp "lib/*:." -d bin src/*.java
```

### Run
```bash
java -cp "bin:lib/*" SecureVaultSwingEnhanced
```

### Clean Build
```bash
rm -rf bin && mkdir bin && javac -cp "lib/*:." -d bin src/*.java
```

## 📚 Documentation
- [Full README](README.md) - Complete guide
- [FEATURES.md](docs/FEATURES.md) - All features
- [CHANGELOG.md](docs/CHANGELOG.md) - Version history
- [FIXES_APPLIED.md](docs/FIXES_APPLIED.md) - Bug fixes

## 💡 Tips
1. Change default password immediately
2. Use strong master passwords
3. Enable favorites for important credentials
4. Regular exports for backup
5. Check Health Dashboard for weak passwords

---
For detailed information, see [README.md](README.md)

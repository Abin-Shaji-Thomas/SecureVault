# Security Policy

## 🔒 Security Overview

SecureVault Pro takes security seriously. This document describes our security features, known limitations, and how to report vulnerabilities responsibly.

---

## 🛡️ Security Features

### Encryption

**Algorithm:** AES-256-CBC (Advanced Encryption Standard)
- Industry-standard symmetric encryption
- 256-bit key length provides strong security
- CBC mode with unique IV for each password

**Key Derivation:** PBKDF2 (Password-Based Key Derivation Function 2)
- SHA-256 hash function
- 100,000 iterations (NIST recommended minimum)
- Unique salt per user (16 bytes, cryptographically random)
- Keys derived from user's master password

**Implementation:**
```java
// Key derivation
SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 256);
SecretKey key = factory.generateSecret(spec);

// Encryption
Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
```

### Authentication

**User Authentication:** SHA-256 with Salt
- Passwords never stored in plain text
- Unique random salt per user (16 bytes)
- Hash stored in database for verification

**No Default Backdoors**
- No hardcoded passwords or master keys
- No remote access capabilities
- No telemetry or data collection

### Session Security

**Auto-Lock Feature**
- 5-minute timeout after inactivity
- Encryption keys cleared from memory on lock
- Re-authentication required to access vault

**Manual Lock**
- Instant lock with Ctrl+L keyboard shortcut
- Clears encryption keys from memory immediately

### Clipboard Security

**Auto-Clear Mechanism**
- Copied passwords cleared after 30 seconds
- Prevents clipboard monitoring attacks
- User notification when clipboard is cleared

### Memory Protection

**Key Management**
- Encryption keys stored in memory only during active session
- Keys cleared from memory on logout/lock
- No keys written to disk or swap

---

## 🎯 Threat Model

### What SecureVault Pro PROTECTS Against

✅ **Unauthorized Database Access**
- Passwords encrypted at rest
- Cannot read passwords without master password

✅ **Password Disclosure via Clipboard**
- Auto-clear prevents prolonged clipboard exposure
- Reduces risk of clipboard monitoring

✅ **Weak Password Usage**
- Strength checker encourages strong passwords
- Password generator creates cryptographically secure passwords
- Health dashboard identifies weak/reused passwords

✅ **Session Hijacking**
- Auto-timeout prevents access to unlocked vault
- Keys cleared from memory on timeout

✅ **Data Loss**
- Import/Export feature for backups
- Local storage ensures no cloud breaches

### What SecureVault Pro DOES NOT Protect Against

❌ **Compromised Operating System**
- Keyloggers can capture master password
- Memory dumps could expose active session keys
- Screen capture could reveal visible passwords

❌ **Physical Access to Unlocked Application**
- Anyone with access to unlocked vault can view passwords
- Always lock vault when leaving computer

❌ **Weak Master Passwords**
- User-chosen passwords may be weak
- Dictionary attacks possible on weak master passwords
- Users responsible for strong master password selection

❌ **Side-Channel Attacks**
- Timing attacks, power analysis not addressed
- Assumes secure execution environment

❌ **Social Engineering**
- Cannot prevent users from disclosing passwords
- Cannot prevent phishing attacks

❌ **Network-Based Attacks**
- Not applicable - SecureVault Pro is 100% offline
- No network communication occurs

---

## 🔍 Known Limitations

### Database Security

**SQLite Storage**
- Database file stored locally: `securevault.db`
- Only passwords are encrypted, not metadata (titles, usernames, URLs)
- Database structure visible to anyone with file access

**Recommendation:** Store the database on an encrypted filesystem for additional protection.

### Java Security

**JVM Dependency**
- Security depends on JVM security
- Vulnerable to JVM exploits
- Keep Java updated to latest version

### Memory Management

**Garbage Collection**
- Java's garbage collector may leave sensitive data in memory
- Strings are immutable - password copies may exist temporarily
- Use char[] arrays cleared immediately where possible

### No Multi-Factor Authentication

- Only password-based authentication currently
- No hardware token or biometric support
- Planned for future releases

---

## 📢 Reporting Security Vulnerabilities

We take security vulnerabilities seriously. If you discover a security issue, please follow responsible disclosure practices.

### How to Report

**DO NOT** open a public GitHub issue for security vulnerabilities.

Instead:

1. **Email:** Send details to the project maintainers (create a GitHub issue titled "Security: Need Contact" to get contact info)
2. **Include:**
   - Description of the vulnerability
   - Steps to reproduce
   - Potential impact
   - Suggested fix (if any)
3. **Wait:** Give us reasonable time to respond and fix (typically 90 days)

### What to Expect

1. **Acknowledgment** within 48 hours
2. **Assessment** within 1 week
3. **Fix development** (timeframe depends on severity)
4. **Public disclosure** after fix is released

### Bug Bounty

Currently, we do not have a paid bug bounty program. However:
- Security researchers will be credited in CHANGELOG
- Significant contributions may be featured in release notes
- We deeply appreciate responsible disclosure

---

## 🚨 Security Best Practices for Users

### Master Password

✅ **DO:**
- Use a strong, unique master password (12+ characters)
- Include uppercase, lowercase, numbers, and special characters
- Use a passphrase: "correct-horse-battery-staple" style
- Change periodically (every 6-12 months)

❌ **DON'T:**
- Use common passwords (password123, qwerty, etc.)
- Reuse passwords from other services
- Write down master password unless secured
- Share master password with others

### Application Usage

✅ **DO:**
- Lock vault when leaving computer (Ctrl+L)
- Keep Java updated to latest security patches
- Store database on encrypted filesystem
- Regular backups using Export feature
- Enable password expiry reminders

❌ **DON'T:**
- Leave vault unlocked and unattended
- Run on untrusted/compromised systems
- Store database in cloud storage without encryption
- Ignore password health warnings

### System Security

✅ **DO:**
- Keep operating system updated
- Use antivirus/anti-malware software
- Enable full-disk encryption
- Use secure, updated Java runtime
- Regular system security scans

❌ **DON'T:**
- Run as administrator/root unnecessarily
- Disable system security features
- Install software from untrusted sources
- Ignore OS security updates

---

## 🔐 Cryptographic Details

### Encryption Specifications

| Feature | Specification |
|---------|--------------|
| **Password Encryption** | AES-256-CBC |
| **Key Derivation** | PBKDF2-HMAC-SHA256 |
| **Iterations** | 100,000 |
| **Salt Size** | 16 bytes (128 bits) |
| **IV Size** | 16 bytes (128 bits) |
| **Random Number Generator** | SecureRandom |
| **Authentication Hash** | SHA-256 |

### Why These Choices?

**AES-256-CBC:**
- NIST approved
- Widely vetted and trusted
- Hardware acceleration available
- No practical attacks known

**PBKDF2 (100,000 iterations):**
- NIST recommended (SP 800-132)
- Slows down brute-force attacks
- Computationally expensive for attackers
- Balance between security and performance

**SHA-256:**
- Part of SHA-2 family
- No known collision attacks
- Widely supported and trusted

---

## 📚 Security Resources

### Standards & References

- [NIST Cryptographic Standards](https://csrc.nist.gov/)
- [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html)
- [AES Specification (FIPS 197)](https://csrc.nist.gov/publications/fips/fips197/fips-197.pdf)
- [PBKDF2 Specification (RFC 2898)](https://tools.ietf.org/html/rfc2898)

### Security Audits

- No formal security audit has been conducted yet
- Community security review welcome
- Consider this when using for sensitive data

---

## 🔄 Security Updates

### Version Policy

- Security fixes released as soon as possible
- Critical vulnerabilities: immediate patch release
- Non-critical: included in next regular release

### Update Notifications

- Watch GitHub repository for security advisories
- Check CHANGELOG.md for security-related changes
- Subscribe to release notifications on GitHub

---

## ✅ Security Checklist for Deployment

Before using SecureVault Pro for sensitive data:

- [ ] Review this security policy
- [ ] Understand threat model and limitations
- [ ] Choose strong master password
- [ ] Enable full-disk encryption on your system
- [ ] Keep Java runtime updated
- [ ] Keep operating system updated
- [ ] Set up regular backup routine
- [ ] Test backup restoration process
- [ ] Configure auto-lock timeout appropriately
- [ ] Understand how to report security issues

---

## 📞 Contact

For security concerns:
- Check GitHub repository for maintainer contact
- Do NOT disclose vulnerabilities publicly
- Allow reasonable time for response and fix

---

## 📝 Document History

- **2025-01-01:** Initial security policy created
- **Version:** 3.0

---

**Remember:** Security is a shared responsibility. Follow best practices and stay informed about security updates.

🔒 **Stay Safe!**

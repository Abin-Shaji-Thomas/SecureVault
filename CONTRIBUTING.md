# Contributing to SecureVault Pro

First off, thank you for considering contributing to SecureVault Pro! It's people like you that make SecureVault Pro such a great tool.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Coding Guidelines](#coding-guidelines)
- [Pull Request Process](#pull-request-process)
- [Issue Guidelines](#issue-guidelines)

---

## Code of Conduct

This project and everyone participating in it is governed by our commitment to fostering an open and welcoming environment. By participating, you are expected to uphold this code.

### Our Standards

- **Be respectful** - Treat everyone with respect
- **Be constructive** - Provide helpful feedback
- **Be collaborative** - Work together towards common goals
- **Be patient** - Not everyone has the same experience level

---

## How Can I Contribute?

### 🐛 Reporting Bugs

Before creating bug reports, please check the existing issues to avoid duplicates. When you create a bug report, include as many details as possible:

**Bug Report Template:**
```markdown
**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '...'
3. See error

**Expected behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Environment:**
 - OS: [e.g. Linux, macOS, Windows]
 - Java Version: [e.g. OpenJDK 17]
 - SecureVault Version: [e.g. 3.0]

**Additional context**
Any other context about the problem.
```

### 💡 Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, include:

- **Clear title** - Summarize the suggestion
- **Detailed description** - Explain the feature and why it would be useful
- **Examples** - Provide examples of how it would work
- **Mockups** - If applicable, add mockups or diagrams

### 📝 Improving Documentation

Documentation improvements are always welcome! This includes:

- Fixing typos or grammatical errors
- Adding missing documentation
- Improving clarity or examples
- Translating documentation (future feature)

### 🔧 Contributing Code

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Make your changes**
4. **Test thoroughly**
5. **Commit with clear messages**
6. **Push to your fork**
7. **Open a Pull Request**

---

## Development Setup

### Prerequisites

- Java 17 or higher (OpenJDK recommended)
- Git
- A Java IDE (IntelliJ IDEA, Eclipse, or NetBeans)

### Setting Up Your Development Environment

1. **Fork and clone the repository:**
   ```bash
   git clone https://github.com/YOUR-USERNAME/SecureVault-Pro.git
   cd SecureVault-Pro
   ```

2. **Install dependencies:**
   ```bash
   # Dependencies are automatically downloaded by run.sh
   ./run.sh
   ```

3. **Compile the project:**
   ```bash
   mkdir -p bin
   javac -Xlint:all -cp "lib/*:." -d bin src/*.java
   ```

4. **Run the application:**
   ```bash
   java -cp "bin:lib/*" SecureVaultSwingEnhanced
   ```

### Project Structure

```
SecureVault-Pro/
├── src/                    # Source code
│   ├── SecureVaultSwingEnhanced.java  # Main application
│   ├── Database.java       # Database operations
│   ├── UserManager.java    # User authentication
│   ├── PasswordEncryption.java  # Encryption logic
│   └── ...                 # Other components
├── lib/                    # External dependencies
├── docs/                   # Documentation
└── bin/                    # Compiled classes
```

---

## Coding Guidelines

### Java Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line length**: Max 120 characters
- **Braces**: Opening brace on same line
- **Naming conventions**:
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Private fields: start with lowercase

### Example:

```java
public class MyNewFeature {
    private static final int MAX_ATTEMPTS = 3;
    private String userName;
    
    public void processData() {
        // Implementation here
    }
}
```

### Comments

- Use JavaDoc for public methods and classes
- Add inline comments for complex logic
- Keep comments up-to-date with code changes

```java
/**
 * Encrypts the provided password using AES-256-CBC.
 * 
 * @param password The plain text password to encrypt
 * @param key The encryption key
 * @return The encrypted password as a Base64 string
 * @throws Exception if encryption fails
 */
public String encryptPassword(String password, SecretKey key) throws Exception {
    // Implementation
}
```

### Error Handling

- Always catch specific exceptions
- Provide meaningful error messages
- Log errors appropriately
- Never expose sensitive information in error messages

```java
try {
    // Database operation
} catch (SQLException e) {
    System.err.println("Database error: " + e.getMessage());
    // Don't expose SQL details to user
    showError("Failed to save credential. Please try again.");
}
```

### Security Considerations

When contributing code that handles sensitive data:

1. **Never log passwords or encryption keys**
2. **Clear sensitive data from memory when done**
3. **Use secure random number generators**
4. **Validate and sanitize all user input**
5. **Follow the principle of least privilege**

---

## Pull Request Process

### Before Submitting

1. **Update documentation** if you've changed APIs
2. **Test your changes** thoroughly
3. **Ensure code compiles** without warnings
4. **Check for code style** compliance
5. **Update CHANGELOG.md** with your changes

### Pull Request Template

```markdown
## Description
Brief description of the changes

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing
Describe the tests you ran and their results

## Checklist
- [ ] My code follows the code style of this project
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] My changes generate no new warnings
- [ ] I have tested my changes thoroughly
```

### Review Process

1. A maintainer will review your PR
2. They may request changes or ask questions
3. Make the requested changes and push to your branch
4. Once approved, a maintainer will merge your PR

---

## Issue Guidelines

### Issue Labels

- `bug` - Something isn't working
- `enhancement` - New feature or request
- `documentation` - Documentation improvements
- `good first issue` - Good for newcomers
- `help wanted` - Extra attention needed
- `security` - Security-related issue
- `wontfix` - This will not be worked on

### Creating Issues

**Use clear, descriptive titles:**
- ✅ "Password generator doesn't include special characters"
- ❌ "Broken feature"

**Provide context:**
- What were you trying to do?
- What happened instead?
- What should have happened?

**Include environment details:**
- Operating system and version
- Java version
- SecureVault Pro version

---

## Testing Guidelines

### Manual Testing Checklist

Before submitting a PR, test the following:

- [ ] Application starts without errors
- [ ] Login with demo credentials works
- [ ] Create new user works
- [ ] Add new credential works
- [ ] Edit existing credential works
- [ ] Delete credential works
- [ ] Search/filter functionality works
- [ ] Import/export works (if applicable)
- [ ] No console errors or warnings

### Security Testing

For security-related changes:

- [ ] Passwords are encrypted correctly
- [ ] No sensitive data logged
- [ ] Session timeout works
- [ ] Clipboard clearing works
- [ ] Memory is cleared properly

---

## Questions?

If you have questions about contributing, feel free to:

- Open an issue with the `question` label
- Start a discussion on GitHub Discussions
- Check existing documentation

---

## Recognition

Contributors will be recognized in:

- CHANGELOG.md for each release
- GitHub's Contributors page
- Special thanks section in future releases

---

Thank you for contributing to SecureVault Pro! 🎉

Your time and effort help make this project better for everyone.

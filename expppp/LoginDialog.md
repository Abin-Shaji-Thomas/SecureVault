# LoginDialog.java - Line-by-Line Explanation

This file contains the login dialog that appears when the application starts. It allows users to either log in with existing credentials or create a new account. The dialog includes a show/hide password toggle and validation for new user creation.

---

## **Import Statements**

```java
import javax.swing.*;
import java.awt.*;
```

- **`import javax.swing.*;`** - Imports all Swing GUI components (JDialog, JTextField, JPasswordField, JButton, etc.)
- **`import java.awt.*;`** - Imports AWT layout managers and classes (Frame, GridBagLayout, Dimension, etc.)

---

## **Class Declaration and JavaDoc**

```java
/**
 * Login dialog with username, password, show password toggle, and create user option
 */
public class LoginDialog extends JDialog {
```

- **JavaDoc comment** - Describes what this dialog does
- **`public class LoginDialog extends JDialog`** - Creates a dialog window that inherits from `JDialog`
- **`JDialog`** - A modal dialog that blocks interaction with other windows until closed

---

## **Instance Variables**

```java
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;
    private boolean succeeded = false;
    private String username;
    private String password;
    private boolean createNewUser = false;
```

- **`private JTextField usernameField;`** - Text input field for username (visible text)
- **`private JPasswordField passwordField;`** - Password input field (displays bullets/dots by default)
- **`private JCheckBox showPasswordCheck;`** - Checkbox to toggle password visibility
- **`private boolean succeeded = false;`** - Tracks if user successfully clicked Login or Create User (vs Cancel/Exit)
- **`private String username;`** - Stores the entered username after dialog closes
- **`private String password;`** - Stores the entered password after dialog closes
- **`private boolean createNewUser = false;`** - Distinguishes between login attempt vs new user creation

**Why store these?** The dialog closes after button click, so we need to save the user's input before the components are destroyed.

---

## **Constructor**

```java
    public LoginDialog(Frame parent) {
        super(parent, "SecureVault - Login", true);
```

- **`public LoginDialog(Frame parent)`** - Constructor takes the parent frame (SecureVaultSwing window)
- **`super(parent, "SecureVault - Login", true);`** - Calls JDialog constructor:
  - `parent` - The parent window (dialog will center on this)
  - `"SecureVault - Login"` - Title shown in dialog title bar
  - `true` - Makes dialog modal (blocks parent window until closed)

### **Main Panel Setup**

```java
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
```

- **`JPanel panel = new JPanel(new GridBagLayout());`** - Creates main panel with GridBagLayout
- **`GridBagLayout`** - Flexible layout manager that allows precise component positioning with constraints
- **`panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));`** - Adds 20-pixel padding on all sides
  - Format: `(top, left, bottom, right)`

### **GridBagConstraints Setup**

```java
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
```

- **`GridBagConstraints gbc = new GridBagConstraints();`** - Creates constraint object for GridBagLayout
- **`gbc.insets = new Insets(5, 5, 5, 5);`** - Adds 5-pixel spacing around each component
- **`gbc.fill = GridBagConstraints.HORIZONTAL;`** - Makes components stretch horizontally to fill available space

---

## **Title Label**

```java
        // Title
        JLabel titleLabel = new JLabel("SecureVault Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
```

- **`JLabel titleLabel = new JLabel("SecureVault Login");`** - Creates label with text
- **`titleLabel.setFont(new Font("Arial", Font.BOLD, 18));`** - Sets font: Arial, Bold, 18-point size

```java
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
```

- **`gbc.gridx = 0;`** - Place in column 0 (left column)
- **`gbc.gridy = 0;`** - Place in row 0 (top row)
- **`gbc.gridwidth = 2;`** - Span across 2 columns (takes full width)
- **`gbc.anchor = GridBagConstraints.CENTER;`** - Center the label within its space
- **`panel.add(titleLabel, gbc);`** - Add label to panel with these constraints

```java
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
```

- **`gbc.gridwidth = 1;`** - Reset to 1 column width for subsequent components
- **`gbc.anchor = GridBagConstraints.WEST;`** - Align subsequent components to the left (west)

---

## **Username Field**

```java
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
```

- **`gbc.gridx = 0;`** - Column 0 (left side)
- **`gbc.gridy = 1;`** - Row 1 (second row)
- **`panel.add(new JLabel("Username:"), gbc);`** - Add label "Username:" at position (0,1)

```java
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
```

- **`usernameField = new JTextField(20);`** - Create text field with preferred width of 20 columns
- **`gbc.gridx = 1;`** - Column 1 (right side, same row)
- **`panel.add(usernameField, gbc);`** - Add text field next to the label

**Result:** Creates label-field pair horizontally aligned

---

## **Password Field**

```java
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
```

- **`gbc.gridy = 2;`** - Row 2 (third row)
- **`panel.add(new JLabel("Password:"), gbc);`** - Add "Password:" label

```java
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
```

- **`passwordField = new JPasswordField(20);`** - Create password field (shows dots instead of characters)
- **`JPasswordField`** - Special text field that masks input for security
- Default echo character is '•' (bullet)

---

## **Show Password Checkbox**

```java
        // Show password checkbox
        showPasswordCheck = new JCheckBox("Show Password");
```

- **`showPasswordCheck = new JCheckBox("Show Password");`** - Create checkbox with label text

```java
        showPasswordCheck.addActionListener(_ -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
```

- **`showPasswordCheck.addActionListener(_ -> { ... });`** - Add lambda to respond to checkbox clicks
- **`_`** - Underscore indicates we don't use the ActionEvent parameter
- **`if (showPasswordCheck.isSelected())`** - Check if checkbox is now checked
  - **`passwordField.setEchoChar((char) 0);`** - Set echo character to null (0) → shows actual characters
- **`else`** - If checkbox is unchecked
  - **`passwordField.setEchoChar('•');`** - Restore bullet echo character → hides password

**Example:** User types "MyPass123":
- Checkbox OFF → Displays: `•••••••••`
- Checkbox ON → Displays: `MyPass123`

```java
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(showPasswordCheck, gbc);
```

- **`gbc.gridy = 3;`** - Row 3 (fourth row)
- Place checkbox in column 1 (aligned with input fields)

---

## **Buttons Panel**

```java
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
```

- **`JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));`** - Create panel for buttons
- **`FlowLayout.CENTER`** - Center-align buttons
- **`10`** - 10-pixel horizontal gap between buttons
- **`0`** - 0 vertical gap

### **Login Button**

```java
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 30));
```

- **`JButton loginBtn = new JButton("Login");`** - Create button with "Login" text
- **`loginBtn.setPreferredSize(new Dimension(100, 30));`** - Set button size: 100 pixels wide, 30 pixels tall

```java
        loginBtn.addActionListener(_ -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            succeeded = true;
            createNewUser = false;
            dispose();
        });
```

- **`loginBtn.addActionListener(_ -> { ... });`** - Lambda executed when Login button is clicked
- **`username = usernameField.getText().trim();`** - Get username text and remove leading/trailing whitespace
- **`password = new String(passwordField.getPassword());`** - Get password as String
  - **`getPassword()`** returns `char[]` for security (can be cleared from memory)
  - Convert to String for consistency with database API
- **`succeeded = true;`** - Mark that user clicked Login (not Cancel)
- **`createNewUser = false;`** - Indicate this is a login attempt, not registration
- **`dispose();`** - Close the dialog window

**Flow:** User clicks Login → Fields saved to instance variables → Dialog closes → Caller checks if succeeded

```java
        buttonPanel.add(loginBtn);
```

- **`buttonPanel.add(loginBtn);`** - Add Login button to button panel

### **Create User Button**

```java
        JButton createUserBtn = new JButton("Create User");
        createUserBtn.setPreferredSize(new Dimension(120, 30));
```

- **`JButton createUserBtn = new JButton("Create User");`** - Create button for new user registration
- **`setPreferredSize(new Dimension(120, 30));`** - Slightly wider (120px) to fit text

```java
        createUserBtn.addActionListener(_ -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
```

- **`createUserBtn.addActionListener(_ -> { ... });`** - Lambda for Create User button
- **First two lines same as Login button** - Get username and password from fields

#### **Validation: Empty Fields**

```java
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
```

- **`if (username.isEmpty() || password.isEmpty())`** - Check if either field is empty
- **`JOptionPane.showMessageDialog(...)`** - Show error dialog:
  - `this` - Parent component (LoginDialog)
  - `"Username and password cannot be empty!"` - Error message
  - `"Error"` - Dialog title
  - `JOptionPane.ERROR_MESSAGE` - Shows red X icon
- **`return;`** - Exit lambda without closing dialog (user must fix error)

#### **Validation: Password Length**

```java
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters long!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
```

- **`if (password.length() < 6)`** - Check if password is too short
- Show error dialog with minimum length requirement
- **`return;`** - Don't proceed with account creation

#### **Success Path**

```java
            succeeded = true;
            createNewUser = true;
            dispose();
        });
```

- **`succeeded = true;`** - Mark operation as successful
- **`createNewUser = true;`** - Indicate this is a NEW USER registration (not login)
- **`dispose();`** - Close dialog (caller will check flags and create account)

**Key Difference:** `createNewUser` flag tells caller whether to call `UserManager.createUser()` vs `UserManager.authenticateUser()`

```java
        buttonPanel.add(createUserBtn);
```

- **`buttonPanel.add(createUserBtn);`** - Add Create User button to panel

### **Exit/Cancel Button**

```java
        JButton cancelBtn = new JButton("Exit");
        cancelBtn.setPreferredSize(new Dimension(100, 30));
        cancelBtn.addActionListener(_ -> {
            succeeded = false;
            dispose();
        });
        buttonPanel.add(cancelBtn);
```

- **`JButton cancelBtn = new JButton("Exit");`** - Create Exit button
- **`succeeded = false;`** - Mark that user cancelled (did not log in or create account)
- **`dispose();`** - Close dialog
- **Result:** Caller will see `succeeded == false` and terminate the application

---

## **Add Button Panel to Main Panel**

```java
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(buttonPanel, gbc);
```

- **`gbc.gridy = 4;`** - Row 4 (fifth row, below all fields)
- **`gbc.gridwidth = 2;`** - Span across both columns (full width)
- **`gbc.insets = new Insets(15, 5, 5, 5);`** - Add extra top margin (15px) to separate from fields
- **`panel.add(buttonPanel, gbc);`** - Add button panel at bottom

---

## **Enter Key Support**

```java
        // Enter key support
        passwordField.addActionListener(_ -> loginBtn.doClick());
```

- **`passwordField.addActionListener(_ -> loginBtn.doClick());`** - When user presses Enter in password field...
- **`loginBtn.doClick();`** - Programmatically click the Login button
- **Result:** User can press Enter to log in (common UX pattern)

**Why only password field?** Because it's the last field in the tab order - user types username, tabs to password, types password, presses Enter.

---

## **Final Dialog Setup**

```java
        add(panel);
```

- **`add(panel);`** - Add the main panel to the dialog (JDialog.add())

```java
        pack();
```

- **`pack();`** - Resize dialog to fit all components at their preferred sizes
- Calculates optimal window size based on GridBagLayout and component dimensions

```java
        setResizable(false);
```

- **`setResizable(false);`** - Disable window resizing (fixed size dialog)
- **Why?** Login dialogs typically have fixed layouts; resizing would look odd

```java
        setLocationRelativeTo(parent);
```

- **`setLocationRelativeTo(parent);`** - Center dialog over parent window
- If parent is null or not showing, centers on screen

```java
    }
```

- **End of constructor** - Dialog is now fully configured but not yet visible

---

## **Getter Methods**

These methods allow the caller to retrieve user input after the dialog closes.

```java
    public String getUsername() {
        return username;
    }
```

- **`public String getUsername()`** - Returns the username entered by user
- Called after dialog closes: `String user = dialog.getUsername();`

```java
    public String getPassword() {
        return password;
    }
```

- **`public String getPassword()`** - Returns the password entered by user
- Called after dialog closes: `String pass = dialog.getPassword();`

```java
    public boolean isSucceeded() {
        return succeeded;
    }
```

- **`public boolean isSucceeded()`** - Returns true if user clicked Login or Create User (false if Exit)
- Caller checks this first: if false, application terminates

```java
    public boolean isCreateNewUser() {
        return createNewUser;
    }
}
```

- **`public boolean isCreateNewUser()`** - Returns true if user clicked Create User (false if Login)
- Distinguishes between authentication vs registration

---

## **How It's Used in SecureVaultSwing**

```java
LoginDialog loginDialog = new LoginDialog(null);
loginDialog.setVisible(true);  // Shows dialog (blocks until closed)

if (!loginDialog.isSucceeded()) {
    System.exit(0);  // User clicked Exit
}

String username = loginDialog.getUsername();
String password = loginDialog.getPassword();

if (loginDialog.isCreateNewUser()) {
    // Create new account
    boolean created = userManager.createUser(username, password);
    if (created) {
        // Account created, now authenticate
        currentUserId = userManager.authenticateUser(username, password);
    }
} else {
    // Attempt login
    currentUserId = userManager.authenticateUser(username, password);
    if (currentUserId == -1) {
        // Show error and loop again
    }
}
```

---

## **Dialog Layout Summary**

```
┌─────────────────────────────────────┐
│      SecureVault Login             │  ← Title (row 0)
│                                     │
│  Username: [___________________]    │  ← Row 1
│                                     │
│  Password: [___________________]    │  ← Row 2
│                                     │
│            [✓] Show Password        │  ← Row 3
│                                     │
│    [Login] [Create User] [Exit]    │  ← Row 4 (buttons)
└─────────────────────────────────────┘
```

---

## **Key Features**

1. **Modal Dialog:** Blocks parent window until user responds
2. **Show/Hide Password:** Checkbox toggles password visibility
3. **Validation:** Create User validates empty fields and minimum password length
4. **Enter Key:** Pressing Enter in password field triggers Login
5. **Three Outcomes:**
   - Login attempt: `succeeded=true`, `createNewUser=false`
   - Create account: `succeeded=true`, `createNewUser=true`
   - Cancel/Exit: `succeeded=false`
6. **Clean Separation:** Dialog only handles UI; caller performs actual authentication/creation

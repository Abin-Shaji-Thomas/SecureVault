# CredentialDialog.java - Line-by-Line Explanation

This file contains the dialog for adding or editing credentials. It features a live password strength meter that updates in real-time as the user types, providing immediate visual feedback about password quality.

---

## **Import Statements**

```java
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
```

- **`import javax.swing.*;`** - Swing GUI components (JFrame, JTextField, JPasswordField, JPanel, JLabel, JOptionPane, etc.)
- **`import javax.swing.event.DocumentEvent;`** - Event fired when text document changes
- **`import javax.swing.event.DocumentListener;`** - Listener interface for document changes
- **`import java.awt.*;`** - AWT classes (GridLayout, FlowLayout, Color, Dimension, Graphics2D, etc.)

**Why DocumentListener?** To detect every keystroke in the password field and update the strength meter in real-time.

---

## **Class Declaration**

```java
/**
 * Dialog for adding/editing credentials with live strength meter
 */
public class CredentialDialog {
```

- **JavaDoc comment** - Describes the dialog's purpose
- **`public class CredentialDialog`** - Public class (not a JDialog subclass, uses JOptionPane instead)

---

## **Instance Variables**

```java
    private JFrame parent;
    private Database.Credential current;
    private Database.Credential result = null;
```

- **`private JFrame parent;`** - Reference to parent window (for centering dialog)
- **`private Database.Credential current;`** - The credential being edited (null if adding new)
- **`private Database.Credential result = null;`** - The result after user clicks OK (null if cancelled)

**Design Pattern:** `current` holds existing data (for edit mode), `result` holds the new/modified data after user submission.

---

## **Constructor**

```java
    public CredentialDialog(JFrame parent, Database.Credential current) {
        this.parent = parent;
        this.current = current;
    }
```

- **`public CredentialDialog(JFrame parent, Database.Credential current)`** - Constructor
- **Parameters:**
  - `parent` - Parent frame for dialog positioning
  - `current` - Existing credential to edit, or `null` to add new credential
- **Stores both parameters** as instance variables for use in `showDialog()`

**Usage:**
- Add mode: `new CredentialDialog(frame, null)`
- Edit mode: `new CredentialDialog(frame, existingCredential)`

---

## **showDialog() Method**

This is the main method that displays the dialog and returns the result.

```java
    public Database.Credential showDialog() {
```

- **`public Database.Credential showDialog()`** - Displays the dialog and waits for user response
- **Returns:** New/edited Credential object if user clicked OK, or `null` if cancelled

### **Create Input Fields**

```java
        JTextField titleField = new JTextField(current == null ? "" : current.title);
        JTextField usernameField = new JTextField(current == null ? "" : current.username);
        JPasswordField passwordField = new JPasswordField(current == null ? "" : current.password);
```

- **`JTextField titleField = new JTextField(...)`** - Create text field for credential title (e.g., "Gmail", "Facebook")
- **Ternary operator:** `current == null ? "" : current.title`
  - If `current` is null (add mode) → empty string ""
  - If `current` exists (edit mode) → pre-fill with existing title
- **`JTextField usernameField`** - Field for username/email
- **`JPasswordField passwordField`** - Field for password (shows bullets by default)

**Example:**
- Add mode: All fields start empty
- Edit mode for Gmail credential: `titleField="Gmail"`, `usernameField="user@gmail.com"`, `passwordField="OldPass123"`

### **Create Form Panel**

```java
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
```

- **`JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));`** - Create panel with GridLayout
- **`GridLayout(0, 1, 6, 6)`**:
  - `0` - Unlimited rows (as many as needed)
  - `1` - 1 column (vertical stacking)
  - `6` - 6-pixel horizontal gap
  - `6` - 6-pixel vertical gap

```java
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
```

- **`panel.add(...)`** - Add components in order (labels and fields alternate)
- **Result:** Vertical stack of label-field pairs

---

## **Live Strength Indicator**

### **Create Strength Display Row**

```java
        // Live strength indicator
        JPanel strengthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel strengthLbl = new JLabel("Strength: ");
        JLabel strengthIcon = new JLabel();
        strengthIcon.setPreferredSize(new Dimension(80, 20));
```

- **`JPanel strengthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));`** - Create horizontal panel for strength display
- **`FlowLayout.LEFT`** - Left-align contents
- **`JLabel strengthLbl = new JLabel("Strength: ");`** - Static label text
- **`JLabel strengthIcon = new JLabel();`** - Label that will display the colored strength circle
- **`strengthIcon.setPreferredSize(new Dimension(80, 20));`** - Reserve 80x20 pixel space for icon

```java
        strengthRow.add(strengthLbl);
        strengthRow.add(strengthIcon);
        panel.add(strengthRow);
```

- **Add both labels to the strength row panel**
- **Add the row to the main panel**

**Visual Layout:**
```
Title: [___________________]
Username: [___________________]
Password: [___________________]
Strength:  🔴  ← Colored circle here
```

### **Initialize Strength Icon**

```java
        // Initialize icon
        updateStrengthIcon(strengthIcon, new String(passwordField.getPassword()));
```

- **`updateStrengthIcon(strengthIcon, new String(passwordField.getPassword()));`** - Set initial strength icon
- **`new String(passwordField.getPassword())`** - Get current password as String
- **Why call this?** In edit mode, password field is pre-filled, so we need to show its strength immediately

**Example:** Editing a credential with password "Pass123" → Shows yellow/red circle based on strength

---

## **Live Update Listener**

### **Create DocumentListener**

```java
        // Live update as user types
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
```

- **`passwordField.getDocument()`** - Get the Document object that stores the text
- **`addDocumentListener(new DocumentListener() { ... })`** - Attach listener to detect text changes
- **DocumentListener interface** - Has 3 methods for different types of changes

### **Update Helper Method**

```java
            private void update() {
                SwingUtilities.invokeLater(() -> {
                    String text = new String(passwordField.getPassword());
                    updateStrengthIcon(strengthIcon, text);
                });
            }
```

- **`private void update()`** - Helper method called by all 3 listener methods
- **`SwingUtilities.invokeLater(() -> { ... });`** - Schedule UI update on the Event Dispatch Thread (EDT)
- **Why invokeLater?** DocumentListener runs on document thread; Swing UI must be updated on EDT
- **`String text = new String(passwordField.getPassword());`** - Get current password
- **`updateStrengthIcon(strengthIcon, text);`** - Update the colored circle based on new password

### **Three Listener Methods**

```java
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
```

- **`insertUpdate(DocumentEvent e)`** - Called when characters are inserted (user types)
- **`removeUpdate(DocumentEvent e)`** - Called when characters are deleted (backspace/delete)
- **`changedUpdate(DocumentEvent e)`** - Called when text attributes change (not relevant for password field)
- **All three call `update()`** - Any text change triggers strength re-calculation

**Example User Flow:**
1. User types 'P' → `insertUpdate()` → `update()` → Check "P" strength → Show red circle
2. User types 'a' (now "Pa") → `insertUpdate()` → `update()` → Check "Pa" strength → Show red circle
3. User types 's', 's', 'w', 'o', 'r', 'd', '1', '2', '3', '!' → Each keystroke updates circle color
4. Final password "Password123!" → Shows green circle (strong password)

---

## **Show the Dialog**

```java
        int res = JOptionPane.showConfirmDialog(parent, panel,
                current == null ? "Add Credential" : "Edit Credential",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
```

- **`int res = JOptionPane.showConfirmDialog(...)`** - Display modal dialog and wait for user response
- **Parameters:**
  - `parent` - Parent frame (dialog centers over this)
  - `panel` - The panel containing all input fields
  - `current == null ? "Add Credential" : "Edit Credential"` - Dialog title depends on mode
  - `JOptionPane.OK_CANCEL_OPTION` - Show OK and Cancel buttons
  - `JOptionPane.PLAIN_MESSAGE` - No icon (plain dialog)
- **Returns:** `JOptionPane.OK_OPTION` if user clicked OK, `JOptionPane.CANCEL_OPTION` if cancelled

**Result:** Dialog blocks here until user clicks OK or Cancel

---

## **Process User Input**

### **Check if User Clicked OK**

```java
        if (res == JOptionPane.OK_OPTION) {
```

- **`if (res == JOptionPane.OK_OPTION)`** - Only proceed if user clicked OK (not Cancel)

### **Extract Field Values**

```java
            String title = titleField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
```

- **`String title = titleField.getText().trim();`** - Get title text and remove leading/trailing spaces
- **`String username = usernameField.getText().trim();`** - Get username text and trim
- **`String password = new String(passwordField.getPassword());`** - Get password as String
- **`trim()`** - Removes accidental spaces: " Gmail " becomes "Gmail"

### **Validate Required Fields**

```java
            if (title.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All fields required.");
                return null;
            }
```

- **`if (title.isEmpty() || username.isEmpty() || password.isEmpty())`** - Check if any field is empty
- **`JOptionPane.showMessageDialog(parent, "All fields required.");`** - Show error message
- **`return null;`** - Return null to indicate failure (validation failed)

**User Experience:** If user leaves a field blank and clicks OK, they see an error and the credential is NOT saved.

### **Create Credential Object**

```java
            // Create credential (id will be assigned by database)
            int id = (current != null) ? current.id : 0;
            result = new Database.Credential(id, title, username, password);
        }
```

- **`int id = (current != null) ? current.id : 0;`** - Determine credential ID:
  - **Edit mode:** Use existing credential's ID (preserves database row)
  - **Add mode:** Use 0 (database will assign new ID)
- **`result = new Database.Credential(id, title, username, password);`** - Create Credential object with entered data
- **Store in `result` instance variable** so it can be returned

**Example:**
- **Add mode:** `new Credential(0, "Netflix", "user@email.com", "NetPass456!")` → Database will assign ID 15
- **Edit mode:** `new Credential(7, "Netflix", "user@email.com", "NewPass789!")` → Updates row with ID 7

---

## **Return Result**

```java
        return result;
    }
```

- **`return result;`** - Return the created/edited Credential object
- **`result` is null if:**
  - User clicked Cancel
  - Validation failed (empty fields)
- **`result` is a Credential if:**
  - User clicked OK and all fields were valid

---

## **updateStrengthIcon() Method**

This method updates the colored circle icon based on password strength.

```java
    private void updateStrengthIcon(JLabel strengthIcon, String password) {
```

- **`private void updateStrengthIcon(JLabel strengthIcon, String password)`** - Helper method to update icon
- **Parameters:**
  - `strengthIcon` - The JLabel to update
  - `password` - The password to check

### **Check Password Strength**

```java
        StrengthChecker.Strength s = StrengthChecker.checkStrength(password);
```

- **`StrengthChecker.Strength s = StrengthChecker.checkStrength(password);`** - Get strength rating
- **Returns:** WEAK, MEDIUM, or STRONG enum value

### **Set Icon Based on Strength**

```java
        if (s == StrengthChecker.Strength.STRONG) {
            strengthIcon.setIcon(createCircleIcon(new Color(72, 187, 120), new Color(40, 120, 70), 14));
        } else if (s == StrengthChecker.Strength.MEDIUM) {
            strengthIcon.setIcon(createCircleIcon(new Color(255, 203, 72), new Color(200, 140, 20), 14));
        } else {
            strengthIcon.setIcon(createCircleIcon(new Color(229, 83, 83), new Color(160, 40, 40), 14));
        }
    }
```

- **STRONG password:**
  - Fill color: `rgb(72, 187, 120)` - Green
  - Border color: `rgb(40, 120, 70)` - Dark green
- **MEDIUM password:**
  - Fill color: `rgb(255, 203, 72)` - Yellow/orange
  - Border color: `rgb(200, 140, 20)` - Dark yellow
- **WEAK password:**
  - Fill color: `rgb(229, 83, 83)` - Red
  - Border color: `rgb(160, 40, 40)` - Dark red
- **`14`** - Circle size (14x14 pixels)

**Visual Result:**
- 🟢 Green circle = Strong password
- 🟡 Yellow circle = Medium password
- 🔴 Red circle = Weak password

---

## **createCircleIcon() Method**

This method generates a colored circle icon using Java 2D graphics.

```java
    private static ImageIcon createCircleIcon(Color fill, Color border, int size) {
```

- **`private static ImageIcon createCircleIcon(...)`** - Static method to create circular icon
- **Parameters:**
  - `fill` - Fill color for the circle
  - `border` - Border color for the circle outline
  - `size` - Diameter of the circle in pixels
- **Returns:** `ImageIcon` object that can be set on a JLabel

### **Create Buffered Image**

```java
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
```

- **`BufferedImage img = new BufferedImage(size, size, TYPE_INT_ARGB)`** - Create blank image
- **`size, size`** - Width and height (square image)
- **`TYPE_INT_ARGB`** - Image type: 32-bit RGBA (supports transparency)

### **Get Graphics Context**

```java
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
```

- **`Graphics2D g = img.createGraphics();`** - Get graphics context for drawing
- **`g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);`** - Enable antialiasing
- **Antialiasing** - Smooths edges of drawn shapes (circle looks better, not pixelated)

### **Draw Filled Circle**

```java
        g.setColor(fill);
        g.fillOval(2, 2, size - 4, size - 4);
```

- **`g.setColor(fill);`** - Set drawing color to the fill color
- **`g.fillOval(2, 2, size - 4, size - 4);`** - Draw filled oval (circle)
  - **`2, 2`** - X, Y position (2 pixels from top-left corner)
  - **`size - 4, size - 4`** - Width and height (4 pixels smaller to leave room for border)

**Example:** For `size=14`: Draw filled circle at (2,2) with dimensions 10x10

### **Draw Circle Border**

```java
        g.setStroke(new BasicStroke(2f));
        g.setColor(border);
        g.drawOval(2, 2, size - 4, size - 4);
```

- **`g.setStroke(new BasicStroke(2f));`** - Set line thickness to 2 pixels
- **`g.setColor(border);`** - Set drawing color to border color
- **`g.drawOval(2, 2, size - 4, size - 4);`** - Draw circle outline (same position/size as filled circle)

**Result:** A filled circle with a contrasting border for depth/clarity

### **Clean Up and Return**

```java
        g.dispose();
        return new ImageIcon(img);
    }
}
```

- **`g.dispose();`** - Release graphics resources
- **`return new ImageIcon(img);`** - Wrap BufferedImage in ImageIcon (Swing's icon format)

---

## **How It's Used in SecureVaultSwing**

### **Add Credential:**
```java
Database.Credential newCred = new CredentialDialog(this, null).showDialog();
if (newCred != null) {
    db.insertCredential(userId, newCred.title, newCred.username, newCred.password);
    loadCredentials();  // Refresh table
}
```

### **Edit Credential:**
```java
Database.Credential selected = ... // Get from table
Database.Credential edited = new CredentialDialog(this, selected).showDialog();
if (edited != null) {
    db.updateCredential(edited.id, edited.title, edited.username, edited.password);
    loadCredentials();  // Refresh table
}
```

---

## **Key Features**

1. **Add/Edit Modes:** Same dialog handles both operations (determined by `current` parameter)
2. **Live Strength Meter:** Updates in real-time as user types
3. **Visual Feedback:** Color-coded circles (green/yellow/red) for instant password quality assessment
4. **Validation:** Ensures all fields are filled before allowing submission
5. **Clean API:** Returns `null` if cancelled/failed, Credential object if successful
6. **Pre-filled Values:** In edit mode, fields show existing credential data
7. **DocumentListener:** Monitors password field for every keystroke
8. **Custom Graphics:** Hand-drawn circle icons with antialiasing for smooth appearance

---

## **Dialog Appearance**

```
┌─────────────────────────────────┐
│      Add Credential            │
├─────────────────────────────────┤
│ Title:                          │
│ [___________________________]   │
│                                 │
│ Username:                       │
│ [___________________________]   │
│                                 │
│ Password:                       │
│ [•••••••••••••••••••••••••••]   │
│                                 │
│ Strength:  🟢                   │  ← Live indicator
│                                 │
│          [ OK ]  [ Cancel ]     │
└─────────────────────────────────┘
```

As user types, the circle changes color instantly based on password strength criteria.

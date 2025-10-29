# SecureVaultSwing.java - Complete Line-by-Line Explanation

## File Purpose
Main application class that creates the GUI window, handles user interactions, manages the application state, and coordinates between all components.

---

## Import Statements (Lines 1-6)

```java
import javax.swing.*;
```
**Line 1**: Imports all Swing GUI components (JFrame, JButton, JTable, JPanel, etc.)

```java
import javax.swing.table.*;
```
**Line 2**: Imports table-related classes (AbstractTableModel, DefaultTableCellRenderer)

```java
import java.awt.*;
```
**Line 3**: Imports AWT (Abstract Window Toolkit) for layout managers, colors, fonts

```java
import java.sql.SQLException;
```
**Line 4**: Imports SQLException for handling database errors

```java
import java.util.ArrayList;
import java.util.List;
```
**Lines 5-6**: Import List interface and ArrayList implementation for storing credentials

---

## Class Declaration (Line 8)

```java
public class SecureVaultSwing extends JFrame {
```
**What it means**: 
- `public class` - Anyone can use this class
- `SecureVaultSwing` - The class name
- `extends JFrame` - Inherits from JFrame, making this class a window

---

## Theme Enum (Lines 10-11)

```java
private enum Theme { LIGHT, DARK }
```
**Purpose**: Defines two possible themes for the application
- `private` - Only usable within this class
- `enum` - Special type that can only have fixed values
- `LIGHT`, `DARK` - The two theme options

---

## Instance Variables (Lines 13-22)

```java
private Database database;
```
**Line 13**: Reference to Database object for saving/loading credentials

```java
private UserManager userManager;
```
**Line 14**: Reference to UserManager for authentication

```java
private int currentUserId = -1;
```
**Line 15**: Stores logged-in user's ID (-1 means not logged in)

```java
private String currentUsername = "";
```
**Line 16**: Stores logged-in user's username (empty if not logged in)

```java
private final List<Database.Credential> credentials = new ArrayList<>();
```
**Line 18**: List to hold all credentials for display
- `final` - This reference can't be reassigned (but contents can change)

```java
private final VaultTableModel tableModel = new VaultTableModel(credentials);
```
**Line 19**: Table model that connects credentials list to the JTable

```java
private final JTable table = new JTable(tableModel);
```
**Line 20**: The actual table component that displays credentials

```java
private JLabel userLabel;
```
**Line 21**: Label showing current username at top of window

```java
private Theme currentTheme = Theme.DARK;
```
**Line 22**: Current theme setting (starts as DARK)

---

## Constructor (Lines 24-52)

```java
public SecureVaultSwing() {
```
**Line 24**: Constructor - runs when you create `new SecureVaultSwing()`

```java
super("SecureVault - Password Manager");
```
**Line 25**: Calls JFrame constructor to set window title

```java
setDefaultCloseOperation(EXIT_ON_CLOSE);
```
**Line 26**: Makes app exit completely when window closes

```java
setSize(900, 600);
```
**Line 27**: Sets window size to 900 pixels wide, 600 pixels tall

```java
setLocationRelativeTo(null);
```
**Line 28**: Centers window on screen (null = center of screen)

```java
try {
    database = new Database();
    userManager = new UserManager(database.getConnection());
} catch (SQLException e) {
    showError("Database initialization failed: " + e.getMessage());
    System.exit(1);
}
```
**Lines 30-35**: 
- **Try block**: Attempt to create database and user manager
- **Line 31**: Create new Database object (connects to SQLite)
- **Line 32**: Create UserManager with database connection
- **Catch block**: If database fails, show error and exit
- **Line 35**: `System.exit(1)` - Close app with error code

```java
if (!showLogin()) {
    System.exit(0);
}
```
**Lines 37-39**: 
- Call `showLogin()` method (shows login dialog)
- If user cancels/closes login, exit app peacefully

```java
buildUI();
```
**Line 41**: Build the main window interface

```java
loadCredentials();
```
**Line 42**: Load user's credentials from database into table

```java
addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent e) {
        if (database != null) database.close();
    }
});
```
**Lines 44-49**: Add listener for window closing event
- **WindowAdapter**: Convenience class for window events
- **windowClosing**: Called when user closes window
- **Line 47**: Close database connection before exiting

---

## Login Method (Lines 54-102)

```java
private boolean showLogin() {
```
**Line 54**: Method to handle login/registration process
- Returns `true` if login successful, `false` if cancelled

```java
while (true) {
```
**Line 55**: Infinite loop - keeps showing login until success or cancel

```java
LoginDialog loginDialog = new LoginDialog(this);
```
**Line 56**: Create login dialog window
- `this` passes current window as parent

```java
loginDialog.setVisible(true);
```
**Line 57**: Show dialog (blocks here until user closes it)

```java
if (!loginDialog.isSucceeded()) {
    return false;
}
```
**Lines 59-61**: If user clicked Cancel/Exit, return false

```java
String username = loginDialog.getUsername();
String password = loginDialog.getPassword();
```
**Lines 63-64**: Get username and password entered by user

```java
if (username.isEmpty() || password.isEmpty()) {
    showError("Username and password cannot be empty!");
    continue;
}
```
**Lines 66-69**: Check if fields are empty
- If empty, show error and loop back to show dialog again

```java
try {
```
**Line 71**: Start try block for database operations

```java
if (loginDialog.isCreateNewUser()) {
```
**Line 72**: Check if user clicked "Create User" button

```java
if (userManager.userExists(username)) {
    showError("Username already exists! Choose a different username.");
    continue;
}
```
**Lines 73-76**: For new user registration:
- Check if username already taken
- If taken, show error and loop back

```java
if (userManager.createUser(username, password)) {
    currentUserId = userManager.authenticateUser(username, password);
    currentUsername = username;
    JOptionPane.showMessageDialog(this,
        "User created successfully! Welcome, " + username + "!",
        "Success", JOptionPane.INFORMATION_MESSAGE);
    return true;
}
```
**Lines 78-84**: Create new user:
- **Line 78**: Call createUser (returns true if successful)
- **Line 79**: Get new user's ID by authenticating
- **Line 80**: Store username
- **Lines 81-83**: Show success message
- **Line 84**: Return true (login complete)

```java
} else {
    showError("Failed to create user!");
    continue;
}
```
**Lines 85-88**: If createUser failed, show error and loop

```java
} else {
    currentUserId = userManager.authenticateUser(username, password);
```
**Lines 89-90**: For existing user login:
- Call authenticateUser (returns user ID or -1)

```java
if (currentUserId != -1) {
    currentUsername = username;
    return true;
```
**Lines 91-93**: If authentication succeeded (ID not -1):
- Store username
- Return true (login complete)

```java
} else {
    showError("Invalid username or password!");
    continue;
}
```
**Lines 94-97**: If authentication failed, show error and loop

```java
} catch (SQLException e) {
    showError("Authentication error: " + e.getMessage());
}
```
**Lines 100-101**: Catch any database errors during login

---

## Build UI Method (Lines 104-113)

```java
private void buildUI() {
```
**Line 104**: Method to construct the main window layout

```java
JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
```
**Line 105**: Create main panel with BorderLayout
- `BorderLayout` - Divides panel into 5 areas (North, South, East, West, Center)
- `10, 10` - 10-pixel gaps between regions

```java
mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
```
**Line 106**: Add 10-pixel padding on all sides

```java
applyTheme(mainPanel);
```
**Line 107**: Apply current theme colors to panel

```java
mainPanel.add(createToolbar(), BorderLayout.NORTH);
```
**Line 109**: Add toolbar to top (North)

```java
mainPanel.add(createTablePanel(), BorderLayout.CENTER);
```
**Line 110**: Add table to center (takes remaining space)

```java
add(mainPanel);
```
**Line 112**: Add mainPanel to the frame (window)

---

## Create Toolbar Method (Lines 115-139)

```java
private JPanel createToolbar() {
```
**Line 115**: Method to create top toolbar with buttons

```java
JPanel toolbar = new JPanel(new BorderLayout());
```
**Line 116**: Create toolbar panel using BorderLayout

```java
JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
```
**Line 118**: Create left side panel
- `FlowLayout.LEFT` - Align components to left
- `5, 5` - 5-pixel horizontal and vertical gaps

```java
leftPanel.add(createButton("Add", _ -> onAdd()));
```
**Line 119**: Add "Add" button
- `_ ->` Lambda expression (parameter ignored)
- `onAdd()` - Method called when clicked

```java
leftPanel.add(createButton("Edit", _ -> onEdit()));
leftPanel.add(createButton("Delete", _ -> onDelete()));
leftPanel.add(createButton("Copy Password", _ -> onCopyPassword()));
leftPanel.add(createButton("Copy Username", _ -> onCopyUsername()));
leftPanel.add(createButton("Generate", _ -> onGenerate()));
leftPanel.add(createButton("Check Strength", _ -> onCheckStrength()));
```
**Lines 120-125**: Add more action buttons to left side

```java
JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
```
**Line 127**: Create right side panel (aligned right)

```java
userLabel = new JLabel("User: " + currentUsername);
userLabel.setFont(new Font("Arial", Font.BOLD, 12));
```
**Lines 128-129**: Create and style username label
- Shows "User: Abin" for example

```java
rightPanel.add(userLabel);
rightPanel.add(createButton("Theme", _ -> onTheme()));
rightPanel.add(createButton("Logout", _ -> onLogout()));
```
**Lines 130-132**: Add label and buttons to right side

```java
toolbar.add(leftPanel, BorderLayout.WEST);
toolbar.add(rightPanel, BorderLayout.EAST);
```
**Lines 134-135**: Add left and right panels to toolbar

```java
return toolbar;
```
**Line 137**: Return completed toolbar

---

## Create Table Panel Method (Lines 141-192)

```java
private JPanel createTablePanel() {
```
**Line 141**: Method to create credentials table

```java
table.setRowHeight(35);
```
**Line 142**: Make rows 35 pixels tall (taller than default)

```java
table.setFont(new Font("Monospaced", Font.PLAIN, 12));
```
**Line 143**: Use monospaced (fixed-width) font for table

```java
table.getTableHeader().setReorderingAllowed(false);
```
**Line 144**: Prevent user from dragging column headers

```java
if (currentTheme == Theme.LIGHT) {
    table.setBackground(Color.WHITE);
    table.setForeground(Color.BLACK);
    table.getTableHeader().setBackground(new Color(230, 235, 245));
    table.getTableHeader().setForeground(Color.BLACK);
} else {
    table.setBackground(new Color(50, 55, 65));
    table.setForeground(Color.WHITE);
    table.getTableHeader().setBackground(new Color(35, 40, 50));
    table.getTableHeader().setForeground(Color.WHITE);
}
```
**Lines 146-156**: Apply theme colors
- **Light theme**: White background, black text
- **Dark theme**: Dark gray background, white text
- `new Color(R, G, B)` - RGB color values (0-255)

```java
table.getColumnModel().getColumn(0).setPreferredWidth(200);
table.getColumnModel().getColumn(1).setPreferredWidth(200);
table.getColumnModel().getColumn(2).setPreferredWidth(150);
table.getColumnModel().getColumn(3).setPreferredWidth(200);
```
**Lines 158-161**: Set column widths
- Column 0 (Title): 200 pixels
- Column 1 (Username): 200 pixels
- Column 2 (Password): 150 pixels
- Column 3 (Strength): 200 pixels

```java
table.getColumnModel().getColumn(3).setCellRenderer(new StrengthBarRenderer());
```
**Line 163**: Use custom renderer for strength column (shows bars instead of text)

```java
JPopupMenu popup = new JPopupMenu();
```
**Line 165**: Create right-click context menu

```java
JMenuItem copyPassItem = new JMenuItem("Copy Password");
copyPassItem.addActionListener(_ -> onCopyPassword());
popup.add(copyPassItem);
```
**Lines 166-168**: Add "Copy Password" option to menu

```java
JMenuItem copyUserItem = new JMenuItem("Copy Username");
copyUserItem.addActionListener(_ -> onCopyUsername());
popup.add(copyUserItem);
```
**Lines 170-172**: Add "Copy Username" option to menu

```java
table.addMouseListener(new java.awt.event.MouseAdapter() {
```
**Line 174**: Add mouse click listener to table

```java
@Override
public void mousePressed(java.awt.event.MouseEvent e) {
    if (e.isPopupTrigger()) showPopup(e);
}
```
**Lines 175-178**: Handle mouse press
- `isPopupTrigger()` checks for right-click

```java
@Override
public void mouseReleased(java.awt.event.MouseEvent e) {
    if (e.isPopupTrigger()) showPopup(e);
}
```
**Lines 179-182**: Handle mouse release
- Some systems trigger popup on release, not press

```java
private void showPopup(java.awt.event.MouseEvent e) {
    int row = table.rowAtPoint(e.getPoint());
    if (row >= 0) {
        table.setRowSelectionInterval(row, row);
        popup.show(e.getComponent(), e.getX(), e.getY());
    }
}
```
**Lines 183-189**: Show popup menu
- **Line 184**: Find which row was clicked
- **Line 186**: Select that row
- **Line 187**: Show popup at click location

```java
JScrollPane scrollPane = new JScrollPane(table);
JPanel panel = new JPanel(new BorderLayout());
panel.add(scrollPane, BorderLayout.CENTER);
return panel;
```
**Lines 191-194**: Wrap table in scroll pane and return

---

## Create Button Method (Lines 196-200)

```java
private JButton createButton(String text, java.awt.event.ActionListener listener) {
```
**Line 196**: Helper method to create styled buttons
- `text` - Button label
- `listener` - What to do when clicked

```java
JButton btn = new JButton(text);
```
**Line 197**: Create button with text

```java
btn.setFocusPainted(false);
```
**Line 198**: Remove focus rectangle (looks cleaner)

```java
btn.addActionListener(listener);
return btn;
```
**Lines 199-200**: Add click handler and return button

---

## Load Credentials Method (Lines 202-211)

```java
private void loadCredentials() {
```
**Line 202**: Method to fetch credentials from database

```java
try {
    List<Database.Credential> data = database.getAllCredentials(currentUserId);
```
**Lines 203-204**: Get all credentials for current user

```java
credentials.clear();
```
**Line 205**: Remove old credentials from list

```java
credentials.addAll(data);
```
**Line 206**: Add new credentials to list

```java
tableModel.fireTableDataChanged();
```
**Line 207**: Tell table to refresh display

```java
} catch (SQLException e) {
    showError("Failed to load credentials: " + e.getMessage());
}
```
**Lines 208-210**: Show error if database fails

---

## Action Handler Methods

### On Add (Lines 213-225)

```java
private void onAdd() {
```
**Line 213**: Called when user clicks "Add" button

```java
CredentialDialog dialog = new CredentialDialog(this, null);
```
**Line 214**: Create dialog for new credential
- `null` means not editing existing credential

```java
Database.Credential cred = dialog.showDialog();
```
**Line 215**: Show dialog and wait for result

```java
if (cred != null) {
```
**Line 217**: If user clicked Save (not Cancel)

```java
try {
    database.insertCredential(currentUserId, cred.title, 
        cred.username, cred.password);
```
**Lines 218-220**: Save new credential to database

```java
loadCredentials();
```
**Line 221**: Refresh table to show new credential

```java
} catch (SQLException e) {
    showError("Failed to add credential: " + e.getMessage());
}
```
**Lines 222-224**: Show error if save fails

### On Edit (Lines 227-248)

```java
private void onEdit() {
    int row = table.getSelectedRow();
```
**Lines 227-228**: Get selected row number

```java
if (row < 0) {
    showError("Please select a credential to edit!");
    return;
}
```
**Lines 229-232**: If no row selected, show error and exit

```java
Database.Credential cred = credentials.get(row);
```
**Line 234**: Get credential from that row

```java
CredentialDialog dialog = new CredentialDialog(this, cred);
```
**Line 235**: Create dialog with existing credential data

```java
Database.Credential updated = dialog.showDialog();
```
**Line 236**: Show dialog, get updated data

```java
if (updated != null) {
    try {
        database.updateCredential(cred.id, updated.title, 
            updated.username, updated.password);
        loadCredentials();
```
**Lines 238-242**: If user saved changes, update database and refresh

### On Delete (Lines 250-271)

```java
private void onDelete() {
    int row = table.getSelectedRow();
    if (row < 0) {
        showError("Please select a credential to delete!");
        return;
    }
```
**Lines 250-255**: Get selected row, error if none selected

```java
int confirm = JOptionPane.showConfirmDialog(this,
    "Are you sure you want to delete this credential?",
    "Confirm Delete", JOptionPane.YES_NO_OPTION);
```
**Lines 257-259**: Show confirmation dialog
- Returns YES_OPTION or NO_OPTION

```java
if (confirm == JOptionPane.YES_OPTION) {
```
**Line 261**: If user clicked Yes

```java
try {
    Database.Credential cred = credentials.get(row);
    database.deleteCredential(cred.id);
    loadCredentials();
```
**Lines 262-265**: Delete from database and refresh

### On Copy Password (Lines 273-285)

```java
private void onCopyPassword() {
    int row = table.getSelectedRow();
    if (row < 0) {
        showError("Please select a credential to copy!");
        return;
    }
```
**Lines 273-278**: Get selected row, error if none

```java
Database.Credential cred = credentials.get(row);
```
**Line 280**: Get credential from row

```java
java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
    .setContents(new java.awt.datatransfer.StringSelection(cred.password), null);
```
**Lines 281-283**: Copy password to system clipboard
- `Toolkit.getDefaultToolkit()` - Get system toolkit
- `getSystemClipboard()` - Get clipboard
- `StringSelection` - Wraps string for clipboard
- `setContents()` - Actually copies it

```java
JOptionPane.showMessageDialog(this, "Password copied to clipboard!",
    "Copied", JOptionPane.INFORMATION_MESSAGE);
```
**Lines 284-285**: Show confirmation message

### On Copy Username (Lines 287-297)

```java
private void onCopyUsername() {
```
**Line 287**: Same as onCopyPassword but copies username

```java
Database.Credential cred = credentials.get(row);
java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
    .setContents(new java.awt.datatransfer.StringSelection(cred.username), null);
```
**Lines 293-295**: Copy username to clipboard

### On Generate (Lines 299-301)

```java
private void onGenerate() {
    new PasswordGeneratorDialog(this).setVisible(true);
}
```
**Lines 299-301**: Create and show password generator dialog

### On Check Strength (Lines 303-305)

```java
private void onCheckStrength() {
    new StrengthCheckerDialog(this, "").setVisible(true);
}
```
**Lines 303-305**: Create and show strength checker dialog
- Empty string means no pre-filled password

### On Theme (Lines 307-336)

```java
private void onTheme() {
    String[] options = {"Light", "Dark"};
    int choice = JOptionPane.showOptionDialog(this,
        "Select Theme:", "Theme",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
        null, options, options[0]);
```
**Lines 307-312**: Show theme selection dialog
- Returns 0 for Light, 1 for Dark, -1 for Cancel

```java
if (choice == 0) {
    currentTheme = Theme.LIGHT;
} else if (choice == 1) {
    currentTheme = Theme.DARK;
} else {
    return; // User cancelled
}
```
**Lines 314-320**: Set theme based on choice

```java
applyTheme(getContentPane());
```
**Line 322**: Apply theme to main content

```java
if (currentTheme == Theme.LIGHT) {
    table.setBackground(Color.WHITE);
    table.setForeground(Color.BLACK);
    table.getTableHeader().setBackground(new Color(230, 235, 245));
    table.getTableHeader().setForeground(Color.BLACK);
} else {
    table.setBackground(new Color(50, 55, 65));
    table.setForeground(Color.WHITE);
    table.getTableHeader().setBackground(new Color(35, 40, 50));
    table.getTableHeader().setForeground(Color.WHITE);
}
```
**Lines 324-333**: Apply theme to table

```java
repaint();
```
**Line 335**: Redraw window to show new colors

### On Logout (Lines 338-355)

```java
private void onLogout() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Confirm Logout", JOptionPane.YES_NO_OPTION);
```
**Lines 338-341**: Confirm logout

```java
if (confirm == JOptionPane.YES_OPTION) {
    currentUserId = -1;
    currentUsername = "";
    credentials.clear();
    tableModel.fireTableDataChanged();
```
**Lines 343-347**: Clear current session data

```java
dispose();
```
**Line 349**: Close current window

```java
SwingUtilities.invokeLater(() -> {
    SecureVaultSwing newInstance = new SecureVaultSwing();
    newInstance.setVisible(true);
});
```
**Lines 350-353**: Create new instance (shows login again)
- `invokeLater` runs on GUI thread

---

## Apply Theme Method (Lines 357-391)

```java
private void applyTheme(Container container) {
```
**Line 357**: Recursively apply theme to all components

```java
Color bg, fg, buttonBg, buttonFg;
if (currentTheme == Theme.LIGHT) {
    bg = new Color(245, 248, 255);
    fg = Color.BLACK;
    buttonBg = Color.WHITE;
    buttonFg = Color.BLACK;
} else {
    bg = new Color(40, 44, 52);
    fg = Color.WHITE;
    buttonBg = new Color(60, 65, 75);
    buttonFg = Color.WHITE;
}
```
**Lines 358-369**: Define colors based on theme

```java
container.setBackground(bg);
container.setForeground(fg);
```
**Lines 371-372**: Apply colors to container

```java
for (Component comp : container.getComponents()) {
```
**Line 374**: Loop through all child components

```java
if (comp instanceof JButton) {
    JButton btn = (JButton) comp;
    btn.setBackground(buttonBg);
    btn.setForeground(buttonFg);
    btn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(currentTheme == Theme.LIGHT ? 
            new Color(200, 200, 200) : new Color(80, 85, 95), 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    btn.setOpaque(true);
```
**Lines 375-384**: Style buttons
- Set background and foreground colors
- Add border (line + padding)
- `setOpaque(true)` - Make button solid (not transparent)

```java
} else if (comp instanceof JLabel) {
    comp.setForeground(fg);
} else if (comp instanceof Container) {
    applyTheme((Container) comp);
}
```
**Lines 385-389**: Handle other component types
- Labels: Just change text color
- Containers: Recursively apply theme to children

---

## Show Error Method (Lines 393-395)

```java
private void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
}
```
**Lines 393-395**: Helper to show error dialog

---

## VaultTableModel Class (Lines 397-421)

```java
static class VaultTableModel extends AbstractTableModel {
```
**Line 397**: Inner class for table data
- `static` - Doesn't need outer class instance
- `extends AbstractTableModel` - Provides table functionality

```java
private final String[] columns = {"Title", "Username", "Password", "Strength"};
```
**Line 398**: Column headers

```java
private final List<Database.Credential> data;
```
**Line 399**: Reference to credentials list

```java
VaultTableModel(List<Database.Credential> data) {
    this.data = data;
}
```
**Lines 401-403**: Constructor stores data reference

```java
@Override public int getRowCount() { return data.size(); }
@Override public int getColumnCount() { return columns.length; }
@Override public String getColumnName(int col) { return columns[col]; }
```
**Lines 405-407**: Required methods for AbstractTableModel

```java
@Override
public Object getValueAt(int row, int col) {
    Database.Credential cred = data.get(row);
    return switch (col) {
        case 0 -> cred.title;
        case 1 -> cred.username;
        case 2 -> "************";
        case 3 -> StrengthChecker.checkStrength(cred.password);
        default -> "";
    };
}
```
**Lines 409-419**: Get value for specific cell
- Column 0: Show title
- Column 1: Show username
- Column 2: Show asterisks (hide password)
- Column 3: Calculate and return strength
- Uses modern switch expression

---

## StrengthBarRenderer Class (Lines 423-458)

```java
static class StrengthBarRenderer extends DefaultTableCellRenderer {
```
**Line 423**: Custom renderer for strength column

```java
@Override
public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
```
**Lines 424-426**: Method called to render each cell

```java
StrengthChecker.Strength strength = (StrengthChecker.Strength) value;
int score = StrengthChecker.computeScore(
    ((VaultTableModel) table.getModel()).data.get(row).password);
```
**Lines 428-430**: Get strength enum and numeric score

```java
JPanel panel = new JPanel(new GridLayout(1, 5, 2, 0));
panel.setOpaque(true);
panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
```
**Lines 432-434**: Create panel with 5 slots for bars
- `GridLayout(1, 5, 2, 0)` - 1 row, 5 columns, 2-pixel gap
- Match table selection color if row selected

```java
Color color = switch (strength) {
    case WEAK -> new Color(220, 53, 69);
    case MEDIUM -> new Color(255, 193, 7);
    case STRONG -> new Color(40, 167, 69);
};
```
**Lines 436-440**: Pick color based on strength
- WEAK: Red
- MEDIUM: Yellow
- STRONG: Green

```java
int filledBars = Math.min(5, (score + 1) / 2 + 1);
```
**Line 442**: Calculate how many bars to fill
- Score 0-1: 1 bar
- Score 2-3: 2 bars
- Score 4-5: 3 bars
- Score 6+: 4-5 bars

```java
for (int i = 0; i < 5; i++) {
    JPanel bar = new JPanel();
    bar.setBackground(i < filledBars ? color : Color.LIGHT_GRAY);
    bar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    panel.add(bar);
}
```
**Lines 444-449**: Create 5 bars
- Filled bars: Use strength color
- Empty bars: Light gray

```java
return panel;
```
**Line 451**: Return panel to display in cell

---

## Main Method (Lines 460-473)

```java
public static void main(String[] args) {
```
**Line 460**: Entry point - Java starts here

```java
SwingUtilities.invokeLater(() -> {
```
**Line 461**: Run GUI code on special GUI thread

```java
try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (Exception e) {
    // Use default
}
```
**Lines 462-466**: Try to use system look and feel
- Makes app look native to OS
- If fails, use default Java look

```java
SecureVaultSwing app = new SecureVaultSwing();
app.setVisible(true);
```
**Lines 468-469**: Create app and show window

---

## Summary: What Happens When You Run

1. **main()** - Java starts here
2. **Constructor** - Creates database, shows login
3. **showLogin()** - User logs in or registers
4. **buildUI()** - Builds main window
5. **loadCredentials()** - Loads data from database
6. **setVisible(true)** - Shows window
7. **User clicks buttons** - Calls action handlers (onAdd, onEdit, etc.)
8. **Window closes** - Database connection closed, app exits

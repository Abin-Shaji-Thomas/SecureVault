import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecureVaultSwing extends JFrame { 
    // Theme enum
    private enum Theme { LIGHT, DARK }
    private Database database;
    private UserManager userManager;
    private int currentUserId = -1;
    private String currentUsername = "";
    private final List<Database.Credential> credentials = new ArrayList<>();
    private final VaultTableModel tableModel = new VaultTableModel(credentials);
    private final JTable table = new JTable(tableModel);
    private JLabel userLabel;
    private Theme currentTheme = Theme.DARK;
    public SecureVaultSwing() {
        super("SecureVault - Password Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        try {
            database = new Database();
            userManager = new UserManager(database.getConnection());
        } catch (SQLException e) {
            showError("Database initialization failed: " + e.getMessage());
            System.exit(1);
        }

        if (!showLogin()) {
            System.exit(0);
        }
        buildUI();
        loadCredentials();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (database != null) database.close();
            }
        });
    }
    private boolean showLogin() {
        while (true) {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.setVisible(true);  
            if (!loginDialog.isSucceeded()) {
                return false;
            }   
            String username = loginDialog.getUsername();
            String password = loginDialog.getPassword();
            
            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and password cannot be empty!");
                continue;
            }
            try {
                if (loginDialog.isCreateNewUser()) {
                    if (userManager.userExists(username)) {
                        showError("Username already exists! Choose a different username.");
                        continue;
                    }
                    
                    if (userManager.createUser(username, password)) {
                        currentUserId = userManager.authenticateUser(username, password);
                        currentUsername = username;
                        JOptionPane.showMessageDialog(this,
                            "User created successfully! Welcome, " + username + "!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        return true;
                    } else {
                        showError("Failed to create user!");
                        continue;
                    }
                } else {
                    currentUserId = userManager.authenticateUser(username, password);
                    if (currentUserId != -1) {
                        currentUsername = username;
                        return true;
                    } else {
                        showError("Invalid username or password!");
                        continue;
                    }
                }
            } catch (SQLException e) {
                showError("Authentication error: " + e.getMessage());
            }
        }
    }
    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        applyTheme(mainPanel);
        
        mainPanel.add(createToolbar(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftPanel.add(createButton("Add", _ -> onAdd()));
        leftPanel.add(createButton("Edit", _ -> onEdit()));
        leftPanel.add(createButton("Delete", _ -> onDelete()));
        leftPanel.add(createButton("Copy Password", _ -> onCopyPassword()));
        leftPanel.add(createButton("Copy Username", _ -> onCopyUsername()));
        leftPanel.add(createButton("Generate", _ -> onGenerate()));
        leftPanel.add(createButton("Check Strength", _ -> onCheckStrength()));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        userLabel = new JLabel("User: " + currentUsername);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        rightPanel.add(userLabel);
        rightPanel.add(createButton("Theme", _ -> onTheme()));
        rightPanel.add(createButton("Logout", _ -> onLogout()));
        
        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);
        
        return toolbar;
    }

    private JPanel createTablePanel() {
        table.setRowHeight(35);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.getTableHeader().setReorderingAllowed(false);
        
        // Apply theme colors to table
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
        
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        table.getColumnModel().getColumn(3).setCellRenderer(new StrengthBarRenderer());
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyPassItem = new JMenuItem("Copy Password");
        copyPassItem.addActionListener(_ -> onCopyPassword());
        popup.add(copyPassItem);
        
        JMenuItem copyUserItem = new JMenuItem("Copy Username");
        copyUserItem.addActionListener(_ -> onCopyUsername());
        popup.add(copyUserItem);
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (e.isPopupTrigger()) showPopup(e);
            }
            private void showPopup(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    table.setRowSelectionInterval(row, row);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        return btn;
    }

    private void loadCredentials() {
        try {
            List<Database.Credential> data = database.getAllCredentials(currentUserId);
            credentials.clear();
            credentials.addAll(data);
            tableModel.fireTableDataChanged();
        } catch (SQLException e) {
            showError("Failed to load credentials: " + e.getMessage());
        }
    }
    //adding data
    private void onAdd() {
        CredentialDialog dialog = new CredentialDialog(this, null);
        Database.Credential cred = dialog.showDialog();
        
        if (cred != null) {
            try {
                database.insertCredential(currentUserId, cred.title, 
                    cred.username, cred.password);
                loadCredentials();
            } catch (SQLException e) {
                showError("Failed to add credential: " + e.getMessage());
            }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential to edit!");
            return;
        }
        
        Database.Credential cred = credentials.get(row);
        CredentialDialog dialog = new CredentialDialog(this, cred);
        Database.Credential updated = dialog.showDialog();
        
        if (updated != null) {
            try {
                database.updateCredential(cred.id, updated.title, 
                    updated.username, updated.password);
                loadCredentials();
            } catch (SQLException e) {
                showError("Failed to update credential: " + e.getMessage());
            }
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential to delete!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this credential?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Database.Credential cred = credentials.get(row);
                database.deleteCredential(cred.id);
                loadCredentials();
            } catch (SQLException e) {
                showError("Failed to delete credential: " + e.getMessage());
            }
        }
    }

    private void onCopyPassword() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential to copy!");
            return;
        }
        
        Database.Credential cred = credentials.get(row);
        // Copy to clipboard without auto-clear
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(new java.awt.datatransfer.StringSelection(cred.password), null);
        JOptionPane.showMessageDialog(this, "Password copied to clipboard!",
            "Copied", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onCopyUsername() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential to copy!");
            return;
        }
        
        Database.Credential cred = credentials.get(row);
        java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
            .setContents(new java.awt.datatransfer.StringSelection(cred.username), null);
        JOptionPane.showMessageDialog(this, "Username copied to clipboard!",
            "Copied", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onGenerate() {
        new PasswordGeneratorDialog(this).setVisible(true);
    }

    private void onCheckStrength() {
        new StrengthCheckerDialog(this, "").setVisible(true);
    }

    private void onTheme() {
        String[] options = {"Light", "Dark"};
        int choice = JOptionPane.showOptionDialog(this,
            "Select Theme:", "Theme",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
        
        if (choice == 0) {
            currentTheme = Theme.LIGHT;
        } else if (choice == 1) {
            currentTheme = Theme.DARK;
        } else {
            return; // User cancelled
        }
        
        // Apply theme to main content
        applyTheme(getContentPane());
        
        // Apply theme to table
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
        
        repaint();
    }

    private void onLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            currentUserId = -1;
            currentUsername = "";
            credentials.clear();
            tableModel.fireTableDataChanged();
            
            dispose();
            SwingUtilities.invokeLater(() -> {
                SecureVaultSwing newInstance = new SecureVaultSwing();
                newInstance.setVisible(true);
            });
        }
    }

    private void applyTheme(Container container) {
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
        
        container.setBackground(bg);
        container.setForeground(fg);
        
        for (Component comp : container.getComponents()) {
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
            } else if (comp instanceof JLabel) {
                comp.setForeground(fg);
            } else if (comp instanceof Container) {
                applyTheme((Container) comp);
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static class VaultTableModel extends AbstractTableModel {
        private final String[] columns = {"Title", "Username", "Password", "Strength"};
        private final List<Database.Credential> data;

        VaultTableModel(List<Database.Credential> data) {
            this.data = data;
        }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return columns.length; }
        @Override public String getColumnName(int col) { return columns[col]; }

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
    }

    static class StrengthBarRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            StrengthChecker.Strength strength = (StrengthChecker.Strength) value;
            int score = StrengthChecker.computeScore(
                ((VaultTableModel) table.getModel()).data.get(row).password);
            
            JPanel panel = new JPanel(new GridLayout(1, 5, 2, 0));
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            
            Color color = switch (strength) {
                case WEAK -> new Color(220, 53, 69);
                case MEDIUM -> new Color(255, 193, 7);
                case STRONG -> new Color(40, 167, 69);
            };
            
            int filledBars = Math.min(5, (score + 1) / 2 + 1);
            
            for (int i = 0; i < 5; i++) {
                JPanel bar = new JPanel();
                bar.setBackground(i < filledBars ? color : Color.LIGHT_GRAY);
                bar.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                panel.add(bar);
            }
            
            return panel;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default
            }
            
            SecureVaultSwing app = new SecureVaultSwing();
            app.setVisible(true);
        });
    }
}

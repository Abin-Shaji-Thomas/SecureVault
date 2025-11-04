import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;

@SuppressWarnings("serial")
public class SecureVaultSwingEnhanced extends JFrame {
    // Theme enum
    private enum Theme { LIGHT, DARK }
    private enum FilterType { ALL, FAVORITES, WEAK, MEDIUM, STRONG }
    private enum SortType { TITLE, USERNAME, DATE_CREATED, DATE_MODIFIED, FAVORITE }
    
    private Database database;
    private UserManager userManager;
    private CategoryManager categoryManager;
    private AttachmentManager attachmentManager;
    private int currentUserId = -1;
    private String currentUsername = "";
    private final List<Database.Credential> allCredentials = new ArrayList<>();
    private final List<Database.Credential> filteredCredentials = new ArrayList<>();
    private final VaultTableModel tableModel = new VaultTableModel(filteredCredentials);
    private final JTable table = new JTable(tableModel);
    private JLabel userLabel;
    private JLabel statusLabel;
    private JTextField searchField;
    private Theme currentTheme = Theme.DARK;
    private FilterType currentFilter = FilterType.ALL;
    private SortType currentSort = SortType.FAVORITE;
    private boolean sortAscending = false;
    
    // Security features
    private Timer sessionTimer;
    private static final int SESSION_TIMEOUT = 5 * 60 * 1000; // 5 minutes
    
    @SuppressWarnings("this-escape")
    public SecureVaultSwingEnhanced() {
        super("SecureVault Pro - Password Manager");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        try {
            database = new Database();
            userManager = new UserManager(database.getConnection());
            categoryManager = new CategoryManager(database.getConnection());
            attachmentManager = new AttachmentManager(database.getConnection());
        } catch (SQLException e) {
            showError("Database initialization failed: " + e.getMessage());
            System.exit(1);
        }

        if (!showLogin()) {
            System.exit(0);
        }
        
        buildUI();
        loadCredentials();
        
        // Set up keyboard shortcuts
        setupKeyboardShortcuts();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (database != null) database.close();
            }
        });
    }
    
    private void setupKeyboardShortcuts() {
        JRootPane rootPane = getRootPane();
        
        // Ctrl+F - Focus search
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "focusSearch");
        rootPane.getActionMap().put("focusSearch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { if(searchField != null) searchField.requestFocus(); }
        });
            
        // Ctrl+N - New credential
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "addNew");
        rootPane.getActionMap().put("addNew", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { onAdd(); }
        });
            
        // Ctrl+Shift+C - Copy password
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK), "copyPass");
        rootPane.getActionMap().put("copyPass", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { onCopyPassword(); }
        });
            
        // Ctrl+L - Lock vault
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK), "lock");
        rootPane.getActionMap().put("lock", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { lockVault(); }
        });
            
        // Delete - Delete credential
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        rootPane.getActionMap().put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { onDelete(); }
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
                        setEncryptionKeyForUser(username, password);
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
                        setEncryptionKeyForUser(username, password);
                        // Initialize demo data for test user on first login
                        try {
                            database.initializeDemoData(currentUserId);
                        } catch (SQLException e) {
                            System.err.println("Warning: Failed to initialize demo data: " + e.getMessage());
                        }
                        return true;
                    } else {
                        showError("Invalid username or password!");
                        continue;
                    }
                }
            } catch (SQLException e) {
                showError("Database error: " + e.getMessage());
            }
        }
    }
    
    private void setEncryptionKeyForUser(String username, String password) {
        try {
            byte[] salt = userManager.getUserSalt(username);
            if (salt != null) {
                SecretKey key = PasswordEncryption.deriveKey(password, salt);
                database.setEncryptionKey(key);
                initSessionTimeout();
            }
        } catch (Exception e) {
            showError("Failed to initialize encryption: " + e.getMessage());
        }
    }
    
    private void initSessionTimeout() {
        if (sessionTimer != null) {
            sessionTimer.stop();
        }
        
        sessionTimer = new Timer(SESSION_TIMEOUT, _ -> {
            JOptionPane.showMessageDialog(this,
                "Session timed out due to inactivity. Please login again.",
                "Session Timeout", JOptionPane.WARNING_MESSAGE);
            lockVault();
        });
        sessionTimer.setRepeats(false);
        sessionTimer.start();
        
        // Reset timer on user activity
        AWTEventListener activityListener = _ -> resetSessionTimer();
        Toolkit.getDefaultToolkit().addAWTEventListener(activityListener,
            AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }
    
    private void resetSessionTimer() {
        if (sessionTimer != null && sessionTimer.isRunning()) {
            sessionTimer.restart();
        }
    }
    
    private void lockVault() {
        if (database != null) {
            database.clearEncryptionKey();
        }
        if (sessionTimer != null) {
            sessionTimer.stop();
        }
        
        currentUserId = -1;
        currentUsername = "";
        allCredentials.clear();
        filteredCredentials.clear();
        tableModel.fireTableDataChanged();
        
        dispose();
        SwingUtilities.invokeLater(() -> {
            new SecureVaultSwingEnhanced().setVisible(true);
        });
    }
    
    private void copyToClipboardSecure(String text, boolean sensitive) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        
        if (sensitive) {
            Timer clearTimer = new Timer(30000, _ -> {
                try {
                    String current = (String) clipboard.getData(DataFlavor.stringFlavor);
                    if (text.equals(current)) {
                        clipboard.setContents(new StringSelection(""), null);
                        updateStatus("Clipboard cleared for security");
                    }
                } catch (Exception ex) {
                    // Ignore
                }
            });
            clearTimer.setRepeats(false);
            clearTimer.start();
            updateStatus("Copied! Will auto-clear in 30 seconds");
        } else {
            updateStatus("Copied to clipboard");
        }
    }
    
    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        
        // Add gradient header
        mainPanel.add(createGradientHeader(), BorderLayout.NORTH);
        
        // Content panel with proper vertical layout
        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Create a panel for toolbar and search/filter
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.add(createToolbar(), BorderLayout.NORTH);
        topPanel.add(createSearchAndFilterPanel(), BorderLayout.CENTER);
        
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);
        
        add(mainPanel);
        applyTheme(mainPanel);
    }
    
    private JPanel createGradientHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Gradient based on theme
                Color color1, color2;
                if (currentTheme == Theme.LIGHT) {
                    color1 = new Color(101, 84, 192);  // Purple
                    color2 = new Color(142, 84, 233);  // Lighter purple
                } else {
                    color1 = new Color(88, 101, 242);  // Blue
                    color2 = new Color(118, 75, 162);  // Purple
                }
                
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), 0, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 60));
        header.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("🔐 SecureVault Pro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 15));
        
        userLabel = new JLabel("👤 " + currentUsername);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(255, 255, 255, 200));
        userLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 20));
        
        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Left panel with GridLayout to ensure all buttons are visible
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        leftPanel.add(createStyledButton("➕ Add", _ -> onAdd(), "Ctrl+N"));
        leftPanel.add(createStyledButton("✏️ Edit", _ -> onEdit(), ""));
        leftPanel.add(createStyledButton("🗑️ Delete", _ -> onDelete(), "Del"));
        leftPanel.add(createStyledButton("🔑 Copy Pass", _ -> onCopyPassword(), "Ctrl+Shift+C"));
        leftPanel.add(createStyledButton("👤 Copy User", _ -> onCopyUsername(), ""));
        leftPanel.add(createStyledButton("⭐ Favorite", _ -> onToggleFavorite(), ""));
        leftPanel.add(createStyledButton("🎲 Generate", _ -> onGenerate(), ""));
        leftPanel.add(createStyledButton("🔍 Strength", _ -> onCheckStrength(), ""));
        leftPanel.add(createStyledButton("📥 Import", _ -> onImport(), ""));
        leftPanel.add(createStyledButton("📤 Export", _ -> onExport(), ""));
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        rightPanel.add(createStyledButton("📊 Health", _ -> onHealthDashboard(), ""));
        rightPanel.add(createStyledButton("🔒 Lock", _ -> lockVault(), "Ctrl+L"));
        rightPanel.add(createStyledButton("🎨 Theme", _ -> onTheme(), ""));
        rightPanel.add(createStyledButton("🚪 Logout", _ -> onLogout(), ""));
        
        toolbar.add(leftPanel, BorderLayout.CENTER);
        toolbar.add(rightPanel, BorderLayout.EAST);
        
        return toolbar;
    }
    
    private JPanel createSearchAndFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 0, 15, 0));
        
        // Search panel with modern styling
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        JLabel searchLabel = new JLabel("🔍");
        searchLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(350, 38));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Apply theme-aware styling to search field
        if (currentTheme == Theme.LIGHT) {
            searchField.setBackground(Color.WHITE);
            searchField.setForeground(new Color(33, 33, 33));
            searchField.setCaretColor(new Color(33, 33, 33));
            searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 204, 220), 2),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
        } else {
            searchField.setBackground(new Color(25, 27, 38));
            searchField.setForeground(new Color(220, 221, 230));
            searchField.setCaretColor(new Color(220, 221, 230));
            searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 42, 54), 2),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
        }
        
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                applyFiltersAndSort();
            }
        });
        // Add placeholder effect
        searchField.putClientProperty("JTextField.placeholderText", "Search credentials...");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        
        JLabel filterLabel = new JLabel("Filter:");
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{
            "All", "⭐ Favorites Only", "🔴 Weak Passwords", "🟡 Medium Passwords", "🟢 Strong Passwords"
        });
        filterCombo.addActionListener(_ -> {
            currentFilter = switch (filterCombo.getSelectedIndex()) {
                case 1 -> FilterType.FAVORITES;
                case 2 -> FilterType.WEAK;
                case 3 -> FilterType.MEDIUM;
                case 4 -> FilterType.STRONG;
                default -> FilterType.ALL;
            };
            applyFiltersAndSort();
        });
        
        JLabel sortLabel = new JLabel("Sort by:");
        JComboBox<String> sortCombo = new JComboBox<>(new String[]{
            "⭐ Favorite First", "Title A-Z", "Title Z-A", "Username A-Z", 
            "Recently Modified", "Recently Created"
        });
        sortCombo.addActionListener(_ -> {
            switch (sortCombo.getSelectedIndex()) {
                case 0 -> { currentSort = SortType.FAVORITE; sortAscending = false; }
                case 1 -> { currentSort = SortType.TITLE; sortAscending = true; }
                case 2 -> { currentSort = SortType.TITLE; sortAscending = false; }
                case 3 -> { currentSort = SortType.USERNAME; sortAscending = true; }
                case 4 -> { currentSort = SortType.DATE_MODIFIED; sortAscending = false; }
                case 5 -> { currentSort = SortType.DATE_CREATED; sortAscending = false; }
            }
            applyFiltersAndSort();
        });
        
        filterSortPanel.add(filterLabel);
        filterSortPanel.add(filterCombo);
        filterSortPanel.add(Box.createHorizontalStrut(20));
        filterSortPanel.add(sortLabel);
        filterSortPanel.add(sortCombo);
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(filterSortPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        table.setRowHeight(42);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 38));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // Fav
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Username
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Password
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Category
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Strength
        table.getColumnModel().getColumn(6).setPreferredWidth(130); // Expiry
        table.getColumnModel().getColumn(7).setPreferredWidth(140); // Modified
        
        table.getColumnModel().getColumn(0).setCellRenderer(new FavoriteRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new CategoryRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new StrengthBarRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new ExpiryRenderer());
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onEdit();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    showContextMenu(e);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(table.getBackground());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(currentTheme == Theme.LIGHT ? 
                new Color(220, 224, 235) : new Color(40, 42, 54), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void showContextMenu(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        if (row >= 0) {
            table.setRowSelectionInterval(row, row);
            
            JPopupMenu popup = new JPopupMenu();
            popup.add(createMenuItem("Copy Password", _ -> onCopyPassword()));
            popup.add(createMenuItem("Copy Username", _ -> onCopyUsername()));
            popup.addSeparator();
            popup.add(createMenuItem("Toggle Favorite", _ -> onToggleFavorite()));
            popup.add(createMenuItem("Edit", _ -> onEdit()));
            popup.add(createMenuItem("Delete", _ -> onDelete()));
            
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
    
    private JMenuItem createMenuItem(String text, ActionListener listener) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(listener);
        return item;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                if (currentTheme == Theme.LIGHT) {
                    g2d.setColor(new Color(240, 242, 248));
                } else {
                    g2d.setColor(new Color(20, 22, 30));
                }
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        statusBar.setPreferredSize(new Dimension(getWidth(), 32));
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        
        statusLabel = new JLabel("✅ Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.add(statusLabel, BorderLayout.WEST);
        
        JLabel countLabel = new JLabel();
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusBar.add(countLabel, BorderLayout.EAST);
        
        // Update count in a timer
        Timer countTimer = new Timer(1000, _ -> {
            int total = allCredentials.size();
            int shown = filteredCredentials.size();
            int favorites = (int) allCredentials.stream().filter(c -> c.isFavorite).count();
            countLabel.setText(String.format("📊 Total: %d  |  Showing: %d  |  ⭐ Favorites: %d", 
                total, shown, favorites));
        });
        countTimer.start();
        
        return statusBar;
    }
    
    private JButton createStyledButton(String text, ActionListener listener, String tooltip) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.addActionListener(listener);
        if (!tooltip.isEmpty()) {
            btn.setToolTipText(tooltip);
        }
        return btn;
    }
    
    private void loadCredentials() {
        try {
            List<Database.Credential> data = database.getAllCredentials(currentUserId);
            allCredentials.clear();
            allCredentials.addAll(data);
            
            // Debug: Show first credential's data
            if (!data.isEmpty()) {
                Database.Credential first = data.get(0);
                System.out.println("DEBUG: First credential loaded:");
                System.out.println("  Title: " + first.title);
                System.out.println("  Category: " + first.category);
                System.out.println("  Website: " + first.websiteUrl);
                System.out.println("  Expiry: " + first.expiryDate);
            }
            
            applyFiltersAndSort();
            updateStatus(String.format("Loaded %d credentials", data.size()));
        } catch (SQLException e) {
            showError("Failed to load credentials: " + e.getMessage());
        }
    }
    
    private void applyFiltersAndSort() {
        String searchText = searchField != null ? searchField.getText().toLowerCase() : "";
        
        filteredCredentials.clear();
        filteredCredentials.addAll(allCredentials.stream()
            .filter(c -> {
                // Apply search filter
                boolean matchesSearch = searchText.isEmpty() ||
                    c.title.toLowerCase().contains(searchText) ||
                    c.username.toLowerCase().contains(searchText) ||
                    (c.notes != null && c.notes.toLowerCase().contains(searchText));
                
                if (!matchesSearch) return false;
                
                // Apply type filter
                return switch (currentFilter) {
                    case FAVORITES -> c.isFavorite;
                    case WEAK -> StrengthChecker.checkStrength(c.password) == StrengthChecker.Strength.WEAK;
                    case MEDIUM -> StrengthChecker.checkStrength(c.password) == StrengthChecker.Strength.MEDIUM;
                    case STRONG -> StrengthChecker.checkStrength(c.password) == StrengthChecker.Strength.STRONG;
                    default -> true;
                };
            })
            .sorted((c1, c2) -> {
                int result = switch (currentSort) {
                    case FAVORITE -> Boolean.compare(c2.isFavorite, c1.isFavorite);
                    case TITLE -> c1.title.compareToIgnoreCase(c2.title);
                    case USERNAME -> c1.username.compareToIgnoreCase(c2.username);
                    case DATE_CREATED -> (c1.createdDate != null && c2.createdDate != null) ?
                        c2.createdDate.compareTo(c1.createdDate) : 0;
                    case DATE_MODIFIED -> (c1.modifiedDate != null && c2.modifiedDate != null) ?
                        c2.modifiedDate.compareTo(c1.modifiedDate) : 0;
                };
                return sortAscending ? result : -result;
            })
            .collect(Collectors.toList()));
        
        tableModel.fireTableDataChanged();
    }
    
    private void onAdd() {
        EnhancedCredentialDialog dialog = new EnhancedCredentialDialog(this, null, categoryManager, currentUserId);
        if (dialog.showDialog()) {
            try {
                database.insertCredential(currentUserId, dialog.getTitle(),
                    dialog.getUsername(), dialog.getPassword(), 
                    dialog.getNotes(), dialog.isFavorite(),
                    dialog.getCategory(), dialog.getWebsiteUrl(), dialog.getExpiryDate());
                loadCredentials();
                updateStatus("Credential added successfully");
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
        
        Database.Credential cred = filteredCredentials.get(row);
        EnhancedCredentialDialog dialog = new EnhancedCredentialDialog(this, cred, categoryManager, currentUserId);
        if (dialog.showDialog()) {
            try {
                database.updateCredential(cred.id, dialog.getTitle(),
                    dialog.getUsername(), dialog.getPassword(),
                    dialog.getNotes(), dialog.isFavorite(),
                    dialog.getCategory(), dialog.getWebsiteUrl(), dialog.getExpiryDate());
                loadCredentials();
                updateStatus("Credential updated successfully");
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
        
        Database.Credential cred = filteredCredentials.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete credential \"" + cred.title + "\"?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                database.deleteCredential(cred.id);
                loadCredentials();
                updateStatus("Credential deleted");
            } catch (SQLException e) {
                showError("Failed to delete credential: " + e.getMessage());
            }
        }
    }
    
    private void onCopyPassword() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential!");
            return;
        }
        copyToClipboardSecure(filteredCredentials.get(row).password, true);
    }
    
    private void onCopyUsername() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential!");
            return;
        }
        copyToClipboardSecure(filteredCredentials.get(row).username, false);
    }
    
    private void onToggleFavorite() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Please select a credential!");
            return;
        }
        
        try {
            Database.Credential cred = filteredCredentials.get(row);
            database.toggleFavorite(cred.id);
            loadCredentials();
            updateStatus(cred.isFavorite ? "Removed from favorites" : "Added to favorites");
        } catch (SQLException e) {
            showError("Failed to toggle favorite: " + e.getMessage());
        }
    }
    
    private void onGenerate() {
        new PasswordGeneratorDialog(this).setVisible(true);
    }
    
    private void onCheckStrength() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Database.Credential cred = filteredCredentials.get(row);
            new StrengthCheckerDialog(this, cred.password).setVisible(true);
        } else {
            new StrengthCheckerDialog(this, "").setVisible(true);
        }
    }
    
    private void onTheme() {
        currentTheme = (currentTheme == Theme.LIGHT) ? Theme.DARK : Theme.LIGHT;
        applyTheme(getContentPane());
        applyTableTheme();
        repaint();
        updateStatus("Theme changed to " + currentTheme);
    }
    
    private void applyTableTheme() {
        if (currentTheme == Theme.LIGHT) {
            // Light theme - Clean white background with dark text
            table.setBackground(Color.WHITE);
            table.setForeground(new Color(33, 33, 33)); // Dark text for rows
            table.setSelectionBackground(new Color(101, 84, 192)); // Purple accent
            table.setSelectionForeground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(101, 84, 192)); // Purple header
            table.getTableHeader().setForeground(Color.WHITE); // White text on purple header
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.setGridColor(new Color(220, 222, 230));
        } else {
            // Dark theme - Samsung inspired deep blue-black
            table.setBackground(new Color(18, 18, 28));
            table.setForeground(new Color(230, 230, 240));
            table.setSelectionBackground(new Color(88, 101, 242)); // Discord blue
            table.setSelectionForeground(Color.WHITE);
            table.getTableHeader().setBackground(new Color(25, 27, 38));
            table.getTableHeader().setForeground(new Color(200, 204, 220));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
            table.setGridColor(new Color(40, 42, 54));
        }
        
        // Add alternating row colors with proper text visibility
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if (currentTheme == Theme.LIGHT) {
                        // Light mode - white/very light gray alternating
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                        c.setForeground(new Color(33, 33, 33)); // Dark text for readability
                    } else {
                        // Dark mode - dark blue alternating
                        c.setBackground(row % 2 == 0 ? new Color(18, 18, 28) : new Color(22, 22, 32));
                        c.setForeground(new Color(230, 230, 240)); // Light text
                    }
                } else {
                    // Selected rows - always white text on colored background
                    c.setForeground(Color.WHITE);
                }
                
                setHorizontalAlignment(column == 0 ? CENTER : LEFT);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        });
    }
    
    private void onImport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Credentials");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "CSV or ZIP files", "csv", "zip"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ImportExportManager importExport = new ImportExportManager(database, currentUserId, database.getEncryptionKey());
                java.io.File selectedFile = fileChooser.getSelectedFile();
                int imported = 0;
                
                if (selectedFile.getName().toLowerCase().endsWith(".zip")) {
                    imported = importExport.importFromArchive(selectedFile, attachmentManager);
                } else {
                    imported = importExport.importFromCSV(selectedFile);
                }
                
                loadCredentials();
                JOptionPane.showMessageDialog(this, 
                    "Successfully imported " + imported + " credentials!",
                    "Import Complete", JOptionPane.INFORMATION_MESSAGE);
                updateStatus("Imported " + imported + " credentials");
            } catch (Exception e) {
                showError("Import failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void onExport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Credentials");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Encrypted Archive (*.zip)", "zip"));
        fileChooser.setSelectedFile(new java.io.File("securevault_backup_" + 
            java.time.LocalDate.now() + ".zip"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                ImportExportManager importExport = new ImportExportManager(database, currentUserId, database.getEncryptionKey());
                java.io.File outputFile = fileChooser.getSelectedFile();
                
                // Ensure .zip extension
                if (!outputFile.getName().toLowerCase().endsWith(".zip")) {
                    outputFile = new java.io.File(outputFile.getAbsolutePath() + ".zip");
                }
                
                importExport.exportToArchive(outputFile, allCredentials, attachmentManager);
                
                JOptionPane.showMessageDialog(this, 
                    "Successfully exported " + allCredentials.size() + " credentials to:\n" + outputFile.getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
                updateStatus("Exported " + allCredentials.size() + " credentials");
            } catch (Exception e) {
                showError("Export failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private void onHealthDashboard() {
        HealthDashboard health = new HealthDashboard(allCredentials);
        HealthDashboard.Stats stats = health.calculateStats();
        java.util.List<Database.Credential> needsAttention = health.getPasswordsNeedingAttention();
        
        HealthDashboardDialog dialog = new HealthDashboardDialog(this, stats, needsAttention.size());
        dialog.setVisible(true);
    }
    
    private void onLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (database != null) {
                database.clearEncryptionKey();
            }
            if (sessionTimer != null) {
                sessionTimer.stop();
            }
            System.exit(0);
        }
    }
    
    private void applyTheme(Container container) {
        Color bg, fg, buttonBg, buttonFg;
        
        if (currentTheme == Theme.LIGHT) {
            // Light theme - Samsung inspired
            bg = new Color(246, 248, 252);          // Soft blue-gray background
            fg = new Color(33, 33, 33);             // Almost black text
            buttonBg = new Color(101, 84, 192);     // Purple accent
            buttonFg = Color.WHITE;                 // White text on buttons
        } else {
            // Dark theme - Samsung/Discord inspired
            bg = new Color(15, 16, 23);             // Deep dark blue-black
            fg = new Color(220, 221, 230);          // Light gray text
            buttonBg = new Color(88, 101, 242);     // Bright blue (Discord)
            buttonFg = Color.WHITE;                 // White text
        }
        
        container.setBackground(bg);
        container.setForeground(fg);
        
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton btn) {
                // Modern gradient-style buttons
                btn.setBackground(buttonBg);
                btn.setForeground(buttonFg);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(buttonBg, 0),
                    BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                btn.setOpaque(true);
                btn.setFocusPainted(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Hover effect
                btn.addMouseListener(new MouseAdapter() {
                    Color originalBg = btn.getBackground();
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        btn.setBackground(brighten(originalBg, 1.2f));
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        btn.setBackground(originalBg);
                    }
                });
            } else if (comp instanceof JLabel label) {
                label.setForeground(fg);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            } else if (comp instanceof JTextField field) {
                if (currentTheme == Theme.LIGHT) {
                    field.setBackground(Color.WHITE);
                    field.setForeground(new Color(33, 33, 33));
                    field.setCaretColor(new Color(33, 33, 33));
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 204, 220), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                } else {
                    field.setBackground(new Color(25, 27, 38));
                    field.setForeground(new Color(220, 221, 230));
                    field.setCaretColor(new Color(220, 221, 230));
                    field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(40, 42, 54), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                    ));
                }
                field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            } else if (comp instanceof JComboBox<?> combo) {
                if (currentTheme == Theme.LIGHT) {
                    combo.setBackground(Color.WHITE);
                    combo.setForeground(new Color(33, 33, 33));
                } else {
                    combo.setBackground(new Color(25, 27, 38));
                    combo.setForeground(new Color(220, 221, 230));
                }
                combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            } else if (comp instanceof Container) {
                applyTheme((Container) comp);
            }
        }
        
        applyTableTheme();
    }
    
    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * factor));
        int g = Math.min(255, (int)(color.getGreen() * factor));
        int b = Math.min(255, (int)(color.getBlue() * factor));
        return new Color(r, g, b);
    }
    
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    @SuppressWarnings("serial")
    static class VaultTableModel extends AbstractTableModel {
        private final String[] columns = {"⭐", "Title", "Username", "Password", "Category", "Strength", "Expiry", "Modified"};
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
                case 0 -> cred.isFavorite;
                case 1 -> cred.title;
                case 2 -> cred.username;
                case 3 -> "••••••••••••";
                case 4 -> cred.category != null && !cred.category.isEmpty() ? cred.category : "Other";
                case 5 -> StrengthChecker.checkStrength(cred.password);
                case 6 -> {
                    if (cred.expiryDate != null && !cred.expiryDate.isEmpty()) {
                        if (HealthDashboard.isExpired(cred.expiryDate)) {
                            yield "⛔ EXPIRED";
                        } else if (HealthDashboard.isExpiringSoon(cred.expiryDate)) {
                            long days = HealthDashboard.getDaysUntilExpiry(cred.expiryDate);
                            yield "⚠️ " + days + " days";
                        } else {
                            yield HealthDashboard.formatDate(cred.expiryDate);
                        }
                    }
                    yield "—";
                }
                case 7 -> cred.modifiedDate != null ? 
                    cred.modifiedDate.replace('T', ' ').substring(0, 16) : "";
                default -> "";
            };
        }
    }
    
    @SuppressWarnings("serial")
    static class FavoriteRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(CENTER);
            
            boolean isFavorite = (Boolean) value;
            if (isFavorite) {
                label.setText("⭐");
                label.setForeground(new Color(255, 193, 7)); // Golden color
            } else {
                label.setText("☆");
                label.setForeground(Color.GRAY);
            }
            
            label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            return label;
        }
    }
    
    @SuppressWarnings("serial")
    static class CategoryRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(CENTER);
            
            String category = (String) value;
            label.setText(category);
            
            // Color code categories with translucent backgrounds
            if (!isSelected) {
                Color bg = switch (category) {
                    case "Social Media" -> new Color(59, 89, 152, 30);
                    case "Banking" -> new Color(40, 167, 69, 30);
                    case "Email" -> new Color(220, 53, 69, 30);
                    case "Work" -> new Color(255, 193, 7, 30);
                    case "Shopping" -> new Color(233, 30, 99, 30);
                    case "Entertainment" -> new Color(156, 39, 176, 30);
                    default -> new Color(108, 117, 125, 30);
                };
                label.setBackground(bg);
                label.setOpaque(true);
            }
            
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            return label;
        }
    }
    
    @SuppressWarnings("serial")
    static class ExpiryRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(CENTER);
            
            String expiry = (String) value;
            label.setText(expiry);
            
            // Color code by expiry status
            if (!isSelected) {
                if (expiry.contains("EXPIRED")) {
                    label.setForeground(new Color(220, 53, 69)); // Red
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                } else if (expiry.contains("⚠️")) {
                    label.setForeground(new Color(255, 193, 7)); // Amber
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                } else if (expiry.equals("—")) {
                    label.setForeground(Color.GRAY);
                } else {
                    label.setForeground(new Color(40, 167, 69)); // Green
                }
            }
            
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            return label;
        }
    }
    
    @SuppressWarnings("serial")
    static class StrengthBarRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            StrengthChecker.Strength strength = (StrengthChecker.Strength) value;
            
            JPanel panel = new JPanel(new GridLayout(1, 5, 3, 0));
            panel.setOpaque(true);
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            
            Color color = switch (strength) {
                case WEAK -> new Color(220, 53, 69);
                case MEDIUM -> new Color(255, 193, 7);
                case STRONG -> new Color(40, 167, 69);
            };
            
            int filledBars = switch (strength) {
                case WEAK -> 2;
                case MEDIUM -> 3;
                case STRONG -> 5;
            };
            
            for (int i = 0; i < 5; i++) {
                JPanel bar = new JPanel();
                bar.setBackground(i < filledBars ? color : new Color(200, 200, 200));
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
            new SecureVaultSwingEnhanced().setVisible(true);
        });
    }
}

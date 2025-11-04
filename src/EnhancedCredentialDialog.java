import javax.swing.*;
import java.awt.*;
import java.util.List;

@SuppressWarnings("serial")
public class EnhancedCredentialDialog extends JDialog {
    private JTextField titleField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea notesArea;
    private JCheckBox favoriteCheck;
    private JComboBox<String> categoryCombo;
    private JTextField websiteField;
    private JTextField expiryField;
    private JLabel attachmentLabel;
    private boolean succeeded = false;
    @SuppressWarnings("unused") // Used in try-catch block for category loading
    private CategoryManager categoryManager;
    @SuppressWarnings("unused") // Used in try-catch block for category loading
    private int userId;

    @SuppressWarnings("this-escape")
    public EnhancedCredentialDialog(Frame parent, Database.Credential credential, CategoryManager categoryManager, int userId) {
        super(parent, credential == null ? "Add Credential" : "Edit Credential", true);
        this.categoryManager = categoryManager;
        this.userId = userId;
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        titleField = new JTextField(20);
        panel.add(titleField, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        usernameField = new JTextField(20);
        panel.add(usernameField, gbc);
        
        // Password with clear labels
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passwordField = new JPasswordField(20);
        
        // Show/Hide button with text
        JButton showBtn = new JButton("👁 Show");
        showBtn.setToolTipText("Show/Hide Password");
        showBtn.setFocusPainted(false);
        showBtn.addActionListener(_ -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar('•');
                showBtn.setText("👁 Show");
            } else {
                passwordField.setEchoChar((char) 0);
                showBtn.setText("👁 Hide");
            }
        });
        
        // Generate button with text
        JButton genBtn = new JButton("🎲 Generate");
        genBtn.setToolTipText("Generate Random Password");
        genBtn.setFocusPainted(false);
        genBtn.addActionListener(_ -> {
            PasswordGeneratorDialog dialog = new PasswordGeneratorDialog((JFrame) getOwner());
            dialog.setVisible(true);
        });
        
        passPanel.add(showBtn, BorderLayout.WEST);
        passPanel.add(passwordField, BorderLayout.CENTER);
        passPanel.add(genBtn, BorderLayout.EAST);
        panel.add(passPanel, gbc);
        
        // Notes
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        panel.add(new JLabel("Notes:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        notesArea = new JTextArea(4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        panel.add(notesScroll, gbc);
        
        // Category
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0; gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        try {
            List<String> categories = categoryManager.getAllCategories(userId);
            categoryCombo = new JComboBox<>(categories.toArray(new String[0]));
            categoryCombo.setEditable(true); // Allow custom category input
        } catch (Exception e) {
            categoryCombo = new JComboBox<>(CategoryManager.DEFAULT_CATEGORIES);
            categoryCombo.setEditable(true);
        }
        panel.add(categoryCombo, gbc);
        
        // Website URL
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(new JLabel("Website:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel urlPanel = new JPanel(new BorderLayout(5, 0));
        websiteField = new JTextField(20);
        websiteField.setToolTipText("Enter website URL (e.g., https://example.com)");
        JButton openUrlBtn = new JButton("🌐 Open");
        openUrlBtn.setToolTipText("Open website in browser");
        openUrlBtn.setFocusPainted(false);
        openUrlBtn.addActionListener(_ -> {
            String url = websiteField.getText().trim();
            if (!url.isEmpty()) {
                try {
                    if (!url.startsWith("http")) url = "https://" + url;
                    Desktop.getDesktop().browse(new java.net.URI(url));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Failed to open URL: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        urlPanel.add(websiteField, BorderLayout.CENTER);
        urlPanel.add(openUrlBtn, BorderLayout.EAST);
        panel.add(urlPanel, gbc);
        
        // Expiry Date
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        panel.add(new JLabel("Expires:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JPanel expiryPanel = new JPanel(new BorderLayout(5, 0));
        expiryField = new JTextField(15);
        expiryField.setToolTipText("Format: YYYY-MM-DD (leave empty for no expiry)");
        JButton set90DaysBtn = new JButton("📅 +90 Days");
        set90DaysBtn.setToolTipText("Set expiry to 90 days from now");
        set90DaysBtn.setFocusPainted(false);
        set90DaysBtn.addActionListener(_ -> {
            String expiryDate = HealthDashboard.calculateExpiryDate(90);
            System.out.println("DEBUG: +90 Days clicked. Setting expiry to: " + expiryDate);
            expiryField.setText(expiryDate);
            System.out.println("DEBUG: Expiry field now contains: " + expiryField.getText());
        });
        expiryPanel.add(expiryField, BorderLayout.CENTER);
        expiryPanel.add(set90DaysBtn, BorderLayout.EAST);
        panel.add(expiryPanel, gbc);
        
        // Attachments (display only)
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        panel.add(new JLabel("Attachments:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        attachmentLabel = new JLabel("(Manage attachments after saving)");
        attachmentLabel.setFont(attachmentLabel.getFont().deriveFont(Font.ITALIC));
        attachmentLabel.setForeground(Color.GRAY);
        panel.add(attachmentLabel, gbc);
        
        // Favorite
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        favoriteCheck = new JCheckBox("⭐ Mark as Favorite");
        panel.add(favoriteCheck, gbc);
        
        // Buttons
        gbc.gridy = 9;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        saveBtn.addActionListener(_ -> {
            if (titleField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (usernameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (new String(passwordField.getPassword()).trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password cannot be empty!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            succeeded = true;
            dispose();
        });
        
        cancelBtn.addActionListener(_ -> {
            succeeded = false;
            dispose();
        });
        
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        panel.add(btnPanel, gbc);
        
        // Set existing values if editing
        if (credential != null) {
            titleField.setText(credential.title);
            usernameField.setText(credential.username);
            passwordField.setText(credential.password);
            notesArea.setText(credential.notes);
            favoriteCheck.setSelected(credential.isFavorite);
            if (credential.category != null && !credential.category.isEmpty()) {
                categoryCombo.setSelectedItem(credential.category);
            }
            websiteField.setText(credential.websiteUrl != null ? credential.websiteUrl : "");
            expiryField.setText(credential.expiryDate != null ? credential.expiryDate : "");
        } else {
            // Set default category for new credentials
            categoryCombo.setSelectedItem("Other");
            // Set default expiry (90 days from now)
            expiryField.setText(HealthDashboard.calculateExpiryDate(90));
        }
        
        add(panel);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    public boolean showDialog() {
        setVisible(true);
        return succeeded;
    }
    
    public String getTitle() {
        return titleField.getText().trim();
    }
    
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }
    
    public String getNotes() {
        return notesArea.getText().trim();
    }
    
    public boolean isFavorite() {
        return favoriteCheck.isSelected();
    }
    
    public String getCategory() {
        Object selected = categoryCombo.getSelectedItem();
        return selected != null ? selected.toString().trim() : "Other";
    }
    
    public String getWebsiteUrl() {
        return websiteField.getText().trim();
    }
    
    public String getExpiryDate() {
        return expiryField.getText().trim();
    }
}

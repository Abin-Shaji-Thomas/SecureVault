import javax.swing.*;
import java.awt.*;

/**
 * Login dialog with username, password, show password toggle, and create user option
 */
@SuppressWarnings("serial")
public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheck;
    private boolean succeeded = false;
    private String username;
    private String password;
    private boolean createNewUser = false;
    
    @SuppressWarnings("this-escape")
    public LoginDialog(Frame parent) {
        super(parent, "SecureVault - Login", true);
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("SecureVault Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        
        // Show password checkbox
        showPasswordCheck = new JCheckBox("Show Password");
        showPasswordCheck.addActionListener(_ -> {
            if (showPasswordCheck.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(showPasswordCheck, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 30));
        loginBtn.addActionListener(_ -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            succeeded = true;
            createNewUser = false;
            dispose();
        });
        buttonPanel.add(loginBtn);
        
        JButton createUserBtn = new JButton("Create User");
        createUserBtn.setPreferredSize(new Dimension(120, 30));
        createUserBtn.addActionListener(_ -> {
            username = usernameField.getText().trim();
            password = new String(passwordField.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters long!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            succeeded = true;
            createNewUser = true;
            dispose();
        });
        buttonPanel.add(createUserBtn);
        
        JButton cancelBtn = new JButton("Exit");
        cancelBtn.setPreferredSize(new Dimension(100, 30));
        cancelBtn.addActionListener(_ -> {
            succeeded = false;
            dispose();
        });
        buttonPanel.add(cancelBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        panel.add(buttonPanel, gbc);
        
        // Enter key support
        passwordField.addActionListener(_ -> loginBtn.doClick());
        
        add(panel);
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean isSucceeded() {
        return succeeded;
    }
    
    public boolean isCreateNewUser() {
        return createNewUser;
    }
}

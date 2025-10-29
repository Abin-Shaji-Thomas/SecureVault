import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
public class CredentialDialog {
    private JFrame parent;
    private Database.Credential current;
    private Database.Credential result = null;
    public CredentialDialog(JFrame parent, Database.Credential current) {
        this.parent = parent;
        this.current = current;
    }
    public Database.Credential showDialog() {
        JTextField titleField = new JTextField(current == null ? "" : current.title);
        JTextField usernameField = new JTextField(current == null ? "" : current.username);
        JPasswordField passwordField = new JPasswordField(current == null ? "" : current.password);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        // strength indicator
        JPanel strengthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel strengthLbl = new JLabel("Strength: ");
        JLabel strengthIcon = new JLabel();
        strengthIcon.setPreferredSize(new Dimension(80, 20));
        strengthRow.add(strengthLbl);
        strengthRow.add(strengthIcon);
        panel.add(strengthRow);
        updateStrengthIcon(strengthIcon, new String(passwordField.getPassword()));

        // Live update as user types
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                SwingUtilities.invokeLater(() -> {
                    String text = new String(passwordField.getPassword());
                    updateStrengthIcon(strengthIcon, text);
                });
            }
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
        //Handle the result and validate
        int res = JOptionPane.showConfirmDialog(parent, panel,
                current == null ? "Add Credential" : "Edit Credential",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            if (title.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All fields required.");
                return null;
            }
            
            // Create credential (id will be assigned by database)
            int id = (current != null) ? current.id : 0;
            result = new Database.Credential(id, title, username, password);
        }

        return result;
    }

    //The strength update helper
    private void updateStrengthIcon(JLabel strengthIcon, String password) {
        StrengthChecker.Strength s = StrengthChecker.checkStrength(password);
        if (s == StrengthChecker.Strength.STRONG) {
            strengthIcon.setIcon(createCircleIcon(new Color(72, 187, 120), new Color(40, 120, 70), 14));
        } else if (s == StrengthChecker.Strength.MEDIUM) {
            strengthIcon.setIcon(createCircleIcon(new Color(255, 203, 72), new Color(200, 140, 20), 14));
        } else {
            strengthIcon.setIcon(createCircleIcon(new Color(229, 83, 83), new Color(160, 40, 40), 14));
        }
    }

    //Drawing the colored circle (custom icon)
    private static ImageIcon createCircleIcon(Color fill, Color border, int size) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(fill);
        g.fillOval(2, 2, size - 4, size - 4);
        g.setStroke(new BasicStroke(2f));
        g.setColor(border);
        g.drawOval(2, 2, size - 4, size - 4);
        g.dispose();
        return new ImageIcon(img);
    }
}

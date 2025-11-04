import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;

@SuppressWarnings("serial")
public class PasswordGeneratorDialog extends JDialog {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{};:,.<>?";
    
    public PasswordGeneratorDialog(JFrame parent) {
        super(parent, "Password Generator", false);
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        JSpinner length = new JSpinner(new SpinnerNumberModel(12, 6, 48, 1));
        JCheckBox up = new JCheckBox("A-Z", true);
        JCheckBox lo = new JCheckBox("a-z", true);
        JCheckBox dg = new JCheckBox("0-9", true);
        JCheckBox sy = new JCheckBox("Symbols", false);
        
        panel.add(new JLabel("Length:"));
        panel.add(length);
        panel.add(up);
        panel.add(lo);
        panel.add(dg);
        panel.add(sy);

        int res = JOptionPane.showConfirmDialog(parent, panel, "Password Generator",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (res == JOptionPane.OK_OPTION) {
            String pwd = generatePassword((int) length.getValue(),
                    up.isSelected(), lo.isSelected(), dg.isSelected(), sy.isSelected());

            // Auto-copy to clipboard
            copyToClipboard(pwd);
            // Show generated password
            showGeneratedPassword(parent, pwd);
        }
    }

    private String generatePassword(int length, boolean includeUpper, boolean includeLower, 
                                    boolean includeDigits, boolean includeSymbols) {
        StringBuilder pool = new StringBuilder();
        if (includeUpper) pool.append(UPPERCASE);
        if (includeLower) pool.append(LOWERCASE);
        if (includeDigits) pool.append(DIGITS);
        if (includeSymbols) pool.append(SYMBOLS);
        
        // Default to alphanumeric if nothing selected
        if (pool.length() == 0) {
            pool.append(UPPERCASE).append(LOWERCASE).append(DIGITS);
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(pool.charAt(random.nextInt(pool.length())));
        }
        return password.toString();
    }
    
    private void copyToClipboard(String text) {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(text), null);
        } catch (Exception ignored) {}
    }

    private void showGeneratedPassword(JFrame parent, String pwd) {
        JDialog d = new JDialog(parent, "Generated Password", true);
        d.setLayout(new BorderLayout(10, 10));

        JTextField field = new JTextField(pwd);
        field.setEditable(false);
        field.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Password (copied to clipboard):", SwingConstants.CENTER), BorderLayout.NORTH);
        top.add(field, BorderLayout.CENTER);

        JButton copyAgain = new JButton("Copy Again");
        JButton checkStrength = new JButton("Check Strength");
        JButton close = new JButton("Close");

        copyAgain.addActionListener(_ -> copyToClipboard(pwd));
        
        checkStrength.addActionListener(_ -> {
            d.dispose();
            new StrengthCheckerDialog(parent, pwd).setVisible(true);
        });
        
        close.addActionListener(_ -> d.dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(copyAgain);
        buttons.add(checkStrength);
        buttons.add(close);

        d.add(top, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);
        d.pack();
        d.setSize(Math.max(420, d.getWidth()), 160);
        d.setLocationRelativeTo(parent);
        d.setVisible(true);
    }
}


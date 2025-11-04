import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Strength Checker Dialog with animated meter
 */
public class StrengthCheckerDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("this-escape")
    public StrengthCheckerDialog(JFrame parent, String prefill) {
        super(parent, "Password Strength Checker", false);
        
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout(4, 4));
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel prompt = new JLabel("Enter password to check:");
        JPasswordField pf = new JPasswordField(prefill == null ? "" : prefill, 24);
        char defaultEcho = pf.getEchoChar();
        JCheckBox show = new JCheckBox("Show");
        
        show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (show.isSelected()) {
                    pf.setEchoChar((char) 0);
                } else {
                    pf.setEchoChar(defaultEcho);
                }
            }
        });
        
        inputRow.add(prompt);
        inputRow.add(pf);
        inputRow.add(show);
        top.add(inputRow, BorderLayout.NORTH);

        // Meter (5 segments)
        JPanel meter = new JPanel(new GridLayout(1, 5, 4, 4));
        for (int i = 0; i < 5; i++) {
            JPanel seg = new JPanel();
            seg.setBackground(new Color(220, 220, 220));
            seg.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            meter.add(seg);
        }
        top.add(meter, BorderLayout.CENTER);

        // Status and suggestions
        JLabel status = new JLabel(" ");
        JTextArea suggestions = new JTextArea(4, 36);
        suggestions.setEditable(false);
        suggestions.setLineWrap(true);
        suggestions.setWrapStyleWord(true);
        suggestions.setBackground(panel.getBackground());

        panel.add(top, BorderLayout.NORTH);
        panel.add(status, BorderLayout.CENTER);
        panel.add(new JScrollPane(suggestions), BorderLayout.SOUTH);

        // Animation state
        final int[] displayed = {0};
        final int[] target = {0};

        Timer anim = new Timer(50, null);
        anim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayed[0] < target[0]) displayed[0]++;
                else if (displayed[0] > target[0]) displayed[0]--;
                
                // Update segments
                for (int i = 0; i < 5; i++) {
                    JPanel seg = (JPanel) meter.getComponent(i);
                    if (i < displayed[0]) {
                        Color col;
                        if (target[0] >= 4) col = new Color(72, 187, 120);
                        else if (target[0] >= 2) col = new Color(255, 203, 72);
                        else col = new Color(229, 83, 83);
                        seg.setBackground(col);
                    } else {
                        seg.setBackground(new Color(230, 230, 230));
                    }
                }
                if (displayed[0] == target[0]) anim.stop();
            }
        });

        // Helper to update display
        Runnable updater = () -> {
            String pwd = new String(pf.getPassword());
            StrengthChecker.Strength s = StrengthChecker.checkStrength(pwd);
            int score = StrengthChecker.computeScore(pwd);
            int filled = (int) Math.round((score / 6.0) * 5.0);
            target[0] = filled;
            if (!anim.isRunning()) anim.start();
            status.setText("Strength: " + s.name());
            
            String[] suggestionArray = StrengthChecker.getSuggestions(pwd);
            StringBuilder sug = new StringBuilder();
            for (String suggestion : suggestionArray) {
                sug.append(suggestion).append("\n");
            }
            suggestions.setText(sug.toString());
        };

        pf.getDocument().addDocumentListener(new DocumentListener() {
            private void docUpdate() {
                SwingUtilities.invokeLater(updater);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                docUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                docUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                docUpdate();
            }
        });

        // Initialize
        updater.run();
        
        JOptionPane.showConfirmDialog(parent, panel, "Password Strength Checker",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        dispose();
    }
}

import javax.swing.*;
import java.awt.*;

/**
 * Health Dashboard Dialog with visual security report
 */
@SuppressWarnings("serial")
public class HealthDashboardDialog extends JDialog {
    
    @SuppressWarnings("this-escape")
    public HealthDashboardDialog(Frame parent, HealthDashboard.Stats stats, int needsAttentionCount) {
        super(parent, "🔐 Security Health Dashboard", true);
        setSize(600, 750);
        setLocationRelativeTo(parent);
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Header with score
        JPanel headerPanel = createHeaderPanel(stats);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Statistics panels - Use BoxLayout for better control
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(Color.WHITE);
        
        JPanel strengthPanel = createPasswordStrengthPanel(stats);
        strengthPanel.setMaximumSize(new Dimension(600, 180));
        statsPanel.add(strengthPanel);
        statsPanel.add(Box.createVerticalStrut(15));
        
        JPanel issuesPanel = createIssuesPanel(stats);
        issuesPanel.setMaximumSize(new Dimension(600, 160));
        statsPanel.add(issuesPanel);
        statsPanel.add(Box.createVerticalStrut(15));
        
        JPanel overviewPanel = createOverviewPanel(stats, needsAttentionCount);
        overviewPanel.setMaximumSize(new Dimension(600, 100));
        statsPanel.add(overviewPanel);
        
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        
        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton closeBtn = new JButton("Close");
        closeBtn.setPreferredSize(new Dimension(120, 35));
        closeBtn.addActionListener(_ -> dispose());
        buttonPanel.add(closeBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        System.out.println("Health Dashboard Created - Stats: " + stats.toString());
    }
    
    private JPanel createHeaderPanel(HealthDashboard.Stats stats) {
        Color scoreColor = getScoreColor(stats.securityScore);
        
        JPanel panel = new JPanel(new BorderLayout(10, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw gradient background
                Color lightColor = new Color(
                    Math.min(255, scoreColor.getRed() + 30),
                    Math.min(255, scoreColor.getGreen() + 30),
                    Math.min(255, scoreColor.getBlue() + 30)
                );
                GradientPaint gradient = new GradientPaint(
                    0, 0, lightColor,
                    0, getHeight(), scoreColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Add subtle shine effect at top
                g2d.setPaint(new GradientPaint(
                    0, 0, new Color(255, 255, 255, 40),
                    0, getHeight() / 3, new Color(255, 255, 255, 0)
                ));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight() / 3, 12, 12);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(scoreColor.darker(), 0),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        
        JLabel titleLabel = new JLabel("Overall Security Score", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titleLabel.setForeground(new Color(255, 255, 255, 240));
        
        JLabel scoreLabel = new JLabel(String.format("%.1f/100", stats.securityScore), SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        scoreLabel.setForeground(Color.WHITE);
        
        String rating = getScoreRating(stats.securityScore);
        JLabel ratingLabel = new JLabel(rating, SwingConstants.CENTER);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        ratingLabel.setForeground(new Color(255, 255, 255, 250));
        
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(ratingLabel);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPasswordStrengthPanel(HealthDashboard.Stats stats) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        
        JLabel titleLabel = new JLabel("📈 Password Strength Distribution");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JPanel barsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        barsPanel.setBackground(Color.WHITE);
        barsPanel.add(createBar("Strong", stats.strong, stats.total, new Color(40, 167, 69)));
        barsPanel.add(createBar("Medium", stats.medium, stats.total, new Color(255, 193, 7)));
        barsPanel.add(createBar("Weak", stats.weak, stats.total, new Color(220, 53, 69)));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(barsPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createIssuesPanel(HealthDashboard.Stats stats) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        
        JLabel titleLabel = new JLabel("⚠️  Security Issues");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JPanel issuesPanel = new JPanel(new GridLayout(4, 2, 15, 10));
        issuesPanel.setBackground(Color.WHITE);
        
        issuesPanel.add(createIssueLabel("🔄 Reused:", stats.reused > 0));
        issuesPanel.add(createIssueValue(stats.reused));
        issuesPanel.add(createIssueLabel("⛔ Expired:", stats.expired > 0));
        issuesPanel.add(createIssueValue(stats.expired));
        issuesPanel.add(createIssueLabel("⏰ Expiring Soon:", stats.expiringSoon > 0));
        issuesPanel.add(createIssueValue(stats.expiringSoon));
        issuesPanel.add(createIssueLabel("📅 No Expiry:", stats.noExpiry > 0));
        issuesPanel.add(createIssueValue(stats.noExpiry));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(issuesPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createOverviewPanel(HealthDashboard.Stats stats, int needsAttentionCount) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        
        JLabel titleLabel = new JLabel("💡 Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(50, 50, 50));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel totalLabel = new JLabel("📊 Total Credentials: " + stats.total);
        totalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalLabel.setForeground(new Color(80, 80, 80));
        
        JLabel attentionLabel = new JLabel();
        if (needsAttentionCount > 0) {
            attentionLabel.setText("⚠️  " + needsAttentionCount + " credential" + (needsAttentionCount > 1 ? "s" : "") + " need immediate attention");
            attentionLabel.setForeground(new Color(220, 53, 69));
        } else {
            attentionLabel.setText("✅ All credentials are in excellent shape!");
            attentionLabel.setForeground(new Color(40, 167, 69));
        }
        attentionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        infoPanel.add(totalLabel);
        infoPanel.add(attentionLabel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createBar(String label, int value, int total, Color color) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setPreferredSize(new Dimension(75, 30));
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComp.setForeground(new Color(70, 70, 70));
        
        int percentage = total > 0 ? (value * 100) / total : 0;
        
        // Create a custom progress bar panel
        JPanel barContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw background with gradient
                GradientPaint bgGradient = new GradientPaint(0, 0, new Color(248, 248, 248), 0, height, new Color(240, 240, 240));
                g2d.setPaint(bgGradient);
                g2d.fillRoundRect(0, 0, width, height, 8, 8);
                
                // Draw filled portion with gradient
                if (percentage > 0) {
                    int filledWidth = (width * percentage) / 100;
                    Color lightColor = new Color(
                        Math.min(255, color.getRed() + 20),
                        Math.min(255, color.getGreen() + 20),
                        Math.min(255, color.getBlue() + 20)
                    );
                    GradientPaint fillGradient = new GradientPaint(0, 0, lightColor, 0, height, color);
                    g2d.setPaint(fillGradient);
                    g2d.fillRoundRect(0, 0, filledWidth, height, 8, 8);
                    
                    // Add subtle shine effect
                    g2d.setColor(new Color(255, 255, 255, 40));
                    g2d.fillRoundRect(0, 0, filledWidth, height / 2, 8, 8);
                }
                
                // Draw border
                g2d.setColor(new Color(210, 210, 210));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, width - 1, height - 1, 8, 8);
                
                // Draw text with shadow
                String text = value + " (" + percentage + "%)";
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int textX = (width - textWidth) / 2;
                int textY = (height + textHeight) / 2 - 2;
                
                // Choose text color based on background
                if (percentage > 40) {
                    // Draw shadow
                    g2d.setColor(new Color(0, 0, 0, 80));
                    g2d.drawString(text, textX + 1, textY + 1);
                    // Draw text
                    g2d.setColor(Color.WHITE);
                } else {
                    g2d.setColor(new Color(60, 60, 60));
                }
                
                g2d.drawString(text, textX, textY);
            }
        };
        barContainer.setPreferredSize(new Dimension(350, 30));
        barContainer.setBackground(Color.WHITE);
        
        panel.add(labelComp, BorderLayout.WEST);
        panel.add(barContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createIssueLabel(String text, boolean hasIssue) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
    
    private JLabel createIssueValue(int value) {
        JLabel label = new JLabel(String.valueOf(value));
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        if (value > 0) {
            label.setForeground(new Color(220, 53, 69));
        } else {
            label.setForeground(new Color(40, 167, 69));
        }
        return label;
    }
    
    private Color getScoreColor(double score) {
        if (score >= 80) return new Color(40, 167, 69);      // Green
        else if (score >= 60) return new Color(255, 193, 7);  // Amber
        else if (score >= 40) return new Color(255, 152, 0);  // Orange
        else return new Color(220, 53, 69);                   // Red
    }
    
    private String getScoreRating(double score) {
        if (score >= 80) return "✅ EXCELLENT";
        else if (score >= 60) return "🟢 GOOD";
        else if (score >= 40) return "🟡 FAIR";
        else return "🔴 POOR";
    }
}

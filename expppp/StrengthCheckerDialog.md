# StrengthCheckerDialog.java - Line-by-Line Explanation

This file contains an animated password strength checker dialog with a visual meter, real-time strength evaluation, and actionable suggestions. Features a 5-segment animated bar that fills based on password score, with color-coded feedback.

---

## **Import Statements**

```java
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
```

- **`import javax.swing.*;`** - Swing GUI components (JDialog, JPanel, JLabel, JPasswordField, JCheckBox, JTextArea, etc.)
- **`import javax.swing.event.DocumentEvent;`** - Event fired when text document changes
- **`import javax.swing.event.DocumentListener;`** - Listener for document changes (detects typing)
- **`import java.awt.*;`** - AWT classes (BorderLayout, FlowLayout, GridLayout, Color, etc.)
- **`import java.awt.event.ActionEvent;`** - Event for button/checkbox actions
- **`import java.awt.event.ActionListener;`** - Listener interface for actions

---

## **Class Declaration**

```java
/**
 * Strength Checker Dialog with animated meter
 */
public class StrengthCheckerDialog extends JDialog {
```

- **JavaDoc comment** - Describes the dialog's purpose
- **`public class StrengthCheckerDialog extends JDialog`** - Extends JDialog (modal/non-modal dialog window)

---

## **Constructor**

```java
    public StrengthCheckerDialog(JFrame parent, String prefill) {
        super(parent, "Password Strength Checker", false);
```

- **`public StrengthCheckerDialog(JFrame parent, String prefill)`** - Constructor
- **Parameters:**
  - `parent` - Parent frame for positioning
  - `prefill` - Optional password to pre-fill (can be null for empty field)
- **`super(parent, "Password Strength Checker", false);`** - Calls JDialog constructor:
  - `parent` - Parent frame
  - `"Password Strength Checker"` - Dialog title
  - `false` - Non-modal (doesn't block parent window)

**Usage:**
- Empty: `new StrengthCheckerDialog(frame, null)` - User enters password
- Pre-filled: `new StrengthCheckerDialog(frame, "MyPass123")` - Analyze specific password

---

## **Main Panel Structure**

```java
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        JPanel top = new JPanel(new BorderLayout(4, 4));
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
```

- **`JPanel panel = new JPanel(new BorderLayout(8, 8));`** - Main panel with 8-pixel gaps
- **`JPanel top = new JPanel(new BorderLayout(4, 4));`** - Top section (input + meter) with 4-pixel gaps
- **`JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT));`** - Horizontal row for password input

---

## **Password Input Field**

```java
        JLabel prompt = new JLabel("Enter password to check:");
        JPasswordField pf = new JPasswordField(prefill == null ? "" : prefill, 24);
        char defaultEcho = pf.getEchoChar();
```

- **`JLabel prompt = new JLabel("Enter password to check:");`** - Prompt label
- **`JPasswordField pf = new JPasswordField(prefill == null ? "" : prefill, 24);`** - Password field:
  - `prefill == null ? "" : prefill` - Use prefill if provided, otherwise empty
  - `24` - Width of 24 columns
- **`char defaultEcho = pf.getEchoChar();`** - Save the default echo character (usually '•')
  - **Why save?** So we can restore it when "Show" checkbox is unchecked

### **Show Password Checkbox**

```java
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
```

- **`JCheckBox show = new JCheckBox("Show");`** - Checkbox to toggle password visibility
- **`show.addActionListener(new ActionListener() { ... })`** - Traditional ActionListener syntax (not lambda)
  - **Why traditional?** Demonstrates alternative syntax; both lambda and traditional work
- **`if (show.isSelected())`** - If checkbox is checked:
  - **`pf.setEchoChar((char) 0);`** - Set echo char to null → shows actual characters
- **`else`** - If checkbox is unchecked:
  - **`pf.setEchoChar(defaultEcho);`** - Restore bullet/dot echo character

### **Assemble Input Row**

```java
        inputRow.add(prompt);
        inputRow.add(pf);
        inputRow.add(show);
        top.add(inputRow, BorderLayout.NORTH);
```

- **`inputRow.add(...)`** - Add prompt, password field, and checkbox horizontally
- **`top.add(inputRow, BorderLayout.NORTH);`** - Place input row at top of top panel

---

## **Strength Meter (5 Segments)**

```java
        // Meter (5 segments)
        JPanel meter = new JPanel(new GridLayout(1, 5, 4, 4));
        for (int i = 0; i < 5; i++) {
            JPanel seg = new JPanel();
            seg.setBackground(new Color(220, 220, 220));
            seg.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            meter.add(seg);
        }
        top.add(meter, BorderLayout.CENTER);
```

- **`JPanel meter = new JPanel(new GridLayout(1, 5, 4, 4));`** - Create meter with GridLayout:
  - `1` - 1 row
  - `5` - 5 columns (5 segments)
  - `4, 4` - 4-pixel gaps between segments
- **`for (int i = 0; i < 5; i++)`** - Create 5 segment panels
- **`JPanel seg = new JPanel();`** - Each segment is an empty panel (visual rectangle)
- **`seg.setBackground(new Color(220, 220, 220));`** - Light gray background (unfilled state)
- **`seg.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));`** - Gray border
- **`meter.add(seg);`** - Add segment to meter
- **`top.add(meter, BorderLayout.CENTER);`** - Place meter below input row

**Visual:**
```
Enter password to check: [••••••••] [✓] Show
[███][   ][   ][   ][   ]  ← 5 segments (1 filled, 4 empty in this example)
```

---

## **Status Label and Suggestions Area**

```java
        // Status and suggestions
        JLabel status = new JLabel(" ");
        JTextArea suggestions = new JTextArea(4, 36);
        suggestions.setEditable(false);
        suggestions.setLineWrap(true);
        suggestions.setWrapStyleWord(true);
        suggestions.setBackground(panel.getBackground());
```

- **`JLabel status = new JLabel(" ");`** - Label to show strength level (e.g., "Strength: STRONG")
- **`JTextArea suggestions = new JTextArea(4, 36);`** - Multi-line text area for suggestions:
  - `4` - 4 rows tall
  - `36` - 36 columns wide
- **`suggestions.setEditable(false);`** - Read-only (display only)
- **`suggestions.setLineWrap(true);`** - Wrap long lines
- **`suggestions.setWrapStyleWord(true);`** - Wrap at word boundaries (not mid-word)
- **`suggestions.setBackground(panel.getBackground());`** - Match background color (seamless look)

### **Add to Main Panel**

```java
        panel.add(top, BorderLayout.NORTH);
        panel.add(status, BorderLayout.CENTER);
        panel.add(new JScrollPane(suggestions), BorderLayout.SOUTH);
```

- **`panel.add(top, BorderLayout.NORTH);`** - Put input and meter at top
- **`panel.add(status, BorderLayout.CENTER);`** - Put status label in center
- **`panel.add(new JScrollPane(suggestions), BorderLayout.SOUTH);`** - Put suggestions at bottom with scrollbar

---

## **Animation State Variables**

```java
        // Animation state
        final int[] displayed = {0};
        final int[] target = {0};
```

- **`final int[] displayed = {0};`** - Current number of filled segments (0-5) displayed in the meter
- **`final int[] target = {0};`** - Target number of filled segments (0-5) based on password score
- **Why arrays?** Java lambdas can only access final/effectively-final variables; using array allows mutation

**Animation Concept:**
- `displayed` gradually increments/decrements toward `target`
- Creates smooth filling/emptying animation
- Example: Password goes from weak (1 segment) to strong (4 segments) → `displayed` animates from 1→2→3→4

---

## **Animation Timer**

```java
        Timer anim = new Timer(50, null);
        anim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayed[0] < target[0]) displayed[0]++;
                else if (displayed[0] > target[0]) displayed[0]--;
```

- **`Timer anim = new Timer(50, null);`** - Create timer that fires every 50 milliseconds
- **`anim.addActionListener(new ActionListener() { ... })`** - Add listener for timer events
- **`if (displayed[0] < target[0]) displayed[0]++;`** - If displayed is less than target, increment (fill one more segment)
- **`else if (displayed[0] > target[0]) displayed[0]--;`** - If displayed is greater than target, decrement (empty one segment)

**Animation Speed:**
- 50ms per frame = 20 frames per second
- Filling from 0 to 5 segments takes 5 frames × 50ms = 250ms (quarter second)

### **Update Segment Colors**

```java
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
```

- **`for (int i = 0; i < 5; i++)`** - Loop through all 5 segments
- **`JPanel seg = (JPanel) meter.getComponent(i);`** - Get the i-th segment panel
- **`if (i < displayed[0])`** - If this segment index is less than displayed count, it should be filled
- **Choose color based on target strength:**
  - **`if (target[0] >= 4)`** - Strong password (4-5 segments filled)
    - Color: `rgb(72, 187, 120)` - Green
  - **`else if (target[0] >= 2)`** - Medium password (2-3 segments filled)
    - Color: `rgb(255, 203, 72)` - Yellow/orange
  - **`else`** - Weak password (0-1 segments filled)
    - Color: `rgb(229, 83, 83)` - Red
- **`seg.setBackground(col);`** - Apply color to segment
- **`else`** - If segment is not filled:
  - **`seg.setBackground(new Color(230, 230, 230));`** - Light gray (empty)

**Visual Example (3 segments filled, medium password):**
```
[███][███][███][   ][   ]  ← Yellow/orange filled segments
```

### **Stop Timer When Complete**

```java
                if (displayed[0] == target[0]) anim.stop();
            }
        });
```

- **`if (displayed[0] == target[0]) anim.stop();`** - Stop timer when animation is complete
- **Why stop?** No need to keep firing events when nothing changes (saves CPU)

---

## **Update Helper (Strength Calculation)**

```java
        // Helper to update display
        Runnable updater = () -> {
            String pwd = new String(pf.getPassword());
            StrengthChecker.Strength s = StrengthChecker.checkStrength(pwd);
            int score = StrengthChecker.computeScore(pwd);
```

- **`Runnable updater = () -> { ... };`** - Lambda that recalculates and updates display
- **`String pwd = new String(pf.getPassword());`** - Get current password from field
- **`StrengthChecker.Strength s = StrengthChecker.checkStrength(pwd);`** - Get strength enum (WEAK/MEDIUM/STRONG)
- **`int score = StrengthChecker.computeScore(pwd);`** - Get numerical score (0-6)

### **Convert Score to Segment Count**

```java
            int filled = (int) Math.round((score / 6.0) * 5.0);
            target[0] = filled;
            if (!anim.isRunning()) anim.start();
```

- **`int filled = (int) Math.round((score / 6.0) * 5.0);`** - Convert score (0-6) to filled segments (0-5)
  - **Formula:** `filled = round((score / 6) × 5)`
  - **Examples:**
    - Score 0 → filled = round((0/6) × 5) = 0 segments
    - Score 1 → filled = round((1/6) × 5) = 1 segment (red)
    - Score 3 → filled = round((3/6) × 5) = 3 segments (yellow)
    - Score 6 → filled = round((6/6) × 5) = 5 segments (green)
- **`target[0] = filled;`** - Set target (animation will move toward this)
- **`if (!anim.isRunning()) anim.start();`** - Start animation if not already running

### **Update Status Text**

```java
            status.setText("Strength: " + s.name());
```

- **`status.setText("Strength: " + s.name());`** - Update status label
- **Example:** "Strength: STRONG" or "Strength: WEAK"

### **Update Suggestions**

```java
            String[] suggestionArray = StrengthChecker.getSuggestions(pwd);
            StringBuilder sug = new StringBuilder();
            for (String suggestion : suggestionArray) {
                sug.append(suggestion).append("\n");
            }
            suggestions.setText(sug.toString());
        };
```

- **`String[] suggestionArray = StrengthChecker.getSuggestions(pwd);`** - Get array of suggestions
- **`StringBuilder sug = new StringBuilder();`** - Build multi-line suggestion text
- **`for (String suggestion : suggestionArray)`** - Loop through each suggestion
- **`sug.append(suggestion).append("\n");`** - Add suggestion with newline
- **`suggestions.setText(sug.toString());`** - Display all suggestions in text area

**Example Output:**
```
❌ Use at least 8 characters
❌ Add uppercase letters (A-Z)
✓ Good length!
```

---

## **Document Listener (Live Updates)**

```java
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
```

- **`pf.getDocument().addDocumentListener(...)`** - Attach listener to password field's document
- **`private void docUpdate()`** - Helper that schedules updater on EDT
- **`SwingUtilities.invokeLater(updater);`** - Run updater on Event Dispatch Thread
- **All three methods call `docUpdate()`** - Any text change triggers strength recalculation

**User Experience:**
- User types 'P' → Meter shows 1 red segment, "Strength: WEAK", suggestions
- User types 'a' → Recalculate, update meter
- User types 's', 's', 'w', 'o', 'r', 'd' → Meter at 2-3 yellow segments
- User types '1', '2', '3', '!' → Meter fills to 5 green segments, "Strength: STRONG"

---

## **Initialize Display**

```java
        // Initialize
        updater.run();
```

- **`updater.run();`** - Run updater once to initialize display with prefill value
- **Why?** If dialog was opened with a pre-filled password, we need to show its strength immediately

---

## **Show Dialog**

```java
        JOptionPane.showConfirmDialog(parent, panel, "Password Strength Checker",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        dispose();
    }
}
```

- **`JOptionPane.showConfirmDialog(...)`** - Display the panel in a modal dialog
  - `parent` - Parent frame
  - `panel` - The main panel with all components
  - `"Password Strength Checker"` - Title
  - `JOptionPane.OK_CANCEL_OPTION` - Show OK and Cancel buttons
  - `JOptionPane.PLAIN_MESSAGE` - No icon
- **Blocks here until user clicks OK or Cancel**
- **`dispose();`** - Clean up dialog resources after it closes

---

## **How It's Used in the Application**

### **From Main Application:**
```java
JButton checkBtn = new JButton("Check Strength");
checkBtn.addActionListener(_ -> 
    new StrengthCheckerDialog(this, null).setVisible(true)
);
```

### **From Password Generator:**
```java
JButton checkStrength = new JButton("Check Strength");
checkStrength.addActionListener(_ -> {
    d.dispose();  // Close generator dialog
    new StrengthCheckerDialog(parent, generatedPassword).setVisible(true);
});
```

---

## **Dialog Appearance**

```
┌───────────────────────────────────────────┐
│      Password Strength Checker            │
├───────────────────────────────────────────┤
│ Enter password to check: [••••••••] [✓] Show
│                                           │
│ [███][███][███][███][   ]  ← Animated meter
│                                           │
│ Strength: STRONG                          │
│                                           │
│ ✓ Good length!                            │
│ ✓ Has uppercase letters                   │
│ ✓ Has lowercase letters                   │
│ ✓ Has numbers                             │
│ ❌ Add special characters (!@#$%^&* etc.) │
│                                           │
│               [ OK ] [ Cancel ]           │
└───────────────────────────────────────────┘
```

---

## **Animation Flow Example**

**User types "Pass123!":**

1. **"P"** (score 1/6):
   - `target[0] = 1`
   - Timer: `displayed[0]` animates 0→1
   - Visual: [🔴][   ][   ][   ][   ]
   - Status: "Strength: WEAK"

2. **"Pa"** (score 1/6):
   - `target[0] = 1`
   - `displayed[0]` already 1, no animation
   - Visual: [🔴][   ][   ][   ][   ]

3. **"Password"** (score 2/6):
   - `target[0] = 2`
   - Timer: `displayed[0]` animates 1→2
   - Visual: [🟡][🟡][   ][   ][   ]
   - Status: "Strength: MEDIUM"

4. **"Password1"** (score 3/6):
   - `target[0] = 3`
   - Timer: `displayed[0]` animates 2→3
   - Visual: [🟡][🟡][🟡][   ][   ]

5. **"Password123"** (score 4/6):
   - `target[0] = 3` (rounds down)
   - Already at 3, no change
   - Visual: [🟡][🟡][🟡][   ][   ]

6. **"Password123!"** (score 5/6):
   - `target[0] = 4`
   - Timer: `displayed[0]` animates 3→4
   - Color changes to green
   - Visual: [🟢][🟢][🟢][🟢][   ]
   - Status: "Strength: STRONG"

---

## **Key Features**

1. **Animated Meter:** Smooth filling/emptying animation (50ms per frame)
2. **5-Segment Display:** Visual representation of password strength (0-5 filled)
3. **Color-Coded:** Red (weak), yellow (medium), green (strong)
4. **Live Updates:** Recalculates with every keystroke
5. **Show/Hide Password:** Toggle visibility with checkbox
6. **Detailed Feedback:** Shows strength level and specific suggestions
7. **Pre-fill Support:** Can analyze a given password or let user type
8. **Smooth UX:** Animation provides pleasant, responsive feedback
9. **Score Mapping:** 6-point score → 5 segments using `round((score/6) × 5)`
10. **Auto-Stop:** Timer stops when animation completes (efficient)

---

## **Technical Highlights**

- **Timer-Based Animation:** Uses `javax.swing.Timer` for smooth visual effects
- **Array Trick:** Uses `int[]` to allow mutation inside lambdas
- **DocumentListener:** Detects every text change for instant feedback
- **EDT Safety:** Uses `SwingUtilities.invokeLater()` for thread-safe UI updates
- **Component Iteration:** Dynamically updates 5 segment panels in loop
- **Color Algorithm:** Determines color based on target, not current display (consistent during animation)

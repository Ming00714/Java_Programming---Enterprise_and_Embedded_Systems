import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class TypingTutor extends JFrame {
    private final JTextArea textArea;
    private final Map<String, JButton> keyButtons = new HashMap<>();
    private final Map<String, Color> originalColors = new HashMap<>();

    public TypingTutor() {
        super("Typing Application");
        setLayout(new BorderLayout());
        setFocusTraversalKeysEnabled(false);

        // 上方說明
        JPanel top = new JPanel(new GridLayout(2,1));
        top.add(new JLabel("Type some text using your keyboard. The keys you press will be highlighted and the text will be displayed."));
        top.add(new JLabel("Note: Clicking the buttons with your mouse will not perform any action."));
        add(top, BorderLayout.NORTH);

        // 輸入區
        textArea = new JTextArea(5, 50);
        textArea.setEditable(true); // 保證游標會閃爍
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 鍵盤主面板
        JPanel keyboard = new JPanel();
        keyboard.setLayout(new BoxLayout(keyboard, BoxLayout.Y_AXIS));
        addRow(keyboard, new String[]{"`","1","2","3","4","5","6","7","8","9","0","-","=","Backspace"});
        addRow(keyboard, new String[]{"Tab","Q","W","E","R","T","Y","U","I","O","P","[","]","\\"});
        addRow(keyboard, new String[]{"Caps","A","S","D","F","G","H","J","K","L",";", "'", "Enter"});

        // 第4+5排 合併
        JPanel combined = new JPanel(new BorderLayout());

        JPanel leftCombined = new JPanel();
        leftCombined.setLayout(new BoxLayout(leftCombined, BoxLayout.Y_AXIS));

        JPanel row4 = new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        for (String k: new String[]{"Shift","Z","X","C","V","B","N","M",",",".","/"}) {
            row4.add(createButton(k));
        }
        leftCombined.add(row4);

        JPanel row5 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        for (int i = 0; i < 4; i++) {
            row5.add(Box.createHorizontalStrut(65)); // 空四格
        }
        row5.add(createButton("Space"));
        leftCombined.add(row5);

        combined.add(leftCombined, BorderLayout.WEST);

        // 方向鍵群組
        JPanel arrows = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        gbc.gridx = 1; gbc.gridy = 0;
        arrows.add(createButton("↑"), gbc);
        gbc.gridy = 1;
        gbc.gridx = 0; arrows.add(createButton("←"), gbc);
        gbc.gridx = 1; arrows.add(createButton("↓"), gbc);
        gbc.gridx = 2; arrows.add(createButton("→"), gbc);

        combined.add(arrows, BorderLayout.EAST);
        keyboard.add(combined);

        add(keyboard, BorderLayout.SOUTH);

        // 全局 KeyEvent Dispatcher（讓 textArea 能打字，同時能亮鍵）
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() != KeyEvent.KEY_PRESSED) return false;

            String keyText = getKeyText(e);
            JButton b = keyButtons.get(keyText);
            if (b != null) {
                originalColors.putIfAbsent(keyText, b.getBackground());
                b.setBackground(Color.YELLOW);

                Timer t = new Timer(150, ev -> b.setBackground(originalColors.get(keyText)));
                t.setRepeats(false);
                t.start();
            }

            // 方向鍵控制 JTextArea 游標
            int pos = textArea.getCaretPosition();
            try {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (pos > 0) textArea.setCaretPosition(pos - 1);
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (pos < textArea.getText().length()) textArea.setCaretPosition(pos + 1);
                    }
                    case KeyEvent.VK_UP -> {
                        int line = textArea.getLineOfOffset(pos);
                        if (line > 0) {
                            int x = (int) textArea.modelToView2D(pos).getX();
                            int upStart = textArea.getLineStartOffset(line - 1);
                            int upEnd = textArea.getLineEndOffset(line - 1);
                            for (int i = upStart; i < upEnd; i++) {
                                Rectangle r = textArea.modelToView2D(i).getBounds();
                                if (r != null && r.x >= x) {
                                    textArea.setCaretPosition(i);
                                    break;
                                }
                            }
                        }
                    }
                    case KeyEvent.VK_DOWN -> {
                        int line = textArea.getLineOfOffset(pos);
                        if (line < textArea.getLineCount() - 1) {
                            int x = (int) textArea.modelToView2D(pos).getX();
                            int downStart = textArea.getLineStartOffset(line + 1);
                            int downEnd = textArea.getLineEndOffset(line + 1);
                            for (int i = downStart; i < downEnd; i++) {
                                Rectangle r = textArea.modelToView2D(i).getBounds();
                                if (r != null && r.x >= x) {
                                    textArea.setCaretPosition(i);
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignored) {}

            return false; // 讓系統繼續處理 key event（包括輸入字元）
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        textArea.requestFocusInWindow(); // 確保游標會閃爍
    }

    private void addRow(JPanel p, String[] ks){
        JPanel r = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        for (String k : ks) r.add(createButton(k));
        p.add(r);
    }

    private JButton createButton(String key){
        JButton b = new JButton(key);
        int w = switch (key) {
            case "Space" -> 360;
            case "Backspace", "Enter" -> 120;
            case "Tab", "Caps" -> 90;
            case "Shift" -> 135;
            case "↑", "↓", "←", "→" -> 50;
            default -> 60;
        };
        b.setPreferredSize(new Dimension(w, 40));
        b.setEnabled(false);
        keyButtons.put(key, b);
        return b;
    }

    private String getKeyText(KeyEvent e){
        return switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE -> "Backspace";
            case KeyEvent.VK_TAB -> "Tab";
            case KeyEvent.VK_CAPS_LOCK -> "Caps";
            case KeyEvent.VK_ENTER -> "Enter";
            case KeyEvent.VK_SHIFT -> "Shift";
            case KeyEvent.VK_SPACE -> "Space";
            case KeyEvent.VK_UP -> "↑";
            case KeyEvent.VK_DOWN -> "↓";
            case KeyEvent.VK_LEFT -> "←";
            case KeyEvent.VK_RIGHT -> "→";
            case KeyEvent.VK_BACK_QUOTE -> "`";
            case KeyEvent.VK_MINUS -> "-";
            case KeyEvent.VK_EQUALS -> "=";
            case KeyEvent.VK_OPEN_BRACKET -> "[";
            case KeyEvent.VK_CLOSE_BRACKET -> "]";
            case KeyEvent.VK_BACK_SLASH -> "\\";
            case KeyEvent.VK_SEMICOLON -> ";";
            case KeyEvent.VK_QUOTE -> "'";
            case KeyEvent.VK_COMMA -> ",";
            case KeyEvent.VK_PERIOD -> ".";
            case KeyEvent.VK_SLASH -> "/";
            case KeyEvent.VK_1 -> "1"; 
            case KeyEvent.VK_2 -> "2"; 
            case KeyEvent.VK_3 -> "3";
            case KeyEvent.VK_4 -> "4"; 
            case KeyEvent.VK_5 -> "5"; 
            case KeyEvent.VK_6 -> "6";
            case KeyEvent.VK_7 -> "7"; 
            case KeyEvent.VK_8 -> "8"; 
            case KeyEvent.VK_9 -> "9";
            case KeyEvent.VK_0 -> "0";
            default -> KeyEvent.getKeyText(e.getKeyCode());
        };
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(TypingTutor::new);
    }
}

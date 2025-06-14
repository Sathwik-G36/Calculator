import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.*;

public class ColorfulCalculator extends JFrame {
    private JTextField display;
    private StringBuilder input = new StringBuilder();
    private double num1 = 0, num2 = 0;
    private char operator = ' ';
    private boolean darkMode = false;
    private JPanel panel;
    private Color lightBackground = new Color(255, 240, 230);
    private Color darkBackground = new Color(40, 40, 40);

    public ColorfulCalculator() {
        setTitle("Colorful Calculator");
        setSize(420, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        panel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = darkMode
                    ? new GradientPaint(0, 0, darkBackground, 0, getHeight(), Color.BLACK)
                    : new GradientPaint(0, 0, lightBackground, 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBounds(0, 0, 420, 650);
        add(panel);

        display = new JTextField();
        display.setBounds(30, 30, 340, 60);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
        panel.add(display);

        JButton toggleTheme = new JButton("☀️/🌙");
        toggleTheme.setBounds(330, 100, 50, 30);
        toggleTheme.setFont(new Font("Arial", Font.BOLD, 12));
        toggleTheme.setFocusPainted(false);
        toggleTheme.addActionListener(e -> {
            darkMode = !darkMode;
            repaint();
        });
        panel.add(toggleTheme);

        String[] buttons = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        int x = 30, y = 150;
        for (int i = 0; i < buttons.length; i++) {
            String label = buttons[i];
            JButton btn = createButton(label);
            btn.setBounds(x, y, 80, 60);
            panel.add(btn);

            x += 90;
            if ((i + 1) % 4 == 0) {
                x = 30;
                y += 70;
            }
        }

        getContentPane().setBackground(Color.WHITE);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Rounded + hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 180, 255));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        button.addActionListener(e -> {
            animateButton(button);
            playClickSound();
            handleInput(text);
        });

        return button;
    }

    private void animateButton(JButton button) {
        Color original = button.getBackground();
        button.setBackground(new Color(144, 238, 144));
        Timer timer = new Timer(150, e -> button.setBackground(original));
        timer.setRepeats(false);
        timer.start();
    }

    private void handleInput(String text) {
        switch (text) {
            case "C":
                input.setLength(0);
                display.setText("");
                operator = ' ';
                break;
            case "=":
                try {
                    num2 = Double.parseDouble(input.toString());
                    double result = calculate(num1, num2, operator);
                    display.setText(String.valueOf(result));
                    input.setLength(0);
                    operator = ' ';
                    num1 = result;
                } catch (Exception ex) {
                    display.setText("Error");
                }
                break;
            case "+": case "-": case "*": case "/":
                if (input.length() > 0) {
                    num1 = Double.parseDouble(input.toString());
                    operator = text.charAt(0);
                    input.setLength(0);
                    display.setText(display.getText() + " " + operator + " ");
                }
                break;
            default:
                input.append(text);
                display.setText(display.getText() + text);
        }
    }

    private double calculate(double a, double b, char op) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> b != 0 ? a / b : 0;
            default -> 0;
        };
    }

    private void playClickSound() {
        try {
            InputStream soundStream = new BufferedInputStream(getClass().getResourceAsStream("/click.wav"));
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("Sound not found or error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ColorfulCalculator::new);
    }
}

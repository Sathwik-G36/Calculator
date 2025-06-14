import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calc extends JFrame {
    private JTextField display;
    private StringBuilder input = new StringBuilder();
    private double num1 = 0, num2 = 0;
    private char operator = ' ';
    private JPanel animatedPanel;
    private float hue = 0f;

    public Calc() {
        setTitle("Moving RGB Calculator");
        setSize(420, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Animated panel for background
        animatedPanel = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.getHSBColor(hue, 0.6f, 1.0f));
            }
        };
        animatedPanel.setBounds(0, 0, 420, 650);
        add(animatedPanel);

        // Animate the hue
        Timer bgTimer = new Timer(40, e -> {
            hue += 0.002;
            if (hue > 1f) hue = 0f;
            animatedPanel.repaint();
        });
        bgTimer.start();

        // Display
        display = new JTextField();
        display.setBounds(30, 30, 340, 60);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        animatedPanel.add(display);

        // Buttons
        String[] btnLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        int x = 30, y = 120;
        for (int i = 0; i < btnLabels.length; i++) {
            JButton btn = createButton(btnLabels[i]);
            btn.setBounds(x, y, 80, 60);
            animatedPanel.add(btn);

            x += 90;
            if ((i + 1) % 4 == 0) {
                x = 30;
                y += 70;
            }
        }

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> handleInput(text));

        return btn;
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
                } catch (Exception e) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calc::new);
    }
}

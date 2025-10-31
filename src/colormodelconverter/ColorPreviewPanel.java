package colormodelconverter;

import javax.swing.*;
import java.awt.*;

public class ColorPreviewPanel extends JPanel {
    private Color currentColor;

    public ColorPreviewPanel() {
        setPreferredSize(new Dimension(200, 150)); // Увеличили высоту
        setCurrentColor(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
        setBackground(color);
        repaint();
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        String hex = String.format("#%02X%02X%02X",
                currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue());

        Color textColor = (currentColor.getRed() * 0.299 + currentColor.getGreen() * 0.587 + currentColor.getBlue() * 0.114) > 186 ?
                Color.BLACK : Color.WHITE;

        g.setColor(textColor);
        g.setFont(new Font("Arial", Font.BOLD, 16)); // Увеличили шрифт
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(hex)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(hex, x, y);
    }
}
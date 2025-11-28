package Color;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Color Converter — RGB / CMYK / HSV");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        RGBPane rgbPane = new RGBPane();
        CMYKPane cmykPane = new CMYKPane();
        HSVPane hsvPane = new HSVPane();

        JPanel previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(200, 150));
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JTextField hexField = new JTextField("#FF0000");
        hexField.setHorizontalAlignment(JTextField.CENTER);
        hexField.setEditable(false);

        JLabel infoLabel = new JLabel("Присутствует погрешность при приведении цвета к разным моделям");
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setForeground(Color.RED);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightPanel.add(infoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(new JLabel("Preview:"));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(previewPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(new JLabel("HEX:"));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(hexField);

        JPanel colorModelsPanel = new JPanel();
        colorModelsPanel.setLayout(new BoxLayout(colorModelsPanel, BoxLayout.Y_AXIS));
        colorModelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        colorModelsPanel.add(rgbPane.getPane());
        colorModelsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        colorModelsPanel.add(cmykPane.getPane());
        colorModelsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        colorModelsPanel.add(hsvPane.getPane());

        new ColorController(rgbPane, cmykPane, hsvPane, previewPanel, hexField);

        frame.add(colorModelsPanel, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);

        frame.pack();
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
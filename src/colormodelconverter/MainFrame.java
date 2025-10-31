package colormodelconverter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private ColorPreviewPanel previewPanel;
    private InfoPanel infoPanel;
    private ColorPanel cmykPanel, rgbPanel, hsvPanel;
    private ColorController controller;

    public MainFrame() {
        setTitle("Конвертер цветовых моделей - CMYK/RGB/HSV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        setupLayout();
        setupController();

        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initializeComponents() {
        cmykPanel = new ColorPanel("CMYK Модель",
                new String[]{"Cyan", "Magenta", "Yellow", "Black"},
                new int[]{0, 0, 0, 0},
                new int[]{100, 100, 100, 100},
                new int[]{0, 0, 0, 0});

        rgbPanel = new ColorPanel("RGB Модель",
                new String[]{"Red", "Green", "Blue"},
                new int[]{0, 0, 0},
                new int[]{255, 255, 255},
                new int[]{0, 0, 0});

        hsvPanel = new ColorPanel("HSV Модель",
                new String[]{"Hue", "Saturation", "Value"},
                new int[]{0, 0, 0},
                new int[]{360, 100, 100},
                new int[]{0, 0, 0});

        previewPanel = new ColorPreviewPanel();
        infoPanel = new InfoPanel();
    }

    private void setupLayout() {

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        leftPanel.add(cmykPanel);
        leftPanel.add(rgbPanel);
        leftPanel.add(hsvPanel);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(infoPanel, BorderLayout.CENTER);

        JPanel previewContainer = new JPanel(new BorderLayout());
        previewContainer.setBorder(BorderFactory.createTitledBorder("Предпросмотр цвета"));
        previewContainer.add(previewPanel, BorderLayout.CENTER);
        rightPanel.add(previewContainer, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel, BorderLayout.CENTER);

        JButton colorChooserButton = new JButton("Выбрать цвет из палитры");
        colorChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color initialColor = previewPanel.getCurrentColor();
                Color chosenColor = JColorChooser.showDialog(
                        MainFrame.this, "Выберите цвет", initialColor);
                if (chosenColor != null) {
                    controller.updateFromColor(chosenColor);
                }
            }
        });

        add(colorChooserButton, BorderLayout.SOUTH);
    }

    private void setupController() {
        controller = new ColorController(previewPanel, infoPanel, cmykPanel, rgbPanel, hsvPanel);
    }
}
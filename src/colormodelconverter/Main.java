package colormodelconverter;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        setupLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void setupLookAndFeel() {
        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            UIManager.put("Slider.paintValue", true);
            UIManager.put("Spinner.arrowButtonSize", new java.awt.Dimension(16, 16));

        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Неподдерживаемый Look and Feel: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Класс Look and Feel не найден: " + e.getMessage());
        } catch (InstantiationException e) {
            System.err.println("Ошибка создания экземпляра Look and Feel: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Ошибка доступа к Look and Feel: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Общая ошибка при настройке Look and Feel: " + e.getMessage());
        }
    }

    private static void createAndShowGUI() {
        try {
            MainFrame mainFrame = new MainFrame();

            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLocationByPlatform(true);

            mainFrame.setVisible(true);

            System.out.println("Приложение " + ColorModelConverter.getVersionInfo() + " успешно запущено");
            System.out.println("Поддерживаемые цветовые модели: CMYK, RGB, HSV");

        } catch (Exception e) {
            System.err.println("Ошибка при создании графического интерфейса: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    "Ошибка при запуске приложения: " + e.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
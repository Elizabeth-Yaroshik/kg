package colormodelconverter;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JTextArea infoArea;

    public InfoPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Информация о цвете"));

        infoArea = new JTextArea(10, 20);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(infoArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateInfo(String cmykInfo, String rgbInfo, String hsvInfo, String additionalInfo) {
        StringBuilder info = new StringBuilder();
        info.append("CMYK Values:\n").append(cmykInfo).append("\n");
        info.append("RGB Values:\n").append(rgbInfo).append("\n");
        info.append("HSV Values:\n").append(hsvInfo).append("\n");
        info.append("Color Information:\n").append(additionalInfo);

        infoArea.setText(info.toString());
    }
}
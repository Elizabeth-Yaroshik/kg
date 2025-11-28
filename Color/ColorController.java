package Color;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorController {
    private RGBPane rgb;
    private CMYKPane cmyk;
    private HSVPane hsv;
    private JPanel previewPanel;
    private JTextField hexField;
    private boolean updating = false;

    public ColorController(RGBPane rgb, CMYKPane cmyk, HSVPane hsv, JPanel previewPanel, JTextField hexField) {
        this.rgb = rgb;
        this.cmyk = cmyk;
        this.hsv = hsv;
        this.previewPanel = previewPanel;
        this.hexField = hexField;

        setupListeners();
        updateFromRGB();
    }

    private void setupListeners() {
        rgb.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!updating) updateFromRGB();
            }
        });

        rgb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    try {
                        int r = Integer.parseInt(rgb.rField.getText());
                        int g = Integer.parseInt(rgb.gField.getText());
                        int b = Integer.parseInt(rgb.bField.getText());
                        rgb.setR(Math.max(0, Math.min(255, r)));
                        rgb.setG(Math.max(0, Math.min(255, g)));
                        rgb.setB(Math.max(0, Math.min(255, b)));
                        updateFromRGB();
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        });

        cmyk.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!updating) updateFromCMYK();
            }
        });

        cmyk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    try {
                        double c = Double.parseDouble(cmyk.cField.getText()) / 100.0;
                        double m = Double.parseDouble(cmyk.mField.getText()) / 100.0;
                        double y = Double.parseDouble(cmyk.yField.getText()) / 100.0;
                        double k = Double.parseDouble(cmyk.kField.getText()) / 100.0;
                        cmyk.setC(Math.max(0, Math.min(1, c)));
                        cmyk.setM(Math.max(0, Math.min(1, m)));
                        cmyk.setY(Math.max(0, Math.min(1, y)));
                        cmyk.setK(Math.max(0, Math.min(1, k)));
                        updateFromCMYK();
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        });

        hsv.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!updating) updateFromHSV();
            }
        });

        hsv.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    try {
                        double h = Double.parseDouble(hsv.hField.getText());
                        double s = Double.parseDouble(hsv.sField.getText()) / 100.0;
                        double v = Double.parseDouble(hsv.vField.getText()) / 100.0;
                        hsv.setH(Math.max(0, Math.min(360, h)));
                        hsv.setS(Math.max(0, Math.min(1, s)));
                        hsv.setV(Math.max(0, Math.min(1, v)));
                        updateFromHSV();
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        });
    }

    private void updateFromRGB() {
        if (updating) return;
        updating = true;

        int r = rgb.getR();
        int g = rgb.getG();
        int b = rgb.getB();

        System.out.println("Updating from RGB: " + r + ", " + g + ", " + b);

        double[] cmykVals = ColorConverter.rgbToCmyk(r, g, b);
        double[] hsvVals = ColorConverter.rgbToHsv(r, g, b);

        cmyk.setC(cmykVals[0]);
        cmyk.setM(cmykVals[1]);
        cmyk.setY(cmykVals[2]);
        cmyk.setK(cmykVals[3]);

        hsv.setH(hsvVals[0]);
        hsv.setS(hsvVals[1]);
        hsv.setV(hsvVals[2]);

        updatePreview(r, g, b);
        updating = false;
    }

    private void updateFromCMYK() {
        if (updating) return;
        updating = true;

        double c = cmyk.getC();
        double m = cmyk.getM();
        double y = cmyk.getY();
        double k = cmyk.getK();

        System.out.println("Updating from CMYK: " + c + ", " + m + ", " + y + ", " + k);

        int[] rgbVals = ColorConverter.cmykToRgb(c, m, y, k);

        rgb.setR(rgbVals[0]);
        rgb.setG(rgbVals[1]);
        rgb.setB(rgbVals[2]);

        double[] hsvVals = ColorConverter.rgbToHsv(rgbVals[0], rgbVals[1], rgbVals[2]);
        hsv.setH(hsvVals[0]);
        hsv.setS(hsvVals[1]);
        hsv.setV(hsvVals[2]);

        updatePreview(rgbVals[0], rgbVals[1], rgbVals[2]);
        updating = false;
    }

    private void updateFromHSV() {
        if (updating) return;
        updating = true;

        double h = hsv.getH();
        double s = hsv.getS();
        double v = hsv.getV();

        System.out.println("Updating from HSV: " + h + ", " + s + ", " + v);

        int[] rgbVals = ColorConverter.hsvToRgb(h, s, v);

        rgb.setR(rgbVals[0]);
        rgb.setG(rgbVals[1]);
        rgb.setB(rgbVals[2]);

        double[] cmykVals = ColorConverter.rgbToCmyk(rgbVals[0], rgbVals[1], rgbVals[2]);
        cmyk.setC(cmykVals[0]);
        cmyk.setM(cmykVals[1]);
        cmyk.setY(cmykVals[2]);
        cmyk.setK(cmykVals[3]);

        updatePreview(rgbVals[0], rgbVals[1], rgbVals[2]);
        updating = false;
    }

    private void updatePreview(int r, int g, int b) {
        Color color = new Color(r, g, b);
        previewPanel.setBackground(color);
        hexField.setText(String.format("#%02X%02X%02X", r, g, b));

        System.out.println("Preview updated: " + r + ", " + g + ", " + b);
    }
}
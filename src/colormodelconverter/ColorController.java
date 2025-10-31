package colormodelconverter;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;

public class ColorController {
    private ColorPreviewPanel previewPanel;
    private InfoPanel infoPanel;
    private ColorPanel cmykPanel, rgbPanel, hsvPanel;
    private boolean updating = false;

    public ColorController(ColorPreviewPanel previewPanel, InfoPanel infoPanel,
                           ColorPanel cmykPanel, ColorPanel rgbPanel, ColorPanel hsvPanel) {
        this.previewPanel = previewPanel;
        this.infoPanel = infoPanel;
        this.cmykPanel = cmykPanel;
        this.rgbPanel = rgbPanel;
        this.hsvPanel = hsvPanel;

        setupListeners();
        updateFromRGB();
    }

    private void setupListeners() {

        for (int i = 0; i < cmykPanel.getComponentCount(); i++) {
            final int index = i;

            cmykPanel.addSliderChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        cmykPanel.getSpinner(index).setValue(cmykPanel.getValue(index));
                        updateFromCMYK();
                        updating = false;
                    }
                }
            });

            cmykPanel.addSpinnerChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        int value = (Integer) cmykPanel.getSpinner(index).getValue();
                        cmykPanel.getSlider(index).setValue(value);
                        updateFromCMYK();
                        updating = false;
                    }
                }
            });
        }

        for (int i = 0; i < rgbPanel.getComponentCount(); i++) {
            final int index = i;

            rgbPanel.addSliderChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        rgbPanel.getSpinner(index).setValue(rgbPanel.getValue(index));
                        updateFromRGB();
                        updating = false;
                    }
                }
            });

            rgbPanel.addSpinnerChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        int value = (Integer) rgbPanel.getSpinner(index).getValue();
                        rgbPanel.getSlider(index).setValue(value);
                        updateFromRGB();
                        updating = false;
                    }
                }
            });
        }

        for (int i = 0; i < hsvPanel.getComponentCount(); i++) {
            final int index = i;

            hsvPanel.addSliderChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        hsvPanel.getSpinner(index).setValue(hsvPanel.getValue(index));
                        updateFromHSV();
                        updating = false;
                    }
                }
            });

            hsvPanel.addSpinnerChangeListener(i, new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (!updating) {
                        updating = true;
                        int value = (Integer) hsvPanel.getSpinner(index).getValue();
                        hsvPanel.getSlider(index).setValue(value);
                        updateFromHSV();
                        updating = false;
                    }
                }
            });
        }
    }

    private void updateFromCMYK() {
        CMYKColor cmyk = new CMYKColor(
                cmykPanel.getValue(0) / 100.0,
                cmykPanel.getValue(1) / 100.0,
                cmykPanel.getValue(2) / 100.0,
                cmykPanel.getValue(3) / 100.0
        );

        RGBColor rgb = ConversionUtils.cmykToRgb(cmyk);

        updating = true;
        rgbPanel.setValue(0, rgb.getRed());
        rgbPanel.setValue(1, rgb.getGreen());
        rgbPanel.setValue(2, rgb.getBlue());
        updating = false;

        updateFromRGB();
    }

    private void updateFromRGB() {
        RGBColor rgb = new RGBColor(
                rgbPanel.getValue(0),
                rgbPanel.getValue(1),
                rgbPanel.getValue(2)
        );

        CMYKColor cmyk = ConversionUtils.rgbToCmyk(rgb);
        HSVColor hsv = ConversionUtils.rgbToHsv(rgb);

        updating = true;

        cmykPanel.setValue(0, cmyk.getCyanPercent());
        cmykPanel.setValue(1, cmyk.getMagentaPercent());
        cmykPanel.setValue(2, cmyk.getYellowPercent());
        cmykPanel.setValue(3, cmyk.getBlackPercent());

        hsvPanel.setValue(0, hsv.getHueDegrees());
        hsvPanel.setValue(1, hsv.getSaturationPercent());
        hsvPanel.setValue(2, hsv.getValuePercent());

        previewPanel.setCurrentColor(rgb.toAWTColor());
        updating = false;

        updateInfo();
    }

    private void updateFromHSV() {
        HSVColor hsv = new HSVColor(
                hsvPanel.getValue(0),
                hsvPanel.getValue(1) / 100.0f,
                hsvPanel.getValue(2) / 100.0f
        );

        RGBColor rgb = ConversionUtils.hsvToRgb(hsv);

        updating = true;
        rgbPanel.setValue(0, rgb.getRed());
        rgbPanel.setValue(1, rgb.getGreen());
        rgbPanel.setValue(2, rgb.getBlue());
        updating = false;

        updateFromRGB();
    }

    public void updateFromColor(Color color) {
        updating = true;
        rgbPanel.setValue(0, color.getRed());
        rgbPanel.setValue(1, color.getGreen());
        rgbPanel.setValue(2, color.getBlue());
        updating = false;

        updateFromRGB();
    }

    private void updateInfo() {
        RGBColor rgb = new RGBColor(rgbPanel.getValue(0), rgbPanel.getValue(1), rgbPanel.getValue(2));
        CMYKColor cmyk = ConversionUtils.rgbToCmyk(rgb);
        HSVColor hsv = ConversionUtils.rgbToHsv(rgb);

        String cmykInfo = String.format("  Cyan: %d%%\n  Magenta: %d%%\n  Yellow: %d%%\n  Black: %d%%",
                cmyk.getCyanPercent(), cmyk.getMagentaPercent(), cmyk.getYellowPercent(), cmyk.getBlackPercent());

        String rgbInfo = String.format("  Red: %d\n  Green: %d\n  Blue: %d\n  Hex: #%02X%02X%02X",
                rgb.getRed(), rgb.getGreen(), rgb.getBlue(), rgb.getRed(), rgb.getGreen(), rgb.getBlue());

        String hsvInfo = String.format("  Hue: %d°\n  Saturation: %d%%\n  Value: %d%%",
                hsv.getHueDegrees(), hsv.getSaturationPercent(), hsv.getValuePercent());

        Color color = rgb.toAWTColor();
        String additionalInfo = String.format("  Brightness: %.2f\n  Luminance: %.2f",
                (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255,
                (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue()) / 255);

        infoPanel.updateInfo(cmykInfo, rgbInfo, hsvInfo, additionalInfo);
    }
}
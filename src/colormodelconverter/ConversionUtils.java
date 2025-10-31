package colormodelconverter;

import java.awt.Color;

public class ConversionUtils {

    public static CMYKColor rgbToCmyk(RGBColor rgb) {
        double r = rgb.getRed() / 255.0;
        double g = rgb.getGreen() / 255.0;
        double b = rgb.getBlue() / 255.0;

        double k = 1 - Math.max(r, Math.max(g, b));

        if (k == 1.0) {
            return new CMYKColor(0, 0, 0, 1);
        }

        double c = (1 - r - k) / (1 - k);
        double m = (1 - g - k) / (1 - k);
        double y = (1 - b - k) / (1 - k);

        return new CMYKColor(c, m, y, k);
    }

    public static RGBColor cmykToRgb(CMYKColor cmyk) {
        double c = cmyk.getCyan();
        double m = cmyk.getMagenta();
        double y = cmyk.getYellow();
        double k = cmyk.getBlack();

        int r = (int) Math.round(255 * (1 - c) * (1 - k));
        int g = (int) Math.round(255 * (1 - m) * (1 - k));
        int b = (int) Math.round(255 * (1 - y) * (1 - k));

        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        return new RGBColor(r, g, b);
    }

    public static HSVColor rgbToHsv(RGBColor rgb) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), hsv);
        return new HSVColor(hsv[0] * 360, hsv[1], hsv[2]);
    }

    public static RGBColor hsvToRgb(HSVColor hsv) {
        Color color = Color.getHSBColor(hsv.getHue() / 360, hsv.getSaturation(), hsv.getValue());
        return new RGBColor(color.getRed(), color.getGreen(), color.getBlue());
    }
}
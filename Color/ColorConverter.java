package Color;

public class ColorConverter {
    public static double[] rgbToCmyk(int r, int g, int b) {
        double rd = r / 255.0, gd = g / 255.0, bd = b / 255.0;
        double k = 1 - Math.max(rd, Math.max(gd, bd));
        if (k == 1.0) return new double[]{0, 0, 0, 1};
        double c = (1 - rd - k) / (1 - k);
        double m = (1 - gd - k) / (1 - k);
        double y = (1 - bd - k) / (1 - k);
        return new double[]{c, m, y, k};
    }

    public static int[] cmykToRgb(double c, double m, double y, double k) {
        int r = (int) Math.round(255 * (1 - c) * (1 - k));
        int g = (int) Math.round(255 * (1 - m) * (1 - k));
        int b = (int) Math.round(255 * (1 - y) * (1 - k));
        return new int[]{r, g, b};
    }

    public static double[] rgbToHsv(int r, int g, int b) {
        double rd = r / 255.0, gd = g / 255.0, bd = b / 255.0;
        double max = Math.max(rd, Math.max(gd, bd));
        double min = Math.min(rd, Math.min(gd, bd));
        double delta = max - min;
        double h = 0;

        if (delta != 0) {
            if (max == rd) {
                h = 60 * (((gd - bd) / delta) % 6);
            } else if (max == gd) {
                h = 60 * (((bd - rd) / delta) + 2);
            } else {
                h = 60 * (((rd - gd) / delta) + 4);
            }
        }

        if (h < 0) h += 360;

        double s = max == 0 ? 0 : delta / max;
        double v = max;

        h = Math.max(0, Math.min(360, h));
        s = Math.max(0, Math.min(1, s));
        v = Math.max(0, Math.min(1, v));

        return new double[]{h, s, v};
    }

    public static int[] hsvToRgb(double h, double s, double v) {
        h = Math.max(0, Math.min(360, h));
        s = Math.max(0, Math.min(1, s));
        v = Math.max(0, Math.min(1, v));

        double c = v * s;
        double x = c * (1 - Math.abs((h / 60.0) % 2 - 1));
        double m = v - c;
        double r = 0, g = 0, b = 0;

        if (0 <= h && h < 60) { r = c; g = x; b = 0; }
        else if (60 <= h && h < 120) { r = x; g = c; b = 0; }
        else if (120 <= h && h < 180) { r = 0; g = c; b = x; }
        else if (180 <= h && h < 240) { r = 0; g = x; b = c; }
        else if (240 <= h && h < 300) { r = x; g = 0; b = c; }
        else if (300 <= h && h < 360) { r = c; g = 0; b = x; }
        else { r = 0; g = 0; b = 0; }

        int red = (int) Math.round((r + m) * 255);
        int green = (int) Math.round((g + m) * 255);
        int blue = (int) Math.round((b + m) * 255);

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new int[]{red, green, blue};
    }
}
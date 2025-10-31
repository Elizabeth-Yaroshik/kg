package colormodelconverter;

public class ColorModelConverter {

    public static String getVersionInfo() {
        return "ColorModelConverter v1.0 - Конвертер цветовых моделей";
    }

    public static String[] getSupportedModels() {
        return new String[]{"CMYK", "RGB", "HSV"};
    }

    public static boolean validateColorValues(String model, double[] values) {
        if (values == null) return false;

        switch (model.toUpperCase()) {
            case "CMYK":
                if (values.length != 4) return false;
                for (double value : values) {
                    if (value < 0 || value > 100) return false;
                }
                return true;

            case "RGB":
                if (values.length != 3) return false;
                for (double value : values) {
                    if (value < 0 || value > 255) return false;
                }
                return true;

            case "HSV":
                if (values.length != 3) return false;
                return values[0] >= 0 && values[0] <= 360 &&
                        values[1] >= 0 && values[1] <= 100 &&
                        values[2] >= 0 && values[2] <= 100;

            default:
                return false;
        }
    }
}
package colormodelconverter;
public class HSVColor {
    private float hue;
    private float saturation;
    private float value;

    public HSVColor(float hue, float saturation, float value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    public float getHue() { return hue; }
    public void setHue(float hue) { this.hue = hue; }

    public float getSaturation() { return saturation; }
    public void setSaturation(float saturation) { this.saturation = saturation; }

    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }

    public int getHueDegrees() { return Math.round(hue); }
    public int getSaturationPercent() { return Math.round(saturation * 100); }
    public int getValuePercent() { return Math.round(value * 100); }
}
package colormodelconverter;
public class CMYKColor {
    private double cyan;
    private double magenta;
    private double yellow;
    private double black;

    public CMYKColor(double cyan, double magenta, double yellow, double black) {
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.black = black;
    }

    public double getCyan() { return cyan; }
    public void setCyan(double cyan) { this.cyan = cyan; }

    public double getMagenta() { return magenta; }
    public void setMagenta(double magenta) { this.magenta = magenta; }

    public double getYellow() { return yellow; }
    public void setYellow(double yellow) { this.yellow = yellow; }

    public double getBlack() { return black; }
    public void setBlack(double black) { this.black = black; }

    public int getCyanPercent() { return (int) Math.round(cyan * 100); }
    public int getMagentaPercent() { return (int) Math.round(magenta * 100); }
    public int getYellowPercent() { return (int) Math.round(yellow * 100); }
    public int getBlackPercent() { return (int) Math.round(black * 100); }
}
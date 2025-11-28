package Color;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CMYKPane {
    private JPanel pane = new JPanel(new GridLayout(5, 3, 5, 5));

    public JSlider c = new JSlider(0, 100, 0);
    public JSlider m = new JSlider(0, 100, 100);
    public JSlider y = new JSlider(0, 100, 100);
    public JSlider k = new JSlider(0, 100, 0);

    public JTextField cField = new JTextField("0.0");
    public JTextField mField = new JTextField("100.0");
    public JTextField yField = new JTextField("100.0");
    public JTextField kField = new JTextField("0.0");

    public CMYKPane() {
        pane.setBorder(BorderFactory.createTitledBorder("CMYK"));

        setupSlider(c);
        setupSlider(m);
        setupSlider(y);
        setupSlider(k);

        setupTextField(cField);
        setupTextField(mField);
        setupTextField(yField);
        setupTextField(kField);

        pane.add(new JLabel("C"));
        pane.add(c);
        pane.add(cField);

        pane.add(new JLabel("M"));
        pane.add(m);
        pane.add(mField);

        pane.add(new JLabel("Y"));
        pane.add(y);
        pane.add(yField);

        pane.add(new JLabel("K"));
        pane.add(k);
        pane.add(kField);
    }

    private void setupSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(200, 50));
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    private void setupTextField(JTextField field) {
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setColumns(5);
    }

    public void addChangeListener(ChangeListener listener) {
        c.addChangeListener(listener);
        m.addChangeListener(listener);
        y.addChangeListener(listener);
        k.addChangeListener(listener);
    }

    public void addActionListener(ActionListener listener) {
        cField.addActionListener(listener);
        mField.addActionListener(listener);
        yField.addActionListener(listener);
        kField.addActionListener(listener);
    }

    public JPanel getPane() {
        return pane;
    }

    public double getC() { return c.getValue() / 100.0; }
    public double getM() { return m.getValue() / 100.0; }
    public double getY() { return y.getValue() / 100.0; }
    public double getK() { return k.getValue() / 100.0; }

    public void setC(double value) {
        int intValue = (int)(value * 100);
        c.setValue(intValue);
        cField.setText(String.format("%.1f", value * 100));
    }

    public void setM(double value) {
        int intValue = (int)(value * 100);
        m.setValue(intValue);
        mField.setText(String.format("%.1f", value * 100));
    }

    public void setY(double value) {
        int intValue = (int)(value * 100);
        y.setValue(intValue);
        yField.setText(String.format("%.1f", value * 100));
    }

    public void setK(double value) {
        int intValue = (int)(value * 100);
        k.setValue(intValue);
        kField.setText(String.format("%.1f", value * 100));
    }
}
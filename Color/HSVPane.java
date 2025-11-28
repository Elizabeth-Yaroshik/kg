package Color;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HSVPane {
    private JPanel pane = new JPanel(new GridLayout(4, 3, 5, 5));

    public JSlider h = new JSlider(0, 360, 0);
    public JSlider s = new JSlider(0, 100, 100);
    public JSlider v = new JSlider(0, 100, 100);

    public JTextField hField = new JTextField("0.0");
    public JTextField sField = new JTextField("100.0");
    public JTextField vField = new JTextField("100.0");

    public HSVPane() {
        pane.setBorder(BorderFactory.createTitledBorder("HSV"));

        setupSlider(h, 60, 360);
        setupSlider(s, 20, 100);
        setupSlider(v, 20, 100);

        setupTextField(hField);
        setupTextField(sField);
        setupTextField(vField);

        pane.add(new JLabel("H"));
        pane.add(h);
        pane.add(hField);

        pane.add(new JLabel("S"));
        pane.add(s);
        pane.add(sField);

        pane.add(new JLabel("V"));
        pane.add(v);
        pane.add(vField);

        addFieldListeners();
    }

    private void setupSlider(JSlider slider, int majorTick, int max) {
        slider.setPreferredSize(new Dimension(200, 50));
        slider.setMajorTickSpacing(majorTick);
        slider.setMinorTickSpacing(majorTick / 2);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    private void setupTextField(JTextField field) {
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setColumns(5);
    }

    private void addFieldListeners() {
        hField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(hField.getText());
                    if (value >= h.getMinimum() && value <= h.getMaximum()) {
                        h.setValue((int)value);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        });

        sField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(sField.getText());
                    if (value >= s.getMinimum() && value <= s.getMaximum()) {
                        s.setValue((int)value);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        });

        vField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double value = Double.parseDouble(vField.getText());
                    if (value >= v.getMinimum() && value <= v.getMaximum()) {
                        v.setValue((int)value);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        });
    }

    public void addChangeListener(ChangeListener listener) {
        h.addChangeListener(listener);
        s.addChangeListener(listener);
        v.addChangeListener(listener);
    }

    public void addActionListener(ActionListener listener) {
        hField.addActionListener(listener);
        sField.addActionListener(listener);
        vField.addActionListener(listener);
    }

    public JPanel getPane() {
        return pane;
    }

    public double getH() {
        return h.getValue();
    }

    public double getS() {
        return s.getValue() / 100.0;
    }

    public double getV() {
        return v.getValue() / 100.0;
    }

    public void setH(double value) {
        int intValue = (int)value;
        h.setValue(intValue);
        hField.setText(String.format("%.1f", value));
    }

    public void setS(double value) {
        int intValue = (int)(value * 100);
        s.setValue(intValue);
        sField.setText(String.format("%.1f", value * 100));
    }

    public void setV(double value) {
        int intValue = (int)(value * 100);
        v.setValue(intValue);
        vField.setText(String.format("%.1f", value * 100));
    }
}
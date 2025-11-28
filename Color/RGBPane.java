package Color;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RGBPane {
    private JPanel pane = new JPanel(new GridLayout(4, 3, 5, 5));

    public JSlider r = new JSlider(0, 255, 255);
    public JSlider g = new JSlider(0, 255, 0);
    public JSlider b = new JSlider(0, 255, 0);

    public JTextField rField = new JTextField("255");
    public JTextField gField = new JTextField("0");
    public JTextField bField = new JTextField("0");

    public RGBPane() {
        pane.setBorder(BorderFactory.createTitledBorder("RGB"));

        setupSlider(r);
        setupSlider(g);
        setupSlider(b);

        setupTextField(rField);
        setupTextField(gField);
        setupTextField(bField);

        pane.add(new JLabel("R"));
        pane.add(r);
        pane.add(rField);

        pane.add(new JLabel("G"));
        pane.add(g);
        pane.add(gField);

        pane.add(new JLabel("B"));
        pane.add(b);
        pane.add(bField);
    }

    private void setupSlider(JSlider slider) {
        slider.setPreferredSize(new Dimension(200, 50));
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    private void setupTextField(JTextField field) {
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setColumns(5);
    }

    public void addChangeListener(ChangeListener listener) {
        r.addChangeListener(listener);
        g.addChangeListener(listener);
        b.addChangeListener(listener);
    }

    public void addActionListener(ActionListener listener) {
        rField.addActionListener(listener);
        gField.addActionListener(listener);
        bField.addActionListener(listener);
    }

    public JPanel getPane() {
        return pane;
    }

    public int getR() { return r.getValue(); }
    public int getG() { return g.getValue(); }
    public int getB() { return b.getValue(); }

    public void setR(int value) { r.setValue(value); rField.setText(String.valueOf(value)); }
    public void setG(int value) { g.setValue(value); gField.setText(String.valueOf(value)); }
    public void setB(int value) { b.setValue(value); bField.setText(String.valueOf(value)); }
}
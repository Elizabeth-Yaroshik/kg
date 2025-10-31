package colormodelconverter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class ColorPanel extends JPanel {
    private JSlider[] sliders;
    private JSpinner[] spinners;
    private String[] labels;

    public ColorPanel(String title, String[] labels, int[] minValues, int[] maxValues, int[] initialValues) {
        this.labels = labels;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(title));

        sliders = new JSlider[labels.length];
        spinners = new JSpinner[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            rowPanel.add(new JLabel(labels[i] + ":"));

            sliders[i] = new JSlider(minValues[i], maxValues[i], initialValues[i]);
            sliders[i].setMajorTickSpacing((maxValues[i] - minValues[i]) / 4);
            sliders[i].setMinorTickSpacing((maxValues[i] - minValues[i]) / 20);
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            rowPanel.add(sliders[i]);

            SpinnerNumberModel model = new SpinnerNumberModel(initialValues[i], minValues[i], maxValues[i], 1);
            spinners[i] = new JSpinner(model);
            ((JSpinner.DefaultEditor) spinners[i].getEditor()).getTextField().setColumns(4);
            rowPanel.add(spinners[i]);

            add(rowPanel);
        }
    }

    public JSlider getSlider(int index) { return sliders[index]; }
    public JSpinner getSpinner(int index) { return spinners[index]; }
    public int getValue(int index) { return sliders[index].getValue(); }

    public void setValue(int index, int value) {
        sliders[index].setValue(value);
        spinners[index].setValue(value);
    }

    public void addSliderChangeListener(int index, ChangeListener listener) {
        sliders[index].addChangeListener(listener);
    }

    public void addSpinnerChangeListener(int index, ChangeListener listener) {
        spinners[index].addChangeListener(listener);
    }

    public int getComponentCount() { return labels.length; }
}
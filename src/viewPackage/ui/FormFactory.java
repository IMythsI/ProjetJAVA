package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public class FormFactory {

    public static final int FIELD_WIDTH = 360;
    public static final int FIELD_HEIGHT = 30;

    public static JTextField createTextField() {
        JTextField field = new JTextField();

        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(new Font("Arial", Font.PLAIN, 15));

        field.putClientProperty(
                "FlatLaf.style",
                "arc:15;" +
                        "focusWidth:1;" +
                        "innerFocusWidth:0"
        );

        return field;
    }

    public static JTextArea createTextArea() {
        JTextArea area = new JTextArea();

        area.setFont(new Font("Arial", Font.PLAIN, 15));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setRows(4);
        area.setMargin(new Insets(10, 12, 10, 12));

        return area;
    }

    public static JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();

        comboBox.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        comboBox.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        comboBox.setFont(AppTheme.BUTTON_FONT);

        return comboBox;
    }

    public static <T> JComboBox<T> createGenericComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();

        comboBox.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        comboBox.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        comboBox.setFont(AppTheme.BUTTON_FONT);

        return comboBox;
    }

    public static JSpinner createNumberSpinner(int min, int max, int value) {
        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(value, min, max, 1)
        );

        spinner.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        spinner.setMinimumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        spinner.setFont(AppTheme.BUTTON_FONT);

        return spinner;
    }

    public static void addFormRow(JPanel panel,
                                  GridBagConstraints gbc,
                                  int row,
                                  String labelText,
                                  JComponent field) {
        JLabel label = new JLabel(labelText);

        label.setFont(AppTheme.BUTTON_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(12, 8, 12, 18);

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(field, gbc);
    }
}
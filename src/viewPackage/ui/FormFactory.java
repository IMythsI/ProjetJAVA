package viewPackage.ui;

import viewPackage.ui.AppTheme;

import javax.swing.*;
import java.awt.*;

public class FormFactory {

    public static JTextField createTextField() {
        JTextField field = new JTextField();

        field.setPreferredSize(new Dimension(320, 44));
        field.setFont(new Font("Arial", Font.PLAIN, 14));

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

        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        return area;
    }

    public static JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();

        comboBox.setPreferredSize(new Dimension(220, 42));
        comboBox.setFont(AppTheme.BUTTON_FONT);

        return comboBox;
    }

    public static JSpinner createNumberSpinner(int min, int max, int value) {
        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(value, min, max, 1)
        );

        spinner.setPreferredSize(new Dimension(100, 42));
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
        gbc.insets = new Insets(10, 8, 10, 8);

        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(field, gbc);
    }
}
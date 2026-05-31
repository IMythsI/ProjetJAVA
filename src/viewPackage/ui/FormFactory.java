package viewPackage.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public final class FormFactory {

    public static final int FIELD_WIDTH = AppTheme.FIELD_WIDTH;
    public static final int FIELD_HEIGHT = AppTheme.FIELD_HEIGHT;
    public static final int TEXT_AREA_HEIGHT = AppTheme.TEXT_AREA_HEIGHT;

    private FormFactory() {

    }

    //TEXT FIELD
    public static JTextField createTextField() {
        JTextField field = new JTextField();

        applyTextComponentStyle(field);

        return field;
    }

    //TEXT AREA
    public static JTextArea createTextArea() {
        JTextArea area = new JTextArea();

        area.setFont(AppTheme.TEXT_FONT);
        area.setForeground(AppTheme.TEXT_PRIMARY);
        area.setBackground(Color.WHITE);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setRows(4);
        area.setMargin(new Insets(10, 12, 10, 12));

        return area;
    }

    public static JScrollPane createTextAreaScrollPane(JTextArea textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);

        scrollPane.setPreferredSize(AppTheme.TEXT_AREA_SIZE);
        scrollPane.setMinimumSize(new Dimension(250, AppTheme.TEXT_AREA_HEIGHT));
        scrollPane.setBorder(createFieldBorder());

        return scrollPane;
    }

    //COMBOBOx
    public static JComboBox<String> createComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();

        applyComboBoxStyle(comboBox);

        return comboBox;
    }

    public static <T> JComboBox<T> createGenericComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();

        applyComboBoxStyle(comboBox);

        return comboBox;
    }

    //SPINNER
    public static JSpinner createNumberSpinner(int min, int max, int value) {
        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(value, min, max, 1)
        );

        applySpinnerStyle(spinner);

        return spinner;
    }

    public static JSpinner createNumberSpinner(int min, int max, int value, int step) {
        JSpinner spinner = new JSpinner(
                new SpinnerNumberModel(value, min, max, step)
        );

        applySpinnerStyle(spinner);

        return spinner;
    }

    //FORM ROW
    public static void addFormRow(JPanel panel,
                                  GridBagConstraints constraints,
                                  int row,
                                  String labelText,
                                  JComponent field) {
        JLabel label = createFormLabel(labelText);

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.weightx = 0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(
                AppTheme.FORM_ROW_VERTICAL_GAP,
                0,
                AppTheme.FORM_ROW_VERTICAL_GAP,
                AppTheme.FORM_ROW_HORIZONTAL_GAP
        );

        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(
                AppTheme.FORM_ROW_VERTICAL_GAP,
                0,
                AppTheme.FORM_ROW_VERTICAL_GAP,
                0
        );

        panel.add(field, constraints);
    }

    public static JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.TEXT_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setPreferredSize(new Dimension(AppTheme.FORM_LABEL_WIDTH, AppTheme.FIELD_HEIGHT));
        label.setHorizontalAlignment(SwingConstants.RIGHT);

        return label;
    }

    //DATE PANEL
    public static JPanel createThreeColumnPanel(JComponent first,
                                                JComponent second,
                                                JComponent third) {
        JPanel panel = new JPanel(new GridLayout(1, 3, AppTheme.COMPONENT_GAP_SMALL, 0));

        panel.setOpaque(false);

        panel.add(first);
        panel.add(second);
        panel.add(third);

        return panel;
    }

    //STYLE
    private static void applyTextComponentStyle(JTextField field) {
        field.setPreferredSize(AppTheme.FIELD_SIZE);
        field.setMinimumSize(new Dimension(250, AppTheme.FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.FIELD_HEIGHT));

        field.setFont(AppTheme.TEXT_FONT);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setBackground(Color.WHITE);

        field.setBorder(createFieldBorder());
    }

    private static void applyComboBoxStyle(JComboBox<?> comboBox) {
        comboBox.setPreferredSize(AppTheme.FIELD_SIZE);
        comboBox.setMinimumSize(new Dimension(250, AppTheme.FIELD_HEIGHT));
        comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.FIELD_HEIGHT));

        comboBox.setFont(AppTheme.TEXT_FONT);
        comboBox.setForeground(AppTheme.TEXT_PRIMARY);
        comboBox.setBackground(Color.WHITE);
    }

    private static void applySpinnerStyle(JSpinner spinner) {
        spinner.setPreferredSize(AppTheme.FIELD_SIZE);
        spinner.setMinimumSize(new Dimension(250, AppTheme.FIELD_HEIGHT));
        spinner.setMaximumSize(new Dimension(Integer.MAX_VALUE, AppTheme.FIELD_HEIGHT));

        spinner.setFont(AppTheme.TEXT_FONT);

        JComponent editor = spinner.getEditor();

        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            JTextField textField = defaultEditor.getTextField();

            textField.setFont(AppTheme.TEXT_FONT);
            textField.setForeground(AppTheme.TEXT_PRIMARY);
            textField.setBackground(Color.WHITE);
            textField.setBorder(createFieldBorder());
        }
    }

    private static Border createFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );
    }
}
package viewPackage.Order;

import modelPackage.Order;
import viewPackage.AbstractPanel;
import viewPackage.MainJFrame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Hashtable;

public class TakeAwayOrderFormPanel extends AbstractPanel {
    private JTextField nameCustomerField;
    private JTextField telCustomerField;
    private JTextField commentField;
    private JSpinner guestCountSpinner;
    private JSlider pickUpTimeSlider;
    private JLabel pickUpTimeValueLabel;

    public TakeAwayOrderFormPanel(MainJFrame mainWindow) {
        super(mainWindow);

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        add(createTopPanel(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(createBackButton(), BorderLayout.WEST);

        JLabel title = new JLabel("Nouvelle commande à emporter", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));

        topPanel.add(title, BorderLayout.CENTER);

        return topPanel;
    }

    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new GridBagLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameCustomerField = new JTextField(22);
        telCustomerField = new JTextField(22);
        guestCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));

        LocalTime now = LocalTime.now();
        int currentMinutes = now.getHour() * 60 + now.getMinute();
        currentMinutes = Math.round(currentMinutes / 15f) * 15;

        pickUpTimeSlider = new JSlider(0,24 * 60,currentMinutes);

        pickUpTimeSlider.setPreferredSize(new Dimension(350, 100));

        pickUpTimeValueLabel = new JLabel(formatTimeFromSlider(), SwingConstants.CENTER);
        pickUpTimeValueLabel.setFont(new Font("Arial", Font.BOLD, 16));

        pickUpTimeSlider.setMajorTickSpacing(6 * 60);
        pickUpTimeSlider.setMinorTickSpacing(15);

        pickUpTimeSlider.setPaintTicks(true);
        pickUpTimeSlider.setPaintLabels(true);

        pickUpTimeSlider.setSnapToTicks(true);

        Hashtable<Integer, JLabel> labels = new Hashtable<>();

        labels.put(0, new JLabel("00h"));
        labels.put(6 * 60, new JLabel("06h"));
        labels.put(12 * 60, new JLabel("12h"));
        labels.put(18 * 60, new JLabel("18h"));
        labels.put(24 * 60, new JLabel("24h"));

        pickUpTimeSlider.setLabelTable(labels);

        pickUpTimeValueLabel = new JLabel(
                formatTimeFromSlider()
        );

        pickUpTimeValueLabel.setFont(
                new Font("Arial", Font.BOLD, 16)
        );

        pickUpTimeSlider.addChangeListener(event -> {
            pickUpTimeValueLabel.setText(
                    formatTimeFromSlider()
            );
        });

        JPanel timePanel = new JPanel(
                new BorderLayout(10, 0)
        );

        timePanel.add(
                pickUpTimeSlider,
                BorderLayout.CENTER
        );

        timePanel.add(
                pickUpTimeValueLabel,
                BorderLayout.EAST
        );

        commentField = new JTextField(22);

        addFormRow(formPanel, gbc, 0, "Nom client *", nameCustomerField);
        addFormRow(formPanel, gbc, 1, "Téléphone", telCustomerField);
        addFormRow(formPanel, gbc, 2, "Nombre de personnes *", guestCountSpinner);
        addFormRow(formPanel, gbc, 3, "Heure de retrait *", timePanel);
        addFormRow(formPanel, gbc, 4, "Commentaire", commentField);

        wrapperPanel.add(formPanel);
        return wrapperPanel;
    }

    private String formatTimeFromSlider() {

        int totalMinutes =
                pickUpTimeSlider.getValue();

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (hours == 24) {
            hours = 0;
        }

        return String.format(
                "%02d:%02d",
                hours,
                minutes
        );
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row,
                            String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setPreferredSize(new Dimension(300, 40));
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton cancelButton = new JButton("Annuler");
        JButton nextButton = new JButton("Choisir les produits");

        cancelButton.addActionListener(event -> mainWindow.goBack());
        nextButton.addActionListener(event -> goToProductSelection());

        buttonPanel.add(cancelButton);
        buttonPanel.add(nextButton);

        return buttonPanel;
    }

    private void goToProductSelection() {
        try {
            Order order = createTakeAwayOrderFromForm();
            mainWindow.showProductSelectionPanel(order);

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Order createTakeAwayOrderFromForm() {

        Integer guestCount =
                (Integer) guestCountSpinner.getValue();

        String nameCustomer =
                getRequiredText(
                        nameCustomerField.getText(),
                        "Nom client"
                );

        String telCustomer =
                emptyToNull(
                        telCustomerField.getText()
                );

        String comment =
                emptyToNull(
                        commentField.getText()
                );

        int totalMinutes =
                pickUpTimeSlider.getValue();

        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        if (hours == 24) {
            hours = 0;
        }

        LocalTime pickUpTime =
                LocalTime.of(hours, minutes);

        return new Order(
                null,
                comment,
                guestCount,
                LocalDate.now(),
                true,
                pickUpTime,
                nameCustomer,
                telCustomer,
                null
        );
    }

    private Integer parseRequiredInteger(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        try {
            int number = Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(fieldName + " doit être supérieur à 0.");
            }

            return number;

        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(fieldName + " doit être un nombre entier.");
        }
    }

    private LocalTime parseRequiredTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        try {
            return LocalTime.parse(value);
        } catch (Exception exception) {
            throw new IllegalArgumentException(fieldName + " doit respecter le format HH:mm.");
        }
    }

    private String getRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " est obligatoire.");
        }

        return value.trim();
    }

    private String emptyToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}

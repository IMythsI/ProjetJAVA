package viewPackage.Order;

import modelPackage.Order;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TakeAwayOrderFormPanel extends AppPage {

    private JPanel formCard;

    private JTextField customerNameField;
    private JTextField phoneField;
    private JComboBox<String> pickUpTimeComboBox;
    private JTextArea commentArea;

    public TakeAwayOrderFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        addCentered(
                createPageTitle("Nouvelle commande à emporter"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Renseignez les informations du client"),
                1,
                new Insets(0, 0, 30, 0)
        );

        addCentered(
                createFormCardWrapper(),
                2,
                new Insets(0, 0, 0, 0)
        );
    }

    private JPanel createFormCardWrapper() {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);

        formCard = createFormCard();
        wrapper.add(formCard);

        wrapper.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent event) {
                resizeFormCard(wrapper);
            }
        });

        return wrapper;
    }

    private JPanel createFormCard() {
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 480);
        card.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.CARD_PADDING_TOP,
                AppTheme.CARD_PADDING_LEFT,
                AppTheme.CARD_PADDING_BOTTOM,
                AppTheme.CARD_PADDING_RIGHT
        ));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        createFormFields();

        FormFactory.addFormRow(formPanel, constraints, 0, "Nom du client *", customerNameField);
        FormFactory.addFormRow(formPanel, constraints, 1, "Téléphone", phoneField);
        FormFactory.addFormRow(formPanel, constraints, 2, "Heure de retrait *", pickUpTimeComboBox);
        FormFactory.addFormRow(formPanel, constraints, 3, "Commentaire", FormFactory.createTextAreaScrollPane(commentArea));

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private void createFormFields() {
        customerNameField = FormFactory.createTextField();
        phoneField = FormFactory.createTextField();
        pickUpTimeComboBox = createPickUpTimeComboBox();
        commentArea = FormFactory.createTextArea();
    }

    private JComboBox<String> createPickUpTimeComboBox() {
        JComboBox<String> comboBox = FormFactory.createComboBox();

        for (int hour = 11; hour <= 22; hour++) {
            comboBox.addItem(String.format("%02d:00", hour));
            comboBox.addItem(String.format("%02d:15", hour));
            comboBox.addItem(String.format("%02d:30", hour));
            comboBox.addItem(String.format("%02d:45", hour));
        }

        comboBox.setSelectedItem(getDefaultPickUpTime());

        return comboBox;
    }

    private String getDefaultPickUpTime() {
        LocalTime now = LocalTime.now().plusMinutes(30);

        int minute = now.getMinute();
        int roundedMinute = ((minute + 14) / 15) * 15;

        int hour = now.getHour();

        if (roundedMinute == 60) {
            hour++;
            roundedMinute = 0;
        }

        if (hour < 11 || hour > 22) {
            return "19:00";
        }

        return String.format("%02d:%02d", hour, roundedMinute);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(
                FlowLayout.RIGHT,
                AppTheme.COMPONENT_GAP_MEDIUM,
                AppTheme.COMPONENT_GAP_MEDIUM
        ));

        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 20));

        JButton cancelButton = ButtonFactory.createSecondaryButton(
                "Annuler",
                () -> mainWindow.goBack()
        );

        JButton nextButton = ButtonFactory.createPrimaryButton(
                "Choisir les produits",
                this::goToProductSelection
        );

        nextButton.setPreferredSize(new Dimension(220, AppTheme.BUTTON_HEIGHT));

        buttonPanel.add(cancelButton);
        buttonPanel.add(nextButton);

        return buttonPanel;
    }

    private void resizeFormCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = 40;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        formCard.setPreferredSize(new Dimension(newWidth, 480));
        formCard.setMinimumSize(new Dimension(minWidth, 440));
        formCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        formCard.revalidate();
        formCard.repaint();
    }

    private void goToProductSelection() {
        try {
            validateForm();

            Order order = createTakeAwayOrderFromForm();

            mainWindow.showProductSelectionPanel(order);

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void validateForm() {
        String customerName = customerNameField.getText().trim();

        if (customerName.isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }

        if (pickUpTimeComboBox.getSelectedItem() == null) {
            throw new IllegalArgumentException("L'heure de retrait est obligatoire.");
        }

        validatePhoneIfPresent();
    }

    private void validatePhoneIfPresent() {
        String phone = phoneField.getText().trim();

        if (phone.isEmpty()) {
            return;
        }

        if (!phone.matches("[0-9+ ]{6,20}")) {
            throw new IllegalArgumentException(
                    "Le numéro de téléphone doit contenir uniquement des chiffres, des espaces ou le signe +."
            );
        }
    }

    private Order createTakeAwayOrderFromForm() {
        String customerName = customerNameField.getText().trim();
        String phone = getNullableText(phoneField.getText());
        String comment = getNullableText(commentArea.getText());

        Integer guestCount = 1;
        LocalTime pickUpTime = LocalTime.parse((String) pickUpTimeComboBox.getSelectedItem());

        return new Order(
                null,
                comment,
                guestCount,
                LocalDate.now(),
                true,
                pickUpTime,
                customerName,
                phone,
                null
        );
    }

    private String getNullableText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        return text.trim();
    }
}
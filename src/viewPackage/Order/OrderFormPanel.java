package viewPackage.Order;

import controllerPackage.ApplicationController;
import exceptionPackage.OrderException;
import exceptionPackage.TableException;
import modelPackage.Order;
import modelPackage.Table;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderFormPanel extends AppPage {

    private final ApplicationController controller;
    private final Order editedOrder;

    private JPanel formCard;
    private JCheckBox takeAwayCheckBox;
    private JSpinner guestCountSpinner;
    private JSpinner orderDateSpinner;
    private JSpinner pickUpTimeSpinner;
    private JTextField customerNameField;
    private JTextField customerPhoneField;
    private JComboBox<Table> tableComboBox;
    private JTextArea commentTextArea;

    public OrderFormPanel(MainJFrame mainWindow) {
        this(mainWindow, null);
    }

    public OrderFormPanel(MainJFrame mainWindow, Order editedOrder) {
        super(mainWindow, true);

        this.controller = new ApplicationController();
        this.editedOrder = editedOrder;

        addCentered(
                createPageTitle(isEditMode() ? "Modifier une commande" : "Ajouter une commande"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createFormCardWrapper(),
                1,
                new Insets(0, 0, 0, 0)
        );
    }

    private boolean isEditMode() {
        return editedOrder != null;
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
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 720);
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

        FormFactory.addFormRow(formPanel, constraints, 0, "À emporter", takeAwayCheckBox);
        FormFactory.addFormRow(formPanel, constraints, 1, "Date *", orderDateSpinner);
        FormFactory.addFormRow(formPanel, constraints, 2, "Personnes *", guestCountSpinner);
        FormFactory.addFormRow(formPanel, constraints, 3, "Table *", tableComboBox);
        FormFactory.addFormRow(formPanel, constraints, 4, "Heure retrait *", pickUpTimeSpinner);
        FormFactory.addFormRow(formPanel, constraints, 5, "Nom client *", customerNameField);
        FormFactory.addFormRow(formPanel, constraints, 6, "Téléphone", customerPhoneField);
        FormFactory.addFormRow(
                formPanel,
                constraints,
                7,
                "Commentaire",
                FormFactory.createTextAreaScrollPane(commentTextArea)
        );

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private void createFormFields() {
        takeAwayCheckBox = new JCheckBox("Commande à emporter");
        takeAwayCheckBox.setOpaque(false);
        takeAwayCheckBox.setFont(AppTheme.TEXT_FONT);
        takeAwayCheckBox.setForeground(AppTheme.TEXT_PRIMARY);
        takeAwayCheckBox.addActionListener(event -> updateFieldAvailability());

        guestCountSpinner = FormFactory.createNumberSpinner(1, 50, 1);

        orderDateSpinner = createDateSpinner(new Date(), "dd/MM/yyyy", Calendar.DAY_OF_MONTH);
        pickUpTimeSpinner = createDateSpinner(new Date(), "HH:mm", Calendar.MINUTE);

        customerNameField = FormFactory.createTextField();
        customerPhoneField = FormFactory.createTextField();

        tableComboBox = FormFactory.createGenericComboBox();
        loadTables();

        commentTextArea = FormFactory.createTextArea();

        if (isEditMode()) {
            fillFormWithEditedOrder();
        }

        updateFieldAvailability();
    }

    private JSpinner createDateSpinner(Date value, String pattern, int calendarField) {
        JSpinner spinner = new JSpinner(new SpinnerDateModel(value, null, null, calendarField));
        spinner.setEditor(new JSpinner.DateEditor(spinner, pattern));
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
        }

        return spinner;
    }

    private void loadTables() {
        try {
            ArrayList<Table> tables = controller.getAllTables();

            for (Table table : tables) {
                tableComboBox.addItem(table);
            }

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de chargement des tables",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void fillFormWithEditedOrder() {
        selectTakeAway(editedOrder.getIsTakeAway());
        guestCountSpinner.setValue(editedOrder.getGuestCount());
        orderDateSpinner.setValue(toDate(editedOrder.getOrderDate()));

        if (editedOrder.getPickUpTime() != null) {
            pickUpTimeSpinner.setValue(toDate(editedOrder.getPickUpTime()));
        }

        customerNameField.setText(
                editedOrder.getNameCustomer() == null ? "" : editedOrder.getNameCustomer()
        );

        customerPhoneField.setText(
                editedOrder.getTelCustomer() == null ? "" : editedOrder.getTelCustomer()
        );

        commentTextArea.setText(
                editedOrder.getComment() == null ? "" : editedOrder.getComment()
        );

        if (editedOrder.getTable() != null) {
            selectTable(editedOrder.getTable().getIdTable());
        }
    }

    private void selectTakeAway(boolean takeAway) {
        takeAwayCheckBox.setSelected(takeAway);
    }

    private void selectTable(Integer idTable) {
        if (idTable == null) {
            return;
        }

        for (int index = 0; index < tableComboBox.getItemCount(); index++) {
            Table table = tableComboBox.getItemAt(index);

            if (table != null && idTable.equals(table.getIdTable())) {
                tableComboBox.setSelectedIndex(index);
                return;
            }
        }
    }

    private Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date toDate(LocalTime localTime) {
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
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
                () -> mainWindow.showOrderListPanel()
        );

        JButton saveButton = ButtonFactory.createPrimaryButton(
                isEditMode() ? "Modifier" : "Choisir les produits",
                this::saveOrder
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private void resizeFormCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 620;
        int horizontalMargin = AppTheme.PAGE_HORIZONTAL_PADDING * 2;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        formCard.setPreferredSize(new Dimension(newWidth, 720));
        formCard.setMinimumSize(new Dimension(minWidth, 680));
        formCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        formCard.revalidate();
        formCard.repaint();
    }

    private void updateFieldAvailability() {
        boolean takeAway = isTakeAwaySelected();

        if (takeAway) {
            guestCountSpinner.setValue(1);
            tableComboBox.setSelectedItem(null);
        } else {
            customerNameField.setText("");
            customerPhoneField.setText("");
        }

        setSpinnerAvailability(guestCountSpinner, !takeAway);
        setComboBoxAvailability(tableComboBox, !takeAway);

        setSpinnerAvailability(pickUpTimeSpinner, takeAway);
        setTextFieldAvailability(customerNameField, takeAway);
        setTextFieldAvailability(customerPhoneField, takeAway);
    }

    private boolean isTakeAwaySelected() {
        return takeAwayCheckBox.isSelected();
    }

    private void setTextFieldAvailability(JTextField field, boolean enabled) {
        field.setEditable(enabled);
        field.setFocusable(enabled);

        if (enabled) {
            field.setBackground(Color.WHITE);
            field.setForeground(AppTheme.TEXT_PRIMARY);
        } else {
            field.setBackground(new Color(240, 243, 248));
            field.setForeground(new Color(145, 145, 145));
        }
    }

    private void setSpinnerAvailability(JSpinner spinner, boolean enabled) {
        spinner.setEnabled(enabled);

        JComponent editor = spinner.getEditor();

        if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
            JTextField textField = defaultEditor.getTextField();

            textField.setEditable(enabled);
            textField.setFocusable(enabled);

            if (enabled) {
                textField.setBackground(Color.WHITE);
                textField.setForeground(AppTheme.TEXT_PRIMARY);
            } else {
                textField.setBackground(new Color(240, 243, 248));
                textField.setForeground(new Color(145, 145, 145));
            }
        }
    }

    private void setComboBoxAvailability(JComboBox<?> comboBox, boolean enabled) {
        comboBox.setEnabled(enabled);

        if (enabled) {
            comboBox.setBackground(Color.WHITE);
            comboBox.setForeground(AppTheme.TEXT_PRIMARY);
        } else {
            comboBox.setBackground(new Color(240, 243, 248));
            comboBox.setForeground(new Color(145, 145, 145));
        }
    }

    private void saveOrder() {
        try {
            Order order = createOrderFromForm();

            if (isEditMode()) {
                controller.updateOrder(order);

                JOptionPane.showMessageDialog(
                        this,
                        "La commande a bien été modifiée.",
                        "Modification réussie",
                        JOptionPane.INFORMATION_MESSAGE
                );

                mainWindow.showOrderListPanel();
                return;
            }

            mainWindow.showProductSelectionPanel(order);

        } catch (OrderException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur d'enregistrement",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur de validation",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private Order createOrderFromForm() {
        boolean takeAway = isTakeAwaySelected();
        Integer idOrder = isEditMode() ? editedOrder.getIdOrder() : null;
        Integer guestCount = (Integer) guestCountSpinner.getValue();
        LocalDate orderDate = getLocalDate(orderDateSpinner);
        String comment = cleanNullableText(commentTextArea.getText());

        LocalTime pickUpTime = null;
        String nameCustomer = null;
        String telCustomer = null;
        Table table = null;

        if (takeAway) {
            pickUpTime = getLocalTime(pickUpTimeSpinner);
            nameCustomer = cleanNullableText(customerNameField.getText());
            telCustomer = cleanNullableText(customerPhoneField.getText());
        } else {
            table = (Table) tableComboBox.getSelectedItem();
        }

        if (guestCount == null || guestCount <= 0) {
            throw new IllegalArgumentException("Le nombre de personnes doit être supérieur à 0.");
        }

        if (orderDate == null) {
            throw new IllegalArgumentException("La date de commande est obligatoire.");
        }

        if (takeAway && nameCustomer == null) {
            throw new IllegalArgumentException(
                    "Le nom du client est obligatoire pour une commande à emporter."
            );
        }

        if (!takeAway && table == null) {
            throw new IllegalArgumentException(
                    "La table est obligatoire pour une commande sur place."
            );
        }

        return new Order(
                idOrder,
                comment,
                guestCount,
                orderDate,
                takeAway,
                pickUpTime,
                nameCustomer,
                telCustomer,
                table
        );
    }

    private LocalDate getLocalDate(JSpinner spinner) {
        Date selectedDate = (Date) spinner.getValue();

        return selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private LocalTime getLocalTime(JSpinner spinner) {
        Date selectedDate = (Date) spinner.getValue();

        return selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
                .withSecond(0)
                .withNano(0);
    }

    private String cleanNullableText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        return text.trim();
    }


}
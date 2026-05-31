package viewPackage.Table;

import controllerPackage.ApplicationController;
import exceptionPackage.TableException;
import modelPackage.Status;
import modelPackage.Table;
import viewPackage.MainJFrame;
import viewPackage.ui.*;

import javax.swing.*;
import java.awt.*;

public class TableFormPanel extends AppPage {

    private final ApplicationController controller;

    private JPanel formCard;
    private JSpinner tableIdSpinner;
    private JSpinner seatsSpinner;
    private JComboBox<StatusOption> statusComboBox;

    public TableFormPanel(MainJFrame mainWindow) {
        super(mainWindow, true);

        controller = new ApplicationController();

        addCentered(
                createPageTitle("Nouvelle table"),
                0,
                new Insets(0, 0, 12, 0)
        );

        addCentered(
                createPageSubtitle("Ajoutez une table dans le restaurant"),
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
        JPanel card = CardFactory.createAdaptiveCard(AppTheme.FORM_CARD_MAX_WIDTH, 360);
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

        FormFactory.addFormRow(formPanel, constraints, 0, "Numéro de table *", tableIdSpinner);
        FormFactory.addFormRow(formPanel, constraints, 1, "Nombre de places *", seatsSpinner);
        FormFactory.addFormRow(formPanel, constraints, 2, "Statut *", statusComboBox);

        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonPanel(), BorderLayout.SOUTH);

        return card;
    }

    private void createFormFields() {
        tableIdSpinner = FormFactory.createNumberSpinner(1, 999, 1);
        seatsSpinner = FormFactory.createNumberSpinner(1, 50, 2);

        statusComboBox = FormFactory.createGenericComboBox();
        statusComboBox.addItem(new StatusOption("Available", "Libre"));
        statusComboBox.addItem(new StatusOption("Reserved", "Réservée"));
        statusComboBox.addItem(new StatusOption("Occupied", "Occupée"));
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

        JButton saveButton = ButtonFactory.createPrimaryButton(
                "Ajouter",
                this::saveTable
        );

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private void resizeFormCard(JPanel wrapper) {
        int availableWidth = wrapper.getWidth();

        int maxWidth = AppTheme.FORM_CARD_MAX_WIDTH;
        int minWidth = 560;
        int horizontalMargin = AppTheme.PAGE_HORIZONTAL_PADDING * 2;

        int newWidth = Math.min(maxWidth, availableWidth - horizontalMargin);
        newWidth = Math.max(minWidth, newWidth);

        formCard.setPreferredSize(new Dimension(newWidth, 360));
        formCard.setMinimumSize(new Dimension(minWidth, 320));
        formCard.setMaximumSize(new Dimension(maxWidth, Integer.MAX_VALUE));

        formCard.revalidate();
        formCard.repaint();
    }

    private void saveTable() {
        try {
            Table table = createTableFromForm();

            controller.addTable(table);

            JOptionPane.showMessageDialog(
                    this,
                    "La table a bien été ajoutée.",
                    "Ajout réussi",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mainWindow.showTableListPanel();

        } catch (TableException exception) {
            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Erreur d'ajout",
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

    private Table createTableFromForm() {
        Integer idTable = (Integer) tableIdSpinner.getValue();
        Integer nbSeats = (Integer) seatsSpinner.getValue();

        StatusOption selectedStatus = (StatusOption) statusComboBox.getSelectedItem();

        if (idTable == null || idTable <= 0) {
            throw new IllegalArgumentException("Le numéro de table doit être supérieur à 0.");
        }

        if (nbSeats == null || nbSeats <= 0) {
            throw new IllegalArgumentException("Le nombre de places doit être supérieur à 0.");
        }

        if (selectedStatus == null) {
            throw new IllegalArgumentException("Le statut est obligatoire.");
        }

        return new Table(
                idTable,
                nbSeats,
                new Status(selectedStatus.getDatabaseValue())
        );
    }

    private static class StatusOption {

        private final String databaseValue;
        private final String displayValue;

        public StatusOption(String databaseValue, String displayValue) {
            this.databaseValue = databaseValue;
            this.displayValue = displayValue;
        }

        public String getDatabaseValue() {
            return databaseValue;
        }

        @Override
        public String toString() {
            return displayValue;
        }
    }
}
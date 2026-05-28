package viewPackage.ui;

import viewPackage.MainJFrame;

import javax.swing.*;
import java.awt.*;

public abstract class AppPage extends JPanel {

    protected final MainJFrame mainWindow;
    protected JPanel mainPanel;

    public AppPage(MainJFrame mainWindow, boolean showBackButton) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(HeaderFactory.createHeader(mainWindow, showBackButton), BorderLayout.NORTH);
        add(createScrollableContent(), BorderLayout.CENTER);
    }

    private JScrollPane createScrollableContent() {
        mainPanel = new ResponsivePanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                AppTheme.PAGE_VERTICAL_PADDING,
                AppTheme.PAGE_HORIZONTAL_PADDING,
                AppTheme.PAGE_VERTICAL_PADDING,
                AppTheme.PAGE_HORIZONTAL_PADDING
        ));

        JScrollPane scrollPane = new JScrollPane(mainPanel);

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25);

        return scrollPane;
    }

    protected void addCentered(Component component, int row, Insets insets) {
        GridBagConstraints constraints = createBaseConstraints(row, insets);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        mainPanel.add(component, constraints);
    }

    protected void addFullWidth(Component component, int row, Insets insets) {
        GridBagConstraints constraints = createBaseConstraints(row, insets);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        mainPanel.add(component, constraints);
    }

    protected void addVerticalSpacer(int row, int height) {
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(1, height));

        GridBagConstraints constraints = createBaseConstraints(row, new Insets(0, 0, 0, 0));
        constraints.fill = GridBagConstraints.VERTICAL;

        mainPanel.add(spacer, constraints);
    }

    private GridBagConstraints createBaseConstraints(int row, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.insets = insets;
        constraints.weightx = 1;
        constraints.gridwidth = 1;

        return constraints;
    }

    protected JLabel createPageTitle(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.TITLE_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }

    protected JLabel createPageSubtitle(String text) {
        JLabel label = new JLabel(text);

        label.setFont(AppTheme.SUBTITLE_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        return label;
    }

    protected void refreshPage() {
        revalidate();
        repaint();

        if (mainPanel != null) {
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    private static class ResponsivePanel extends JPanel implements Scrollable {

        public ResponsivePanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 25;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return Math.max(visibleRect.height - 50, 50);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}
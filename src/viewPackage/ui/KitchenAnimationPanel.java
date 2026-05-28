package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public class KitchenAnimationPanel extends JPanel implements Runnable {

    private static final int ANIMATION_DELAY = 500;
    private static final int MAX_DOT_COUNT = 3;

    private final JLabel iconLabel;
    private final JLabel animationLabel;

    private Thread animationThread;
    private volatile boolean running;
    private int dotCount;

    public KitchenAnimationPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(
                FlowLayout.CENTER,
                AppTheme.COMPONENT_GAP_SMALL,
                AppTheme.COMPONENT_GAP_SMALL
        ));

        iconLabel = createIconLabel();
        animationLabel = createAnimationLabel();

        add(iconLabel);
        add(animationLabel);

        startAnimation();
    }

    private JLabel createIconLabel() {
        JLabel label = new JLabel("👨‍🍳");

        label.setFont(AppTheme.EMOJI_FONT);
        label.setForeground(AppTheme.PRIMARY);
        label.setPreferredSize(AppTheme.SMALL_ICON_BUTTON_SIZE);

        return label;
    }

    private JLabel createAnimationLabel() {
        JLabel label = new JLabel("La cuisine se prépare");

        label.setFont(AppTheme.TEXT_FONT);
        label.setForeground(AppTheme.TEXT_SECONDARY);

        return label;
    }

    private void startAnimation() {
        if (animationThread != null && animationThread.isAlive()) {
            return;
        }

        running = true;

        animationThread = new Thread(this);
        animationThread.setName("KitchenAnimationThread");
        animationThread.start();
    }

    @Override
    public void run() {
        while (running) {
            updateAnimationText();

            try {
                Thread.sleep(ANIMATION_DELAY);
            } catch (InterruptedException exception) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateAnimationText() {
        dotCount = (dotCount + 1) % (MAX_DOT_COUNT + 1);

        String dots = ".".repeat(dotCount);
        String text = "La cuisine se prépare" + dots;

        SwingUtilities.invokeLater(() -> animationLabel.setText(text));
    }

    @Override
    public void removeNotify() {
        stopAnimation();
        super.removeNotify();
    }

    private void stopAnimation() {
        running = false;

        if (animationThread != null) {
            animationThread.interrupt();
            animationThread = null;
        }
    }
}
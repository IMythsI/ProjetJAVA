package viewPackage.ui;

import javax.swing.*;
import java.awt.*;

public class KitchenAnimationPanel extends JPanel implements Runnable {

    private JLabel animationLabel;
    private JLabel iconLabel;

    private Thread animationThread;
    private boolean running;

    private int step;

    public KitchenAnimationPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        iconLabel = new JLabel("👨‍🍳");
        iconLabel.setPreferredSize(new Dimension(46, 34));
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setForeground(AppTheme.PRIMARY);

        animationLabel = new JLabel("Kitchen is preparing");
        animationLabel.setFont(AppTheme.SUBTITLE_FONT);
        animationLabel.setForeground(AppTheme.TEXT_SECONDARY);

        add(iconLabel);
        add(animationLabel);

        startAnimation();
    }

    private void startAnimation() {
        running = true;

        animationThread = new Thread(this);
        animationThread.setName("Kitchen animation thread");
        animationThread.start();
    }

    @Override
    public void run() {
        while (running) {
            updateAnimation();

            try {
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateAnimation() {
        step = (step + 1) % 4;

        String dots = ".".repeat(step);
        String text = "Kitchen is preparing" + dots;

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
        }
    }
}
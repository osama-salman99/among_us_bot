import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AmongUsApp {
    private static final String APP_NAME = "Among Us Bot";
    private static final String SIZE_PANEL_TEXT = "Size:";
    private static final String SHOW_OVERLAY_TEXT = "Show overlay";
    private static final String HIDE_OVERLAY_TEXT = "Hide overlay";
    private static final String MUTE_TEXT = "Mute";
    private static final String UNMUTE_TEXT = "Unmute";
    private static final String RESTART_TEXT = "Restart";
    private static final int MAXIMUM_WIDTH = 300;
    private static final int MINIMUM_WIDTH = 200;
    private static final int COLUMN_HEIGHT = 25;
    private static final int INCREASE_VALUE = 10;
    private final ArrayList<Player> players;
    private final int SCREEN_HEIGHT;
    private JFrame mainFrame;
    private JFrame overlayFrame;
    private boolean overlayShown;
    private int currentWidth;

    public AmongUsApp() {
        SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        currentWidth = (MAXIMUM_WIDTH + MINIMUM_WIDTH) / 2;
        players = new ArrayList<>();
        overlayShown = false;
        initializeFrame();
        addButtons();
        createOverlay();
        showFrame();
        Runtime.getRuntime().addShutdownHook(new Thread(this::unmuteAll));
    }

    private void initializeFrame() {
        mainFrame = new JFrame(APP_NAME);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 200);
        mainFrame.setEnabled(false);
    }

    private void addButtons() {
        JPanel mainPanel = new JPanel(new GridLayout(4, 1));
        JLabel sizeTextArea = new JLabel(SIZE_PANEL_TEXT, SwingConstants.CENTER);
        JPanel sizeButtonsPanel = new JPanel(new GridLayout(1, 2));
        JButton increaseSizeButton = new JButton("+");
        JButton decreaseSizeButton = new JButton("-");
        JButton overlayButton = new JButton(SHOW_OVERLAY_TEXT);

        increaseSizeButton.addActionListener(event -> increaseSize());
        decreaseSizeButton.addActionListener(event -> decreaseSize());

        sizeButtonsPanel.add(increaseSizeButton);
        sizeButtonsPanel.add(decreaseSizeButton);

        overlayButton.addActionListener(event -> {
            if (overlayShown) {
                overlayButton.setText(SHOW_OVERLAY_TEXT);
                hideOverlay();
            } else {
                overlayButton.setText(HIDE_OVERLAY_TEXT);
                showOverlay();
            }
            overlayShown = !overlayShown;
        });

        mainPanel.add(sizeTextArea);
        mainPanel.add(sizeButtonsPanel);
        mainPanel.add(overlayButton);
        mainFrame.add(mainPanel);
    }

    private void createOverlay() {
        overlayFrame = new JFrame();
        overlayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        overlayFrame.setUndecorated(true);
        overlayFrame.setAlwaysOnTop(true);
        overlayFrame.setSize(currentWidth, COLUMN_HEIGHT);
        overlayFrame.setLayout(new GridLayout(0, 1));
        overlayFrame.setOpacity(0.80F);

        JPanel mutePanel = new JPanel(new GridLayout(1, 2));
        JButton muteAllButton = new JButton(MUTE_TEXT);
        JButton unmuteAllButton = new JButton(UNMUTE_TEXT);
        JButton restartButton = new JButton(RESTART_TEXT);

        muteAllButton.addActionListener(event -> muteAll());
        muteAllButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                muteAllButton.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                muteAllButton.setForeground(Color.RED);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        unmuteAllButton.addActionListener(event -> unmuteAll());
        unmuteAllButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                unmuteAllButton.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                unmuteAllButton.setForeground(Color.GREEN);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        restartButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                restartButton.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                restartButton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        restartButton.addActionListener(event -> restartGame());

        muteAllButton.setForeground(Color.RED);
        muteAllButton.setOpaque(false);
        muteAllButton.setFocusPainted(false);
        muteAllButton.setBorderPainted(false);
        muteAllButton.setContentAreaFilled(false);

        unmuteAllButton.setForeground(Color.GREEN);
        unmuteAllButton.setOpaque(false);
        unmuteAllButton.setFocusPainted(false);
        unmuteAllButton.setBorderPainted(false);
        unmuteAllButton.setContentAreaFilled(false);

        restartButton.setForeground(Color.WHITE);

        mutePanel.add(muteAllButton);
        mutePanel.add(unmuteAllButton);

        mutePanel.setBackground(Color.BLACK);

        overlayFrame.add(mutePanel);
        overlayFrame.add(restartButton);
    }

    private void muteAll() {
        for (Player player : players) {
            new Thread(player::mute).start();
        }
        Player.setAllMuted(true);
    }

    private void unmuteAll() {
        for (Player player : players) {
            new Thread(player::unmute).start();
        }
        Player.setAllMuted(false);
    }

    private void hideOverlay() {
        overlayFrame.setVisible(false);
    }

    private void showOverlay() {
        overlayFrame.setVisible(true);
        overlayFrame.setLocation(0, SCREEN_HEIGHT - overlayFrame.getHeight());
    }

    private void increaseSize() {
        if (currentWidth <= (MAXIMUM_WIDTH - INCREASE_VALUE)) {
            currentWidth += INCREASE_VALUE;
            updateOverlaySize();
        }
    }

    private void decreaseSize() {
        if (currentWidth >= (MINIMUM_WIDTH + INCREASE_VALUE)) {
            currentWidth -= INCREASE_VALUE;
            updateOverlaySize();
        }
    }

    private void showFrame() {
        mainFrame.setVisible(true);
    }

    private void updateOverlaySize() {
        int width = currentWidth;
        int height = (players.size() + 1) * COLUMN_HEIGHT;
        overlayFrame.setSize(width, height);
    }

    public void addPlayer(Player player) {
        if (players.contains(player)) {
            removePlayer(player);
        }
        players.add(player);
        overlayFrame.add(player.getPlayerPanel());
        refreshOverlay();
    }

    public void removePlayer(Player player) {
        int index = players.indexOf(player);
        overlayFrame.remove(players.remove(index).getPlayerPanel());
        refreshOverlay();
    }

    private synchronized void refreshOverlay() {
        updateOverlaySize();
        if (overlayFrame.isVisible()) {
            hideOverlay();
            showOverlay();
        }
    }

    public String getPassword() {
        String password = JOptionPane.showInputDialog(mainFrame, "Please enter your server's password");
        if (password == null) {
            System.exit(0);
        }
        return password;
    }

    public void enableFrame() {
        mainFrame.setEnabled(true);
    }

    private void restartGame() {
        unKillAll();
        unmuteAll();
    }

    private void unKillAll() {
        for (Player player : players) {
            player.unKill();
        }
    }
}

package Game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Game extends JFrame {
    Random rand = new Random();

    int boardSize;
    int turn;

    AiLogics ai = new AiLogics(this);

    Clip backgroundMusic = getClip("Sounds\\menu background.wav");

    BoardButton[][] board;

    final Color WALL = Color.BLACK;
    final Color EMPTY = Color.DARK_GRAY;
    final Color P1Color = new Color(0, 170, 200);
    final Color P2Color = new Color(175, 0, 0);
    final Color P3Color = new Color(0, 175, 0);
    final Color P4Color = new Color(180, 120, 0);

    private BackgroundPanel background = new BackgroundPanel();
    private MenuSettingCommands menus = new MenuSettingCommands(this, background);

    final int locationX = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 1280) / 2;
    final int locationY = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 720) / 2;

    boolean fullscreen = false;
    boolean sound = true;
    boolean backgroundMusicPlaying = false;
    boolean addingUnits = false;

    Object lastClickedButton;
    float lasClickTime;

    int unitSetCount;
    int menuIndex = 0;
    int p1Mode = 1;
    int p2Mode = 2;
    int p3Mode = 0;
    int p4Mode = 0;

    public Game() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocation(locationX, locationY);
        setResizable(false);
        try {
            background.setBackground(ImageIO.read(new File("Sprites\\background.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void start() {
        if (!backgroundMusicPlaying && sound) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicPlaying = true;
        }
        if (menuIndex == 0) {
            menus.setMainMenu();
        } else if (menuIndex == 1) {
            menus.setSettingsMenu();
        } else if (menuIndex == 2) {
            menus.setPlayerModeMenu();
        } else if (menuIndex == 3) {
            menus.setBoardSizeMenu();
        } else if (menuIndex == 4) {
            int playerCount = 2;
            if (p3Mode != 0) {
                ++playerCount;
            }
            if (p4Mode != 0) {
                ++playerCount;
            }
            turn = (rand.nextInt(playerCount));
            menus.setGameStart();
        } else if (menuIndex == 5) {
            menus.setHowToPlayMenu();
        }
        setVisible(true);
        menuIndex = -1;
    }

    int countPlayerUnits() {
        int result = 0;

        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                if (board[i][j].getBelonging() == turn) {
                    ++result;
                }
            }
        }

        return result;
    }

    void refreshBoard() {
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j].refresh();
            }
        }
    }

    void passTurnRefreshBoard() {
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j].setWaiting(false);
                board[i][j].setActive(false);
                board[i][j].refresh();
            }
        }
    }

    int checkWin() {
        int p1 = 0;
        int p2 = 0;
        int p3 = 0;
        int p4 = 0;

        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                if (board[i][j].getBelonging() == 0) {
                    ++p1;
                } else if (board[i][j].getBelonging() == 1) {
                    ++p2;
                } else if (board[i][j].getBelonging() == 2) {
                    ++p3;
                } else if (board[i][j].getBelonging() == 3) {
                    ++p4;
                }
            }
        }

        int result = -1;
        int sum = p1 + p2 + p3 + p4;
        if (sum == p1)
            result = 0;
        if (sum == p2)
            result = 1;
        if (sum == p3)
            result = 2;
        if (sum == p4)
            result = 3;

        return result;
    }

    void setPlayerModeTexts() {
        JButton p1Button = (JButton) background.getComponent(0);
        JButton p2Button = (JButton) background.getComponent(1);
        JButton p3Button = (JButton) background.getComponent(2);
        JButton p4Button = (JButton) background.getComponent(3);

        if (p1Mode == 1) {
            p1Button.setText("Player");
        } else if (p1Mode == 2) {
            p1Button.setText("AI");
        }

        if (p2Mode == 1) {
            p2Button.setText("Player");
        } else if (p2Mode == 2) {
            p2Button.setText("AI");
        }

        if (p3Mode == 0) {
            p3Button.setText("None");
        } else if (p3Mode == 1) {
            p3Button.setText("Player");
        } else if (p3Mode == 2) {
            p3Button.setText("AI");
        }

        if (p4Mode == 0) {
            p4Button.setText("None");
        } else if (p4Mode == 1) {
            p4Button.setText("Player");
        } else if (p4Mode == 2) {
            p4Button.setText("AI");
        }
    }

    void setTurnShower() {
        JLabel turnText = (JLabel) background.getComponent(background.getComponents().length - 1);
        JButton surrenderButton = (JButton) background.getComponent(background.getComponents().length - 2);
        if (!addingUnits) {
            String text = null;

            if (turn == 0) {
                text = "Blue's";
            } else if (turn == 1) {
                text = "Red's";
            } else if (turn == 2) {
                text = "Green's";
            } else if (turn == 3) {
                text = "Orange's";
            }
            turnText.setText(text + " turn.");
            surrenderButton.setText("Add Units");
        } else {
            turnText.setText(unitSetCount > 0 ? "Unit Count: " + unitSetCount : "Pass Turn!");
            surrenderButton.setText("Pass Turn");
        }
    }

    Clip getClip(String Sound) {
        try {
            File soundFile = new File(Sound);
            Clip clip;
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(stream);
            clip = audioClip;
            return clip;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.exit(0);
        }
        return getClip("ERROR");
    }

    void makeButtonSound() {
        if (sound)
            getClip("Sounds\\button.wav").start();
    }

    boolean doubleClick(Object obj) {
        return System.nanoTime() - lasClickTime < 250000000 && obj == lastClickedButton;
    }

    private class BackgroundPanel extends JPanel {
        Image image;

        void setBackground(Image image) {
            this.image = image;
        }

        @Override
        public void paintComponent(Graphics G) {
            super.paintComponent(G);
            G.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            repaint();
        }
    }
}

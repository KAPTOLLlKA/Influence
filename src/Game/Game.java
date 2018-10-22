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

    final static int NONE = 0;
    final static int PLAYER = 1;
    final static int AI = 2;

    int boardSize;
    int turn;

    Clip backgroundMusic = getClip("Sounds\\background music.wav");

    BoardButton[][] board;

    final static Color WALL = Color.BLACK;
    final static Color EMPTY = Color.DARK_GRAY;
    final static Color P1Color = new Color(0, 170, 200);
    final static Color P2Color = new Color(175, 0, 0);
    final static Color P3Color = new Color(0, 175, 0);
    final static Color P4Color = new Color(200, 120, 0);

    private BackgroundPanel background = new BackgroundPanel();
    private MenuSettingCommands menus = new MenuSettingCommands(this, background);

    final int locationX = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 1280) / 2;
    final int locationY = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 720) / 2;

    boolean fullscreen = false;
    boolean sound = true;
    boolean backgroundMusicPlaying = false;
    boolean addingUnits = false;
    boolean computerMakeTurn = false;

    int unitSetCount;
    int menuIndex = 0;
    int p1Mode = PLAYER;
    int p2Mode = AI;
    int p3Mode = NONE;
    int p4Mode = NONE;

    AiLogic ai1 = new AiLogic(this);
    AiLogic ai2 = new AiLogic(this);
    AiLogic ai3;
    AiLogic ai4;

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
            menuIndex = -1;
            int playerCount = 2;
            if (p3Mode != NONE) {
                ++playerCount;
                ai3 = new AiLogic(this);
                if (p4Mode != NONE) {
                    ++playerCount;
                    ai4 = new AiLogic(this);
                }
            }
            turn = 2/*(rand.nextInt(playerCount))*/;
            menus.setGameStart();
            setVisible(true);
        } else if (menuIndex == 5) {
            menus.setHowToPlayMenu();
        }
        if (computerMakeTurn) {
            computerMakeTurn = false;
            if (turn == 0 && p1Mode == AI) {
                ai1.makeTurn();
            } else if (turn == 1 && p2Mode == AI) {
                ai2.makeTurn();
            } else if (turn == 2 && p3Mode == AI) {
                ai3.makeTurn();
            } else if (turn == 3 && p4Mode == AI) {
                ai4.makeTurn();
            }
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

    boolean isNone() {
        boolean result = false;

        if (turn == 0 && p1Mode == NONE) {
            result = true;
        } else if (turn == 1 && p2Mode == NONE) {
            result = true;
        } else if (turn == 2 && p3Mode == NONE) {
            result = true;
        } else if (turn == 3 && p4Mode == NONE) {
            result = true;
        }

        return result;
    }

    boolean isAi() {
        boolean result = false;

        if (turn == 0 && p1Mode == AI) {
            result = true;
        } else if (turn == 1 && p2Mode == AI) {
            result = true;
        } else if (turn == 2 && p3Mode == AI) {
            result = true;
        } else if (turn == 3 && p4Mode == AI) {
            result = true;
        }

        return result;
    }

    boolean isPlayer() {
        boolean result = false;

        if (turn == 0 && p1Mode == PLAYER) {
            result = true;
        } else if (turn == 1 && p2Mode == PLAYER) {
            result = true;
        } else if (turn == 2 && p3Mode == PLAYER) {
            result = true;
        } else if (turn == 3 && p4Mode == PLAYER) {
            result = true;
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

        if (p1Mode == PLAYER) {
            p1Button.setText("Player");
        } else if (p1Mode == AI) {
            p1Button.setText("AI");
        }

        if (p2Mode == PLAYER) {
            p2Button.setText("Player");
        } else if (p2Mode == AI) {
            p2Button.setText("AI");
        }

        if (p3Mode == NONE) {
            p3Button.setText("--");
        } else if (p3Mode == PLAYER) {
            p3Button.setText("Player");
        } else if (p3Mode == AI) {
            p3Button.setText("AI");
        }

        if (p4Mode == NONE) {
            p4Button.setText("--");
        } else if (p4Mode == PLAYER) {
            p4Button.setText("Player");
        } else if (p4Mode == AI) {
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

    JButton getPassTurnButton() {
        return (JButton) background.getComponent(background.getComponents().length - 2);
    }

    void passTime(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Some error occured :(");
        }
    }

    void makeButtonSound() {
        if (sound)
            getClip("Sounds\\button.wav").start();
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

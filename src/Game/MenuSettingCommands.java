package Game;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MenuSettingCommands {
    private JLabel influence;
    private Game game;
    private JPanel panel;
    private JTextField sizeField = new JTextField("Board size:");
    private JLabel turnShower = new JLabel();
    private JButton passTurn = new JButton("Add Units");

    private final Color buttonColor = new Color(100, 0, 200);

    private final Font buttonFont = new Font("Bahnschrift SemiBold", Font.BOLD, 20);

    private final Color textColor = new Color(200, 100, 0);

    MenuSettingCommands(Game game, JPanel panel) {
        this.game = game;
        this.panel = panel;
        influence = new JLabel("Influence");
        influence.setForeground(textColor);
        influence.setFont(new Font("Bahnschrift SemiBold", Font.BOLD, 50));
        turnShower.setForeground(influence.getForeground());
        turnShower.setFont(buttonFont);
    }

    void setMainMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new CentralLayout(game));
        JButton startGame = new JButton("Start Game");
        JButton settings = new JButton("Settings");
        JButton howToPlay = new JButton("How To Play");
        JButton exit = new JButton("Exit");

        startGame.setForeground(Color.BLACK);
        settings.setForeground(Color.BLACK);
        howToPlay.setForeground(Color.BLACK);
        exit.setForeground(Color.BLACK);

        startGame.setBackground(buttonColor);
        settings.setBackground(buttonColor);
        howToPlay.setBackground(buttonColor);
        exit.setBackground(buttonColor);

        startGame.setFont(buttonFont);
        settings.setFont(buttonFont);
        howToPlay.setFont(buttonFont);
        exit.setFont(buttonFont);

        startGame.addActionListener(new StartListener());
        settings.addActionListener(new SettingsListener());
        howToPlay.addActionListener(new HowToPlayListener());
        exit.addActionListener(new ExitListener());

        panel.add(influence);
        panel.add(startGame);
        panel.add(settings);
        panel.add(howToPlay);
        panel.add(exit);

        game.add(panel);
    }

    void setSettingsMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new CentralLayout(game));

        JButton fullscreen = new JButton("Fullscreen: " + (game.fullscreen ? "On" : "Off"));
        JButton sound = new JButton("Sound: " + (game.sound ? "On" : "Off"));
        JButton back = new JButton("Back");

        fullscreen.setForeground(Color.BLACK);
        sound.setForeground(Color.BLACK);
        back.setForeground(Color.BLACK);

        fullscreen.setBackground(buttonColor);
        sound.setBackground(buttonColor);
        back.setBackground(buttonColor);

        fullscreen.setFont(buttonFont);
        sound.setFont(buttonFont);
        back.setFont(buttonFont);

        fullscreen.addActionListener(new FullscreenListener(fullscreen));
        sound.addActionListener(new SoundListener(sound));
        back.addActionListener(new BackListener(0));

        panel.add(influence);
        panel.add(fullscreen);
        panel.add(sound);
        panel.add(back);

        game.add(panel);
    }

    void setPlayerModeMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new CentralPlayerModeLayout(game));

        JButton P1 = new JButton("Player");
        JButton P2 = new JButton("AI");
        JButton P3 = new JButton("None");
        JButton P4 = new JButton("None");
        JButton next = new JButton("Next");
        JButton back = new JButton("Back");

        P1.setForeground(Color.BLACK);
        P2.setForeground(Color.BLACK);
        P3.setForeground(Color.BLACK);
        P4.setForeground(Color.BLACK);
        next.setForeground(Color.BLACK);
        back.setForeground(Color.BLACK);

        P1.setBackground(game.P1Color);
        P2.setBackground(game.P2Color);
        P3.setBackground(game.P3Color);
        P4.setBackground(game.P4Color);
        next.setBackground(buttonColor);
        back.setBackground(buttonColor);

        P1.setFont(buttonFont);
        P2.setFont(buttonFont);
        P3.setFont(buttonFont);
        P4.setFont(buttonFont);
        next.setFont(buttonFont);
        back.setFont(buttonFont);

        P1.addActionListener(new PlayerModeListener(0));
        P2.addActionListener(new PlayerModeListener(1));
        P3.addActionListener(new PlayerModeListener(2));
        P4.addActionListener(new PlayerModeListener(3));
        next.addActionListener(new ToBoardSizeMenuListener());
        back.addActionListener(new BackListener(0));

        panel.add(P1);
        panel.add(P2);
        panel.add(P3);
        panel.add(P4);
        panel.add(next);
        panel.add(back);

        game.add(panel);
    }

    void setBoardSizeMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new CentralLayout(game));

        JLabel size = new JLabel("Enter size");
        JButton start = new JButton("Start");
        JButton back = new JButton("Back");

        size.setForeground(new Color(200, 100, 0));
        sizeField.setForeground(Color.BLACK);
        start.setForeground(Color.BLACK);
        back.setForeground(Color.BLACK);

        sizeField.setBackground(Color.DARK_GRAY);
        start.setBackground(buttonColor);
        back.setBackground(buttonColor);

        size.setFont(new Font("SansSerif", Font.BOLD, 50));
        sizeField.setFont(new Font("SansSerif", Font.BOLD, 40));
        start.setFont(buttonFont);
        back.setFont(buttonFont);

        start.addActionListener(new StartGameListener());
        back.addActionListener(new BackListener(2));

        panel.add(size);
        panel.add(sizeField);
        panel.add(start);
        panel.add(back);

        game.add(panel);
    }

    void setGameStart() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new GameboardLayout(game));

        JButton surrender = new JButton("Main Menu");

        String text = null;
        if (game.turn == 0) {
            text = "Blue's";
        } else if (game.turn == 1) {
            text = "Red's";
        } else if (game.turn == 2) {
            text = "Green's";
        } else if (game.turn == 3) {
            text = "Orange's";
        }
        turnShower.setText(text + " turn.");

        surrender.setForeground(Color.BLACK);
        passTurn.setForeground(Color.BLACK);

        surrender.setBackground(buttonColor);
        passTurn.setBackground(buttonColor);

        surrender.setFont(buttonFont);
        passTurn.setFont(buttonFont);

        surrender.addActionListener(new MainMenuListener());
        passTurn.addActionListener(new PassTurnListener());

        panel.add(surrender);

        game.board = new BoardButton[game.boardSize][game.boardSize];
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (i == game.boardSize - 1 && j == 0) {
                    game.board[i][j] = new BoardButton(game, 0, i, j);
                } else if (i == 0 && j == game.boardSize - 1) {
                    game.board[i][j] = new BoardButton(game, 1, i, j);
                } else if (i == 0 && j == 0 && game.p3Mode != game.NONE) {
                    game.board[i][j] = new BoardButton(game, 2, i, j);
                } else if (i == game.boardSize - 1 && j == game.boardSize - 1 && game.p4Mode != game.NONE) {
                    game.board[i][j] = new BoardButton(game, 3, i, j);
                } else {
                    game.board[i][j] = new BoardButton(game, -1, i, j);
                }
                panel.add(game.board[i][j]);
            }
        }

        panel.add(passTurn);
        panel.add(turnShower);

        game.add(panel);
        game.setVisible(true);
        if ((game.turn == 0 && game.p1Mode == game.AI) || (game.turn == 1 && game.p2Mode == game.AI) || (game.turn == 2 && game.p3Mode == game.AI) || (game.turn == 3 && game.p4Mode == game.AI)) {
            game.computerMakeTurn = true;
        }
    }

    void setHowToPlayMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new HowToPlayLayout(game));

        JButton back = new JButton("Back");
        JLabel[] howTo = new JLabel[13];
        howTo[0] = new JLabel("Your goal is to conquer all of the enemies' fields.");
        howTo[1] = new JLabel("On your turn you first conquer fields, then add new units to already conquered ones.");
        howTo[2] = new JLabel("On one field there are maximum 8 units.");
        howTo[3] = new JLabel("You can attack/conquer only with fields, that have 2 or more units in them and at least one side that is empty or belongs to enemy.");
        howTo[4] = new JLabel("To choose field which you want to play from, double click on it.");
        howTo[5] = new JLabel("Black fields are walls (they're set randomly).");
        howTo[6] = new JLabel("When attacking enemy, there are few rules:");
        howTo[7] = new JLabel("1) If defending field has equal units to attacking one, then you have 50% chance to conquer it.");
        howTo[8] = new JLabel("2) If defending field has one less unit, than attacking one, then you have 75% chance to conquer it.");
        howTo[9] = new JLabel("3) If defending field has one more unit than attacking field, then you still have 25% chance to conquer it.");
        howTo[10] = new JLabel("4) If difference is more than 1, then field with more units wins.");
        howTo[11] = new JLabel();
        howTo[12] = new JLabel("That's all, go and fight! :)");

        back.setBackground(buttonColor);

        back.setFont(buttonFont);

        back.addActionListener(new BackListener(0));

        panel.add(back);

        back.setForeground(Color.BLACK);
        for (int i = 0; i < howTo.length; ++i) {
            howTo[i].setForeground(textColor);
            howTo[i].setFont(buttonFont);
            panel.add(howTo[i]);
        }

        game.add(panel);
    }

    private class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            game.menuIndex = 2;
            game.start();
        }
    }

    private class SettingsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            game.menuIndex = 1;
            game.start();
        }
    }

    private class HowToPlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            game.menuIndex = 5;
            game.start();
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            System.exit(0);
        }
    }

    private class FullscreenListener implements ActionListener {
        JButton fullscreen;

        FullscreenListener(JButton fullscreen) {
            this.fullscreen = fullscreen;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            if (!game.fullscreen) {
                game.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                game.setSize(1280, 720);
                game.setLocation(game.locationX, game.locationY);
            }
            game.fullscreen = !game.fullscreen;
            panel.setLayout(new CentralLayout(game));
            fullscreen.setText("Fullscreen: " + (game.fullscreen ? "On" : "Off"));
        }
    }

    private class SoundListener implements ActionListener {
        JButton sound;

        SoundListener(JButton sound) {
            this.sound = sound;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.sound = !game.sound;
            game.backgroundMusicPlaying = !game.backgroundMusicPlaying;
            sound.setText("Sound: " + (game.sound ? "On" : "Off"));
            if (game.sound) {
                game.makeButtonSound();
                game.backgroundMusic = game.getClip("Sounds\\menu background.wav");
                game.backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            } else
                game.backgroundMusic.close();
        }
    }

    private class BackListener implements ActionListener {
        private int menuChange;

        BackListener(int menuChange) {
            this.menuChange = menuChange;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            game.menuIndex = menuChange;
            game.start();
        }
    }

    private class PlayerModeListener implements ActionListener {
        private int player;

        PlayerModeListener(int player) {
            this.player = player;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            if (player == 0) {
                ++game.p1Mode;
                if (game.p1Mode > game.AI) {
                    game.p1Mode = game.PLAYER;
                }
            } else if (player == 1) {
                ++game.p2Mode;
                if (game.p2Mode > game.AI) {
                    game.p2Mode = game.PLAYER;
                }
            } else if (player == 2) {
                ++game.p3Mode;
                if (game.p3Mode > game.AI) {
                    game.p3Mode = game.NONE;
                    game.p4Mode = game.NONE;
                }
            } else if (player == 3) {
                ++game.p4Mode;
                if (game.p3Mode == game.NONE || game.p4Mode > game.AI) {
                    game.p4Mode = game.NONE;
                }
            }
            game.setPlayerModeTexts();
        }
    }

    private class ToBoardSizeMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            game.menuIndex = 3;
            game.start();
        }
    }

    private class StartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int i = 0;
            for (; i < sizeField.getText().length(); ++i) {
                if (sizeField.getText().charAt(i) >= '0' && sizeField.getText().charAt(i) <= '9')
                    break;
            }
            String number = sizeField.getText().substring(i);
            try {
                game.boardSize = Integer.parseInt(number);
                if (game.boardSize > 15) {
                    JOptionPane.showMessageDialog(game, "Maximum board size is 15!");
                    game.boardSize = 0;
                    game.makeButtonSound();
                } else if (game.boardSize < 5) {
                    JOptionPane.showMessageDialog(game, "Minimum board size is 5!");
                    game.boardSize = 0;
                    game.makeButtonSound();
                } else {
                    game.menuIndex = 4;
                    game.start();
                }
            } catch (Exception ex) {
                game.makeButtonSound();
                JOptionPane.showMessageDialog(game, "Enter an integer!");
            }
        }
    }

    private class MainMenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            int decision = JOptionPane.showConfirmDialog(null, "Exit to main menu?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (decision == 0) {
                game.menuIndex = 0;
                game.p1Mode = game.PLAYER;
                game.p2Mode = game.AI;
                game.p3Mode = game.NONE;
                game.p4Mode = game.NONE;
                game.addingUnits = false;
                game.start();
            }
        }
    }

    private class PassTurnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeButtonSound();
            if (game.addingUnits) {
                do {
                    ++game.turn;
                    if (game.turn > 3) {
                        game.turn = 0;
                    }
                } while (game.countPlayerUnits() == 0);
                if (game.isAi()) {
                    game.computerMakeTurn = true;
                }
                game.addingUnits = false;
            } else {
                game.unitSetCount = game.countPlayerUnits();
                game.addingUnits = true;
            }
            game.passTurnRefreshBoard();
            game.setTurnShower();
            if (game.computerMakeTurn) {
                game.start();
            }
        }
    }
}
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

    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 20);

    MenuSettingCommands(Game game, JPanel panel) {
        this.game = game;
        this.panel = panel;
        influence = new JLabel("Influence");
        influence.setForeground(new Color(200, 100, 0));
        influence.setFont(new Font("SansSerif", Font.BOLD, 50));
        turnShower.setForeground(influence.getForeground());
        turnShower.setFont(buttonFont);
    }

    void setMainMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new CentralLayout(game));
        JButton startGame = new JButton("Start Game");
        JButton settings = new JButton("Settings");
        JButton exit = new JButton("Exit");

        startGame.setForeground(Color.BLACK);
        settings.setForeground(Color.BLACK);
        exit.setForeground(Color.BLACK);

        startGame.setBackground(buttonColor);
        settings.setBackground(buttonColor);
        exit.setBackground(buttonColor);

        startGame.setFont(buttonFont);
        settings.setFont(buttonFont);
        exit.setFont(buttonFont);

        startGame.addActionListener(new StartListener());
        settings.addActionListener(new SettingsListener());
        exit.addActionListener(new ExitListener());

        panel.add(influence);
        panel.add(startGame);
        panel.add(settings);
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
        back.addActionListener(new BackListener(0));

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

        JButton surrender = new JButton("Surrender");

        turnShower.setText((game.turn == -1 ? "Blue's" : "Red's") + " turn.");

        surrender.setForeground(Color.BLACK);
        passTurn.setForeground(Color.BLACK);

        surrender.setBackground(buttonColor);
        passTurn.setBackground(buttonColor);

        surrender.setFont(buttonFont);
        passTurn.setFont(buttonFont);

        surrender.addActionListener(new SurrenderListener());
        passTurn.addActionListener(new PassTurnListener());

        panel.add(surrender);

        game.board = new BoardButton[game.boardSize][game.boardSize];
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (i == game.boardSize - 1 && j == 0)
                    game.board[i][j] = new BoardButton(game, -1, i, j);
                else if (i == 0 && j == game.boardSize - 1)
                    game.board[i][j] = new BoardButton(game, 1, i, j);
                else
                    game.board[i][j] = new BoardButton(game, 0, i, j);
                panel.add(game.board[i][j]);
            }
        }

        panel.add(passTurn);
        panel.add(turnShower);

        game.add(panel);
        game.setVisible(true);
    }

    private class StartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            game.menuIndex = 2;
            game.start();
        }
    }

    private class SettingsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            game.menuIndex = 1;
            game.start();
        }
    }

    private class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
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
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
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
                game.getClip("Sounds\\button.wav").start();
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
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            game.menuIndex = menuChange;
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
                    if (game.sound)
                        game.getClip("Sounds\\button.wav").start();
                } else if (game.boardSize < 5) {
                    JOptionPane.showMessageDialog(game, "Minimum board size is 5!");
                    game.boardSize = 0;
                    if (game.sound)
                        game.getClip("Sounds\\button.wav").start();
                } else {
                    game.menuIndex = 3;
                    game.start();
                }
            } catch (Exception ex) {
                if (game.sound)
                    game.getClip("Sounds\\button.wav").start();
                JOptionPane.showMessageDialog(game, "Enter an integer!");
            }
        }
    }

    private class SurrenderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            JOptionPane.showMessageDialog(null, (game.turn == -1 ? "Blues" : "Reds") + " lost!");
            game.menuIndex = 0;
            game.start();
        }
    }

    private class PassTurnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            if (game.addingUnits) {
                game.turn *= -1;
                game.addingUnits = false;
            } else {
                game.unitSetCount = game.countPlayerUnits();
                game.addingUnits = true;
            }
            game.passTurnRefreshBoard();
            game.setTurnShower();
        }
    }
}
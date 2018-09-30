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

    private final Color textColor = new Color(200, 100, 0);

    MenuSettingCommands(Game game, JPanel panel) {
        this.game = game;
        this.panel = panel;
        influence = new JLabel("Influence");
        influence.setForeground(textColor);
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

    void setHowToPlayMenu() {
        game.getContentPane().removeAll();
        panel.removeAll();

        panel.setLayout(new HowToPlayLayout(game));

        JButton back = new JButton("Back");
        JLabel howTo1 = new JLabel("Your goal is to conquer all of the enemy's fields.");
        JLabel howTo2 = new JLabel("On your turn you first conquer fields, then add new units to already conquered ones.");
        JLabel howTo3 = new JLabel("On one field there are maximum 8 units.");
        JLabel howTo4 = new JLabel("You can attack/conquer only with fields, that have 2 or more units in them.");
        JLabel howTo5 = new JLabel("To choose field which you want to play from, double click on it.");
        JLabel howTo6 = new JLabel("Black fields are walls (they're set randomly).");
        JLabel howTo7 = new JLabel("When attacking enemy, there are few rules:");
        JLabel howTo8 = new JLabel("1.If defending field has equal units to attacking one, then you have 50% chance to conquer it.");
        JLabel howTo9 = new JLabel("2.If defending unit has one less unit, than attacking one, then you have 75% chance to conquer it.");
        JLabel howTo10 = new JLabel("3.If defending unit has one more unit than attacking field, then you still have 25% chance to conquer it.");
        JLabel howTo11 = new JLabel("4.If difference is more than 2, then wins field with more units.");
        JLabel howTo12 = new JLabel("");
        JLabel howTo13 = new JLabel("That's all, go and fight! :)");

        back.setForeground(Color.BLACK);
        howTo1.setForeground(textColor);
        howTo2.setForeground(textColor);
        howTo3.setForeground(textColor);
        howTo4.setForeground(textColor);
        howTo5.setForeground(textColor);
        howTo6.setForeground(textColor);
        howTo7.setForeground(textColor);
        howTo8.setForeground(textColor);
        howTo9.setForeground(textColor);
        howTo10.setForeground(textColor);
        howTo11.setForeground(textColor);
        howTo12.setForeground(textColor);
        howTo13.setForeground(textColor);

        back.setBackground(buttonColor);

        back.setFont(buttonFont);
        howTo1.setFont(buttonFont);
        howTo2.setFont(buttonFont);
        howTo3.setFont(buttonFont);
        howTo4.setFont(buttonFont);
        howTo5.setFont(buttonFont);
        howTo6.setFont(buttonFont);
        howTo7.setFont(buttonFont);
        howTo8.setFont(buttonFont);
        howTo9.setFont(buttonFont);
        howTo10.setFont(buttonFont);
        howTo11.setFont(buttonFont);
        howTo12.setFont(buttonFont);
        howTo13.setFont(buttonFont);

        back.addActionListener(new BackListener(0));

        panel.add(back);
        panel.add(howTo1);
        panel.add(howTo2);
        panel.add(howTo3);
        panel.add(howTo4);
        panel.add(howTo5);
        panel.add(howTo6);
        panel.add(howTo7);
        panel.add(howTo8);
        panel.add(howTo9);
        panel.add(howTo10);
        panel.add(howTo11);
        panel.add(howTo12);
        panel.add(howTo13);

        game.add(panel);
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

    private class HowToPlayListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.sound)
                game.getClip("Sounds\\button.wav").start();
            game.menuIndex = 4;
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
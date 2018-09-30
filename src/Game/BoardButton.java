package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BoardButton extends JButton {
    private int belongsTo;

    private Game game;

    private boolean active = false;
    private boolean waiting = false;

    private int unitCount = 0;
    private int x;
    private int y;

    BoardButton(Game Game, int BelongsTo, int X, int Y) {
        game = Game;
        belongsTo = BelongsTo;
        x = X;
        y = Y;
        if (belongsTo == 0) {
            setBackground(game.rand.nextInt(100) < 15 && canPutHere() ? game.WALL : game.EMPTY);
        } else {
            int units = 2;
            if (game.boardSize >= 8 && game.boardSize <= 12)
                units = 4;
            else if (game.boardSize >= 13 && game.boardSize <= 15)
                units = 6;

            unitCount = units;
        }
        addActionListener(new ClickListener());
        refresh();
    }

    private boolean canPutHere() {
        boolean result = true;

        if (x == 0 && y == game.boardSize - 2)
            result = false;
        else if (x == 1 && y == game.boardSize - 1)
            result = false;
        else if (x == game.boardSize - 1 && y == 1)
            result = false;
        else if (x == game.boardSize - 2 && y == 0)
            result = false;
        else if (x == 1 && y == game.boardSize - 2)
            result = false;
        else if (x == game.boardSize - 2 && y == 1)
            result = false;

        return result;
    }

    private void addUnit() {
        if (unitCount < 8)
            ++unitCount;
    }

    void refresh() {
        if (getBackground() != game.WALL) {
            if (belongsTo == -1)
                setBackground(game.P1OnePointColor);
            else if (belongsTo == 1)
                setBackground(game.P2OnePointColor);
            if (active)
                setBackground(getBackground().brighter());
            setText(unitCount == 0 ? "" : "" + unitCount);
            setForeground(Color.BLACK);
        } else
            setEnabled(false);
    }

    private boolean thereIsNoActive() {
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getActive())
                    return false;
            }
        }
        return true;
    }

    private BoardButton getActiveField() {
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getActive())
                    return game.board[i][j];
            }
        }
        return new BoardButton(game, -1, 0, 0);
    }

    private int getActiveFieldUnits() {
        return getActiveField().getUnitCount();
    }

    private void findActiveAndRemoveIt() {
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getActive())
                    game.board[i][j].setActive(false);
            }
        }
    }

    private boolean getActive() {
        return active;
    }

    private int getUnitCount() {
        return unitCount;
    }

    int getBelonging() {
        return belongsTo;
    }

    private void setUnitCount(int units) {
        if (units >= 1 && units <= 8) {
            unitCount = units;
        }
    }

    void setWaiting(boolean bool) {
        waiting = bool;
    }

    private void setBelonging(int belonging) {
        belongsTo = belonging;
    }

    void setActive(boolean bool) {
        active = bool;
        if (active)
            setSidesWaiting(true);
        else
            setSidesWaiting(false);
    }

    private void setSidesWaiting(boolean bool) {
        if (x > 0 && game.board[x - 1][y].getBackground() != game.WALL && game.board[x - 1][y].getUnitCount() <= 8)
            game.board[x - 1][y].setWaiting(bool);
        if (y > 0 && game.board[x][y - 1].getBackground() != game.WALL && game.board[x][y - 1].getUnitCount() <= 8)
            game.board[x][y - 1].setWaiting(bool);
        if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() != game.WALL && game.board[x + 1][y].getUnitCount() <= 8)
            game.board[x + 1][y].setWaiting(bool);
        if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() != game.WALL && game.board[x][y + 1].getUnitCount() <= 8)
            game.board[x][y + 1].setWaiting(bool);
    }

    private boolean hasSide() {
        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBackground() != game.WALL && game.board[x - 1][y].getBelonging() != game.turn)
            result = true;
        else if (y > 0 && game.board[x][y - 1].getBackground() != game.WALL && game.board[x][y - 1].getBelonging() != game.turn)
            result = true;
        else if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() != game.WALL && game.board[x + 1][y].getBelonging() != game.turn)
            result = true;
        else if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() != game.WALL && game.board[x][y + 1].getBelonging() != game.turn)
            result = true;

        return result;
    }

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!game.addingUnits) {
                if (belongsTo == game.turn && game.doubleClick(this) && hasSide() && unitCount > 1) {
                    if (!waiting) {
                        if (!active && thereIsNoActive()) {
                            setActive(true);
                        } else if (!active) {
                            findActiveAndRemoveIt();
                            setActive(true);
                        } else {
                            setActive(false);
                        }
                    } else {
                        if (!active) {
                            findActiveAndRemoveIt();
                            setActive(true);
                        } else
                            setActive(false);
                    }
                } else if (waiting && !thereIsNoActive() && getActiveFieldUnits() > 1) {
                    if (belongsTo == 0) {
                        setUnitCount(getActiveFieldUnits() - 1);
                        getActiveField().setUnitCount(1);
                        findActiveAndRemoveIt();
                        setActive(true);
                        setBelonging(game.turn);
                    } else if (belongsTo == game.turn * -1) {
                        int activeFieldUnits = getActiveFieldUnits();
                        int decision;

                        if (activeFieldUnits == unitCount) {
                            decision = game.rand.nextInt(2);

                            if (decision == 0) {
                                setBelonging(game.turn);
                                getActiveField().setUnitCount(1);
                                findActiveAndRemoveIt();
                                setActive(true);
                            } else {
                                getActiveField().setUnitCount(1);
                            }
                            setUnitCount(1);
                        } else if (activeFieldUnits == unitCount + 1) {
                            decision = game.rand.nextInt(4);

                            if (decision != 0) {
                                setBelonging(game.turn);
                                getActiveField().setUnitCount(1);
                                findActiveAndRemoveIt();
                                setActive(true);
                            } else {
                                getActiveField().setUnitCount(1);
                            }
                            setUnitCount(1);
                        } else if (activeFieldUnits == unitCount - 1) {
                            decision = game.rand.nextInt(4);

                            if (decision == 0) {
                                setBelonging(game.turn);
                                getActiveField().setUnitCount(1);
                                findActiveAndRemoveIt();
                                setActive(true);
                            } else {
                                getActiveField().setUnitCount(1);
                            }
                            setUnitCount(1);
                        } else if (activeFieldUnits < unitCount) {
                            getActiveField().setUnitCount(1);
                            setUnitCount(unitCount - activeFieldUnits);
                        } else {
                            setBelonging(game.turn);
                            getActiveField().setUnitCount(1);
                            findActiveAndRemoveIt();
                            setActive(true);
                            setUnitCount(activeFieldUnits - unitCount);
                        }
                    }
                }
                game.lasClickTime = System.nanoTime();
                game.lastClickedButton = this;
                if (game.checkWin()) {
                    game.refreshBoard();
                    game.menuIndex = 0;
                    JOptionPane.showMessageDialog(null, (game.turn == -1 ? "Blues" : "Reds") + " won!");
                    game.start();
                }
            } else if (game.unitSetCount > 0 && belongsTo == game.turn && unitCount < 8) {
                addUnit();
                --game.unitSetCount;
                game.setTurnShower();
            }

            game.refreshBoard();
        }
    }
}
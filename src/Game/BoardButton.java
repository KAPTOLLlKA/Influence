package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

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
        if (belongsTo == 0)
            setBackground(game.rand.nextInt(100) < 15 && canPutHere() ? game.WALL : game.EMPTY);
        else
            unitCount = 8;
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
        ++unitCount;
    }

    private boolean decreaseUnit() {
        if (unitCount > 1) {
            --unitCount;
            return true;
        }
        return false;
    }

    void refresh() {
        if (getBackground() != game.WALL) {
            if (belongsTo == -1)
                setBackground(doP1Brighter(game.P1OnePointColor, unitCount));
            else if (belongsTo == 1)
                setBackground(doP2Brighter(game.P2OnePointColor, unitCount));
            if (active)
                setBackground(getBackground().brighter());
            setText(unitCount == 0 ? "" : "" + unitCount);
            setForeground(Color.BLACK);
        } else
            setEnabled(false);
    }

    private Color doP1Brighter(Color color, int k) {
        return new Color(0, color.getGreen() + (k - 1) * 20, color.getBlue() + (k - 1) * 10);
    }

    private Color doP2Brighter(Color color, int k) {
        return new Color(color.getRed() + (k - 1) * 20, 0, 0);
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

    private boolean findActiveDecreaseHisUnitAndRefreshIt() {
        return getActiveField().decreaseUnit();
    }

    private void findActiveAndRemoveIt() {
        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getActive())
                    game.board[i][j].setActive(false);
            }
        }
    }

    private boolean hasEmptySides() {
        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBackground() == game.EMPTY)
            result = true;
        else if (y > 0 && game.board[x][y - 1].getBackground() == game.EMPTY)
            result = true;
        else if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() == game.EMPTY)
            result = true;
        else if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() == game.EMPTY)
            result = true;

        return result;
    }

    private boolean getActive() {
        return active;
    }

    private boolean getWaiting() {
        return waiting;
    }

    private int getUnitCount() {
        return unitCount;
    }

    private void setUnitCount(int units) {
        if (units >= 1) {
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

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (belongsTo == game.turn && game.doubleClick(this)) {
                if (hasEmptySides() && !waiting) {
                    if (!active && thereIsNoActive())
                        setActive(true);
                    else if (active)
                        setActive(false);
                } else if (waiting) {
                    if (!active) {
                        findActiveAndRemoveIt();
                        setActive(true);
                    } else
                        setActive(false);
                }
            } else if (waiting && !thereIsNoActive()) {
                if (belongsTo != game.turn * -1) {
                    if (findActiveDecreaseHisUnitAndRefreshIt()) {
                        addUnit();
                        setBelonging(game.turn);
                    }
                } else {
                    int activeUnits = getActiveFieldUnits();
                    int decision;

                    if (activeUnits == unitCount) {
                        decision = game.rand.nextInt(2);
                        if (decision == 0) {
                            setBelonging(game.turn);
                        }
                        getActiveField().setUnitCount(1);
                        setUnitCount(1);
                    } else if (activeUnits == unitCount + 1) {
                        decision = game.rand.nextInt(4);
                        if (decision != 0) {
                            setBelonging(game.turn);
                        }
                        getActiveField().setUnitCount(1);
                        setUnitCount(1);
                    } else if (activeUnits == unitCount - 1) {
                        decision = game.rand.nextInt(4);
                        if (decision == 0) {
                            setBelonging(game.turn);
                        }
                        getActiveField().setUnitCount(1);
                        setUnitCount(1);
                    } else if (activeUnits < unitCount) {
                        setUnitCount(unitCount - activeUnits + 1);
                        getActiveField().setUnitCount(1);
                    } else {
                        setBelonging(game.turn);
                        setUnitCount(activeUnits - unitCount);
                        getActiveField().setUnitCount(1);
                    }
                }
            }

            game.lasClickTime = System.nanoTime();
            game.lastClickedButton = this;
            game.refreshBoard();
        }
    }
}
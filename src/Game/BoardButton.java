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

    BoardButton(Game game1, int BelongsTo, int X, int Y) {
        game = game1;
        belongsTo = BelongsTo;
        x = X;
        y = Y;
        int fontSize = 30 - game.boardSize;
        setFont(new Font("SansSerif", Font.BOLD, fontSize));
        if (belongsTo == -1) {
            setBackground(game.rand.nextInt(100) < 15 && canPutHere() ? Game.WALL : Game.EMPTY);
            if (getBackground() == Game.WALL) {
                belongsTo = -2;
            }
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

        if ((x == game.boardSize - 1 && y == 1) || (x == game.boardSize - 2 && y == 0) || (x == game.boardSize - 2 && y == 1)) {
            result = false;
        } else if ((x == 0 && y == game.boardSize - 2) || (x == 1 && y == game.boardSize - 1) || (x == 1 && y == game.boardSize - 2))
            result = false;
        else if (game.p3Mode != Game.NONE && ((x == 0 && y == 1) || (x == 1 && y == 0) || (x == 1 && y == 2)))
            result = false;
        else if (game.p4Mode != Game.NONE && ((x == game.boardSize - 2 && y == game.boardSize - 1) || (x == game.boardSize - 1 && y == game.boardSize - 2) || (x == game.boardSize - 2 && y == game.boardSize - 2)))
            result = false;

        return result;
    }

    private void addUnit() {
        if (unitCount < 8)
            ++unitCount;
    }

    void refresh() {
        if (getBackground() != Game.WALL) {
            if (belongsTo == 0) {
                setBackground(Game.P1Color);
            } else if (belongsTo == 1) {
                setBackground(Game.P2Color);
            } else if (belongsTo == 2) {
                setBackground(Game.P3Color);
            } else if (belongsTo == 3) {
                setBackground(Game.P4Color);
            }
            if (active)
                setBackground(getBackground().brighter());
            setText(unitCount == 0 ? "" : "" + unitCount);
            setForeground(Color.BLACK);
        } else
            setEnabled(false);
    }

    boolean isEndangered() {
        if (unitCount == 1) {
            return false;
        }

        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBelonging() != game.turn && game.board[x - 1][y].getBelonging() >= 0) {
            result = true;
        } else if (y > 0 && game.board[x][y - 1].getBelonging() != game.turn && game.board[x][y - 1].getBelonging() >= 0) {
            result = true;
        } else if (x < game.boardSize - 1 && game.board[x + 1][y].getBelonging() != game.turn && game.board[x + 1][y].getBelonging() >= 0) {
            result = true;
        } else if (y < game.boardSize - 1 && game.board[x][y + 1].getBelonging() != game.turn && game.board[x][y + 1].getBelonging() >= 0) {
            result = true;
        }

        return result;
    }

    boolean isEndangeredAndNotDefended() {
        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBelonging() != game.turn && game.board[x - 1][y].getUnitCount() > unitCount) {
            result = true;
        } else if (y > 0 && game.board[x][y - 1].getBelonging() != game.turn && game.board[x][y - 1].getUnitCount() > unitCount) {
            result = true;
        } else if (x < game.boardSize - 1 && game.board[x + 1][y].getBelonging() != game.turn && game.board[x + 1][y].getUnitCount() > unitCount) {
            result = true;
        } else if (y < game.boardSize - 1 && game.board[x][y + 1].getBelonging() != game.turn && game.board[x][y + 1].getUnitCount() > unitCount) {
            result = true;
        }

        return result;
    }

    int getNeighbourFieldsAttack() {
        int result = 0;

        if (x > 0 && game.board[x - 1][y].getBelonging() != game.turn && game.board[x - 1][y].getUnitCount() > 1) {
            result += game.board[x - 1][y].unitCount;
        }
        if (y > 0 && game.board[x][y - 1].getBelonging() != game.turn && game.board[x][y - 1].getUnitCount() > 1) {
            result += game.board[x][y - 1].unitCount;
        }
        if (x < game.boardSize - 1 && game.board[x + 1][y].getBelonging() != game.turn && game.board[x + 1][y].getUnitCount() > 1) {
            result += game.board[x + 1][y].unitCount;
        }
        if (y < game.boardSize - 1 && game.board[x][y + 1].getBelonging() != game.turn && game.board[x][y + 1].getUnitCount() > 1) {
            result += game.board[x][y + 1].unitCount;
        }

        if (result > 7) {
            result = 7;
        }

        return result;
    }

    boolean iHaveEmptySide() {
        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBackground() != Game.EMPTY)
            result = true;
        else if (y > 0 && game.board[x][y - 1].getBackground() != Game.EMPTY)
            result = true;
        else if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() != Game.EMPTY)
            result = true;
        else if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() != Game.EMPTY)
            result = true;

        return result;
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

    int getBoardX() {
        return x;
    }

    int getBoardY() {
        return y;
    }

    boolean getActive() {
        return active;
    }

    int getUnitCount() {
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

    private void setBelonging(int belonging) {
        belongsTo = belonging;
    }

    void setWaiting(boolean bool) {
        waiting = bool;
    }

    void setActive(boolean bool) {
        active = bool;
        if (active)
            setSidesWaiting(true);
        else
            setSidesWaiting(false);
    }

    private void setSidesWaiting(boolean bool) {
        if (x > 0 && game.board[x - 1][y].getBackground() != Game.WALL && game.board[x - 1][y].getUnitCount() <= 8)
            game.board[x - 1][y].setWaiting(bool);
        if (y > 0 && game.board[x][y - 1].getBackground() != Game.WALL && game.board[x][y - 1].getUnitCount() <= 8)
            game.board[x][y - 1].setWaiting(bool);
        if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() != Game.WALL && game.board[x + 1][y].getUnitCount() <= 8)
            game.board[x + 1][y].setWaiting(bool);
        if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() != Game.WALL && game.board[x][y + 1].getUnitCount() <= 8)
            game.board[x][y + 1].setWaiting(bool);
    }

    boolean hasSide() {
        boolean result = false;

        if (x > 0 && game.board[x - 1][y].getBackground() != Game.WALL && game.board[x - 1][y].getBelonging() != game.turn) {
            result = true;
        } else if (y > 0 && game.board[x][y - 1].getBackground() != Game.WALL && game.board[x][y - 1].getBelonging() != game.turn) {
            result = true;
        } else if (x < game.boardSize - 1 && game.board[x + 1][y].getBackground() != Game.WALL && game.board[x + 1][y].getBelonging() != game.turn) {
            result = true;
        } else if (y < game.boardSize - 1 && game.board[x][y + 1].getBackground() != Game.WALL && game.board[x][y + 1].getBelonging() != game.turn) {
            result = true;
        }

        return result;
    }

    private void boardClick() {
        if (!game.addingUnits) {
            if (belongsTo == game.turn && hasSide() && unitCount > 1) {
                if (!waiting) {
                    if (!active && thereIsNoActive()) {
                        setActive(true);
                        if (game.sound && !game.isAi())
                            game.getClip("Sounds\\accept.wav").start();
                    } else if (!active) {
                        findActiveAndRemoveIt();
                        setActive(true);
                        if (game.sound && !game.isAi())
                            game.getClip("Sounds\\accept.wav").start();
                    } else {
                        setActive(false);
                    }
                } else {
                    if (!active) {
                        findActiveAndRemoveIt();
                        setActive(true);
                        if (game.sound && !game.isAi())
                            game.getClip("Sounds\\accept.wav").start();
                    } else
                        setActive(false);
                }
            } else if (waiting && !thereIsNoActive() && getActiveFieldUnits() > 1) {
                if (belongsTo == -1) {
                    setUnitCount(getActiveFieldUnits() - 1);
                    getActiveField().setUnitCount(1);
                    findActiveAndRemoveIt();
                    setActive(true);
                    setBelonging(game.turn);
                } else if (belongsTo != game.turn) {
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
            int winner = game.checkWin();
            if (winner != -1) {
                game.refreshBoard();
                game.menuIndex = 0;

                String text = null;
                if (winner == 0) {
                    text = "Blues";
                } else if (winner == 1) {
                    text = "Reds";
                } else if (winner == 2) {
                    text = "Greens";
                } else if (winner == 3) {
                    text = "Oranges";
                }

                JOptionPane.showMessageDialog(null, (text + " won!"));
                game.start();
            }
        } else if (game.unitSetCount > 0 && belongsTo == game.turn && unitCount < 8) {
            addUnit();
            if (game.sound && !game.isAi())
                game.getClip("Sounds\\add.wav").start();
            --game.unitSetCount;
            game.setTurnShower();
        }

        game.refreshBoard();
    }

    private class ClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boardClick();
        }
    }
}
package Game;

import javax.swing.*;
import java.util.ArrayList;

class AiLogic {
    private Game game;

    private ArrayList<BoardButton> playableFields = new ArrayList<>();

    AiLogic(Game game) {
        this.game = game;
    }

    void makeTurn() {
        JButton passTurnButton = game.getPassTurnButton();
        findMyFields();

        while (playableFields.size() > 0) {
            ArrayList<BoardButton> attackedFields = findUnitsUnderAttack();

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size(); ++i) {
                    if (!attackedFields.get(i).getActive())
                        attackedFields.get(i).doClick();
                    attackLowestUnitFieldOrRun(attackedFields.get(i));
                    game.passTime(150);
                }
            } else {
                BoardButton maxUnitsField = playableFields.get(0);
                for (int i = 0; i < playableFields.size(); ++i) {
                    if (maxUnitsField.getUnitCount() < playableFields.get(i).getUnitCount()) {
                        maxUnitsField = playableFields.get(i);
                    }
                }
                if (!maxUnitsField.getActive())
                    maxUnitsField.doClick();

                int side = getMostTurnsSide(maxUnitsField);
                int x = maxUnitsField.getBoardX();
                int y = maxUnitsField.getBoardY();

                if (side == 0) {
                    game.board[x][y - 1].doClick();
                } else if (side == 1) {
                    game.board[x - 1][y].doClick();
                } else if (side == 2) {
                    game.board[x][y + 1].doClick();
                } else {
                    game.board[x + 1][y].doClick();
                }
            }

            game.passTime(150);
            findMyFields();
        }

        passTurnButton.doClick();

        while (game.unitSetCount > 0) {
            ArrayList<BoardButton> attackedFields = findEndangeredUnitsThatAreNotDefended();

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size(); ++i) {
                    int add = attackedFields.get(i).getNeighbourFieldsAttack() - attackedFields.get(i).getUnitCount();

                    for (int j = 0; j < add; ++j) {
                        attackedFields.get(i).doClick();
                        game.passTime(150);
                    }
                }
            } else {
                
            }
        }

        game.passTime(150);
        passTurnButton.doClick();
    }

    private void findMyFields() {
        playableFields.clear();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].getUnitCount() > 1 && game.board[i][j].hasSide()) {
                    playableFields.add(game.board[i][j]);
                }
            }
        }
    }

    private int getMostTurnsSide(BoardButton field) {
        int result = 0;
        int maxSide;

        int x = field.getBoardX();
        int y = field.getBoardY();

        int left = 1;
        while (y > left - 1 && game.board[x][y - left].getBelonging() >= -1) {
            ++left;
        }
        maxSide = left;

        int up = 1;
        while (x > up - 1 && game.board[x - up][y].getBelonging() >= -1) {
            ++up;
        }

        if (up > maxSide) {
            maxSide = up;
            result = 1;
        }

        int right = 1;
        while (y < game.boardSize - right && game.board[x][y + right].getBelonging() == -1) {
            ++right;
        }

        if (right > maxSide) {
            maxSide = right;
            result = 2;
        }

        int down = 1;
        while (x < game.boardSize - down && game.board[x + down][y].getBelonging() >= -1) {
            ++down;
        }

        if (down > maxSide) {
            result = 3;
        }

        return result;
    }

    private ArrayList<BoardButton> findUnitsUnderAttack() {
        ArrayList<BoardButton> result = new ArrayList<>();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].isEndangered()) {
                    result.add(game.board[i][j]);
                }
            }
        }

        return result;
    }

    private ArrayList<BoardButton> findEndangeredUnitsThatAreNotDefended() {
        ArrayList<BoardButton> result = new ArrayList<>();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].isEndangeredAndNotDefended()) {
                    result.add(game.board[i][j]);
                }
            }
        }

        return result;
    }

    private void attackLowestUnitFieldOrRun(BoardButton field) {
        int x = field.getBoardX();
        int y = field.getBoardY();

        int[] nFieldsUnits = new int[4];

        if (y > 0 && game.board[x][y - 1].getBelonging() >= -1 && game.board[x][y - 1].getBelonging() != game.turn) {
            nFieldsUnits[0] = game.board[x][y - 1].getUnitCount();
        } else {
            nFieldsUnits[0] = -1;
        }
        if (x > 0 && game.board[x - 1][y].getBelonging() >= -1 && game.board[x - 1][y].getBelonging() != game.turn) {
            nFieldsUnits[1] = game.board[x - 1][y].getUnitCount();
        } else {
            nFieldsUnits[1] = -1;
        }
        if (y < game.boardSize - 1 && game.board[x][y + 1].getBelonging() >= -1 && game.board[x][y + 1].getBelonging() != game.turn) {
            nFieldsUnits[2] = game.board[x][y + 1].getUnitCount();
        } else {
            nFieldsUnits[2] = -1;
        }
        if (x < game.boardSize - 1 && game.board[x + 1][y].getBelonging() >= -1 && game.board[x + 1][y].getBelonging() != game.turn) {
            nFieldsUnits[3] = game.board[x + 1][y].getUnitCount();
        } else {
            nFieldsUnits[3] = -1;
        }

        int minUnits = 9;
        int toAttackI = 0;
        int lastZero = 0;

        for (int i = 0; i < 4; ++i) {
            if (nFieldsUnits[i] < minUnits && nFieldsUnits[i] > 0) {
                minUnits = nFieldsUnits[i];
                toAttackI = i;
            }
            if (nFieldsUnits[i] == 0) {
                lastZero = i;
            }
        }

        if (minUnits <= field.getUnitCount() + 1 || (minUnits > field.getUnitCount() + 1 && !field.iHaveEmptySide())) {
            if (toAttackI == 0) {
                game.board[x][y - 1].doClick();
            } else if (toAttackI == 1) {
                game.board[x - 1][y].doClick();
            } else if (toAttackI == 2) {
                game.board[x][y + 1].doClick();
            } else {
                game.board[x + 1][y].doClick();
            }
        } else {
            if (lastZero == 0) {
                game.board[x][y - 1].doClick();
            } else if (lastZero == 1) {
                game.board[x - 1][y].doClick();
            } else if (lastZero == 2) {
                game.board[x][y + 1].doClick();
            } else {
                game.board[x + 1][y].doClick();
            }
        }
    }
}

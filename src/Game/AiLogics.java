package Game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.function.Predicate;

class AiLogic {
    private Game game;

    private ArrayList<BoardButton> myFields = new ArrayList<>();

    private static final int DELAY = 200;

    AiLogic(Game game) {
        this.game = game;
    }

    void makeTurn() {
        JButton passTurnButton = game.getPassTurnButton();
        findMyPlayableFields();

        while (myFields.size() > 0) {
            ArrayList<BoardButton> attackedFields = findFieldsUnderAttack();

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size(); ++i) {
                    if (!attackedFields.get(i).getActive()) {
                        attackedFields.get(i).doClick();
                    }
                    attackLowestUnitFieldOrRun(attackedFields.get(i));
                    game.passTime(DELAY);
                }
            } else {
                BoardButton maxUnitsField = myFields.get(0);
                for (int i = 0; i < myFields.size(); ++i) {
                    if (maxUnitsField.getUnitCount() < myFields.get(i).getUnitCount()) {
                        maxUnitsField = myFields.get(i);
                    }
                }
                if (!maxUnitsField.getActive()) {
                    maxUnitsField.doClick();
                }

                int side = getMostTurns(maxUnitsField).side;
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

            game.passTime(DELAY);
            findMyPlayableFields();
        }

        if (!game.addingUnits) {
            passTurnButton.doClick();
        }
//There is still an infinite loop way (and check ai to ai turn pass)
        while (game.unitSetCount > 0) {
            int c = 0;
            findMyFields();
            ArrayList<BoardButton> attackedFields = findEndangeredFieldsThatAreNotDefended();

            for (int i = 0; i < myFields.size(); ++i) {
                myFields.removeIf(new Predicate<BoardButton>() {
                    @Override
                    public boolean test(BoardButton boardButton) {
                        return getMostTurns(boardButton).sideMoves <= boardButton.getUnitCount();
                    }
                });
            }

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size() && game.unitSetCount > 0; ++i) {
                    int add = attackedFields.get(i).getNeighbourFieldsAttack() - attackedFields.get(i).getUnitCount();

                    for (int j = 0; j < add && game.unitSetCount > 0; ++j) {
                        attackedFields.get(i).doClick();
                        game.passTime(DELAY);
                    }
                }
                for (int i = 0; i < attackedFields.size() && game.unitSetCount > 0; ++i) {
                    attackedFields.get(i).doClick();
                    game.passTime(DELAY);
                }

                ++c;
            }

            if (myFields.size() != 0) {
                BoardButton maxMovesButton = myFields.get(0);
                int maxMoves = getMostTurns(maxMovesButton).sideMoves;
                for (BoardButton myField : myFields) {
                    int iFieldMoves = getMostTurns(myField).sideMoves;
                    if (maxMoves < iFieldMoves) {
                        maxMovesButton = myField;
                        maxMoves = iFieldMoves;
                    }
                }

                maxMoves -= (maxMovesButton.getUnitCount() - 1);
                for (int i = 0; i < maxMoves; ++i) {
                    maxMovesButton.doClick();
                    game.passTime(DELAY);
                }

                ++c;
            }

            if (c == 0) {
                for (int i = 0; i < myFields.size() && game.unitSetCount > 0; ++i) {
                    if (myFields.get(i).hasSide()) {
                        while (myFields.get(i).getUnitCount() < 9 && game.unitSetCount > 0) {
                            myFields.get(i).doClick();
                            game.passTime(DELAY);
                        }
                    }
                }
            }

            if (c == 0 && game.unitSetCount > 0) {
                break;
            }
        }

        game.passTime(DELAY);
        if (game.addingUnits) {
            passTurnButton.doClick();
        }
    }

    private void findMyPlayableFields() {
        myFields.clear();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].getUnitCount() > 1 && game.board[i][j].hasSide()) {
                    myFields.add(game.board[i][j]);
                }
            }
        }
    }

    private void findMyFields() {
        myFields.clear();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn) {
                    myFields.add(game.board[i][j]);
                }
            }
        }
    }

    private Pair getMostTurns(BoardButton field) {
        int side = 0;
        int maxSideMoves;

        int x = field.getBoardX();
        int y = field.getBoardY();

        int left = 1;
        while (y > left - 1 && game.board[x][y - left].getBelonging() == -1) {
            ++left;
        }
        --left;
        maxSideMoves = left;

        int up = 1;
        while (x > up - 1 && game.board[x - up][y].getBelonging() == -1) {
            ++up;
        }
        --up;

        if (up > maxSideMoves) {
            maxSideMoves = up;
            side = 1;
        }

        int right = 1;
        while (y < game.boardSize - right && game.board[x][y + right].getBelonging() == -1) {
            ++right;
        }
        --right;

        if (right > maxSideMoves) {
            maxSideMoves = right;
            side = 2;
        }

        int down = 1;
        while (x < game.boardSize - down && game.board[x + down][y].getBelonging() == -1) {
            ++down;
        }
        --down;

        if (down > maxSideMoves) {
            maxSideMoves = down;
            side = 3;
        }

        return new Pair(side, maxSideMoves);
    }

    private ArrayList<BoardButton> findFieldsUnderAttack() {
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

    private ArrayList<BoardButton> findEndangeredFieldsThatAreNotDefended() {
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

    private ArrayList<BoardButton> findFieldsThatHaveAttackEqualToNeighbours() {
        ArrayList<BoardButton> result = new ArrayList<>();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].isEndangeredAndDefended()) {
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

    private class Pair {
        int side;
        int sideMoves;

        Pair(int side, int sideMoves) {
            this.side = side;
            this.sideMoves = sideMoves;
        }
    }
}

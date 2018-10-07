package Game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.function.Predicate;

class AiLogic {
    private Game game;

    private ArrayList<BoardButton> playableFields = new ArrayList<>();

    private static final int DELAY = 2000;

    AiLogic(Game game) {
        this.game = game;
    }
//Fix bugs
    void makeTurn() {
        JButton passTurnButton = game.getPassTurnButton();
        findMyPlayableFields();

        while (playableFields.size() > 0) {
            ArrayList<BoardButton> attackedFields = findFieldsUnderAttack();

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size(); ++i) {
                    if (!attackedFields.get(i).getActive())
                        attackedFields.get(i).doClick();
                    attackLowestUnitFieldOrRun(attackedFields.get(i));
                    game.passTime(DELAY);
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
        int firstlyAttackedFieldsCount = 0;
        boolean firstEnter = true;
        boolean addRemainings = false;
        while (game.unitSetCount > 0) {
            int c = 0;
            findMyFields();
            ArrayList<BoardButton> attackedFields = findEndangeredFieldsThatAreNotDefended();
            ArrayList<BoardButton> defendedFields = findFieldsThatHaveAttackEqualToNeighbours();
            if (firstEnter) {
                firstEnter = false;
                firstlyAttackedFieldsCount = attackedFields.size();
            }

            for (int i = 0; i < playableFields.size(); ++i) {
                playableFields.removeIf(new Predicate<BoardButton>() {
                    @Override
                    public boolean test(BoardButton boardButton) {
                        return getMostTurns(boardButton).sideMoves <= boardButton.getUnitCount();
                    }
                });
            }

            if (attackedFields.size() != 0) {
                for (int i = 0; i < attackedFields.size(); ++i) {
                    int add = attackedFields.get(i).getNeighbourFieldsAttack() - attackedFields.get(i).getUnitCount();

                    for (int j = 0; j < add; ++j) {
                        attackedFields.get(i).doClick();
                        game.passTime(DELAY);
                    }
                }
                ++c;
            } else if (defendedFields.size() == firstlyAttackedFieldsCount && !addRemainings) {
                addRemainings = true;
                for (int i = 0; i < defendedFields.size(); ++i) {
                    defendedFields.get(i).doClick();
                    game.passTime(DELAY);
                    defendedFields.get(i).doClick();
                    game.passTime(DELAY);
                }
                ++c;
            } else if (playableFields.size() != 0){
                BoardButton maxMovesButton = playableFields.get(0);
                int maxMoves = getMostTurns(maxMovesButton).sideMoves;
                for (int i = 0; i < playableFields.size(); ++i) {
                    int iFieldMoves = getMostTurns(playableFields.get(i)).sideMoves;
                    if (maxMoves < iFieldMoves) {
                        maxMovesButton = playableFields.get(i);
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
                break;
            }
        }

        game.passTime(DELAY);
        if (game.addingUnits) {
            passTurnButton.doClick();
        }
    }

    private void findMyPlayableFields() {
        playableFields.clear();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].getUnitCount() > 1 && game.board[i][j].hasSide()) {
                    playableFields.add(game.board[i][j]);
                }
            }
        }
    }

    private void findMyFields() {
        playableFields.clear();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn) {
                    playableFields.add(game.board[i][j]);
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

    private ArrayList<BoardButton> findFieldsThatHaveEnemyNeighbours() {
        ArrayList<BoardButton> result = new ArrayList<>();

        for (int i = 0; i < game.boardSize; ++i) {
            for (int j = 0; j < game.boardSize; ++j) {
                if (game.board[i][j].getBelonging() == game.turn && game.board[i][j].hasEnemyNeighbours()) {
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

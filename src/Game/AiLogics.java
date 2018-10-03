package Game;

import javax.swing.*;
import java.util.ArrayList;

class AiLogic {
    private Game game;

    private JButton passTurnButton;

    private ArrayList<BoardButton> playableFields = new ArrayList<>();

    AiLogic(Game game) {
        this.game = game;
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

    void makeTurn() {
        passTurnButton = game.getPassTurnButton();
        findMyFields();

        //while (playableFields.size() > 0) {
            ArrayList<BoardButton> attackedFields = findUnitsUnderAttack();
            int fieldsQuantity = playableFields.size();
            int attackedFieldsQuantity = attackedFields.size();

            if (attackedFieldsQuantity != 0) {
                for (int i = 0; i < attackedFieldsQuantity; ++i) {
                    attackedFields.get(i).doClick();
                    float start = System.nanoTime();
                    while (System.nanoTime() - start < 200000000);
                    attackedFields.get(i).doClick();
                    attackLowestUnitFieldOrRun(attackedFields.get(i));
                }
            } else {

            }

            //findMyFields();
            passTurnButton.doClick();
            float start = System.nanoTime();
            while (System.nanoTime() - start < 100000000);
            passTurnButton.doClick();
        //}

        playableFields.clear();
    }

    private ArrayList<BoardButton> findUnitsUnderAttack() {
        ArrayList<BoardButton> result = new ArrayList<>();

        for (int i = 0; i < playableFields.size(); ++i) {
            if (playableFields.get(i).isEndangered()) {
                result.add(playableFields.get(i));
            }
        }

        return result;
    }

    private void attackLowestUnitFieldOrRun(BoardButton field) {
        int x = field.getBoardX();
        int y = field.getBoardY();

        int[] nFieldsUnits = new int[4];

        if (y > 0 && game.board[x][y - 1].getBelonging() >= 0 && game.board[x][y - 1].getBelonging() != game.turn) {
            nFieldsUnits[0] = game.board[x][y - 1].getUnitCount();
        } else {
            nFieldsUnits[0] = -1;
        }
        if (x > 0 && game.board[x - 1][y].getBelonging() >= -1 && game.board[x - 1][y].getBelonging() != game.turn) {
            nFieldsUnits[1] = game.board[x - 1][y].getUnitCount();
        } else {
            nFieldsUnits[1] = -1;
        }
        if (y < game.boardSize - 1 && game.board[x][y + 1].getBelonging() >= 0 && game.board[x][y + 1].getBelonging() != game.turn) {
            nFieldsUnits[2] = game.board[x][y + 1].getUnitCount();
        } else {
            nFieldsUnits[2] = -1;
        }
        if (x < game.boardSize - 1 && game.board[x + 1][y].getBelonging() >= 0 && game.board[x + 1][y].getBelonging() != game.turn) {
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

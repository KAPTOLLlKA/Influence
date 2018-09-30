package Game;

import java.awt.*;
import java.util.ArrayList;

class AiLogics {
    private Game game;

    private ArrayList<Dimension> fields = new ArrayList<>();

    AiLogics(Game game) {
        this.game = game;
    }

    void addField(int x, int y) {
        Dimension newField = new Dimension(x, y);
        if (!fields.contains(newField))
            fields.add(newField);
    }

    void deleteField(int x, int y) {
        fields.remove(new Dimension(x, y));
    }

    void makeTurn() {

    }
}

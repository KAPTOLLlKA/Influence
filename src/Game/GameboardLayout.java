package Game;

import java.awt.*;

class GameboardLayout implements LayoutManager {
    private Game game;

    GameboardLayout(Game game) {
        this.game = game;
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container container) {
        Component list[] = container.getComponents();
        int buttonSize = (game.getHeight() - 50) / game.boardSize;
        int firstX = (game.getWidth() - buttonSize * game.boardSize) / 2;
        int x = firstX;
        int y = 10;
        list[0].setBounds(0, 0, 150, 50);
        for (int i = 1; i < list.length - 1; ++i) {
            list[i].setBounds(x, y, buttonSize, buttonSize);
            x += buttonSize;
            if (i % game.boardSize == 0) {
                x = firstX;
                y += buttonSize;
            }
        }
        list[list.length - 2].setBounds(game.getWidth() - 165, 0, 150, 50);
        list[list.length - 1].setBounds(game.getWidth() - 165, 100, 150, 50);
    }
}

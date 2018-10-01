package Game;

import java.awt.*;

class CentralPlayerModeLayout implements LayoutManager {
    Game game;

    CentralPlayerModeLayout(Game game) {
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
    public void layoutContainer(Container parent) {
        Component list[] = parent.getComponents();
        int x = (game.getWidth() - 300) / 2;
        int y = (game.getHeight() - (list.length * 110)) / 2 - 10;
        for (int i = 0; i < list.length; ++i) {
            list[i].setBounds(x, y, 300, 100);
            y += 110;
        }
    }
}

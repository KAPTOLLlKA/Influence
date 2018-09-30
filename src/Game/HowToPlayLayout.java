package Game;

import java.awt.*;

class HowToPlayLayout implements LayoutManager {
    private Game game;

    HowToPlayLayout(Game game) {
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
        Component[] list = parent.getComponents();

        list[0].setBounds(0, 0, 150, 50);
        int y = 60;
        int width = game.getWidth();
        int height = (game.getHeight() - 60) / (list.length - 1);
        for (int i = 1; i < list.length; ++i) {
            list[i].setBounds(10, y, width, height);
            y += height - 10;
        }
    }
}

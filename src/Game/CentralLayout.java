package Game;

import java.awt.*;

class CentralLayout implements LayoutManager {
    private Game game;

    CentralLayout(Game game) {
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
        int x = (game.getWidth() - 300) / 2;
        int y = (game.getHeight() - (list.length - 2) * 110) / 2;
        list[0].setBounds(x + 35, 0, 500, 200);
        for (int i = 1; i < list.length; ++i) {
            list[i].setBounds(x, y, 300, 100);
            y += 110;
        }
    }
}

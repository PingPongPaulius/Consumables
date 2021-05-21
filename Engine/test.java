package Engine;

import Engine.Tokens.Items.Item;
import Engine.UI.Menu.Menu;
import Engine.UI.Menu.MenuPanel;

import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        new Window(new MenuPanel(new Menu()), 10, new ArrayList<Item>());
    }

}

package Engine.UI;

import Engine.Tokens.Player;

import java.awt.*;

public class HealthBar {

    final int X;
    final int Y;
    final int RED_WIDTH;
    final int HEIGHT;
    int greenWidth;
    Player player;

    public HealthBar(Player player){
        this.X = 450;
        this.Y = 30;
        this.RED_WIDTH = 100;
        this.greenWidth = 100;
        this.HEIGHT = 50;
        this.player = player;
    }

    void updateHP(){
        greenWidth = (int) (player.getMAX_HEALTH() - (player.getMAX_HEALTH() - player.HP()));
    }

    public void render(Graphics g){
        updateHP();

        g.drawString(greenWidth + " / " + RED_WIDTH, X+25, Y-5);

        g.setColor(new Color(0xC90606));
        g.fillRect(X, Y, RED_WIDTH, HEIGHT);
        g.setColor(new Color(0x0FC71C));
        g.fillRect(X, Y, greenWidth, HEIGHT);
    }


}

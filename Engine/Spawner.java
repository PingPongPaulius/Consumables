package Engine;

import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public class Spawner extends Token implements Runnable{

    Window game;
    boolean draw;
    final Image src = ImageLoader.getImage("src/Art/House.png");

    public Spawner(Window game){
        this.x = 300;
        this.y = 628;
        this.game = game;
        this.draw = false;
    }

    @Override
    public void run() {
        this.countdown();
    }

    public void countdown(){
        draw = true;
        for(int s = 0; s < 5; s++){
            //System.out.println(s+1);
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e){
                // Rip
            }
        }
        game.initialiseLevel();
        draw = false;
    }

    public boolean running(){
        return draw;
    }

    public boolean exit(Player p){
        return p.x + p.width > this.x && p.x <= this.x + 300 && p.y <= this.y + 200 && p.y + p.height > this.y;
    }

    @Override
    public void render(Graphics g) {
        if(draw) g.drawImage(src,(int) x, (int) y, 300, 200, null);
    }
}

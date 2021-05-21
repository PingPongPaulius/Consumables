package Engine.Tokens.Projectiles;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public class Zone extends Token {

    Image[] sprites = new Image[]{
            ImageLoader.getImage("src/Art/TreeMan/Zone.png")
    };

    int damage;
    GraphicRenderer renderer;

    int dealDamage = 5;

    public Zone(float x, float y, int damage, GraphicRenderer renderer){
        this.width = 64;
        this.height = 20;
        this.x = x-width/2;
        this.y = y+10;
        this.damage = damage;
        this.renderer = renderer;
        renderer.add(this);
    }

    @Override
    public void render(Graphics g) {

        for(Token t: renderer.list()){
            if(t instanceof Player && dealDamage >= 5){
                Player p = (Player) t;
                if(p.collidesWith(this)) {
                    p.takeDamage(damage);
                    dealDamage = 0;
                }
            }
        }

        float add = (float) (10f * FPS.getDeltaTime());

        dealDamage += add;
        height+= add;
        this.y-= add;

        if (height >= 40) renderer.remove(this);

        g.drawImage(sprites[0], (int) x,(int) y,(int) width,(int) height,null);
    }
}

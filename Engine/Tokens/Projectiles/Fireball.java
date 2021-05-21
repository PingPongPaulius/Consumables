package Engine.Tokens.Projectiles;


import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Enemy;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public class Fireball extends Projectile{

    int damage = 10;
    GraphicRenderer renderer;
    boolean goingRight;

    Image[] sprites = new Image[]{ImageLoader.getImage("src/Art/Items/fireball/1.png"),
                                    ImageLoader.getImage("src/Art/Items/fireball/icon.png")};

    int index = 0;

    private float ticks = 1000;

    public Fireball(Player p, GraphicRenderer renderer, boolean goingRight){
        this.speed = 140f;
        if(goingRight) this.x = p.x + p.width;
        else this.x = p.x;
        this.y = p.y + p.height/4;
        this.height = 32;
        this.width = 32;
        this.renderer = renderer;
        renderer.add(this);
        this.goingRight = goingRight;
    }

    public boolean colliding(Token t){
        return x + width > t.x &&
                t.x + t.width > x &&
                t.y + t.height > y &&
                y + width > t.y;
    }

    public void destroy(){
        renderer.remove(this);
    }

    @Override
    public void render(Graphics g) {

        for(Token t: renderer.list()){
            if(t.equals(this)) continue;
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                if(colliding(platform)){
                    destroy();
                }
            }
            if(t instanceof Enemy){
                if(colliding(t)) {
                    Enemy enemy = (Enemy) t;
                    enemy.receiveDamage(damage);
                    destroy();
                }
            }
        }

        if(ticks <= 0) destroy();

        if(index == 0) index = 1;
        else index = 0;

        Image src = sprites[index];
        if(goingRight){
            this.x+= speed * FPS.getDeltaTime();
        }
        else{
            this.x-= speed * FPS.getDeltaTime();
        }
        ticks -= speed * FPS.getDeltaTime();

        g.drawImage(src, (int) x, (int) y, (int)width, (int)height, null);


    }
}

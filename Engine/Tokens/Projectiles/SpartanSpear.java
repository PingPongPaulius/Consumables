package Engine.Tokens.Projectiles;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public class SpartanSpear extends Projectile{

    float distance = 0;

    float damage;

    boolean right;
    boolean destroyed = false;

    GraphicRenderer renderer;

    Image[] sprites = new Image[]{
            ImageLoader.getImage("src/Art/Enemy/Spartan/RSpear.png"),
            ImageLoader.getImage("src/Art/Enemy/Spartan/spear.png")

    };

    public SpartanSpear(float x, float y, boolean goingRight, GraphicRenderer r){
        this.x = x;
        this.y = y+8;
        this.width = 64;
        this.height = 10;
        this.speed = 100f;
        this.damage = 10;
        this.right = goingRight;
        renderer = r;
        renderer.add(this);
    }

    public void destroy(){
        renderer.remove(this);
        destroyed = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    @Override
    public void render(Graphics g) {

        float tickDist = (float) (speed * FPS.getDeltaTime());

        Image img;

        if(right){
            this.x += tickDist;
            img = sprites[0];
        }
        else{
            this.x -= tickDist;
            img = sprites[1];
        }

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform p = (Platform) t;
                if(this.collidesWith(p))
                    destroy();
            }
            if(t instanceof Player){
                Player p = (Player) t;
                if(this.collidesWith(p)) {
                    p.takeDamage(damage);
                    destroy();
                }
            }
        }

        distance += tickDist;

        g.drawImage(img, (int) x, (int) y, (int) width, (int) height, null);

        if(distance >= 3000) destroy();

    }
}

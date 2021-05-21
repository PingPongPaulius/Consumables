package Engine.Tokens.Projectiles;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.Shield;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public class Sapling extends Projectile{

    float toX;

    boolean right;

    int maxHeight = 90;

    GraphicRenderer renderer;

    Image sprite = ImageLoader.getImage("src/Art/TreeMan/sapling.png");

    public Sapling(float fromX, float fromY, float toX, GraphicRenderer renderer){
        this.speed = 300f;
        this.width = 16;
        this.height = 16;
        if(fromX > toX) right = false;
        else right = true;
        if(right){
            this.x = fromX + 128;
            this.toX += 50;
        }
        else {
            speed *= -1;
            this.x = fromX;
        }
        this.y = fromY;
        renderer.add(this);
        this.renderer = renderer;
    }


    @Override
    public void render(Graphics g) {
        float fallVelocity;
        if(maxHeight > 0){
            fallVelocity = -Physics.getGravity();
            maxHeight -= FPS.getDeltaTime();
        }
        else fallVelocity = Physics.getGravity();

        this.x += speed * FPS.getDeltaTime();
        this.y += fallVelocity*FPS.getDeltaTime();

        g.drawImage(sprite, (int)x, (int)y, (int)width, (int) height, null);

        for(Token t: renderer.list()){
            if(t.collidesWith(this)){
                if( t instanceof Platform) {
                    renderer.remove(this);
                    new Zone(x, y, 5, renderer);
                }
                if(t instanceof Player){
                    Player p = (Player) t;
                    p.takeDamage(1);
                    renderer.remove(this);
                }
            }
            if(t instanceof Shield){
                Shield shield = (Shield) t;
                if(shield.collision(this)) renderer.remove(this);
            }
        }
    }
}

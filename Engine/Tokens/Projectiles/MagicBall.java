package Engine.Tokens.Projectiles;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;

import java.awt.*;

public class MagicBall extends Projectile{

    int i = 1;

    GraphicRenderer renderer;

    public MagicBall(float x, float y, boolean goingRight, GraphicRenderer renderer){

        int speedModifier;

        if(goingRight) speedModifier = 1;
        else speedModifier = -1;
        if(goingRight) x += 32f;
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 32;
        this.speed = 280f * speedModifier;
        this.renderer = renderer;
    }

    public boolean isOutOfBounds(){
        return this.x < 0 || this.y < 0 || this.x > 1000 || this.y > 1000;
    }

    @Override
    public void render(Graphics g) {
        if(i == 1) {
            g.drawImage(ImageLoader.getImage("src/Art/MagicBall.png"), (int) x, (int) y, (int) width, (int) height, null);
            i=0;
        }
        else {
            g.drawImage(ImageLoader.getImage("src/Art/MagicBall2.png"), (int) x, (int) y, (int) width, (int) height, null);
            i=1;
        }
        if(isOutOfBounds())
            this.renderer.remove(this);

        this.x += speed * FPS.getDeltaTime();
    }

    public void changeSpeed(){

        float distance = 500 - this.x;
        this.x = 500;
        this.x += distance;

        speed *= -1;
    }
}

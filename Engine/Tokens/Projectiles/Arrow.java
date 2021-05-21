package Engine.Tokens.Projectiles;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Enemy;
import Engine.Tokens.Platform;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Arrow extends Projectile{

    Image sprite;

    GraphicRenderer renderer;

    float angle;

    float launchAngle;
    float verticalVelocity;

    public Arrow(float x, float y, boolean right, GraphicRenderer renderer){

        this.x = x;
        this.y = y-32;
        this.width = 64;
        this.height = 64;

        this.sprite = ImageLoader.getImage("src/Art/Items/Bow/arrow.png");

        if(right){
            speed = 100;
            launchAngle = 315;
        }
        else{
            speed = -100;
            launchAngle = 235;
        }

        verticalVelocity = 270;

        this.renderer = renderer;
        this.renderer.add(this);

        angle = launchAngle;
    }

    @Override
    public void render(Graphics g) {

        for(Token t: renderer.list()){
            if(Token.collides(t.x, t.y, t.width, t.height, x, y+29, width, height-29)){
                if(t instanceof Platform) renderer.remove(this);
                if(t instanceof Enemy) {
                    Enemy e = (Enemy) t;
                    e.receiveDamage(50);
                    renderer.remove(this);
                }
            }
        }


        Graphics2D g2d = (Graphics2D) g;

        x+= FPS.getDeltaTime() * speed;
        verticalVelocity = (float) (verticalVelocity - Physics.getGravity() * FPS.getDeltaTime());
        y-= verticalVelocity * FPS.getDeltaTime();

        double rotationFactor = ((verticalVelocity - Physics.getGravity() * FPS.getDeltaTime()) * FPS.getDeltaTime()) / 6;
        if(speed > 0)
            g2d.drawImage(this.sprite, rotate(Math.abs(rotationFactor)),null);
        else
            g2d.drawImage(this.sprite, rotate(-Math.abs(rotationFactor)),null);
    }

    AffineTransform rotate(double rotationFactor){
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        angle+=rotationFactor;
        at.rotate(Math.toRadians(angle), width/2, height/2);
        at.scale(2,2);

        return at;
    }

}

package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Projectiles.Fireball;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class FireBall extends Item{

    Player player;
    GraphicRenderer renderer;
    boolean rotate;
    boolean used;
    float dropHeight = 100;
    float angle = 0;

    private int animationLength;

    public FireBall(float x, float y){
        defaultVals(x, y);
    }

    public FireBall(float x, float y, Player player, GraphicRenderer renderer){
        defaultVals(x, y);
        add(player, renderer);
    }

    private void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0004";
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/fireball/icon.png");
        animationLength = 500;
    }

    @Override
    public int getDefaultPrice() {
        return 60;
    }

    @Override
    public void consume() {
        isConsumed = true;
    }

    @Override
    public void dropAnimation(GraphicRenderer renderer) {
        this.renderer = renderer;
        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                setOnGround(platform);              // if colliding with platform aka standing on ground
                if(isOnGround) return;               // if is standing on some ground then end the loop, this may break horizontal collider so just make another for loop then
            }
        }
        if(dropHeight <= 0)
            this.y += 0.5f;
        else{
            this.y -= 0.5f;
            this.dropHeight -= 0.5f;
        }
        rotate = true;
    }

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public String stringify() {
        return this.ID;
    }

    @Override
    public void add(Player p, GraphicRenderer r) {
        this.player = p;
        this.renderer = r;
    }

    @Override
    public void render(Graphics g) {

        rotate = false;

        if(isConsumed && animationLength >=0){

            animationLength--;
            if(animationLength <= 0){
                boolean direction = false;
                if(player.isFacingRight()) direction = true;
                new Fireball(player, renderer, direction);
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 500;
            }
        }
        else if(!isPicked){

            dropAnimation(renderer);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            if(rotate) {
                at.rotate(Math.toDegrees(angle++), width/2, height/2);
            }
            g2d.drawImage(this.icon, at, null);
        }
    }
}

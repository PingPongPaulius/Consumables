package Engine.Tokens.Items;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Projectiles.MagicBall;
import Engine.Tokens.Projectiles.Projectile;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Shield extends Item {

    private boolean used;
    private boolean rotate;
    private int animationLength;
    private int dropHeight;
    private Image[] srcs;
    private int angle;
    Player p;
    GraphicRenderer r;
    private boolean isActive;

    private float animationX;
    private float animationY;
    private float animationWidth;
    private float animationHeight;


    public Shield(float x, float y){
        defaultVals(x, y);
    }

    public Shield(float x, float y, GraphicRenderer r, Player p){
        defaultVals(x, y);
        add(p, r);
    }

    private void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0003";
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/Sheild/icon.png");
        animationLength = 1000;
        dropHeight = 30;
        srcs = new Image[]{
                ImageLoader.getImage("src/Art/Items/Sheild/1.png")
        };
        angle = 0;
        isActive = true;
    }

    @Override
    public int getDefaultPrice() {
        return 100;
    }

    @Override
    public void consume() {
        isConsumed = true;
        animationInit();
    }

    @Override
    public void dropAnimation(GraphicRenderer renderer) {
        this.r = renderer;
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
        this.p = p;
        this.r = r;
    }

    public void animationInit(){
        this.animationX = p.x - 20;
        this.animationY = p.y + p.height;
        this.animationWidth = p.width + 40;
        this.animationHeight = p.height + 10;
    }

    public boolean collision(Projectile magicBall){
        return magicBall.x + magicBall.width > animationX &&
                animationX + animationWidth > magicBall.x &&
                animationY + animationHeight > magicBall.y &&
                magicBall.y + magicBall.width > animationY && isActive;
    }

    @Override
    public void render(Graphics g) {

        rotate = false;

        if(isConsumed && animationLength >= 0){
            Image src = srcs[0];
            isActive = true;
            g.drawImage(src, (int)animationX, (int)animationY, (int)animationWidth, (int)animationHeight, null);

            if(animationY <=  p.y + 10) animationLength--;
            else animationY -= 100F * FPS.getDeltaTime();

            if(animationLength <= 0){       // after everything ends set these values to default otherwise the second item will not fire
                p.endUse();
                this.isConsumed = false;
                this.animationLength = 200;
                isActive = false;          // removes the invisible shield
            }

        }
        else if(!isPicked){
            dropAnimation(r);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            if(rotate) {
                at.rotate(Math.toDegrees(angle++), width/2, height/2);
            }
            g2d.drawImage(this.icon, at, null);
        }

    }
}

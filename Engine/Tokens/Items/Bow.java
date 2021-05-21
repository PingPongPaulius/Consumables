package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Projectiles.Arrow;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Bow extends Item{

    boolean used;
    boolean rotate;
    int animationLength;
    int dropHeight;
    Image[] srcs;
    double angle;

    int animationHeight;
    int animationWidth;
    int animationX;
    int animationY;

    GraphicRenderer renderer;
    Player player;

    public Bow(float x, float y){
        defaultVals(x, y);
    }

    public Bow(float x, float y, GraphicRenderer r, Player p){
        defaultVals(x, y);
        add(p, r);
    }

    void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0007"; //<-- enter value here
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/Bow/icon.png"); //<--- add any path here
        animationLength = 1000; //change how long your aniation should be
        dropHeight = 30; //<--- how high item shoots before falling, this happens during the drop
        srcs = ImageLoader.loadSprites("src/Art/Items/Bow/SpriteSheetBow.png", 32, 32, 5);
        angle = 0;
    }


    @Override
    public int getDefaultPrice() {
        return 90;
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
        return ID;
    }

    @Override
    public void add(Player p, GraphicRenderer r) {
        this.player = p;
        this.renderer = r;
    }

    public void animationInit(){
        animationHeight = 64;
        animationWidth = 64;
        animationX = (int) (player.x + player.width/4);
        animationY = (int) (player.y + player.height/4);
    }

    @Override
    public void render(Graphics g) {

        //starting the animation

        rotate = false;
        animationInit();

        // animation that happens when user presses the use button

        if(isConsumed && animationLength >=0){

            Image src = Physics.getImage(srcs, animationLength, 100);

            animationInit();

            Graphics2D g2d = (Graphics2D) g;

            //g.drawImage(src, animationX, animationY, animationWidth, animationHeight, null);
            g2d.drawImage(src, rotate(), null );
            animationLength--;
            if(animationLength <= 0){       // after everything ends set these values to default otherwise the second item will not fire
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 200;
                new Arrow(animationX, animationY, !player.isFacingRight(),renderer);
            }
        }
        //if item isn't picked usually stays the same
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

    AffineTransform rotate(){
        AffineTransform at = AffineTransform.getTranslateInstance(animationX, animationY);
        if(player.isFacingLeft())
            at.rotate(Math.toRadians(315), width/2, height/2);
        else
            at.rotate(Math.toRadians(235), width/2, height/2);
        at.scale(2,2);

        return at;
    }

}

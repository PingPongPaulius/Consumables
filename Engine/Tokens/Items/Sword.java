package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Enemy;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Sword extends  Item{
    boolean used;
    boolean rotate;
    int animationLength;
    int dropHeight;
    Image[][] srcs = new Image[2][3];
    double angle;

    int animationHeight;
    int animationWidth;
    int animationX;
    int animationY;

    GraphicRenderer renderer;
    Player player;

    public void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0010"; //<-- enter value here
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/Sword/icon.png"); //<--- add any path here
        animationLength = 300; //change how long your aniation should be
        dropHeight = 100; //<--- how high item shoots before falling, this happens during the drop
        srcs[0] = ImageLoader.loadSprites("src/Art/Items/Sword/sheet.png", 32, 32, 3);
        for(int i = 0; i < 3; i++){
            srcs[1][i] = ImageLoader.flipHorizontally((BufferedImage) srcs[0][i]);
        }
        angle = 0;
    }

    public Sword(float x, float y){
        defaultVals(x, y);
    }

    public Sword(float x, float y, Player player, GraphicRenderer renderer){
        defaultVals(x, y);
        add(player, renderer);
    }

    @Override
    public int getDefaultPrice() {
        return 10;
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
        if(player.isFacingRight()) {
            animationX = (int) (player.x-player.width/4);
        }
        else{
            animationX = (int) (player.x + player.width/4);
        }
        animationY = (int) (player.y+height/2);
    }

    @Override
    public void render(Graphics g) {
        rotate = false;
        animationInit();

        // animation that happens when user presses the use button

        if(isConsumed && animationLength >=0){
            int side = 0;
            if(player.isFacingRight()) side = 1;

            Image src = Physics.getImage(srcs[side], animationLength, 100);   //<-- requires a method that has every image and then sets the animation image to display

            animationInit();
            g.drawImage(src,animationX, animationY, animationWidth, animationHeight, null);
            animationLength--;
            if(animationLength <= 0){       // after everything ends set these values to default otherwise the second item will not fire
                for(Token t: renderer.list()){
                    if(Token.collides(animationX, animationY, animationWidth, animationHeight, t.x, t.y, t.width, t.height) && t instanceof Enemy){
                        Enemy e = (Enemy) t;
                        e.receiveDamage(20);
                    }
                }
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 300;
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
}

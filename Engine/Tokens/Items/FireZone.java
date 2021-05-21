package Engine.Tokens.Items;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Enemy;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class FireZone extends Item{

    public Player player;
    GraphicRenderer renderer;
    boolean rotate;
    boolean used;
    int animationLength;
    Image[] srcs;
    int dropHeight = 100;

    int animationHeight;
    int animationWidth;
    int animationX;
    int animationY;

    int angle = 0;

    public FireZone(float x, float y){
        defaultVals(x, y);
    }

    public FireZone(float x, float y, Player player, GraphicRenderer renderer){
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
        this.ID = "0006";
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/FireZone/Icon.png");
        animationLength = 1000;
        srcs = new Image[]{ImageLoader.getImage("src/Art/Items/FireZone/0.png"),
                ImageLoader.getImage("src/Art/Items/FireZone/1.png"),
                ImageLoader.getImage("src/Art/Items/FireZone/2.png"),
                ImageLoader.getImage("src/Art/Items/FireZone/3.png"),
                 ImageLoader.getImage("src/Art/Items/FireZone/4.png")};
    }

    @Override
    public int getDefaultPrice() {
        return 500;
    }

    @Override
    public void consume() {
        isConsumed = true;
        animationInit();
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
        animationHeight = 32;
        animationWidth = 100;
        int side = (int) (200 + player.width);
        if(player.isFacingLeft()) side = - 200;
        animationX = (int) (player.x + side);
        animationY = (int) (player.y + (player.height - animationHeight));
    }

    @Override
    public void render(Graphics g) {
        //starting the animation

        rotate = false;

        // animation that happens when user presses the use button

        if(isConsumed && animationLength >=0){

            Image src = getSrc();
            g.drawImage(src,(int)animationX, (int) animationY, (int)animationWidth, (int)animationHeight, null);
            animationLength-= FPS.getDeltaTime();

            for(Token t: renderer.list()){
                if(t instanceof Enemy){
                    Enemy enemy = (Enemy) t;
                    if(Token.collides(enemy.x, enemy.y, enemy.width, enemy.height, animationX, animationY, animationWidth, animationHeight)){
                        enemy.receiveDamage(5);
                    }
                }
            }

            if(animationLength <= 0){       // after everything ends set these values to default otherwise the second item will not fire
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 1000;
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

    public Image getSrc(){
        if(animationLength > 800) return srcs[0];
        if(animationLength > 600) return srcs[1];
        if(animationLength > 400) return srcs[2];
        if(animationLength > 200) return srcs[3];
        return srcs[4];
    }
}

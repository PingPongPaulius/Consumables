package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class JumpBottle extends Item{

    Player p;
    GraphicRenderer renderer;
    boolean used;
    boolean rotate;
    int animationLength;
    Image[] srcs;
    float dropHeight;
    float animationHeight;
    float animationWidth;
    float animationX;
    float animationY;
    int angle;


    public JumpBottle(float x, float y){
        defaultVals(x, y);
    }

    public JumpBottle(float x, float y, Player p, GraphicRenderer r){
        defaultVals(x, y);
        this.p = p;
        renderer = r;
    }

    private void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0002";
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/Jump/icon.png");
        animationLength = 200;
        dropHeight = 10;
        srcs = new Image[]{ImageLoader.getImage("src/Art/Items/Jump/1.png"),
                            ImageLoader.getImage("src/Art/Items/Jump/2.png")};
        angle = 0;
    }

    @Override
    public int getDefaultPrice() {
        return 80;
    }

    @Override
    public void consume() {
        isConsumed = true;
        p.setMaxJump(300);
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
        this.p = p;
        renderer = r;
    }

    public void animationInit(){
        animationHeight = 32;
        animationWidth = p.width/2;
        animationX = p.x + p.width/4;
        animationY = p.y + p.height;
    }

    @Override
    public void render(Graphics g) {

        rotate = false;
        animationInit();

        if(isConsumed && animationLength >=0){

            Image src = getSrc();

            animationInit();
            g.drawImage(src,(int)animationX, (int) animationY, (int)animationWidth, (int)animationHeight, null);
            animationLength--;
            if(animationLength <= 0){
                p.endUse();
                this.isConsumed = false;
                this.animationLength = 200;
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

    public Image getSrc(){
        if(animationLength%2==0) return srcs[0];
        return srcs[1];
    }
}

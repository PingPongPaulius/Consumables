package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class HPBottle extends Item{

    Player player;
    GraphicRenderer renderer;
    boolean rotate;
    boolean used;
    float dropHeight = 100;
    float angle = 0;

    private float animationX;
    private float animationY;
    private float animationWidth;
    private float animationHeight;
    private int animationLength;

    private Image[] srcs;

    public HPBottle(float x, float y){
        defaultVals(x, y);
    }

    public HPBottle(float x, float y, Player player, GraphicRenderer renderer){
        defaultVals(x, y);
        this.player = player;
        this.renderer = renderer;
    }

    private void defaultVals(float x, float y){
        this.x = x;
        this.y = y;
        this.used = false;
        this.rotate = false;
        this.height = 32;
        this.width = 32;
        this.ID = "0001";
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/HealthPot/Icon.png");
        animationLength = 1000;
        srcs = new Image[]{ImageLoader.getImage("src/Art/Items/HealthPot/heal1.png"),
                ImageLoader.getImage("src/Art/Items/HealthPot/heal2.png"),
                ImageLoader.getImage("src/Art/Items/HealthPot/heal3.png"),
                ImageLoader.getImage("src/Art/Items/HealthPot/heal4.png")};
    }

    @Override
    public int getDefaultPrice() {
        return 50;
    }

    @Override
    public void consume() {
        isConsumed = true;
        player.takeDamage(-10);
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

    public void animationInit(){
        animationHeight = 32;
        animationWidth = player.width;
        animationX = player.x;
        animationY = player.y - animationHeight;
    }

    @Override
    public void render(Graphics g) {

        rotate = false;

        if(isConsumed && animationLength >=0){

            Image src = getSrc();

            animationInit();
            g.drawImage(src,(int)animationX, (int) animationY, (int)animationWidth, (int)animationHeight, null);
            animationLength--;
            if(animationLength <= 0){
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 1000;
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
        if(animationLength > 750) return srcs[0];
        if(animationLength > 500) return srcs[1];
        if(animationLength > 250) return srcs[2];
        return srcs[3];
    }
}

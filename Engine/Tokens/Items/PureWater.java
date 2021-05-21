package Engine.Tokens.Items;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class PureWater extends Item{

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

    public PureWater(float x, float y){
        defaultVals(x, y);
    }

    public PureWater(float x, float y, GraphicRenderer r, Player p){
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
        this.ID = "0008"; //<-- enter value here
        this.isConsumed = false;
        this.isPicked = false;
        this.isOnGround = false;
        this.icon = ImageLoader.getImage("src/Art/Items/anitStun/icon.png"); //<--- add any path here
        animationLength = 40; //change how long your aniation should be
        dropHeight = 65; //<--- how high item shoots before falling, this happens during the drop
        srcs = new Image[]{};
        angle = 0;
    }

    @Override
    public int getDefaultPrice() {
        return 110;
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
        animationHeight = (int) player.height;
        animationWidth = (int) player.width;
        animationX = (int) player.x;
        animationY = (int) player.y;
    }

    @Override
    public void render(Graphics g) {

        //starting the animation

        rotate = false;
        animationInit();

        // animation that happens when user presses the use button

        if(isConsumed && animationLength >=0){

            animationInit();

            Image src = player.getCurrentSprite();
            src = animate(src);
            g.drawImage(src,animationX, animationY, animationWidth, animationHeight, null);
            animationLength-= FPS.getDeltaTime();
            if(animationLength <= 0){       // after everything ends set these values to default otherwise the second item will not fire
                player.stun(0, 0);
                player.endUse();
                this.isConsumed = false;
                this.animationLength = 40;
            }
        }
        //if item isn't picked usually stays the same
        else if(!isPicked){

            dropAnimation(renderer);
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            if(rotate) {
                at.rotate(Math.toRadians(angle++), width/2, height/2);
            }
            g2d.drawImage(this.icon, at, null);
        }

    }

    public BufferedImage animate(Image img){
        BufferedImage sprite = (BufferedImage) img;
        BufferedImage out = new BufferedImage(sprite.getWidth(), sprite.getHeight(), 2);
        for(int x = 0; x < sprite.getWidth(); x++){
            for(int y = 0; y < sprite.getHeight(); y++){
                out.setRGB(x, y, sprite.getRGB(x, y)+55);
            }
        }
        return out;
    }

}

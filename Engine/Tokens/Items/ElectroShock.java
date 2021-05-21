package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Tokens.Enemy;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.geom.AffineTransform;

// not working

public class ElectroShock extends Item {

    private int duration;
    private float damage;

    private float animationX;
    private float animationY;
    private float animationWidth;
    private float animationHeight;
    Player player;
    GraphicRenderer renderer;

    private float dropHeight = 100f;
    private boolean rotate;

    final Image[] SPRITES = new Image[5];

    float angle = 0;

    public ElectroShock(float x, float y){
        defaultVal(x, y);
    }

    public ElectroShock(float x, float y, Player player, GraphicRenderer renderer){
        this.player = player;
        defaultVal(x, y);
        this.renderer = renderer;
    }

    public void defaultVal(float x, float y){
        this.ID = "0000";
        this.x = x;
        this.y = y;
        this.duration = 3000;
        this.width = 32;
        this.height = 32;
        this.damage = 3f;
        this.isConsumed = false;
        this.icon = ImageLoader.getImage("src/Art/Items/electro.png");
        this.isOnGround = false;
        SPRITES[0] = ImageLoader.getImage("src/Art/Items/ElectroShock/sprite1.png");
        SPRITES[1] = ImageLoader.getImage("src/Art/Items/ElectroShock/sprite2.png");
        SPRITES[2] = ImageLoader.getImage("src/Art/Items/ElectroShock/sprite3.png");
        SPRITES[3] = ImageLoader.getImage("src/Art/Items/ElectroShock/sprite4.png");
        SPRITES[4] = ImageLoader.getImage("src/Art/Items/ElectroShock/sprite5.png");
    }

    @Override
    public int getDefaultPrice() {
        return 20;
    }

    @Override
    public void consume(){
        this.isConsumed = true;
    }

    public void animationInit(){
        this.animationWidth = 32;
        this.animationHeight = 128;
        if(player.isFacingLeft()) {
            this.animationX = player.x - animationWidth;
            this.animationY = player.y - (this.animationHeight - player.height);
        }
        else if(player.isFacingRight()){
            this.animationX = player.x + player.width;
            this.animationY = player.y - (this.animationHeight - player.height);
        }
    }

    public void dropAnimation(GraphicRenderer renderer){
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

    public boolean isUsed(){
        return duration <= 0;
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

    public boolean collides(Enemy enemy){
        return this.animationX + this.animationWidth >= enemy.x &&
                this.animationX <= enemy.x + enemy.width &&
                this.animationY + this.animationHeight >= enemy.y &&
                this.animationY <= enemy.y + enemy.height;
    }

    @Override
    public void render(Graphics g) {

        rotate = false;

        dropAnimation(renderer);
        int i;

        if((duration/100) % 2 == 0) i = 3;
        else i = 4;
        if(isConsumed) {
            animationInit();
            if(duration > 2200)
                g.drawImage(SPRITES[0], (int) animationX, (int) animationY, (int) animationWidth, (int) animationHeight, null);
            else if(duration > 1600)
                g.drawImage(SPRITES[1], (int) animationX, (int) animationY, (int) animationWidth, (int) animationHeight, null);
            else if(duration > 800)
                g.drawImage(SPRITES[2], (int) animationX, (int) animationY, (int) animationWidth, (int) animationHeight, null);
            else {
                for(Token t: renderer.list()){
                    if(t instanceof Enemy){
                        Enemy enemy = (Enemy) t;
                        if(collides(enemy)){
                            enemy.receiveDamage(damage);
                        }
                    }
                }
                g.drawImage(SPRITES[i], (int) animationX, (int) animationY, (int) animationWidth, (int) animationHeight, null);
            }
            duration--;
            if(duration <= 0) {
                this.duration = 3000;
                isConsumed = false;
                player.endUse();
            }
        }
        else {
            if (!isPicked) {
                Graphics2D g2d = (Graphics2D) g;
                AffineTransform at = AffineTransform.getTranslateInstance(x, y);
                if(rotate){
                    angle += 1;
                    at.rotate(Math.toDegrees(angle), width/2, height/2);
                }
                g2d.drawImage(this.icon, at, null);
            }
        }
    }

    @Override
    public String toString(){
        return ID + "";
    }
}

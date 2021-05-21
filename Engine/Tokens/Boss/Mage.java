package Engine.Tokens.Boss;

import Engine.*;
import Engine.Tokens.Items.ElectroShock;
import Engine.Tokens.Items.HPBottle;
import Engine.Tokens.Items.Item;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Mage extends Boss {

    Rectangle hitbox;

    boolean goRight;
    boolean goLeft;
    boolean jump;

    float speed;

    Player player;

    GraphicRenderer renderer;

    int maxJump;

    float jumpX;

    int animation;

    Image[][] sprites;

    public Mage(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.drops = new Item[5];
        this.width = 128;
        this.height = 128;
        this.health = 50;
        this.renderer = renderer;
        this.player = player;
        speed = 140f;
        goLeft = true;
        goRight = false;
        jump = false;
        maxJump = 0;
        jumpX = x;
        animation = 0;
        updateHitbox();

        Image[] walkingRight = ImageLoader.loadSprites("src/Art/Enemy/MegaWizard/walking.png", 32, 32, 2);
        Image[] walkingLeft = new Image[2];
        for(int i = 0; i < 2; i++){
            walkingLeft[i] = ImageLoader.flipHorizontally((BufferedImage) walkingRight[i]);
        }

        sprites = new Image[][]{
                walkingRight,
                walkingLeft
        };

    }

    public void RIP(){
        renderer.remove(this);
        for (int i = 1; i < drops.length; i++) drops[i] = new ElectroShock(x, y, player, renderer);
        drops[0] = new HPBottle(x, y, player, renderer);
        Item drop = drop();
        drop.dropAnimation(renderer);
        renderer.add(drop);
    }

    public void updateHitbox(){
        hitbox = new Rectangle((int)x, (int)y,(int) width,(int) height);
    }

    @Override
    void BossFightMusic(MusicPlayer mp) {

    }

    @Override
    public void receiveDamage(float damage) {
        this.health -= damage/2;
        if(health > 50) health = 50;
    }

    public ArrayList<Platform> collides(){
        ArrayList<Platform> collisions = new ArrayList<>();
        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                if(hitbox.intersects(platform.hitbox)) collisions.add(platform);
            }
        }
        return collisions;
    }

    public boolean canJump(){
        Mage temp = new Mage(x, y, renderer, player);
        temp.y += 1f;
        temp.updateHitbox();
        return temp.collides().size() > 0 && jump;
    }

    public void changeDirections(){
        goLeft = !goLeft;
        goRight = !goRight;
    }

    @Override
    public void render(Graphics g) {

        if(this.health <= 0) RIP();

        int side = 0;

        float xVel = 0, yVel;

        if(goRight){
            xVel = speed;
        }
        else if(goLeft){
            xVel = -speed;
            side = 1;
        }

        this.x += xVel * FPS.getDeltaTime();

        ArrayList<Platform> collision;

        updateHitbox();

        collision = collides();

        while(collision.size() > 0) {

            jump = true;

            for (Platform p : collision) {
                if (xVel > 0) {
                    x = p.x - width;
                } else if (xVel < 0) {
                    x = p.x + p.width;
                }
            }

            updateHitbox();
            collision = collides();
        }

        if(canJump() && maxJump <= 0){
            if(jumpX == x) changeDirections();
            else {
                maxJump = 20;
                jumpX = x;
            }
        }

        if(maxJump > 0){
            yVel = -Physics.getGravity();
            maxJump -= FPS.getDeltaTime();
            jump = false;
        }
        else yVel = Physics.getGravity();

        this.y += yVel * FPS.getDeltaTime() * 7f;

        updateHitbox();

        if(player.hitbox.intersects(hitbox)){
            player.takeDamage(500);
        }

        collision = collides();

        while(collision.size() > 0) {

            for (Platform p : collision) {
                if (yVel > 0) {
                    y = p.y - height;
                } else if (yVel < 0) {
                    y = p.y + p.height;
                }
            }

            updateHitbox();
            collision = collides();
        }

        int frameSpeed = 100;

        Image src = Physics.getImage(sprites[side], animation, frameSpeed);

        animation++;

        if(Physics.resetAnimation(animation, 2, frameSpeed)){
            animation = 0;
        }

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(src, (int)x, (int)y,(int) width,(int) height, null);
    }

}

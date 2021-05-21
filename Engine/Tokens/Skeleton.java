package Engine.Tokens;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Skeleton extends Enemy{

    float maxHealth;
    Player player;

    Rectangle hitBox;

    int attackingFrames;

    Image[][] sprites;

    int animation = 0;

    public Skeleton(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        this.renderer = renderer;
        this.player = player;
        this.health = 3;
        this.drops = new Item[5];
        maxHealth = health;

        speed = 100;

        if(player.x > x){
            goRight = true;
            goLeft = false;
        }
        else{
            goLeft = true;
            goRight = false;
        }
        maxJump = 0;

        Image[] walking = new Image[]{
                ImageLoader.getImage("src/Art/Enemy/Skeleton/0.png"),
                ImageLoader.getImage("src/Art/Enemy/Skeleton/1.png"),
                ImageLoader.getImage("src/Art/Enemy/Skeleton/2.png"),
                ImageLoader.getImage("src/Art/Enemy/Skeleton/3.png"),
                ImageLoader.getImage("src/Art/Enemy/Skeleton/4.png"),
        };

        Image[] attack = new Image[]{
                ImageLoader.getImage("src/Art/Enemy/Skeleton/A0.png"),
                    ImageLoader.getImage("src/Art/Enemy/Skeleton/A1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Skeleton/A2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Skeleton/A3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Skeleton/A4.png"),
        };

        this.sprites = new Image[4][5];
        sprites[0] = walking;
        sprites[2] = attack;
        for(int i = 0; i < 5; i++){
            sprites[1][i] = ImageLoader.flipHorizontally((BufferedImage) walking[i]);
            sprites[3][i] = ImageLoader.flipHorizontally((BufferedImage) attack[i]);
        }

    }

    public void RIP(){
        renderer.remove(this);
        drops[0] = new MoneyBag(x, y, player, renderer);
        drops[1] = new Sword(x, y, player, renderer);
        drops[2] = new Sword(x, y, player, renderer);
        drops[3] = new Sword(x, y, player, renderer);
        drops[4] = new Sword(x, y, player, renderer);
        Item drop = drop();
        drop.dropAnimation(renderer);
        renderer.add(drop);
    }

    public void updateHitbox(){
        hitBox = new Rectangle((int)x, (int)y,(int) width,(int) height);
    }

    @Override
    public void receiveDamage(float damage) {
        this.health -= damage;
        if(health > maxHealth) health = maxHealth;
    }

    ArrayList<Platform> collidesWith(){
        ArrayList<Platform> out = new ArrayList<>();
        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform plat = (Platform) t;
                if(hitBox.intersects(plat.hitbox)) out.add(plat);
            }
        }
        return out;
    }

    @Override
    public void render(Graphics g) {

        updateHitbox();

        float xMovement;
        float yMovement = Physics.getGravity() *  5f;

        if(goRight) xMovement = speed;
        else if(goLeft) xMovement = -speed;
        else xMovement = 0;

        this.x += xMovement * FPS.getDeltaTime();
        // collision check;

        ArrayList<Platform> collision = collidesWith();

        resetWidth(xMovement, collision);

        this.y += (float) (yMovement * FPS.getDeltaTime());
        // collision check

        collision = collidesWith();

        resetHeight(yMovement, collision);

        if(hitBox.intersects(player.hitbox) && !attacking){
            attacking = true;
            goRight = false;
            goLeft = false;
            attackingFrames = 200;
        }

        int side = 0;
        if(goRight) side = 1;
        if(attacking&& player.x < x + width/2){
            side = 2;
        }
        else if(attacking){
            side = 3;
        }

        if(attacking){
            attackingFrames-= FPS.getDeltaTime();
            if(attackingFrames <= 0){
                attacking = false;
                if(player.x > x){
                    goRight = true;
                    goLeft = false;
                }
                else{
                    goLeft = true;
                    goRight = false;
                }
                if(hitBox.intersects(player.hitbox)) player.takeDamage(1);
            }
        }

        if(health <= 0) RIP();

        Image src = Physics.getImage(sprites[side], animation, 40);

        animation++;

        if(Physics.resetAnimation(animation, sprites[0].length, 40)) animation = 0;

        g.drawImage(src, (int)x,(int)y,(int)width,(int)height,null);

    }








    public void resetHeight(float yMovement, ArrayList<Platform> collision){
        if (yMovement < 0) {
            for(Platform p: collision) {
                y = p.y + p.height;
            }
        }
        else if(yMovement > 0){
            for(Platform p: collision) {
                y = p.y - height;
            }
        }
        updateHitbox();

        collision = collidesWith();

        if(collision.size() != 0) resetHeight(yMovement, collision);
    }

    void changeDirections(){
        goLeft = !goLeft;
        goRight = !goRight;
    }

    public void resetWidth(float xMovement, ArrayList<Platform> collision){

        if(collision.size() > 0) changeDirections();

        if(xMovement > 0){
            for(Platform p: collision){
                x = p.x - width;
            }
        }
        else if(xMovement < 0){
            for(Platform p: collision){
                x = p.x + p.width;
            }
        }

        updateHitbox();

        collision = collidesWith();

        if(collision.size() != 0){
            resetWidth(xMovement, collision);
        }
    }

}

package Engine.Tokens;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.ElectroShock;
import Engine.Tokens.Items.FireBall;
import Engine.Tokens.Items.Item;
import Engine.Tokens.Items.Shield;

import java.awt.*;
import java.util.Random;

public class Owl extends Enemy{

    Player player;
    GraphicRenderer renderer;

    Image[] sprites = new Image[]{
            ImageLoader.getImage("src/Art/Enemy/Owl/0.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/1.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/2.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/3.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/4.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/5.png"),
            ImageLoader.getImage("src/Art/Enemy/Owl/6.png")
    };

    int animation;

    boolean esterEgg = false;

    float maxHealth;

    public Owl(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.renderer = renderer;
        this.player = player;
        this.height = 32;
        this.width = 32;
        this.health = 1;
        goRight = true;
        goLeft = false;
        speed = 400f;
        damage = 9;
        drops = new Item[5];
        Random r = new Random();
        if(r.nextInt(500) == 1) esterEgg = true;
        maxHealth = health;
    }

    void deathAnimation(Graphics g){
        sprites = new Image[]{
                ImageLoader.getImage("src/Art/Enemy/Owl/dead.png"),
                ImageLoader.getImage("src/Art/Enemy/Owl/EasterEgg.png")
        };

        this.y += FPS.getDeltaTime() * Physics.getGravity() * 5f;
        if(isOnGround) RIP();
        else if(esterEgg) g.drawImage(sprites[1], (int)x, (int)y, (int)width, (int)height, null);
        else g.drawImage(sprites[0], (int)x, (int)y, (int)width, (int)height, null);
    }

    void RIP(){
        renderer.remove(this);
        drops[0] = new FireBall(x, y, player, renderer);
        drops[1] = new ElectroShock(x, y, player, renderer);
        drops[2] = new FireBall(x, y, player, renderer);
        drops[3] = new FireBall(x, y, player, renderer);
        drops[4] = new Shield(x, y, renderer, player);
        Item drop = drop();
        drop.dropAnimation(renderer);
        renderer.add(drop);
    }

    @Override
    public void receiveDamage(float damage) {
        health -= damage;
        if(health > maxHealth) health = maxHealth;
    }

    void changeDirections(){
        goRight = !goRight;
        goLeft = !goLeft;
    }

    public boolean attack(){

        Rectangle zone = null;

        if(goRight){
            zone = new Rectangle((int)(x + width + 100), (int)(y + height + 200), 1, 50);
        }
        else if(goLeft){
            zone = new Rectangle((int)(x - 100), (int)(y + height + 200), 1, 50);
        }

        return zone != null && player.hitbox.intersects(zone);
    }

    @Override
    public void render(Graphics g) {

        canGoLeft = true;
        canGoRight = true;
        isOnGround = false;
        attacking = false;

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                horizontalCollider(platform);
                setOnGround(platform);
            }
        }

        if(goLeft && canGoLeft) x -= speed * FPS.getDeltaTime();
        else if(goRight && canGoRight) this.x += speed * FPS.getDeltaTime();
        else changeDirections();

        int frameSpeed = 50;

        Image src = Physics.getImage(sprites, animation, frameSpeed);

        animation++;

        if(animation > frameSpeed * sprites.length) animation = 0;

        if(player.hitbox.intersects(new Rectangle((int)x, (int)y, (int)width, (int)height))){
            player.takeDamage((float) (damage*FPS.getDeltaTime()));
        }

        if(y > 500){
            this.y -= Physics.getGravity() * FPS.getDeltaTime() * 5f;
        }

        if(isOnGround) maxJump = 0;
        else if (maxJump > 0){
            maxJump-=FPS.getDeltaTime();
            this.y += Physics.getGravity() * FPS.getDeltaTime() * 8f;
        }
        else if(attack()){
            maxJump = 20;
        }

        if(this.health <= 0){
            deathAnimation(g);
        }
        else
            g.drawImage(src, (int)x, (int)y, (int)width, (int)height, null);

    }

}

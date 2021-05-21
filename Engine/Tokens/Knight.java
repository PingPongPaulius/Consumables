package Engine.Tokens;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.*;

import java.awt.*;

public class Knight extends Enemy{

    GraphicRenderer renderer;
    Player player;

    Image[][] sprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Enemy/Knight/IL1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IL2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IL3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IL4.png")
            },
            {
                    ImageLoader.getImage("src/Art/Enemy/Knight/IR1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IR2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IR3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/IR4.png")
            }
    };

    Image[][] attack = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL5.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PL6.png")
            },
            {
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR5.png"),
                    ImageLoader.getImage("src/Art/Enemy/Knight/PR6.png")
            }
    };

    Rectangle visionHitBox;

    boolean chargeRight;
    int chargeDistance = 0;

    float maxHealth;

    public Knight(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.width = 128;
        this.height = 128;
        this.health = 50;
        this.renderer = renderer;
        this.player = player;
        this.drops = new Item[5];
        this.goLeft = false;
        this.goRight = false;
        this.damage = 1;
        this.idleCount = 0;
        this.isOnGround = false;
        this.speed = 160f;
        this.maxJump = 0;
        maxHealth = health;
    }

    @Override
    public void receiveDamage(float damage) {

        health-=damage;
        if(health > maxHealth) health = maxHealth;
    }

    public void updateVisionZone(){
        this.visionX = x - 400;
        this.visionY = y;
        this.visionWidth = 800 + width;
        this.visionHeight = 128;
        visionHitBox = new Rectangle((int)visionX, (int)visionY, (int)visionWidth, (int)visionHeight);
    }

    public void RIP(){
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
    public void render(Graphics g) {
        canGoLeft = true;
        canGoRight = true;
        isOnGround = false;
        updateVisionZone();

        float fallVelocity;
        float velocity = 0;

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                setOnGround(platform);
                horizontalCollider(platform);
            }
        }

        Image src;
        int side = 0;
        if(x < player.x){
            side = 1;
        }

        if(!canGoLeft || !canGoRight) jump = true;
        if(jump && isOnGround){
            maxJump = 0.3f;
            jump = false;
        }

        this.x += velocity * FPS.getDeltaTime();

        if(isOnGround) fallVelocity = 0;
        else fallVelocity = Physics.getGravity();

        if(maxJump > 0){
            maxJump -= FPS.getDeltaTime();
            fallVelocity = -Physics.getGravity();
        }

        this.y += fallVelocity * FPS.getDeltaTime() * 5f;


        if(chargeDistance <= 0) attacking = false;

        int hitboxShift = 0;

        if(attacking){
            if(chargeRight){
                hitboxShift = 50;
                if(canGoRight)
                    this.x += speed * FPS.getDeltaTime();
            }
            else{
                if(canGoLeft)
                    this.x -= speed * FPS.getDeltaTime();
            }

            chargeDistance-= speed * FPS.getDeltaTime();

            if(player.hitbox.intersects(new Rectangle((int)x+hitboxShift,(int)y+20,(int)width-50,(int)height-40))){
                player.stun(500, 0);
                chargeDistance = 1000;
            }

        }
        else if(player.hitbox.intersects(new Rectangle((int)visionX,(int)visionY,(int)visionWidth,(int)visionHeight))){
            attacking = true;
            chargeRight = !(player.x < x);
            chargeDistance = 2000;
        }

        if(attacking){

            if(chargeRight) side = 1;
            else side = 0;

            if (idleCount > 500) src = attack[side][5];
            else if (idleCount > 400) src = attack[side][4];
            else if (idleCount > 300) src = attack[side][3];
            else if (idleCount > 200) src = attack[side][2];
            else if (idleCount > 100) src = attack[side][1];
            else src = attack[side][0];

            if(idleCount >= 600) idleCount = 0;
        }
        else{
            if (idleCount > 300) src = sprites[side][3];
            else if (idleCount > 200) src = sprites[side][2];
            else if (idleCount > 100) src = sprites[side][1];
            else src = sprites[side][0];
            if (idleCount >= 400) idleCount = 0;
        }

        if(health <= 0) RIP();

        g.drawImage(src, (int)x,(int)y,(int)width,(int)height,null);
        idleCount++;
    }
}

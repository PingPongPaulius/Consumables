package Engine.Tokens;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.FireBall;
import Engine.Tokens.Items.HPBottle;
import Engine.Tokens.Items.Item;
import Engine.Tokens.Items.Shield;
import Engine.Tokens.Projectiles.Sapling;

import java.awt.*;
import java.util.Random;

public class TreeMan extends Enemy{

    Image[][] sprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/TreeMan/L1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/L2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/L3.png"),
                    ImageLoader.getImage("src/Art/TreeMan/L4.png")
            },
            {
                    ImageLoader.getImage("src/Art/TreeMan/R1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/R2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/R3.png"),
                    ImageLoader.getImage("src/Art/TreeMan/R4.png")
            }
    };

    Image[][] attack = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/TreeMan/AL0.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AL1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AL2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AL3.png")
            },
            {
                    ImageLoader.getImage("src/Art/TreeMan/AR1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AR2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AR3.png"),
                    ImageLoader.getImage("src/Art/TreeMan/AR4.png")
            }
    };

    Image[][] Throw = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/TreeMan/TL1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TL2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TL3.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TL4.png")
            },
            {
                    ImageLoader.getImage("src/Art/TreeMan/TR1.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TR2.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TR3.png"),
                    ImageLoader.getImage("src/Art/TreeMan/TR4.png")
            }
    };

    Player player;
    GraphicRenderer renderer;

    int animation;
    int attackCoolDown = 0;

    boolean throwAction = false;
    int throwAnimation = 0;

    Rectangle hitBox;
    Rectangle throwHitBox;

    float maxHealth;

    public TreeMan(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.width = 128;
        this.height = 128;
        this.health = 10;
        this.speed = 200;
        this.renderer = renderer;
        this.player = player;
        this.damage = 5;
        this.drops = new Item[5];
        this.goLeft = true;
        this.goRight = false;
        this.attacking = false;
        this.maxJump = 0;
        updateThrowHitBox();
        maxHealth = health;
    }

    public void updateThrowHitBox(){
        if(goRight)
            throwHitBox = new Rectangle((int)(x+width+500), (int)y, 100, 900);
        else
            throwHitBox = new Rectangle((int)(x-500), (int)y, 100, 900);
    }

    private void changeDirection(){
        if(this.goLeft){
            this.goLeft = false;
            this.goRight = true;
        }
        else{
            this.goLeft = true;
            this.goRight = false;
        }
    }

    public void RIP(){
        renderer.remove(this);
        drops[0] = new HPBottle(0, 0, player, renderer);
        drops[1] = new HPBottle(0, 0, player, renderer);
        drops[2] = new Shield(0, 0, renderer, player);
        drops[3] = new Shield(0, 0, renderer, player);
        drops[4] = new FireBall(0, 0, player, renderer);
        Item drop = drop();
        drop.dropAnimation(renderer);
        renderer.add(drop);
    }


    @Override
    public void receiveDamage(float damage) {
         health -= damage;
         if(health > maxHealth) health = maxHealth;
    }

    @Override
    public void render(Graphics g) {

        canGoRight = true;
        canGoLeft = true;

        isOnGround = false;

        float fallVelocity;

        int side = 0;

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform p = (Platform) t;
                setOnGround(p);
                horizontalCollider(p);
            }
        }


        if(attacking || throwAction) this.x += 0;
        else if(goLeft && canGoLeft) this.x -= speed * FPS.getDeltaTime();
        else if(goRight && canGoRight) this.x += speed * FPS.getDeltaTime();

        if((!canGoLeft && goLeft)|| (!canGoRight &&  goRight)){
            Random random = new Random();
            if(random.nextInt(2) == 1) {
                changeDirection();
            }
            else jump = true;
        }

        if(jump) maxJump = 150;

        if(isOnGround) fallVelocity = 0;
        else fallVelocity = Physics.getGravity();

        if(maxJump > 0){
            fallVelocity = -Physics.getGravity();
            maxJump -= 1;
            jump = false;
        }

        this.y += fallVelocity * FPS.getDeltaTime() * 5f;

        Image src;

        if(goRight) side = 1;

        if(animation > 300) src = sprites[side][3];
        else if(animation > 200) src = sprites[side][2];
        else if(animation > 100) src = sprites[side][1];
        else src = sprites[side][0];

        if(animation > 400) animation = 0;

        animation++;

        hitBox = new Rectangle((int)x+40,(int)y,(int)width-40,(int)height);

        if(hitBox.intersects(player.hitbox)){
            attacking = true;
        }

        updateThrowHitBox();

        if(throwHitBox.intersects(player.hitbox)){
            throwAction = true;
        }

        if(throwAction){
            if (throwAnimation > 750) src = Throw[side][3];
            else if (throwAnimation > 500) src = Throw[side][2];
            else if (throwAnimation > 250) src = Throw[side][1];
            else src = Throw[side][0];
            throwAnimation++;
        }

        if(throwAnimation > 1000){
            new Sapling(x, y, player.x, renderer);
            throwAnimation = 0;
            throwAction = false;
        }

        if(attacking) attackCoolDown++;

        if(attacking) {
            if (attackCoolDown > 750) src = attack[side][3];
            else if (attackCoolDown > 500) src = attack[side][2];
            else if (attackCoolDown > 250) src = attack[side][1];
            else src = attack[side][0];
        }

        if(attackCoolDown >= 1000){
            player.takeDamage(damage);
            attackCoolDown = 0;
            attacking = false;
        }

        if(health < 0) RIP();

        g.drawImage(src, (int)x,(int)y,(int)width,(int)height, null);

    }
}

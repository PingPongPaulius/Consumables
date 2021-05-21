package Engine.Tokens;

import Engine.FPS;
import Engine.GraphicRenderer;
import Engine.ImageLoader;
import Engine.Physics;
import Engine.Tokens.Items.ElectroShock;
import Engine.Tokens.Items.HPBottle;
import Engine.Tokens.Items.Item;
import Engine.Tokens.Items.Shield;
import Engine.Tokens.Projectiles.MagicBall;

import java.awt.*;
import java.util.Random;

public class Wizard extends Enemy{

    Player player;
    float fallVelocity;
    MagicBall magicBall;

    private int castingAnimationIndex;

    boolean casting;

    final Image[] SPRITES;
    Image src;

    boolean attackRight;

    public float maxHealth;

    public Wizard(float x, float y, GraphicRenderer renderer, Player player){
        this.x = x;
        this.y = y;
        this.width = 32;
        this.height = 64;
        this.damage = 2f;
        this.health = 5f;
        this.speed = 90f;
        this.drops = new Item[10];
        this.renderer = renderer;
        isOnGround = true;
        this.player = player;
        this.maxJump = 140f;
        this.jump = false;
        this.idleCount = 0;
        this.goLeft = true;
        this.goRight = false;
        this.attacking = false;
        this.magicBall = null;
        this.casting = false;
        this.castingAnimationIndex = 0;
        SPRITES = new Image[]{ImageLoader.getImage("src/Art/Enemy/WizardCasting1.png"),
                ImageLoader.getImage("src/Art/Enemy/WizardCasting2.png"),
                ImageLoader.getImage("src/Art/Enemy/wizard.png"),
                ImageLoader.getImage("src/Art/Enemy/wizard2.png"),
                ImageLoader.getImage("src/Art/Enemy/WizardCasting3.png"),
                ImageLoader.getImage("src/Art/Enemy/WizardCasting4.png")};
        maxHealth = this.health;
    }

    public void dead(){
        renderer.remove(this);
        for (int i = 1; i < drops.length; i++) drops[i] = new ElectroShock(x, y, player, renderer);
        drops[0] = new HPBottle(x, y, player, renderer);
        Item drop = drop();
        drop.dropAnimation(renderer);
        renderer.add(drop);
    }

    private void updateVision(){
        this.visionX = x - 500f;
        this.visionY = y - 100f;
        this.visionWidth = 1000 + this.width;
        this.visionHeight = 200 + this.height;
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

    public boolean collides(Token t){
        return magicBall.x + magicBall.width > t.x &&
                t.x + t.width > magicBall.x &&
                t.y + t.height > magicBall.y &&
                magicBall.y + magicBall.width > t.y;
    }

    public boolean collides(Shield s){
        return s.collision(magicBall);
    }

    @Override
    public void render(Graphics g) {

        updateVision();

        canGoLeft = true;                           // can go both ways
        canGoRight = true;
        isOnGround = false;

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                setOnGround(platform);              // if colliding with platform aka standing on ground
                horizontalCollider(platform);       // if is touching some kind of a wall then set where it can go
                if(magicBall != null){
                    if(collides(platform)){
                        renderer.remove(magicBall);
                        magicBall = null;
                        attacking = false;
                    }
                }
            }
            if(t instanceof Shield){
                Shield s = (Shield) t;
                if(magicBall != null){
                    if(collides(s)){
                        renderer.remove(magicBall);
                        magicBall = null;
                        attacking = false;
                    }
                }
            }
            if(t instanceof Player){
                if(magicBall != null){
                    Player player = (Player) t;
                    if(collides(player)){
                        this.player.takeDamage(this.damage);
                        renderer.remove(magicBall);
                        magicBall = null;
                        attacking = false;
                    }
                }
            }
        }

        if(isOnGround) fallVelocity = 0f;           // if stading on ground then dont go down
        else fallVelocity = Physics.getGravity();                   // else fall

        if(Token.collides(visionX, visionY, visionWidth, visionHeight, player.x, player.y, player.width, player.height)){
            if(player.x < this.x){
                attackRight = false;
                // player in the vision zone and on the left

                float distance = this.x - (player.x + player.width);
                // if player is closer than 200f then run else attack
                if(distance < 200){
                    goRight = true;
                    attacking = false;
                }
                else{
                    goRight = false;
                    attacking = true;
                }
                goLeft = false;

            }
            else{
                // player in vision zone and on the right or on the enemy
                attackRight = true;
                float distance = player.x - (this.x + this.width);

                if(distance < 200){
                    goLeft = true;
                    attacking = false;
                }
                else{
                    goLeft = false;
                    attacking = true;
                }
                goRight = false;
            }
        }
        else{
            // player is not in vision zone
            attacking = false;
            if((goLeft && !canGoLeft) || (!canGoRight && goRight)){             // if can not go left but wants to then or (same with right):
                Random rand = new Random();
                if(rand.nextInt(2) == 1 && !jumping){                     // either change directions or not
                    changeDirection();
                }
            }
            if(!goRight && !goLeft){                                            // if doesnt move at all move left
                goLeft = true;
            }


        }

        if(!canGoLeft && goLeft) jump = true;                                   // if can not go left but wants to go left try jumping
        if(!canGoRight && goRight) jump = true;                                 // same here but with right

        if(goLeft && canGoLeft) this.x -= speed * FPS.getDeltaTime();                                // if can go left and wants to go left change x to simulate the movement
        if(goRight && canGoRight) this.x += speed * FPS.getDeltaTime();

        if(jump && isOnGround) jumping = true;                                  // if wants to jump and is on ground then jumping (jumping state) is true

        // set the max jump if space is pressed
        if(jumping && isOnGround){                                              // if jumping animation started in other words if jumping and still on ground then initialise max jump
            maxJump = 140f;
            jump = false;                                                       // set jump to false as it wants to jump once
        }

        if(maxJump > 0){                                                       // if max jump is more than 0 ten make fall velocity negative aka go up and decrease maxjump with every frame
            fallVelocity = -Physics.getGravity();
            this.maxJump -= 0.5f;
        }

        if (maxJump == 1f) jumping = false;                                                 // if maxjump is 1 close to the ground then it is not jumping anymore and it is basically not jumping anymore

        if(renderer.contains(magicBall)){
            attackRight = false;
        }

        // if ball is null and should attack start casting

        if(attacking && magicBall == null && this.x >= 0 && this.x <= 999){
            casting = true;
        }

        // casting animation

        if(casting){
            if(castingAnimationIndex <= 900)
                this.castingAnimationIndex++;
        }
        else castingAnimationIndex=0;

        // if casting == 900 stop casting and draw the ball

        if(castingAnimationIndex >= 900 && magicBall == null){
            casting = false;
            magicBall = new MagicBall(this.x-32, this.y + height/4, attackRight, renderer);
            renderer.add(magicBall);
        }

        if(attackRight && castingAnimationIndex < 450 && castingAnimationIndex > 1) src = SPRITES[4];
        else if(attackRight && casting) src = SPRITES[5];
        else if(castingAnimationIndex < 450 && castingAnimationIndex > 1) src = SPRITES[0];
        else if(casting){ src = SPRITES[1];

        }
        else if(goRight) src = SPRITES[3];
        else src = SPRITES[2];

        g.drawImage(src,(int)x, (int)y, (int) width, (int)height, null);

        if(magicBall != null){
            if(magicBall.isOutOfBounds()){
                renderer.remove(magicBall);
                magicBall = null;
                attacking = false;
            }
        }
        //g.drawRect((int)visionX, (int)visionY, (int) visionWidth, (int)visionHeight);       // draw vision box

        if(health <= 0) dead();                                                             // kill if its below 0 health

        this.y += fallVelocity * 5f * FPS.getDeltaTime();                                                             // controlling enemies height

    }

    @Override
    public void receiveDamage(float damage) {
        this.health -= damage;
        if(health > maxHealth) health = maxHealth;
    }
}

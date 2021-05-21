package Engine.Tokens;

import Engine.*;
import Engine.Tokens.Items.Item;
import Engine.UI.Inventory;

import java.awt.*;
import java.util.ArrayList;

public class Player extends Token{

    public boolean isOnGround;
    float fallVelocity;

    Controller keyboard;

    boolean jumping;

    float MaxJump = 0F;

    public boolean canGoLeft = true;
    public boolean canGoRight = true;

    Inventory inventory;

    float healthPoints;

    private float speed;
    private final float MAX_HEALTH;

    private boolean using;

    boolean facingLeft;
    boolean facingRight;

    private int stunTicks;

    private int score;

    int stash;

    GraphicRenderer renderer;

    int spriteAnimation = 0;

    Image[][] sprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Player/L1.png"),
                    ImageLoader.getImage("src/Art/Player/L2.png"),
                    ImageLoader.getImage("src/Art/Player/L3.png"),
                    ImageLoader.getImage("src/Art/Player/L4.png"),
                    ImageLoader.getImage("src/Art/Player/L5.png"),
                    ImageLoader.getImage("src/Art/Player/L6.png")

            },
            {
                    ImageLoader.getImage("src/Art/Player/1.png"),
                    ImageLoader.getImage("src/Art/Player/2.png"),
                    ImageLoader.getImage("src/Art/Player/42.png"),
                    ImageLoader.getImage("src/Art/Player/5.png"),
                    ImageLoader.getImage("src/Art/Player/6.png"),
                    ImageLoader.getImage("src/Art/Player/7.png")
            }
    };

    Image src;

    public Rectangle hitbox;

    public Player(float x, float y, float width, float height, Controller keyboard, Inventory inventory, int score, GraphicRenderer renderer){
        this.MAX_HEALTH = 20f;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isOnGround = false;
        this.fallVelocity = 0;
        this.keyboard = keyboard;
        this.jumping = false;
        this.inventory = inventory;
        this.healthPoints = MAX_HEALTH;
        this.speed = 200f;
        this.using = false;
        facingLeft = false;
        facingRight = false;
        this.score = score;
        this.stunTicks = 0;
        this.renderer = renderer;
        hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);
        stash = 0;
    }
    // check if it is on platform, if not then activate the fall animation

    public void stun(int ticks, float damage){
       stunTicks = ticks;
       takeDamage(damage);
    }

    public int getStash(){
        return stash;
    }

    public void updateStash(int i){
        stash += i;
    }

    public void takeDamage(float damage){
        this.healthPoints -= damage;
        if(healthPoints > 100) healthPoints = 100;
    }

    public void dead(){
        //System.exit(1);
    }

    public boolean collide(Item item){
            return this.x + this.width >= item.x &&
                    this.x <= item.x + item.width &&
                    this.y + this.height >= item.y &&
                    this.y <= item.y + item.height;
    }

    public ArrayList<Platform> collisionTest(){
        ArrayList<Platform> collision = new ArrayList<>();
        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                if(platform.hitbox.intersects(hitbox)) collision.add(platform);
            }
        }
        return collision;
    }

    public Image getCurrentSprite(){
        return src;
    }

    public void endUse(){
        using = false;
    }

    public boolean isFacingLeft(){
        return facingLeft;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public float getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public float HP(){
        return this.healthPoints;
    }

    public void setMaxJump(float i){
        MaxJump = i;
    }

    public void accelerate(float y){
        fallVelocity += y;
    }

    @Override
    public void render(Graphics g) {

        canGoRight = true;
        canGoLeft = true;

        jumping = this.keyboard.jump;

        if(stunTicks > 0){
            stunTicks-= 1 * FPS.getDeltaTime();
            jumping = false;
            canGoLeft = false;
            canGoRight = false;
        }

        float horizontalMovement = 0;

        if(this.keyboard.a && canGoLeft){
            horizontalMovement = (float) (-speed * FPS.getDeltaTime());
            facingRight = false;
            facingLeft = true;
        }
        if(this.keyboard.d && canGoRight) {
            horizontalMovement = (float) (speed * FPS.getDeltaTime());
            this.facingLeft = false;
            this.facingRight = true;
        }

        if(jumping && MaxJump <= 0){
            MaxJump = 1f;
        }

        else this.fallVelocity = Physics.getGravity();

        // if max jump is more than 0 than fall velocity goes up
        if(MaxJump >= 0.55){
            this.fallVelocity = -Physics.getGravity();
            this.MaxJump -= FPS.getDeltaTime();
        }
        else if(MaxJump >= 0){
            this.MaxJump -= FPS.getDeltaTime();
        }

        float verticalMovement = (float) (fallVelocity * FPS.getDeltaTime() * 5);

        this.x += horizontalMovement;

        hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);

        ArrayList<Platform> platforms = collisionTest();

        for(Platform p: platforms){
            if(horizontalMovement > 0) x = p.x - width;
            if(horizontalMovement < 0) x = p.x + p.width;
        }

        this.y += verticalMovement;

        hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);

        ArrayList<Platform> platforms2 = collisionTest();

        for(Platform p: platforms2){
            if(verticalMovement > 0) y = p.y - height;
            if(verticalMovement < 0) y = p.y + p.height;
        }

        spriteAnimation++;
        int side = 0;
        if(facingLeft) side = 1;

        if(spriteAnimation > 1000) src = sprites[side][5];
        else if(spriteAnimation > 800) src = sprites[side][4];
        else if(spriteAnimation > 600) src = sprites[side][3];
        else if(spriteAnimation > 400) src = sprites[side][2];
        else if(spriteAnimation > 200) src = sprites[side][1];
        else src = sprites[side][0];

        if(spriteAnimation > 1200) spriteAnimation = 0;


        // set the max jump if space is pressed

        if(healthPoints <= 0) dead();


        // check vertical collisions

        accelerate(Physics.getGravity());

        if(y < 0) y = 0;

        if(!using){
            if(keyboard.item1){
                if(inventory.consumable(1)) {
                    inventory.use(1);
                    using = true;
                }
            }
            if(keyboard.item2){
                if(inventory.consumable(2)) {
                    inventory.use(2);
                    using = true;
                }
            }
            if(keyboard.item3){
                inventory.use(3);
                if(inventory.consumable(3))
                    using = true;
            }
            if(keyboard.item4){
                inventory.use(4);
                if(inventory.consumable(4))
                    using = true;
            }
            if(keyboard.item5){
                inventory.use(5);
                if(inventory.consumable(5))
                    using = true;
            }
            if(keyboard.item6){
                inventory.use(6);
                if(inventory.consumable(6))
                    using = true;
            }
            if(keyboard.item7){
                inventory.use(7);
                if(inventory.consumable(7))
                    using = true;
            }
            if(keyboard.item8){
                inventory.use(8);
                if(inventory.consumable(8))
                    using = true;
            }
            if(keyboard.item9){
                inventory.use(9);
                if(inventory.consumable(9))
                    using = true;
            }
        }

        g.drawImage(src,(int) x, (int) y, (int) width, (int) height, null);

    }
     public int displayScore(){
        return score;
     }

     public void addScore(int i){
        this.score += i;
     }

}

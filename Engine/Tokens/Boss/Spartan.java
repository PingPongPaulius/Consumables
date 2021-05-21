package Engine.Tokens.Boss;

import Engine.*;
import Engine.Tokens.Enemy;
import Engine.Tokens.Items.*;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Projectiles.Fireball;
import Engine.Tokens.Projectiles.SpartanSpear;
import Engine.Tokens.Token;

import java.awt.*;
import java.util.Random;


abstract class State{

    static State roam, die, throwSpear, jump, run, current;

    abstract void initialise(Spartan s);
    abstract void update(Graphics g, Spartan s);

    static void changeState(State s, Spartan spartan){
        current = s;
        s.initialise(spartan);
    }

}

class run extends State{

    final Image[][] walkingSprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L1.png")
            },

            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/1.png")
            }
    };

    private int animation = 1000;

    boolean movingLeft;
    boolean movingRight;

    @Override
    void initialise(Spartan s) {
    }

    @Override
    void update(Graphics g, Spartan s) {

        float speed = 250;

        if(s.player.x < s.x){
            movingLeft = false;
            movingRight = true;
        }
        else{
            movingLeft = true;
            movingRight = false;
        }

        if(s.canGoLeft && movingLeft){
            s.x -= speed * FPS.getDeltaTime();
        }
        else if(s.canGoRight && movingRight){
            s.x += speed * FPS.getDeltaTime();
        }
        else{
            if(s.isOnGround) {
                    s.jump = true;
            }
        }

        if(animation > 400) animation =0;

        int index = 0;

        if(animation > 300) index = 3;
        else if(animation > 200) index = 2;
        else if(animation > 100) index = 1;

        int direction;
        if(movingLeft) direction = 1;
        else direction = 0;

        Image src = walkingSprites[direction][index];

        g.drawImage(src, (int) s.x,(int) s.y, (int) s.width, (int) s.height, null);
        g.setColor(Color.GREEN);
        g.fillRect((int)s.x+10, (int)s.y-20, (int)s.getHP()/2, 5);
        g.setColor(Color.black);
        animation+=1;

        if(!s.seesPlayer()){
            State.changeState(State.throwSpear, s);
        }

    }
}

class roam extends State{

    final Image[][] walkingSprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L1.png")
            },

            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/1.png")
            }
    };

    boolean movingRight;
    boolean movingLeft;

    float speed = 190f;

    private int animation = 0;

    @Override
    void initialise(Spartan s) {
        Random rand = new Random();
        if(rand.nextInt(2) == 1)
            movingLeft = true;
        else
            movingRight = false;
    }

    public void animation(Graphics g, Spartan s){
        if(animation > 400) animation =0;

        int index = 0;

        if(animation > 300) index = 3;
        else if(animation > 200) index = 2;
        else if(animation > 100) index = 1;

        int direction;
        if(movingLeft) direction = 1;
        else direction = 0;

        Image src = walkingSprites[direction][index];

        g.drawImage(src, (int) s.x,(int) s.y, (int) s.width, (int) s.height, null);
        g.setColor(Color.GREEN);
        g.fillRect((int)s.x+10, (int)s.y-20, (int)s.getHP()/2, 5);
        g.setColor(Color.black);
        animation+=1;
    }

    public void changeDirection(){
        movingRight = !movingRight;
        movingLeft = !movingLeft;
    }

    @Override
    void update(Graphics g, Spartan spartan) {

        animation(g, spartan);
        if(spartan.canGoLeft && movingLeft){
            spartan.x -= speed * FPS.getDeltaTime();
        }
        else if(spartan.canGoRight && movingRight){
            spartan.x += speed * FPS.getDeltaTime();
        }
        else{
            if(spartan.isOnGround) {
                Random rand = new Random();
                if (rand.nextInt(2) == 1)
                    changeDirection();
                else {
                    spartan.jump = true;
                }
            }
        }
        if(spartan.seesPlayer()){
            changeState(State.throwSpear, spartan);
        }
    }
}

class die extends State{

    final Image[] walkingSprites = new Image[]
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 5.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/death 6.png")
            };

    int index = 1400;

    @Override
    void initialise(Spartan s) {
    }

    @Override
    void update(Graphics g, Spartan s) {

        Image src;
        if(index > 1200) src = walkingSprites[0];
        else if(index > 1000) src = walkingSprites[0];
        else if(index > 800) src = walkingSprites[1];
        else if(index > 600) src = walkingSprites[2];
        else if(index > 400) src = walkingSprites[3];
        else if(index > 200) src = walkingSprites[4];
        else src = walkingSprites[5];


        g.drawImage(src, (int) s.x,(int) s.y, (int) s.width, (int) s.height, null);
        if(index < 0) {
            s.renderer.remove(s);
            for(int i = 0; i < 3; i++) {
                Item drop = s.drop();
                drop.dropAnimation(s.renderer);
                s.renderer.add(drop);
            }
        }

        index--;
    }
}

class jump extends State{

    final Image[] walkingSprites = new Image[]
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/jump1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/jump2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/drop0.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/drop1.png")
            };

    boolean jumpingUp;
    int index;


    @Override
    void initialise(Spartan s) {
        jumpingUp = true;
        index = 600;
    }

    @Override
    void update(Graphics g, Spartan s) {

        Image src;

        if(jumpingUp){
            s.y -= Physics.getGravity() *10f*FPS.getDeltaTime();
            if(index > 400) src = walkingSprites[0];
            else if(index > 300) src = walkingSprites[1];
            else src = walkingSprites[0];
        }
        else{
            s.y += Physics.getGravity() *5f*FPS.getDeltaTime();
            if(s.isOnGround){
                if(s.jumpStun()){
                    s.player.stun(500, 5);
                }
                s.drawStun();
                if(Token.collides(s.player.x ,s.player.y, s.player.width, s.player.height, s.x-40, s.y-20, s.width+40, s.height)){
                    changeState(State.run, s);
                }
                else
                    changeState(roam, s);
            }
            if(index%2 == 0) src = walkingSprites[2];
            else src = walkingSprites[3];
        }
        if(s.y + s.height < 0) jumpingUp = false;

        g.drawImage(src, (int) s.x,(int) s.y, (int) s.width, (int) s.height, null);

        index--;

    }

}

class throwSpear extends State{

    SpartanSpear spear;
    int index;
    int side;

    final Image[][] walkingSprites = new Image[][]{
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/throw 1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/throw 2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/throw 3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/throw 4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/1.png")
            },
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/t1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/t2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/t3.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/t4.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/L1.png")
            }
    };

    @Override
    void initialise(Spartan s) {
        spear = null;
        index = 800;
    }

    @Override
    void update(Graphics g, Spartan s) {

        if(spear == null && index < 100) {
            if (s.player.x < s.x) {
                spear = new SpartanSpear(s.x, s.y, false, s.renderer);
            } else {
                spear = new SpartanSpear(s.x, s.y, true, s.renderer);
            }
        }
        else if(spear != null && spear.isDestroyed()){
            spear = null;
            index = 800;
        }

        if(s.player.x < s.x) side = 0;
        else side = 1;

        Image src;

        if(index > 600) src = walkingSprites[side][0];
        else if(index > 400) src = walkingSprites[side][1];
        else if(index > 200) src = walkingSprites[side][2];
        else if(index > 0) src = walkingSprites[side][3];
        else src = walkingSprites[side][4];

        index--;


        g.drawImage(src, (int) s.x,(int) s.y, (int) s.width, (int) s.height, null);
        g.setColor(Color.GREEN);
        g.fillRect((int)s.x+10, (int)s.y-20, (int)s.getHP()/2, 5);
        g.setColor(Color.black);

        if(Token.collides(s.player.x ,s.player.y, s.player.width, s.player.height, s.x-40, s.y-20, s.width+40, 1000)){
            changeState(State.jump, s);
        }

        if(!s.seesPlayer()) changeState(State.roam, s);

    }
}

public class Spartan extends Boss{

    GraphicRenderer renderer;
    Player player;

    int drawStun = 0;
    boolean isOnGround;
    boolean canGoRight;
    boolean canGoLeft;
    boolean jump = false;

    float maxHealth;

    private int maxJump = 0;
    float fallVelocity = 0;

    final Image[] sprites = new Image[]
            {
                    ImageLoader.getImage("src/Art/Enemy/Spartan/lan1.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/land2.png"),
                    ImageLoader.getImage("src/Art/Enemy/Spartan/land3.png")
            };

    public Spartan(float x, float y, GraphicRenderer renderer, Player player, MusicPlayer mp){
        this.x = x;
        this.y = y;
        this.width = 64;
        this.height = 64;
        this.health = 100;
        this.damage = 30;
        this.renderer = renderer;
        this.player = player;
        this.drops = new Item[]{new ScoreBottle(x, y, player, renderer), new JumpBottle(x, y, player, renderer), new JumpBottle(x, y, player, renderer), new Shield(0, 0, renderer, player)};
        State.roam = new roam();
        State.die = new die();
        State.throwSpear = new throwSpear();
        State.jump = new jump();
        State.run = new run();
        State.changeState(State.roam, this);
        maxHealth = health;
        this.musicPath = "src/Art/Songs/bossFight.wav";
        BossFightMusic(mp);
    }

    public void BossFightMusic(MusicPlayer mp){
        mp.changeTune(musicPath);
    }

    public float getHP(){
        return health;
    }

    public boolean seesPlayer(){
        float VZx = x - 400;
        float VZy = y - 100;
        float VZw = 400*2 + width;
        float VZh = 100 + height;
        return Enemy.collides(VZx, VZy, VZw, VZh, player.x, player.y, player.width, player.height);
    }

    public boolean jumpStun(){
        float VZx = x - 200;
        float VZy = y + height;
        float VZw = 200*2 + width;
        float VZh =  2*height;
        return Enemy.collides(VZx, VZy, VZw, VZh, player.x, player.y, player.width, player.height);
    }

    public void setOnGround(Platform p){
        if(this.y + height >= p.y && this.y <= p.y + p.height){
            if(this.x + width >= p.x && this.x <= p.x + p.width){
                isOnGround = true;
            }
        }
    }

    public void horizontalCollider(Platform p){
        if(this.y + height > p.y && this.y < p.y + p.height && this.x + this.width > p.x - 1.0f && this.x < p.x){
            canGoRight = false;
        }
        if(this.y + height > p.y && this.y < p.y + p.height && this.x < p.x + p.width    + 1.0f && this.x + width > p.x + p.width){
            canGoLeft = false;
        }
    }

    public void drawStun(){
        drawStun = 150;
    }

    @Override
    public void receiveDamage(float damage) {

        this.health -= damage;
        if(health > maxHealth) health = maxHealth;
    }

    @Override
    public void render(Graphics g) {

        isOnGround = false;
        canGoLeft = true;
        canGoRight = true;

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                Platform platform = (Platform) t;
                horizontalCollider(platform);
                setOnGround(platform);
            }
            if(t instanceof Fireball){
                Fireball fb = (Fireball) t;
                if(collides(fb.x ,fb.y, fb.width, fb.height, x-40, y-20, width+40, height))
                    State.changeState(State.jump, this);
            }
        }

        if(isOnGround) fallVelocity = 0f;           // if stading on ground then dont go down
        else fallVelocity = Physics.getGravity();

        if (jump && isOnGround){
            maxJump = 600;
            jump = false;
        }

        if(maxJump > 0){
            maxJump--;
            fallVelocity -= Physics.getGravity()*2;
        }

        State.current.update(g, this);
        this.y += fallVelocity * 5f * FPS.getDeltaTime();
        if(this.health <=0) State.changeState(State.die, this);

        if(drawStun > 0){
            float VZx = x - 200;
            float VZy = y - height/2;
            float VZw = 200*2 + width;
            float VZh =  2*height;

            Image src;

            if(drawStun > 120) src = sprites[0];
            else if(drawStun > 70) src = sprites[1];
            else src = sprites[2];

            g.drawImage(src,(int)VZx, (int)VZy, (int)VZw, (int)VZh, null);
            drawStun-=1*FPS.getDeltaTime();
        }

    }
}

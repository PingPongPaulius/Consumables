package Engine.Tokens;

import Engine.GraphicRenderer;
import Engine.Tokens.Items.Item;

import java.util.Random;

public abstract class Enemy extends Token{

    protected float health;
    protected Item[] drops;
    protected float damage;
    GraphicRenderer renderer;
    float visionX;
    float visionY;
    float visionWidth;
    float visionHeight;
    boolean isOnGround;
    boolean canGoLeft;
    boolean canGoRight;
    boolean jumping;
    float maxJump;
    boolean goLeft;
    boolean goRight;
    boolean jump;

    int idleCount;

    boolean attacking;

    float speed;

    public void setOnGround(Platform p){
        if(p.height >= 3000) return;            // may be cause of the problems but eh
        if(this.y + height >= p.y && this.y <= p.y + p.height){
            if(this.x + width > p.x && this.x < p.x + p.width){
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

    public abstract void receiveDamage(float damage);

    public Item drop(){
        Random rand = new Random();
        return drops[rand.nextInt(drops.length)];
    }
}

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
import java.util.ArrayList;
import java.util.Random;

public class Elf extends Enemy{

    GraphicRenderer renderer;
    Player player;

    Image[][] sprites = new Image[][]{
            {
                ImageLoader.getImage("src/Art/Enemy/Elf/L1.png"),
                ImageLoader.getImage("src/Art/Enemy/Elf/L2.png")
            },
            {
                ImageLoader.getImage("src/Art/Enemy/Elf/R1.png"),
                ImageLoader.getImage("src/Art/Enemy/Elf/R2.png")
            }
    };

    int animation = 0;

    Enemy patient;

    public Elf(float x, float y, GraphicRenderer renderer, Player player){

        this.x = x;
        this.y = y;
        this.renderer = renderer;
        this.player = player;

        this.width = 64;
        this.height = 64;

        this.health = 20;

        this.drops = new Item[5];

        this.speed = 170f;

        goLeft = true;
        goRight = false;

        patient = null;
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

    public void changeDirections(){
        goLeft = !goLeft;
        goRight = !goRight;
    }

    @Override
    public void receiveDamage(float damage) {
        this.health -= damage;
    }

    @Override
    public void render(Graphics g) {

        canGoLeft = true;
        canGoRight = true;
        isOnGround = false;

        ArrayList<Enemy> patients = new ArrayList<>();

        for(Token t: renderer.list()){
            if(t instanceof Platform){
                horizontalCollider((Platform) t);
                setOnGround((Platform) t);
            }
            if(patient == null && t instanceof Enemy){
                Enemy enemy = (Enemy) t;
                if(enemy != this) patients.add(enemy);
            }
        }

        if(patient != null){
            if(patient.health <= 0) patient = null;
            if(patient != null) {
                float patientsRight = patient.x + patient.width;
                float patientsLeft = patient.x;
                float thisRight = x + width;
                float thisLeft = x;

                if(thisLeft > patientsRight) {
                    goLeft =  true;
                    goRight = false;
                }
                else if(thisRight < patientsLeft){
                    goRight = true;
                    goLeft = false;
                }

                if(Math.abs(patientsLeft - thisRight) <= 200 || Math.abs(patientsRight - thisLeft) <= 200){
                    patient.receiveDamage(-1);
                    g.setColor(Color.yellow);
                    int x1, x2, y1 = (int) patient.y, y2 = (int) (y + height/2);
                    if(goRight){
                        x1 = (int) patientsLeft;
                        x2 = (int) thisRight;
                    }
                    else{
                        x1 = (int) patientsRight;
                        x2 = (int) thisLeft;
                    }
                    g.drawLine(x1, y1, x2, y2);
                    g.setColor(Color.black);

                }
            }
        }
        else if(patients.size() > 0){
            Random r = new Random();
            patient = patients.get(r.nextInt(patients.size()));
        }

        if(goLeft && canGoLeft) x -= speed * FPS.getDeltaTime();
        else if(goRight && canGoRight) x += speed * FPS.getDeltaTime();
        else changeDirections();

        int side = 0;

        if(goRight) side = 1;

        int frameSpeed = 100;

        if(Physics.resetAnimation(animation, sprites[side].length, frameSpeed)) animation = 0;

        Image src = Physics.getImage(sprites[side], animation, frameSpeed);

        if(health <= 0) RIP();

        animation++;

        g.drawImage(src, (int)x,(int)y,(int)width,(int)height,null);
    }
}

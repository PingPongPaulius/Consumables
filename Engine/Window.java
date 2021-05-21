package Engine;

import Engine.Tokens.*;
import Engine.Tokens.Boss.Boss;
import Engine.Tokens.Boss.Mage;
import Engine.Tokens.Boss.Spartan;
import Engine.Tokens.Items.Item;
import Engine.Tokens.Projectiles.MagicBall;
import Engine.UI.HealthBar;
import Engine.UI.Inventory;
import Engine.UI.Menu.MenuPanel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Window extends JPanel {

    GraphicRenderer renderer;

    Player player;

    Inventory inventory;

    Controller controller;

    HealthBar hpBar;

    Random rand = new Random();

    ArrayList<Enemy> ALL_ENEMIES;
    int currLevel;
    ArrayList<Enemy> Level;
    boolean areEnemies = false;

    int LevelScore = 0;

    Spawner spawner;

    MenuPanel menu;

    JFrame thisFrame;

    int balance = 0;

    MusicPlayer mp3;

    public Window(MenuPanel menu, int startingScore, ArrayList<Item> items){

        thisFrame = new JFrame("consumables");
        thisFrame.setUndecorated(true);
        thisFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        thisFrame.setSize(1000, 1070);
        thisFrame.setLocationRelativeTo(null);
        this.controller = new Controller();
        thisFrame.addMouseListener(controller);
        thisFrame.addKeyListener(controller);
        renderer = new GraphicRenderer();
        inventory = new Inventory(16, controller);
        this.player = new Player(100, 600, 50, 100, controller, inventory, startingScore, renderer);
        hpBar = new HealthBar(player);
        this.ALL_ENEMIES = new ArrayList<>();
        this.Level = new ArrayList<>();
        ALL_ENEMIES.add(new Wizard(0, 0, renderer, player));
        spawner = new Spawner(this);
        currLevel = 1;
        initRenderer();
        setPlayerInventory(items);
        thisFrame.add(this);
        thisFrame.setVisible(true);
        this.menu = menu;
        FPS.calcBeginTime();
        mp3 = new MusicPlayer("src/Art/Songs/GameSong.wav");
        mp3.play();
    }

    public ArrayList<Item> getPlayerInventory(){
        return inventory.itemsToList();
    }

    public void setPlayerInventory(ArrayList<Item> items){
        for(Item i: items){
            i.add(player, renderer);
            i.give(player);
            renderer.add(i);
        }
    }

    public void initRenderer(){
        renderer.add(new Platform(-800, 650, 300, 200,renderer));
        renderer.add(new Platform(-500, 700, 64, 64,renderer));
        renderer.add(new Platform(-436, 700, 64, 64,renderer));
        renderer.add(new Platform(-372, 700, 64, 64,renderer));
        renderer.add(new Platform(-308, 700, 64, 64,renderer));
        renderer.add(new Platform(-244, 764, 64, 64,renderer));
        renderer.add(new Platform(-180, 828, 64, 64,renderer));
        renderer.add(new Platform(-116, 828, 64, 64,renderer));
        renderer.add(new Platform(-52, 828, 64, 64,renderer));
        renderer.add(new Platform(12, 828, 64, 64,renderer));
        renderer.add(new Platform(76, 828, 64, 64,renderer));
        renderer.add(new Platform(140, 828, 64, 64,renderer));
        renderer.add(new Platform(204, 828, 64, 64,renderer));
        renderer.add(new Platform(264, 828, 64, 64,renderer));
        renderer.add(new Platform(328, 828, 64, 64,renderer));
        renderer.add(new Platform(372, 828, 64, 64,renderer));
        renderer.add(new Platform(436, 828, 64, 64,renderer));
        renderer.add(new Platform(500, 828, 64, 64,renderer));
        renderer.add(new Platform(564, 828, 64, 64,renderer));
        renderer.add(new Platform(628, 828, 64, 64,renderer));
        renderer.add(new Platform(672, 828, 64, 64,renderer));
        renderer.add(new Platform(736, 828, 64, 64,renderer));
        renderer.add(new Platform(800, 828, 64, 64,renderer));
        renderer.add(new Platform(864, 828, 64, 64,renderer));
        renderer.add(new Platform(928, 828, 64, 64,renderer));
        renderer.add(new Platform(972, 828, 64, 64,renderer));
        renderer.add(new Platform(1036, 828, 64, 64,renderer));
        renderer.add(new Platform(1100, 828, 64, 64,renderer));
        renderer.add(new Platform(1164, 828, 64, 64,renderer));
        renderer.add(new Platform(1228, 828, 64, 64,renderer));
        renderer.add(new Platform(1272, 828, 64, 64,renderer));
        renderer.add(new Platform(1336, 828, 64, 64,renderer));
        renderer.add(new Platform(1300, 828, 64, 64,renderer));
        renderer.add(new Platform(1364, 828, 64, 64,renderer));
        renderer.add(new Platform(1428, 828, 64, 64,renderer));
        renderer.add(new Platform(1472, 828, 64, 64,renderer));
        renderer.add(new Platform(1536, 828, 64, 64,renderer));
        renderer.add(new Platform(1600, 810, 300, 200, renderer));

        // walls
        renderer.add(new Platform(-1000, -2500, 200, 4000, renderer));
        renderer.add(new Platform(1900, -2500, 200, 4000, renderer));
        renderer.add(new Mage(500, 500, renderer, player));
        renderer.add(spawner);
        renderer.add(player);
    }

    public void initialiseLevel(){
        LevelScore = 0;
        ArrayList<Token> list = renderer.list();
        for(int i = 0; i < currLevel; i++){
            int enemy = rand.nextInt(ALL_ENEMIES.size());
            Enemy toBeAdded = ALL_ENEMIES.get(enemy);
            Enemy add = null;
            float X_SPAWN = getSpawnPos(rand, list);
            if(toBeAdded instanceof Wizard){
                balance += 1;
                add = new Wizard(X_SPAWN, 600, renderer, player);       // player x find centre otherwise may spawn outside the map
            }
            if(toBeAdded instanceof TreeMan){
                balance += 2;
                add = new TreeMan(X_SPAWN, 600, renderer, player);
            }
            if(toBeAdded instanceof Knight){
                balance += 10;
                add = new Knight(X_SPAWN, 600, renderer, player);
            }
            if(toBeAdded instanceof Owl){
                balance += 0;
                add = new Owl(X_SPAWN, 500, renderer, player);
            }
            if(toBeAdded instanceof Skeleton){
                balance += 5;
                add = new Skeleton(X_SPAWN, 500, renderer, player);
            }
            LevelScore += 1;
            renderer.add(add);
        }
        if(currLevel%10 == 0){
            balance += 100;
            renderer.add(new Spartan(getSpawnPos(rand, renderer.list()), 600, renderer, player, mp3));
        }
        if((currLevel-1)%10==0){
            mp3.changeTune("src/Art/Songs/GameSong.wav");
        }
        if(currLevel == 5) ALL_ENEMIES.add(new Skeleton(0, 0, renderer, player));
        if(currLevel == 10) ALL_ENEMIES.add(new Owl(0, 0, renderer, player));
        if(currLevel == 15) ALL_ENEMIES.add(new TreeMan(0, 0, renderer, player));
        if(currLevel == 20) ALL_ENEMIES.add(new Knight(0, 0, renderer, player));
        if(currLevel == 25) ALL_ENEMIES.add(new Elf(0, 0, renderer, player));
        currLevel++;
    }

    public float getSpawnPos(Random rand, ArrayList<Token> list) {
        ArrayList<Platform> platforms = new ArrayList<>();
        for(Token t: list){
            if(t instanceof Platform && t.height < 1000)
                platforms.add((Platform) t);
        }
        Platform spawnPlatform = platforms.get(rand.nextInt(platforms.size()));
        return (spawnPlatform.x + spawnPlatform.width)/2;

    }

    public void finishGame(){
        menu.update(player.displayScore(), balance+player.getStash(), getPlayerInventory());
        thisFrame.dispose();
        mp3.stop();
    }

    public void RIP(){
        menu.died(player.displayScore());
        thisFrame.dispose();
        mp3.stop();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        renderer.clean();
        renderer.fill();

        renderer.render(g);

        Stack<Token> toRemove = new Stack<>();
        boolean moveForward = false;
        boolean moveBackward = false;

        areEnemies = false;
        for(Token t: renderer.list()){
            if(t instanceof Item){
                Item item = (Item) t;
                if(player.collide(item) && item.isntPicked()){
                    item.picked();
                    inventory.pickUp(item);
                }
                if(item.isUsed()){
                    toRemove.add(item);
                }
            }
            if(player.x + player.width > 1000 && controller.d)
               moveForward = true;
            if(player.x < 0 && controller.a)
                moveBackward = true;
            if(t instanceof Enemy){
                Enemy enemy = (Enemy) t;
                if(enemy.y >= 1000 || enemy.y <= -200) renderer.remove(enemy);
                areEnemies = true;
            }

        }

        if(moveBackward || moveForward){
            for(Token t: renderer.list()){
                if(t instanceof MagicBall){
                    MagicBall mb = (MagicBall) t;
                    mb.changeSpeed();
                    continue;
                }
                if(moveBackward) t.x += 1000;
                else t.x -= 1000;
            }
        }

        while(!toRemove.empty()) renderer.remove(toRemove.pop());   // removing used items

        hpBar.render(g);

        g.setColor(Color.black);

        g.drawString("Score:" + player.displayScore() ,10, 50);

        inventory.draw(g);

        repaint();
        // initialise level
        if(!areEnemies && !spawner.running()){
            Thread thread = new Thread(spawner);
            thread.start();
            player.addScore(LevelScore);
        }

        if(!areEnemies){
            if(spawner.exit(player) && controller.w) finishGame();
        }

        if(player.HP() <= 0) RIP();

        // delta time
        FPS.calcDeltaTime();

    }


}

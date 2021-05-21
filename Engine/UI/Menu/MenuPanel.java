package Engine.UI.Menu;

import Engine.ImageLoader;
import Engine.Tokens.Items.Item;
import Engine.UI.Font;
import Engine.User;
import Engine.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MenuPanel extends JPanel implements MouseListener {
    //src/Art/menu/add.png

    final Image icon = ImageLoader.getImage("src/Art/menu/GameIcon.png");
    final Image userIcon = ImageLoader.getImage("src/Art/menu/ChooseUser.png");
    final Image topIcon = ImageLoader.getImage("src/Art/menu/Top.png");
    final Image shop = ImageLoader.getImage("src/Art/menu/shop.png");
    final Image exit = ImageLoader.getImage("src/Art/menu/exit.png");
    final Image back = ImageLoader.getImage("src/Art/menu/back.png");
    final Image add = ImageLoader.getImage("src/Art/menu/add.png");
    final Image delete = ImageLoader.getImage("src/Art/menu/delete.png");
    final Image select = ImageLoader.getImage("src/Art/menu/select.png");
    final Image info = ImageLoader.getImage("src/Art/menu/info.png");

    User player = null;

    int i = 0;

    MenuPanel instance;

    Menu parent;

    ArrayList<Item> items;
    ArrayList<Item> itemsBought = new ArrayList<>();

    Shop market;

    final Font text = new Font();

    public MenuPanel(Menu parent){
        instance = this;
        addMouseListener(this);
        this.parent = parent;
        //market = new Shop(player);
    }

    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        //g.drawImage(ImageLoader.flipHorizontally((BufferedImage) ImageLoader.loadSprites("src/Art/font.png", 8, 8, 36)[0]), 10 , 10, null);
        //text.render(graphics, " Hello this text, is a bit: tricky \n :( oh... I also made few mistakes", 10, 10);
        if(i == 0)
            mainMenu(g);
        else if(i == 1)
            users(g);
        else if(i ==2)
            top(g);
        else if(i == 3)
            shop(g);
        else if(i == 4)
            info(g);
        repaint();
    }

    public void info(Graphics2D g){
        g.drawImage(exit, 202, 400, 64, 64, null);
        g.drawImage(back, 20, 400, 64, 64, null);
        text.render(g, " How to play:\n" +
                "You spawn in the world and you have to fight enemies that come in waves. Each wave is bigger than the other.\n" +
                "You have to defeat enemies by using your items (you can store 16). To use items click 1-9. \n" +
                "To change item positions click on the bag, click and drag one item on the slot you want it to be.\n" +
                "Navigation: A-left, D-right, Space-jump. When you defeat the wave you have an option to go back and reset.\n" +
                "You can do that by going to maps middle and clicking W to go back to the menu.\n" +
                "Each wave gives you score, the bigger the wave the bigger the score. If you die your score, balance and items will reset.\n" +
                "But if your score is high enough you get to TOP 5 pub ranking!\n" +
                "To get Items you have to go to store and buy them. You start with 100 balance and by killing enemies you balance rises!\n" +
                "Navigating through menu:\n" +
                "Top is play button (You must be logged in)\n" +
                "middle right is log in button: + means add user, old man means to retire (delete user) and sword means log it you have to type a name and you are all set\n" +
                "middle button is the pub (TOP 5)\n" +
                "middle right is the shop\n" +
                "bottom right is this screen! Yay thats me.\n" +
                "bottom middle exiting the game and going back to real world ", 10, 10);
        parent.setSize(2000, 500);
        //g.drawString("Ni hao this is how you play: ", 20, 10);
        //g.drawString("In menu you click on head or disk(top icon) then click + to add a user", 20 , 30);
        //g.drawString("after that you click sword and enter the name you created.", 20, 50);
        //g.drawString("when you spawn in you have to kill monsters and to become",20, 70);
        //g.drawString("top 5 brewery player. after each wave you can click w in the middle spawn to comeback!", 10 , 90);
        //g.drawString( "top 5 updates after your death your score will reset too",20, 110);
        //g.drawString("by the way controls are A, D, Space and to use items is 1-9", 20, 130);
    }

    public void mainMenu(Graphics2D g){
        g.drawImage(icon, 202, 80, 64, 64, null);
        g.drawImage(userIcon, 80, 200, 64, 64, null);
        g.drawImage(topIcon, 206, 200, 64, 64, null);
        g.drawImage(shop, 330, 200, 64, 64, null);
        g.drawImage(exit, 202, 320, 64, 64, null);
        g.drawImage(info, 80, 320, 64, 64, null);
    }

    public void shop(Graphics2D g){
        market.paintComponent(g);
        text.render(g, player.getMoney() + "", 10, 10);
        g.drawImage(exit, 202, 320, 64, 64, null);
        g.drawImage(back, 20, 320, 64, 64, null);
    }

    public void users(Graphics2D g){
        g.drawImage(exit, 202, 320, 64, 64, null);
        g.drawImage(back, 20, 320, 64, 64, null);
        g.drawImage(add, 40, 100, 64, 64, null);
        g.drawImage(delete, 140, 100, 64, 64, null);
        g.drawImage(select, 300, 100, 64, 64, null);
        text.render(g, "users created:", 10, 200);
        int y = 200;
        for(User u: getUsers("src/Users.txt")){
            text.render(g, "Name: "+u.getName() + " Score: " + u.getScore() + " Balance: " + u.getMoney(), 10, y+= 15);
        }
    }

    public void top(Graphics2D g){
        g.drawImage(exit, 202, 320, 64, 64, null);
        g.drawImage(back, 20, 320, 64, 64, null);
        int y = 50;
        text.render(g, "PUB's best:", 200, 50);
        for(User u: topList()){
            text.render(g, u.getName() + " " + u.getScore(), 200, y+=50);
        }
    }

    public void died(int score){
        player.addScore(score);
        addToTop(player);
        player.reset();
        removeInventory(player.getName());
        deleteFile(player.getName());
        addToFile(player.getName());
        parent.setVisible(true);
        parent.playMusic();
        market.refresh();
    }

    public void update(int score, int money, ArrayList<Item> inventory){
        player.addMoney(money);
        player.addScore(score);
        rememberItems(inventory);
        ArrayList<User> users = getUsers("src/Users.txt");
        try {
            FileWriter fw = new FileWriter("src/Users.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for(User user: users) {
                if (!player.getName().equals(user.getName())) {
                    bw.write(user.getName() + " " + user.getScore() + " " + user.getMoney());
                    bw.newLine();
                }
            }
            bw.write(player.getName() + " " + player.getScore() + " " + player.getMoney());
            bw.newLine();
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in update() method");
        }
        parent.setVisible(true);
        parent.playMusic();
        market.refresh();
    }

    public void addToTop(User user){
        try {
            FileWriter fw = new FileWriter("src/Top5.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(user.getName() + " " + user.getScore() + " " + user.getMoney());
            bw.newLine();
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in addToTop() method");
        }

    }

    public ArrayList<User> topList(){
        ArrayList<User> users = getUsers("src/Top5.txt");
        Collections.sort(users);
        while(users.size() > 5)
            users.remove(users.size()-1);
        try {
            FileWriter fw = new FileWriter("src/Top5.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for(User user: users) {
                bw.write(user.getName() + " " + user.getScore() + " " + user.getMoney());
                bw.newLine();
            }
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in toplist() method");
        }
        return users;
    }

    public void addUser(){
        JFrame small = new JFrame();
        JPanel panel = new JPanel();
        JButton add = new JButton("ADD");
        JTextField input = new JTextField(5);
        panel.add(input);
        panel.add(add);
        small.add(panel);
        small.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        small.setLocationRelativeTo(this);
        small.pack();
        small.setVisible(true);

        add.addActionListener(e -> {
            String s = input.getText();
            if(!s.equals("") && !exist(s)){
                addToFile(s);
                small.dispose();
            }
        });
    }

    public void addToFile(String s){
        String[] name = s.split(" ");
        try {
            FileWriter fw = new FileWriter("src/Users.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(name[0] + " " + 0 + " " + 100);
            bw.newLine();
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in addtofile() method");
        }
    }

    public boolean exist(String s){
        ArrayList<User> users = getUsers("src/Users.txt");
        for(User u: users){
            if(s.equals(u.getName())) return true;
        }
        return false;
    }

    public ArrayList<User> getUsers(String path){
        Scanner read = read(path);
        ArrayList<User> users = new ArrayList<>();
        while (read.hasNextLine()){
            String line = read.nextLine();
            String[] user = line.split(" ");
            try {
                users.add(new User(user[0], Integer.parseInt(user[1]), Integer.parseInt(user[2])));
            }
            catch(Exception e){
                System.out.println("bad name");
            }
        }
        return users;
    }

    public static Scanner read(String path){
        File results = new File(path);
        Scanner read = null;
        try {
            read = new Scanner(results);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return read;
    }

    public void deleteUser(){
        JFrame small = new JFrame();
        JPanel panel = new JPanel();
        JButton add = new JButton("Delete");
        JTextField input = new JTextField(5);
        panel.add(input);
        panel.add(add);
        small.add(panel);
        small.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        small.setLocationRelativeTo(this);
        small.pack();
        small.setVisible(true);

        add.addActionListener(e -> {
            String s = input.getText();
            if(!s.equals("") && exist(s)){
                deleteFile(s);
                removeInventory(s);
                small.dispose();
            }
        });
    }

    public void deleteFile(String s){
        ArrayList<User> users = getUsers("src/Users.txt");
        try {
            FileWriter fw = new FileWriter("src/Users.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for(User user: users) {
                if(!s.equals(user.getName())) {
                    bw.write(user.getName() + " " + user.getScore() + " " + user.getMoney());
                    bw.newLine();
                }
            }
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in deletefile() method");
        }
    }

    public void selectUser(){
        JFrame small = new JFrame();
        JPanel panel = new JPanel();
        JButton add = new JButton("Select");
        JTextField input = new JTextField(5);
        panel.add(input);
        panel.add(add);
        small.add(panel);
        small.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        small.setLocationRelativeTo(this);
        small.pack();
        small.setVisible(true);

        add.addActionListener(e -> {
            String s = input.getText();
            if(!s.equals("") && exist(s)){
                select(s);
                small.dispose();
                market = new Shop(player);
            }
        });
    }

    public void select(String s){
        for(User u: getUsers("src/Users.txt")){
            if(s.equals(u.getName()))
                player = u;
        }
    }

    public void rememberItems(ArrayList<Item> inventory){
        try {
            Scanner read = read("src/Inventory.txt");
            ArrayList<String> lines = new ArrayList<>();
            while(read.hasNextLine()){
                lines.add(read.nextLine());
            }
            read.close();
            FileWriter fw = new FileWriter("src/Inventory.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for(String line: lines){
                String[] tokens = line.split(" ");
                if(!tokens[0].equals(player.getName())){
                    bw.write(line);
                    bw.newLine();
                }
            }
            StringBuilder itemString = new StringBuilder(player.getName() + " ");
            for(Item i: inventory){
                itemString.append(i.stringify()).append(" ");
            }
            bw.write(itemString.toString());
            bw.newLine();
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in remeberitems() method");
        }
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> out = new ArrayList<>();
        Scanner read = read("src/Inventory.txt");
        while(read.hasNextLine()){
            String line = read.nextLine();
            String[] tokens = line.split(" ");
            if(tokens[0].equals(player.getName())){
                for (int i = 1; i < tokens.length; i++) { // 1 because 0 is the username
                    out.add(Item.findItem(tokens[i]));
                }
            }
        }
        return out;
    }

    public void removeInventory(String username){
        try {
            Scanner read = read("src/Inventory.txt");
            ArrayList<String> lines = new ArrayList<>();
            while(read.hasNextLine()){
                lines.add(read.nextLine());
            }
            read.close();
            FileWriter fw = new FileWriter("src/Inventory.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for(String line: lines){
                String[] tokens = line.split(" ");
                if(!tokens[0].equals(username)){
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.close();
        }
        catch (IOException e){
            System.out.println("Error in remeberitems() method");
        }
    }

    public void buy(int index){
            Item bought = market.buy(index);
            if(bought != null){
                ArrayList<Item> updated = getItems();
                updated.add(bought);
                if(different(16, updated))
                    update(player.getScore(), 0, updated);
            }
    }

    public boolean different(int x, ArrayList<Item> list){
        ArrayList<Item> temp = new ArrayList<>();
        for(Item i: list){
            boolean exist = false;
            for(Item it: temp){
                if(i.equals(it)) exist = true;
            }
            if(!exist) temp.add(i);
        }
        return temp.size() <= x;
    }


    public boolean click(int x, int y, int tx, int ty){
        return y > ty && y < ty+64 && x > tx && x < tx + 64;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(i == 0) {
            // game
            if (click(e.getX(), e.getY(), 202, 80)) {
                if(player == null) i = 1;
                else{                                   // initialise game
                    parent.setVisible(false);
                    parent.STFU();
                    items = getItems();
                    new Window(instance, player.getScore(), items);
                }
            }
            // user select
            if (click(e.getX(), e.getY(), 80, 200)) {
                i = 1;
            }
            // top 5
            if (click(e.getX(), e.getY(), 206, 200)) {
                i = 2;
            }
            // shop
            if (click(e.getX(), e.getY(), 330, 200)) {
                i = 3;
            }
            if(click(e.getX(), e.getY(), 80, 320)){
                i = 4;
            }

            if (click(e.getX(), e.getY(), 202, 320)) {
                System.exit(1);
            }
        }

        if( i == 1){
            if(click(e.getX(), e.getY(), 20, 320)){
                i = 0;
            }
            if(click(e.getX(), e.getY(), 40, 100)){
                addUser();
            }
            if(click(e.getX(), e.getY(),140, 100)){
                deleteUser();
            }
            if(click(e.getX(), e.getY(),300, 100)){
                selectUser();
            }

            if (click(e.getX(), e.getY(), 202, 320)) {
                System.exit(1);
            }
        }

        if(i == 2){
            if(click(e.getX(), e.getY(), 20, 320)){
                i = 0;
            }

            if (click(e.getX(), e.getY(), 202, 320)) {
                System.exit(1);
            }

        }

        if(i == 3){
            if(player == null) i = 1;
            else {                                   // initialise market
                if (click(e.getX(), e.getY(), 20, 320)) {
                    i = 0;
                }
                if (click(e.getX(), e.getY(), 10, 60)) {
                   buy(1);
                }
                if(click(e.getX(), e.getY(), 74, 60)){
                    buy(2);
                }
                if (click(e.getX(), e.getY(), 138, 60)) {
                    buy(3);
                }
                if(click(e.getX(), e.getY(), 202, 60)){
                    buy(4);
                }
                if (click(e.getX(), e.getY(), 10, 124)) {
                    buy(5);
                }
                if(click(e.getX(), e.getY(), 74, 124)){
                    buy(6);
                }
                if (click(e.getX(), e.getY(), 138, 124)) {
                    buy(7);
                }
                if(click(e.getX(), e.getY(), 202, 124)){
                    buy(8);
                }
            }

            if (click(e.getX(), e.getY(), 202, 320)) {
                System.exit(1);
            }

        }

        if (i == 4){
            if(click(e.getX(), e.getY(), 20, 400)){
                i = 0;
                parent.setSize(500, 500);
            }

            if (click(e.getX(), e.getY(), 202, 400)) {
                System.exit(1);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

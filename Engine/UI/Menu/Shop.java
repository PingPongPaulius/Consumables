package Engine.UI.Menu;

import Engine.ShopBrain.CellMap;
import Engine.Tokens.Items.*;
import Engine.User;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Shop {

    private final int[] SIZE = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    private int currentSize;
    ArrayList<Item> allItems;
    int size;
    User user;
    private ArrayList<Item> onCounter;
    Random r;

    public Shop(User user){
        this.user = user;
        allItems = new ArrayList<>();
        addAllItems();
        r = new Random();
        currentSize = SIZE[r.nextInt(8)];
        initCounter(currentSize, r);
    }

    public void refresh(){
        allItems = new ArrayList<>();
        addAllItems();
        initCounter(currentSize, r);
        updateReputation(getReputation()-0.05);
    }

    public void initCounter(int size, Random r){
        this.onCounter = new ArrayList<>();
        ArrayList<Item> temp = allItems;
        for(int i = 0; i < size; i++){
            Item item = temp.get(r.nextInt(temp.size()));
            onCounter.add(item);
        }
        for(int i = 0; i < 8 - size; i++) onCounter.add(null);
    }

    private double reputation(){                          // takes into account "the money player has" and "reputation".
        Random rand = new Random();
        ArrayList<Float> data = new ArrayList<>();
        data.add((float) user.getMoney());
        data.add((float) size);
        float out = new CellMap(data, rand).bounce();
        BigDecimal bd = new BigDecimal(out).setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public double getReputation(){
        Scanner read = read();
        String line;
        while(read.hasNextLine()){
            line = read.nextLine();
            String[] token = line.split(" ");
            if(token[0].equals(user.getName())) return Double.parseDouble(token[1]);
        }
        return 0;
    }

    public void updateReputation(double satisfaction){
        Scanner read = read();
        ArrayList<String> allLines = new ArrayList<>();
        while(read.hasNextLine()){
            allLines.add(read.nextLine());
        }
        BufferedWriter bw = writer();
        try {
            for (String line : allLines) {
                String[] token = line.split(" ");
                if (!token[0].equals(user.getName())) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.write(user.getName() + " " + satisfaction);
            bw.newLine();
            bw.close();
        }
        catch (IOException e){
            System.out.println("writing to file shop");
        }
    }

    public Scanner read(){
        return MenuPanel.read("src/shop.txt");
    }

    public BufferedWriter writer(){
        BufferedWriter bw = null;
        try{
            FileWriter fw = new FileWriter("src/shop.txt");
            bw = new BufferedWriter(fw);

        }
        catch (IOException e){
            System.out.println("Error in upadteClient sat() method");
        }
        return bw;
    }

    public void addAllItems(){
        allItems.add(new ElectroShock(0, 0));
        allItems.add(new HPBottle(0, 0));
        allItems.add(new JumpBottle(0, 0));
        allItems.add(new Shield(0, 0));
        allItems.add(new ScoreBottle(0, 0));
        allItems.add(new FireBall(0, 0));
        allItems.add(new FireZone(0, 0));
        allItems.add(new Bow(0, 0));
        allItems.add(new PureWater(0, 0));
        allItems.add(new Sword(0, 0));
    }

    public void paintComponent(Graphics2D g){
        int x = 10, y = 60;
        for(int i = 0; i < 8; i++){
            if(onCounter.get(i) == null) g.drawRect(x, y, 64, 64);
            else{
                g.drawImage(onCounter.get(i).getImage(), x, y, 64, 64, null);
                g.drawString(onCounter.get(i).getDefaultPrice() + "", x, y+10);
            }
            x+= 64;
            if(x > 220){
                y += 64;
                x = 10;
            }
        }
    }

    public Item buy(int index){
        Item item = onCounter.get(index-1);
        if(item == null) return null;
        int price = item.getDefaultPrice();
        user.addMoney(-price);
        if(user.getMoney() >= 0){
            updateReputation(getReputation()+0.1);
            if(r.nextInt(3)==0 || currentSize < 8) currentSize+= 1;
            return onCounter.get(index-1);
        }
        user.addMoney(price);
        updateReputation(getReputation()-0.2);
        if(r.nextInt(3)>0 || currentSize > 1) currentSize-= 1;
        return null;
    }

    public static void main(String[] args) {
        Shop shop = new Shop(new User("Momo", 160, 55));
        shop.updateReputation(0.25);
        System.out.println(shop.reputation());
    }


}

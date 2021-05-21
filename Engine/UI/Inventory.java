package Engine.UI;

import Engine.Controller;
import Engine.ImageLoader;
import Engine.Tokens.Items.Item;

import java.awt.*;
import java.util.ArrayList;

public class Inventory {

    class MutableInt{

        int value;

        public MutableInt(){
            this.value = 1;
        }

        public void add(){ value++;}
        public void remove(){value--;}
        public int get(){return value;}

        @Override
        public String toString(){
            return value + "";
        }

    }

    class invMap{

        private Item[] items;
        private MutableInt[] ints;

        public invMap(int size){
            this.ints = new MutableInt[size];
            this.items = new Item[size];
        }

        public MutableInt get(Item i){
            if(i != null) {
                for (int index = 0; index < items.length; index++) {
                    if (i.equals(items[index])) return ints[index];
                }
            }
            return null;
        }

        public void put(Item item, MutableInt i){
            if(!containsKey(item) && item != null) {
                for(int currSpot =0; currSpot < items.length; currSpot++ ){
                    if(items[currSpot] == null){
                        items[currSpot] = item;
                        ints[currSpot] = i;
                        break;
                    }
                }
            }
        }

        public boolean containsKey(Item i){
            if( i != null) {
                for (Item item : items) {
                    if (i.equals(item)) return true;
                }
            }
            return false;
        }

        public void remove(Item i){
            for (int index = 0; index < items.length; index++) {
                if (items[index] == i) {
                    items[index] = null;
                    ints[index] = null;
                }
            }
        }

        public int getIndex(Item item){
            for(int i = 0; i < items.length; i++){
                if(item.equals(items[i])) return i;
            }
            return -1; // throw out of bounds exception
        }

        public void setAt(int index, Item item, MutableInt amount){
            if(!containsKey(item)){
                items[index] = item;
                ints[index] = amount;
            }
        }

        public void swap(Item i1, Item i2){

            MutableInt amountOf1 = get(i1);
            MutableInt amountOf2 = get(i2);

            int indexOf1 = getIndex(i1);
            int indexOf2 = getIndex(i2);

            remove(i1);
            remove(i2);

            setAt(indexOf1, i2, amountOf2);
            setAt(indexOf2, i1, amountOf1);
        }

        public void move(Item item, int at){
            MutableInt amount = get(item);

            remove(item);

            setAt(at, item, amount);

        }

        public Item itemAt(int i){
            if(i < items.length)
                return items[i];
            return null;
        }

        @Override
        public String toString(){
            String s = "[";
            for(int i = 0; i < items.length; i++){
                s += items[i] + ": " + ints[i] + ", ";
            }
            return s;
        }

        public ArrayList<Item> itemsToList(){
            ArrayList<Item> out = new ArrayList<>();
            for(int i = 0; i < items.length; i++){
                if(items[i] != null){
                    int amount = ints[i].get();
                    for(int index= 0; index < amount; index++)
                        out.add(items[i]);
                }
            }
            return out;
        }

    }


    invMap inventory;
    private final int maxCapacity;
    private int currentCapacity;
    Controller mouse;
    boolean drawInventory;

    boolean itemSelected;
    Item Chosen1;
    Item Chosen2;

    public Inventory(int maxCapacity, Controller mouse){
        this.inventory = new invMap(maxCapacity);
        this.maxCapacity = maxCapacity;
        this.currentCapacity = 0;
        this.mouse = mouse;
        this.drawInventory = false;
        this.itemSelected = false;
    }

    public ArrayList<Item> itemsToList(){
        return inventory.itemsToList();
    }

    public void pickUp(Item i){

        MutableInt amount = inventory.get(i);

        if(amount == null && currentCapacity <= maxCapacity){
            inventory.put(i, new MutableInt());
            currentCapacity++;
        }
        else if(inventory.containsKey(i) && amount != null){
            amount.add();
        }

    }

    public boolean consumable(int i){
        return inventory.containsKey(inventory.itemAt(i-1));
    }

    public void use(int index){

        Item i = inventory.itemAt(index-1);
        if(i == null)  return;

        MutableInt amount = inventory.get(i);
        i.consume();
        if(amount != null){
            amount.remove();
            if(amount.get() <= 0){
                inventory.remove(i);
            }
        }

    }

    public void changeSpots(Item i1, Item i2){
        inventory.swap(i1, i2);
    }

    public void draw(Graphics g){
        int side = 70;
        for(int i = 0; i < 9; i++){
            g.drawRect(150 + i*side, 910, side, side);
            Item item = inventory.itemAt(i);
            if(item != null) {
                g.drawImage(item.getImage(), 153 + i * side, 913, 64, 64, null);
                g.drawString(inventory.get(item).toString(), i*side+153+(side-15) , 925);
            }
        }
        g.drawImage(ImageLoader.getImage("src/Art/Items/BackPack.png"), 153 + 9 * side, 913, 64, 64, null);
        g.drawRect(150+9*side, 910, side, side);

        if(mouse.clickedOn(150+9*side, 910, side, side) && !drawInventory){    // open inventory
            drawInventory = true;
        }
        if(mouse.clickedOn(710, 300-95, side/2f, side/2f)){             // close inventory
            drawInventory = false;
            resetItemSelection();
        }
        if(drawInventory){
            drawInv(g, side);
        }

    }


    void drawInv(Graphics g, int side){

        int invY = 100;
        int invX = 150;
        for(int i = 0; i < maxCapacity; i++){

            g.drawRect(invX, invY, side, side);
            Item item = inventory.itemAt(i);

            if(item != null) {
                g.drawImage(item.getImage(), invX+3, invY+3, 64, 64, null);
                g.drawString(inventory.get(item).toString(), invX+(side-12) , invY+12);
            }
            //else g.drawString(0 + "", invX+(side-12) , invY+12);

            if(mouse.clickedOn(invX+10, invY, side, side) && !itemSelected){
                itemSelected = true;
                this.Chosen1 = inventory.itemAt(i);
            }
            if(mouse.releasedOn(invX+10, invY, side, side) && Chosen1 != null){
                Chosen2 = inventory.itemAt(i);
                if(Chosen1.equals(Chosen2)) Chosen2 = null;
                else {
                    if(Chosen2 != null)
                        changeSpots(Chosen1, Chosen2);
                    else
                        inventory.move(Chosen1, i);
                    resetItemSelection();
                }
            }
            if(Chosen1 == null && itemSelected) resetItemSelection();



            invX += side;
            if(invX-150 == 560){
                invY+= side;
                invX = 150;
            }
        }
        g.drawRect(710,205,35, 35);
    }

    public void resetItemSelection(){
        itemSelected = false;
        this.Chosen1 = null;
        this.Chosen2 = null;
        mouse.reset();
    }


}

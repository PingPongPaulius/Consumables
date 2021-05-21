package Engine.Tokens.Items;

import Engine.GraphicRenderer;
import Engine.Tokens.Platform;
import Engine.Tokens.Player;
import Engine.Tokens.Token;

import java.awt.*;

public abstract class Item extends Token implements Comparable<Item>{

    public Image icon;
    protected String ID;
    public boolean isConsumed;
    boolean isOnGround;
    boolean isPicked;

    public abstract int getDefaultPrice();

    public Image getImage(){
        return icon;
    };

    public void give(Player player){
        this.x = player.x;
        this.y = player.y;
    }

    public abstract void consume();

    public void setOnGround(Platform p){
        if(this.y + height >= p.y && this.y <= p.y + p.height){
            if(this.x + width > p.x && this.x < p.x + p.width){
                isOnGround = true;
                return;
            }
        }
        isOnGround = false;
    }

    public abstract void dropAnimation(GraphicRenderer renderer);

    public void picked(){
        isPicked = true;
    }

    public boolean isntPicked(){
        return !isPicked;
    }

    public abstract boolean isUsed();

    public abstract String stringify();

    public abstract void add(Player p, GraphicRenderer r);

    public static Item findItem(String s){
        if(s.equals("0000")) return new ElectroShock(0, 0);
        if(s.equals("0001")) return new HPBottle(0, 0);
        if(s.equals("0002")) return new JumpBottle(0, 0);
        if(s.equals("0003")) return new Shield(0, 0);
        if(s.equals("0004")) return new FireBall(0, 0);
        if(s.equals("0005")) return new ScoreBottle(0, 0);
        if(s.equals("0006")) return new FireZone(0, 0);
        if(s.equals("0007")) return new Bow(0, 0);
        if(s.equals("0008")) return new PureWater(0, 0);
        if(s.equals("0009")) return new MoneyBag(0, 0);
        if(s.equals("0010")) return new Sword(0, 0);
        return null;
    }

    @Override
    public int compareTo(Item other) {
        if(ID.equals(other.ID)) return 0;
        return 1;
    }

    public boolean equals(Item o) {
        if(o == null) return false;
        if (this == o) return true;
        if(this.ID.equals(o.ID)) return true;
        return false;
    }


}

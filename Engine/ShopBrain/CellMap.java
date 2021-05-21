package Engine.ShopBrain;

import java.util.ArrayList;
import java.util.Random;

public class CellMap {

    BrainCell[] map;
    ArrayList<Float> infoGain;
    Random rand;

    public CellMap(ArrayList<Float> data, Random rand){       //input reputation, money has, money given, inventory size
        map = new BrainCell[4];
        for(int i = 0; i < 4; i++) map[i] = new BrainCell(rand);
        infoGain = data;
        infoGain.add(0F);
        this.rand = rand;
    }

    public float bounce(){
        float out = 0;
        for (int i = 0; i < 10; i++){
            out = map[rand.nextInt(4)].process(infoGain);
        }
        return out;
    }

}

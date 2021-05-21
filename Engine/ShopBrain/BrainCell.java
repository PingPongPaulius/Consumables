package Engine.ShopBrain;

import java.util.ArrayList;
import java.util.Random;

public class BrainCell {

    private ArrayList<Float> weights;
    float output;

    public BrainCell(Random rand){
        weights = new ArrayList<>(4);
        for(int i =0; i < 4; i++){
            weights.add(rand.nextFloat()-1);
        }
        output = 0;
    }

    public float process(ArrayList<Float> input){
        for(int i = 0; i < weights.size(); i++){
            output += weights.get(i)*input.get(i);
        }
        output =  (float)(1f/ Math.exp(-output));
        return output;
    }

}

package Engine;

import java.awt.*;

public class Physics {

    private Physics(){}

    private static float gravity = 100F;

    public static float getGravity() {
        return gravity;
    }

    public static void setGravity(float gravity) {
        Physics.gravity = gravity;
    }

    public static Image getImage(Image[] sprites, int animation, int frameSpeed){

        Image frame = sprites[0];
        int index = 0;

        for(int i = 0; i < sprites.length; i++){
            if(animation < index){
                frame = sprites[i];
                break;
            }
            index += frameSpeed;
        }

        return frame;

    }

    public static boolean resetAnimation(int animation, int spriteLength, int frameSpeed){
        return animation > frameSpeed * spriteLength;
    }
}

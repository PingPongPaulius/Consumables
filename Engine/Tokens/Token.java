package Engine.Tokens;

import java.awt.*;

public abstract class Token {

    public float y;
    public float x;
    public float width;
    public float height;

    public static boolean collides(float x1, float y1, float width1, float height1, float x2, float y2, float width2, float height2){
        return x1 + width1 >= x2
                && x1 <= x2 + width2
                && y1 + height1 >= y2
                && y1 <= y2 + height2;
    }

    public boolean collidesWith(Token t){
        return x + width >= t.x
                && x <= t.x + t.width
                && y + height >= t.y
                && y <= t.y + t.height;
    }

    public abstract void render(Graphics g);

}

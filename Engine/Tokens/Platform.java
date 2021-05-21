package Engine.Tokens;

import Engine.GraphicRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Platform extends Token {

    private BufferedImage terrain;

    GraphicRenderer renderer;

    public Rectangle hitbox;

    public Platform(float x, float y, float width, float height, GraphicRenderer renderer){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            this.terrain = ImageIO.read(new File("src/Art/Ground.png"));
        }
        catch (IOException e){

        }
        this.renderer = renderer;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(terrain, (int) x, (int) y, (int) width, (int) height,  null);
        this.hitbox = new Rectangle((int) x, (int) y, (int) width, (int) height);

    }
}

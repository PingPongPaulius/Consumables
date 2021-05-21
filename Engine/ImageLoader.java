package Engine;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {

    public static Image getImage(String filePath){

        File file = new File(filePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static Image[] loadSprites(String filepath, int oneSpriteWidth, int oneSpriteHeight, int amount){
        File file = new File(filepath);
        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image[] sprites = new Image[amount];

        for(int i = 0; i < amount; i++){
            assert sheet != null;
            sprites[i] = sheet.getSubimage(oneSpriteWidth * i, 0, oneSpriteWidth, oneSpriteHeight);
        }

        return sprites;

    }
    // ty to https://stackoverflow.com/users/6394637/eprow https://stackoverflow.com/questions/12562680/how-to-flip-an-image-horizontally
    public static BufferedImage flipHorizontally(BufferedImage sprite){
        BufferedImage out = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = sprite.getWidth()-1; x >= 0; x--){
            for(int y = 0; y < sprite.getHeight(); y++){
                out.setRGB(sprite.getWidth()-x-1, y, sprite.getRGB(x, y));
            }
        }
        return out;
    }

    public static BufferedImage flipVertically(BufferedImage sprite){
        BufferedImage out = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < sprite.getWidth(); x++){
            for(int y = sprite.getHeight()-1; y > 0; y--){
                out.setRGB(x, sprite.getHeight()-y, sprite.getRGB(x, y));
            }
        }
        return out;
    }

    public static AffineTransform rotateBy(double degrees, float x, float y){
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Math.toRadians(degrees));
        return at;
    }

    public static AffineTransform scaleBy(float xScale, float yScale, AffineTransform at){
        at.scale(xScale, yScale);
        return at;
    }

}

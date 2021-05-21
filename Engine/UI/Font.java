package Engine.UI;

import Engine.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Font {

    int spacing;
    Image[] dictionary;
    final char[] REPRESENTATIVES = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ',', ':', '.', ' ', '+', '-', '(', '!', '?', ')'};

    public Font(){
        spacing = 2;
        dictionary = ImageLoader.loadSprites("src/Art/font.png", 8, 8, REPRESENTATIVES.length);
        for(int i = 0; i < dictionary.length; i++){
            dictionary[i] = ImageLoader.flipHorizontally((BufferedImage) dictionary[i]);
        }
    }

    public Image test(){
        return dictionary[0];
    }

    private BufferedImage getChar(char c){
        Image out = null;
        for(int i = 0; i < REPRESENTATIVES.length; i++){
            if(REPRESENTATIVES[i] == Character.toUpperCase(c)){
                out = dictionary[i];
                break;
            }
        }
        return (BufferedImage) out;
    }

    public Image merge(Image image, char c, int curHeight){
        BufferedImage out;
        BufferedImage temp = (BufferedImage) image;
        BufferedImage character = getChar(Character.toUpperCase(c));
        // if first char then image is created else output will be part of the text
        if(image == null) out = new BufferedImage(8, 8, 2);
        else{
            if(character == null) out = new BufferedImage(temp.getWidth() + spacing, curHeight, 2);
            else out = new BufferedImage(temp.getWidth() + 8 + spacing, curHeight, 2);
            for(int x = 0; x < temp.getWidth(); x++){
                for(int y = 0; y < temp.getHeight(); y++){
                    out.setRGB(x, y+(curHeight-8), temp.getRGB(x, y));
                }
            }
        }

        if(character == null) return out;
        int overallX;
        if(temp == null) overallX = 7;
        else overallX = temp.getWidth() - 1;
        for(int x = 0; x < character.getWidth(); x++){
            for(int y = 0; y < character.getHeight(); y++){
                out.setRGB(overallX, y, character.getRGB(x, y));
            }
            overallX--;
        }

        return out;

    }

    public Image join(BufferedImage sprite, char c){
        BufferedImage letter = getChar(c);
        if(sprite == null) return ImageLoader.flipHorizontally(letter);
        int characterSpacing = 4;
        int height = 0;

        BufferedImage out = new BufferedImage(sprite.getWidth()+characterSpacing+8, sprite.getHeight() + height, 2);

        for(int x = 0; x < sprite.getWidth(); x++){
            for(int y = 0; y < sprite.getHeight(); y++){
                out.setRGB(x, y, sprite.getRGB(x, y));
            }
        }

        int overallX = out.getWidth()-1;
        if(null != letter) {
            for (int x = 0; x < letter.getWidth(); x++) {
                for (int y = 0; y < letter.getHeight(); y++) {
                    out.setRGB(overallX, y, letter.getRGB(x, y));
                }
                overallX--;
            }
        }

        return out;

    }

    public void render(Graphics g, String text ,int x, int y){
        Image src = null;
        for(char c: text.toCharArray()){
            if(c == '\n'){
                render(g, after(text), x, y+20);
                break;
            }
            src = join((BufferedImage) src, c);
        }

        g.drawImage(src, x, y, null);

    }

    public String after(String text){
        StringBuilder out = new StringBuilder();
        boolean add = false;
        for(int i = 0; i < text.length(); i++){
            if(add) out.append(text.charAt(i));
            if(text.charAt(i) == '\n'){
                add = true;
            }
        }
        return out.toString();
    }

}

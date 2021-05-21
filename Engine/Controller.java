package Engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Controller implements KeyListener, MouseListener {

    public boolean jump;
    public boolean a;
    public boolean d;
    public boolean w;

    public boolean item1;
    public boolean item2;
    public boolean item3;
    public boolean item4;
    public boolean item5;
    public boolean item6;
    public boolean item7;
    public boolean item8;
    public boolean item9;

    private double clickedX;
    private double clickedY;

    private double releasedX;
    private double releasedY;

    public void Controller(){
        this.jump = false;
        this.a = false;
        this.d = false;
        this.item1 = false;
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A) a = true;
        if(e.getKeyCode() == KeyEvent.VK_W) w = true;
        if(e.getKeyCode() == KeyEvent.VK_D) d = true;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) jump = true;
        if(e.getKeyCode() == KeyEvent.VK_1) item1 = true;
        if(e.getKeyCode() == KeyEvent.VK_2) item2 = true;
        if(e.getKeyCode() == KeyEvent.VK_3) item3 = true;
        if(e.getKeyCode() == KeyEvent.VK_4) item4 = true;
        if(e.getKeyCode() == KeyEvent.VK_5) item5 = true;
        if(e.getKeyCode() == KeyEvent.VK_6) item6 = true;
        if(e.getKeyCode() == KeyEvent.VK_7) item7 = true;
        if(e.getKeyCode() == KeyEvent.VK_8) item8 = true;
        if(e.getKeyCode() == KeyEvent.VK_9) item9 = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A) a = false;
        if(e.getKeyCode() == KeyEvent.VK_D) d = false;
        if(e.getKeyCode() == KeyEvent.VK_W) w = false;
        if(e.getKeyCode() == KeyEvent.VK_SPACE) jump = false;
        if(e.getKeyCode() == KeyEvent.VK_1) item1 = false;
        if(e.getKeyCode() == KeyEvent.VK_2) item2 = false;
        if(e.getKeyCode() == KeyEvent.VK_3) item3 = false;
        if(e.getKeyCode() == KeyEvent.VK_4) item4 = false;
        if(e.getKeyCode() == KeyEvent.VK_5) item5 = false;
        if(e.getKeyCode() == KeyEvent.VK_6) item6 = false;
        if(e.getKeyCode() == KeyEvent.VK_7) item7 = false;
        if(e.getKeyCode() == KeyEvent.VK_8) item8 = false;
        if(e.getKeyCode() == KeyEvent.VK_9) item9 = false;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
            this.clickedX = e.getX();
            this.clickedY = e.getY() + 2;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.releasedX = e.getX();
        this.releasedY = e.getY();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public boolean clickedOn(float x, float y, float width, float height){

        return clickedX > x && clickedY > y && clickedX < x + width && clickedY < y + height;
    }

    public boolean releasedOn(float x, float y, float width, float height){

        return this.releasedX > x && releasedY > y && this.releasedX < x + width && releasedY < y + height;
    }

    public void reset(){
        clickedX = 0;
        clickedY = 0;

        releasedX = 0;
        releasedY = 0;
    }

}

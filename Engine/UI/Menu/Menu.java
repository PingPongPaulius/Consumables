package Engine.UI.Menu;

import Engine.MusicPlayer;

import javax.swing.*;

public class Menu extends JFrame {

    MenuPanel panel;
    MusicPlayer mpTroll;
    public Menu(){
        panel = new MenuPanel(this);
        add(panel);
        //setLocationRelativeTo(null);
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
        mpTroll = new MusicPlayer("src/Art/Songs/MenuSong.wav");
        mpTroll.play();
    }

    public void playMusic(){
        mpTroll.play();
    }

    public void STFU(){
        mpTroll.stop();
    }

    public static void main(String[] args) {
        new Menu();
    }

}

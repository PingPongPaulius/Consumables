package Engine;

import Engine.Tokens.Token;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class GraphicRenderer {

    private final ArrayList<Token> tokens = new ArrayList<>();
    ArrayList<Token> remove = new ArrayList<>();
    ArrayList<Token> toAdd = new ArrayList<>();

    public void add(Token t){
        toAdd.add(t);
    }

    public void remove(Token t){
        remove.add(t);
    }

    public void render(Graphics g){
        try {
            for (Token t : tokens) t.render(g);
        }
        catch (ConcurrentModificationException e){
            System.out.println(e);
        }
    }

    public boolean contains(Token token){
        return tokens.contains(token) || toAdd.contains(token) || remove.contains(token);
    }

    public void clean(){
        for(Token t: remove){
            tokens.remove(t);
        }
        remove.clear();
    }

    public void fill(){
        for(Token t: toAdd){
            tokens.add(t);
        }
        toAdd.clear();
    }

    public ArrayList<Token> list(){
        return tokens;
    }

}

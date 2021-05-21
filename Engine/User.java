package Engine;

public class User implements Comparable<User>{

    private int score;
    private int money;
    private String name;

    public User(String name, int score, int money){
        this.name = name;
        this.score = score;
        this.money = money;
    }

    public void addScore(int i){
        this.score = i;
    }

    public void addMoney(int i){
        this.money += i;
    }

    public String getName(){return name;}

    public int getScore() {
        return score;
    }

    public int getMoney(){
        return money;
    }

    public void reset(){
        this.score = 0;
        this.money = 100;
    }

    @Override
    public String toString(){
        return name + " " + score + " " + money;
    }

    @Override
    public int compareTo(User o) {
            if(this.score < o.score) return 1;
            if(this.score > o.score) return -1;
            return 0;
    }
}

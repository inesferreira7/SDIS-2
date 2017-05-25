import java.util.ArrayList;

public class Player{

    String name;
    ArrayList cards;
    int points;

    public Player(String name, ArrayList cards, int points){
        this.name=name;
        this.cards=cards;
        this.points=points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getCards() {
        return cards;
    }

    public void setCards(ArrayList cards) {
        this.cards = cards;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}


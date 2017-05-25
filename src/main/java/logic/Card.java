package logic;

/**
 * Created by chrx on 5/25/17.
 */
public class Card {

    private String text;

    public Card(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Card{" +
                "text='" + text + '\'' +
                '}';
    }
}

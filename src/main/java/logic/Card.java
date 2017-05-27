package logic;

import java.io.Serializable;

/**
 * Created by chrx on 5/25/17.
 */
public class Card implements Serializable {

    protected String text;

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

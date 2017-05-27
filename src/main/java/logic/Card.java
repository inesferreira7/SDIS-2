package logic;

import java.io.*;
import java.util.Base64;

/**
 * Created by chrx on 5/25/17.
 */
public class Card implements Serializable {

    protected String text;

    public Card(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Card{" +
                "text='" + text + '\'' +
                '}';
    }

    public String toSerializedString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Card getFromSerializedString(String s) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(s));
            ObjectInputStream ois = new ObjectInputStream(bais);
            Card card = (Card) ois.readObject();
            return card;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return text.equals(card.text);
    }

}

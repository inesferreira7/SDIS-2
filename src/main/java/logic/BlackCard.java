package logic;

/**
 * Created by chrx on 5/25/17.
 */
public class BlackCard extends Card {

    private int pick;

    public BlackCard(String text, int pick) {
        super(text);
        this.pick = pick;
    }

    public int getPick() {
        return pick;
    }

    @Override
    public String toString() {
        return "BlackCard{" + "text=" + text + ", " +
                "pick=" + pick +
                '}';
    }

}

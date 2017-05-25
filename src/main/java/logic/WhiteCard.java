package logic;

/**
 * Created by chrx on 5/25/17.
 */
public class WhiteCard extends Card{

    private Player owner;

    public WhiteCard(String text, Player p) {
        super(text);
        this.owner = p;
    }
}

package logic;

/**
 * Created by chrx on 5/25/17.
 */
public class BlackCard extends Card{

    private int pick;
    private int whiteSpaces;

    public BlackCard(String text, int pick, int whiteSpaces) {
        super(text);
        this.pick = pick;
        this.whiteSpaces = whiteSpaces;
    }

    public int getWhiteSpaces(){
        return whiteSpaces;
    }
}

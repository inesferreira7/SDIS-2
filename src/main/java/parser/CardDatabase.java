package parser;

import logic.BlackCard;
import logic.WhiteCard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by chrx on 5/25/17.
 */
public class CardDatabase {

    private static ArrayList<WhiteCard> whiteCards;
    private static ArrayList<BlackCard> blackCards;

    private static final String cardDatabaseFile = "cards.json";
    private static final String whiteCardsKey = "whiteCards";
    private static final String blackCardsKey = "blackCards";

    public static void parseCards() {
        whiteCards = new ArrayList<>();
        blackCards = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader(cardDatabaseFile));

            JSONArray whiteArray = (JSONArray) json.get(whiteCardsKey);
            JSONArray blackArray = (JSONArray) json.get(blackCardsKey);

//            System.out.println(whiteArray);

            for (String cardText : (Iterable<String>) whiteArray) {
                whiteCards.add(new WhiteCard(cardText, null));
            }

            for (JSONObject card : (Iterable<JSONObject>) blackArray) {
                blackCards.add(new BlackCard((String) card.get("text"), ((Long) card.get("pick")).intValue()));
            }

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseCards();
        System.out.println(whiteCards);
        System.out.println(blackCards);
    }

}

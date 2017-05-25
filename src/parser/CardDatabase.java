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

    private ArrayList<WhiteCard> whiteCards;
    private ArrayList<BlackCard> blackCards;

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("cards.json"));

            JSONArray whiteCards = (JSONArray) json.get("whiteCards");
            JSONArray blackCards = (JSONArray) json.get("blackCards");

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}

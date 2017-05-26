package logic;

/**
 * Created by up201404990 on 25-05-2017.
 */
public class GameLogic {
    /*test variables*/
    WhiteCard wCard;
    Player player;

    /*test variables*/

    private Board board;
    public static final int MIN_NUM_PLAYERS = 3, MAX_NUM_PLAYERS = 6;
    public static final int PLAYERS_PICKING = 0, PICK_WINNER = 1, END_ROUND = 2;
    private int gameState;
    public static final int WHITECARDS_PER_PLAYER = 5;

    private static GameLogic instance = null;

    public static GameLogic getInstance() {
        if(instance == null) instance = new GameLogic();
        return instance;
    }

    private GameLogic(){
        board  =  new Board();
        this.gameState = -1;
    }

    public void makePlay(){

        board.pickBlackCard();
        board.dealWhiteCards(WHITECARDS_PER_PLAYER);

        if(gameState == PLAYERS_PICKING){
            for(int i = 0; i < board.getPlayers().size(); i++){
                playerPickedCard(player ,wCard);
            }
        }

        if(gameState == PICK_WINNER){
            selectWinner(wCard);
        }

        if(gameState == END_ROUND){
            endRound();
        }
    }

    public void playerPickedCard(Player player, WhiteCard whiteCard){
        player.removeCard(whiteCard);
        board.addWhiteCardToBoard(whiteCard);
    }


    public void selectWinner(WhiteCard winnerCard){
        winnerCard.getOwner().addPoints(1);
    }


    public void endRound(){
        board.clearBoard();
    }







}

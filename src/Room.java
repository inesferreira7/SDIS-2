public class Room{

    String id;
    int players;
    boolean gameStarted;
    boolean gameEnded;

    public Room(String id, int players, boolean gameStarted, boolean gameEnded) {

        this.id = id;
        this.players = players;
        this.gameStarted = gameStarted;
        this.gameEnded = gameEnded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players){
        this.players=players;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
}
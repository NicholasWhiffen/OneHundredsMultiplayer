import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OneHundreds {
    synchronized ArrayList<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public Map<String, Card> getPlayedCards() {
        return playedCards;
    }

    public void addPlayedCards(String name, Card playedCards) {
        this.playedCards.put(name, playedCards);
    }

    public void clearPlayedCards(){
        this.playedCards.clear();
    }

    private Map<String, Card> playedCards = new HashMap<>();


    public Map<String, Integer> getPlayerScores() {
        return playerScores;
    }

    public void addPlayerScores(String name, int wins) {
        this.playerScores.put(name, wins);
    }

    private final ArrayList<Player> players = new ArrayList<>();
    private final Map<String, Integer> playerScores = new HashMap<>();
    private final CardDeck deck = new CardDeck();
    private boolean cardsDealt = false;

    public String getRoundWinner() {
        return roundWinner;
    }

    public void setRoundWinner(String roundWinner) {
        this.roundWinner = roundWinner;
    }

    private String roundWinner = "";

    public boolean isCardsDealt() {
        return cardsDealt;
    }

    public boolean isPlayingGame() {
        return playingGame;
    }

    public void setPlayingGame(boolean playingGame) {
        this.playingGame = playingGame;
    }

    private boolean playingGame = false;

}

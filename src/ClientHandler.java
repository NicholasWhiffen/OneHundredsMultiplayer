import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ClientHandler extends Thread{

    private final Socket socket;
    private final OneHundreds game;
    private final LinkedList<Card> playerHand = new LinkedList<>();

    public Map<String, Card> getPlayedCards() {
        return playedCards;
    }

    public void addPlayedCards(String name, Card playedcard) {
        this.playedCards.put(name, playedcard);
    }

    private final Map<String, Card> playedCards = new HashMap<>();

    synchronized boolean isCardsDealt() {
        return cardsDealt;
    }

    public void setCardsDealt(boolean cardsDealt) {
        this.cardsDealt = cardsDealt;
    }

    private boolean cardsDealt = false;

    public Player getPlayer() {
        return player;
    }

    private Player player;

    synchronized void createPlayer(String name) {
        player = new Player(name, playerHand);
    }

    public ClientHandler(Socket socket, OneHundreds game) {
        super("clientHandler");
        this.socket = socket;
        this.game = game;
    }

    @Override
    public void run() {
        PrintWriter out;
        BufferedReader in;
        try {
            // get the outputstream of client
            out = new PrintWriter(
                    socket.getOutputStream(), true);

            // get the inputstream of client
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            String inputLine;
            do {
                out.println("Enter Name");
                inputLine = in.readLine();
            } while (inputLine.equals("not valid name"));
            createPlayer(inputLine);
            game.addPlayer(player);
            game.addPlayerScores(inputLine, 0);
            System.out.println(inputLine + " is ready");

            while(true){
                if(isCardsDealt()){
                    for(Card card : playerHand){
                        out.print(card.getValue() + card.getStatus() + ", ");
                    }
                    out.println("end of hand");
                    break;
                }
            }
            while(game.isPlayingGame()){
                for(Map.Entry<String, Card> entry : playedCards.entrySet()){
                    out.println(entry.getKey()  + " played: " + entry.getValue().getValue() + " - " + entry.getValue().getStatus());
                }
                out.println(game.getRoundWinner()+" has won the round!\n");
            }
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

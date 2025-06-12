import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    public static void main(String[] args) throws IOException {

        ArrayList<ClientHandler> clients = new ArrayList<>();
        int portNumber = 5566;
        Socket newSocket;
        OneHundreds game = new OneHundreds();
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("OneHundreds Server is Running...");
            ExecutorService executorService = Executors.newCachedThreadPool();
            do {
                newSocket = serverSocket.accept();
                System.out.println("New client connected");
                ClientHandler client = new ClientHandler(newSocket, game);
                clients.add(client);
            } while (clients.size() != 4);
            for (ClientHandler client : clients){
                executorService.execute(client);
                System.out.println("Client running");
            }
            while(true){
                if(game.getPlayers().size() == 4){
                    break;
                }
            }
            CardDeck deck = new CardDeck();
            deck.generateDeck();
            deck.shuffleDeck(deck.getDeck(), 2);
            try {
                while (deck.getDeck().size() - clients.size() >= 0) {
                    for (ClientHandler client : clients) {
                        client.getPlayer().addCard(deck.getDeck().get(0));
                        deck.getDeck().remove(0);
                    }
                }
                for (ClientHandler client : clients) {
                    client.setCardsDealt(true);
                    System.out.println(client.isCardsDealt());
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("cards dealt\n");
            }
            System.out.println("dealt");

            while(clients.get(0).getPlayer().getHand().size() > 0) {
                game.setPlayingGame(true);
                Map<String, Card> playedCards = new HashMap<>();
                //Play cards
                for (ClientHandler client : clients) {
                    Card playedCard = client.getPlayer().getHand().getFirst();
                    System.out.println(client.getPlayer().getName() + " played: " + playedCard.getValue() + " - " + playedCard.getStatus());
                    client.addPlayedCards(client.getPlayer().getName(), playedCard);
                    game.addPlayedCards(client.getPlayer().getName(), playedCard);
                    playedCards.put(client.getPlayer().getName(), playedCard);
                    client.getPlayer().getHand().remove(0);
                }
                //Compare cards
                String winner = "";
                Card highestCard = new Card("n", 0);
                for (Map.Entry<String, Card> entry : playedCards.entrySet()) {
                    if (entry.getValue().getValue() < highestCard.getValue() && entry.getValue().getStatus().equals("w")) {
                        highestCard = entry.getValue();
                        winner = entry.getKey();
                    } else if (entry.getValue().getStatus().equals("w") && highestCard.getStatus().equals("n")) {
                        highestCard = entry.getValue();
                        winner = entry.getKey();
                    } else if (entry.getValue().getValue() > highestCard.getValue() && highestCard.getStatus().equals("n")) {
                        highestCard = entry.getValue();
                        winner = entry.getKey();
                    }
                }
                game.clearPlayedCards();
                game.setRoundWinner(winner);
                game.getPlayerScores().put(winner, game.getPlayerScores().get(winner) + 1);
            }
            game.setPlayingGame(false);
            System.out.println("Remaining cards:");
            deck.printDeck();
            System.out.println("Final scores:");
            String winner = "";
            int highestScore = 0;
            for(Map.Entry<String, Integer> player : game.getPlayerScores().entrySet()) {
                System.out.println(player.getKey() + ": " + player.getValue());
                if(player.getValue() > highestScore){
                    winner = player.getKey();
                    highestScore = player.getValue();
                }else if(player.getValue() == highestScore){
                    winner = "Its a tie!";
                }
            }
            System.out.println("The winner is:\n" + winner);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}

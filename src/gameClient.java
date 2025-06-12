import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class gameClient {
    public static void main(String[] args) throws IOException {

        String hostName = "localhost";
        int portNumber = 5566;

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))
        ) {
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Server: " + in.readLine());
            while(true) {
                String name = stdIn.readLine();
                if (name.matches("[a-zA-Z]{4,10}")) {
                    out.println(name);
                    break;
                } else {
                    out.println("not valid name");
                }
            }

            String fromServer;

            do {
                System.out.println("Your hand: " + in.readLine());
            } while (!in.readLine().equals("end of hand"));
            while((fromServer = in.readLine()) != null){
                System.out.println(fromServer);
            }
        } catch (UnknownHostException e) {
            System.err.println("Couldn't find host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}

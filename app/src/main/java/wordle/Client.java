package wordle;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

import wordle.Controller.GameController;

public class Client {

   
    public static void main(String[] args) {

        String hostname = "";
        String username = "";
        int port = 27993;
        boolean TLS = false;

        if (args.length < 2) {
            System.out.println("./client <-p port> <-s> [hostname] [Northeastern username]");
            System.exit(1);
        } else if (args.length == 2) {
            hostname = args[0];
            username = args[1];
        } else if (args[0].equals("-p")) {
            port = Integer.parseInt(args[1]);

            if (args[2].equals("-s")) {
                port = 27994;
                TLS = true;
                hostname = args[3];
                username = args[4];
            }
            hostname = args[2];
            username = args[3];
        } else if (args[0].equals("-s")) {
            port = 27994;
            TLS = true;
            hostname = args[2];
            username = args[3];
        } else {
            System.out.println("./client <-p port> <-s> [hostname] [Northeastern username]");
            System.exit(1);
        }

        try {
            Socket socket;
            if (TLS) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(hostname, port);
            } else {
                socket = new Socket(hostname, port);
            }

            GameController controller = new GameController(socket, username);
            controller.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

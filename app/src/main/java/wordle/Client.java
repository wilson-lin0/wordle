package wordle;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class Client {
   
    public static void main(String[] args) {

        String hostname = "";
        String username = "";

        
        int port = 27993; // Default port is 27993
        boolean TLS = false; // Default TLS is false

        // Determines the ports and TLS-encryption based on input args

        // Illegal number of args
        if (args.length < 2) {
            System.out.println("./client <-p port> <-s> [hostname] [Northeastern username]");
            System.exit(1);

        // Default port and TLS
        } else if (args.length == 2) {
            hostname = args[0];
            username = args[1];

        // Custom port is and default TLS
        } else if (args[0].equals("-p")) {
            port = Integer.parseInt(args[1]);
            hostname = args[2];
            username = args[3];

            // Custom port and TLS is enabled
            if (args[2].equals("-s")) {
                TLS = true;
                hostname = args[3];
                username = args[4];
            }
        
        // TLS is true
        } else if (args[0].equals("-s")) {
            port = 27994;
            TLS = true;
            hostname = args[1];
            username = args[2];

        // Illegal argument
        } else {
            System.out.println("./client <-p port> <-s> [hostname] [Northeastern username]");
            System.exit(1);
        }

        // Connects to socket depending on port and if TLS is true
        try {
            Socket socket;
            if (TLS) {
                SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                socket = factory.createSocket(hostname, port);
            } else {
                socket = new Socket(hostname, port);
            }

            // Passes socket and username into GameController
            GameController controller = new GameController(socket, username);
            controller.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

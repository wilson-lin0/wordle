package wordle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameController {

    private final Socket socket;
    private final PrintWriter output;
    private final InputStream input;
    private final ObjectMapper mapper = new ObjectMapper();

    private final String username;
    private String gameID;

    private List<String> wordList = new ArrayList<>();

    public GameController(Socket socket, String username) throws IOException {
        this.socket = socket;
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = socket.getInputStream();
        this.username = username;
        fileToArray("src/main/resources/project1-words.txt");
    }

    // Starts the function, continues recieving messages until the socket is closed
    public void run() {
        sendHello(this.output, this.username);
        
        try {
            JsonParser parser = this.mapper.getFactory().createParser(this.input);
            while (!this.socket.isClosed()) {
                JsonNode message = parser.readValueAs(JsonNode.class);
                delegateMessage(message);
            }
        } catch (IOException e) {

        }
    }

    // Recieves messages and interprets and responds to the type field of message
    private void delegateMessage(JsonNode message) {
        String type = message.get("type").asText();

        if ("start".equals(type)) {
            handleStart(message);
        } else if ("retry".equals(type)) {
            handleRetry(message);
        } else if ("bye".equals(type)) {
            handleBye(message);
        }
    }

    // Sends the first Hello Json to start program
    private static void sendHello(PrintWriter output, String username) {
        JSONObject hello = new JSONObject();
        hello.put("type", "hello");
        hello.put("northeastern_username", username);
        output.println(hello.toString());
    }

    // Stores the game ID and sends the first guess "adieu"
    private void handleStart(JsonNode arguments) {
        this.gameID = arguments.get("id").asText();

        JSONObject firstGuess = new JSONObject();
        firstGuess.put("type", "guess");
        firstGuess.put("id", this.gameID);
        firstGuess.put("word", "adieu");
        output.println(firstGuess.toString());
    }

    // Updates the wordlist based on last guess and its marks, and sends a guess
    private void handleRetry(JsonNode arguments) {
        JsonNode guesses = arguments.get("guesses");
        JsonNode lastGuess = guesses.get(guesses.size() - 1);
        String word = lastGuess.get("word").asText();
        JSONArray marks = new JSONArray(lastGuess.get("marks").toString());

        updateWordList(word, marks);
        String nextGuess = guessWord();

        JSONObject guessMessage = new JSONObject();
        guessMessage.put("type", "guess");
        guessMessage.put("id", this.gameID);
        guessMessage.put("word", nextGuess);

        this.output.println(guessMessage.toString());
    }

    // Gets the flag and closes the socket
    private void handleBye(JsonNode arguments) {
        String flag = arguments.get("flag").asText();
        System.out.println(flag);
        try {
            this.socket.close();
        } catch (IOException e) {

        }
    }

    // Creates a new wordlist, and populate the new list if each word in the old list is a valid guess
    private void updateWordList(String guess, JSONArray marks) {
        List<String> updatedList = new ArrayList<>();

        for (String word : this.wordList) {
            if (validGuess(word, guess, marks)) {
                updatedList.add(word);
            }
        }

        this.wordList = updatedList;
    }

    // Determines if the word 
    private boolean validGuess(String word, String guess, JSONArray marks) {
        for (int i = 0; i < marks.length(); i++) {
            int mark = marks.getInt(i);
            char guessChar = guess.charAt(i);
            char wordChar = word.charAt(i);

            if (mark == 2) {
                if (wordChar != guessChar) {
                    return false;
                }
            } else if (mark == 1) {
                if (wordChar == guessChar || word.indexOf(guessChar) == -1) {
                    return false;
                }
            } else if (mark == 0) {
                if (wordChar == guessChar) {
                    return false;
                }
            }            
        }
        return true;
    }

    // Converts the class's wordlist file into an array
    private void fileToArray(String file) {
        List<String> newList = new ArrayList<>();
        try {BufferedReader reader = new BufferedReader(new FileReader(file));
            String word; 
            while ((word = reader.readLine()) != null) {
                newList.add(word.trim());
            }
        } catch (IOException e) {

        }
        this.wordList = newList;
    }

    // Guesses the first word in the list
    private String guessWord() {
        return wordList.get(0);
    }
}

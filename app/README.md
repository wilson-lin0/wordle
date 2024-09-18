I pass the arguments into my Client.java, which interprets the arguments, and connects it to the socket depending on the port and if TLS was enabled.
The socket is passed into my GameController.java, which runs the program in the run method. 
It first sends a JSON string to the server in sendHello, then is constantly waiting for a server response (using a while loop with the server being open). 
Every JSON message it recieves from the server is handled by the delegate message method that recieves the JSON field "type" and sends it to its respective methods. 
My first guess is adieu, and based on the marks the server sends back, it determines words in the array (assembled in fileToArray method) that match the mark, and updates a new list with only valid words.
The game continues until the parser recieves a message with the field "bye", which it then closes the socket, ending the game.
I also downloaded JAR files for external libraries that I used to parse JSON that I included in my classpath.
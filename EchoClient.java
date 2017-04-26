package clientserver;

import java.net.Socket;             // Used to connect to the server
import java.io.ObjectInputStream;   // Used to read objects sent from the server
import java.io.ObjectOutputStream;  // Used to write objects to the server
import java.io.BufferedReader;      // Needed to read from the console
import java.io.InputStreamReader;   // Needed to read from the console

/**
 * Simple client class.  This class connects to an EchoServer to send
 * text back and forth.  Java message serialization is used to pass
 * Message objects around.
 *
 *
 */
public class EchoClient
{

     public static final int SERVER_PORT = 8765;
     //For a remote server connection, the server IP would be something like 128.125.23.22
     public static final String SERVER_IP = "localhost";
     
    /**
     * Main method.
    */
    
    public static void main(String[] args)
    {
	try{
	    // Connect to the specified server
	    final Socket sock = new Socket(EchoClient.SERVER_IP, EchoClient.SERVER_PORT);
	    System.out.println("Connected to " + EchoClient.SERVER_IP + " on port " + EchoServer.SERVER_PORT);
	    
	    // Set up I/O streams with the server
	    final ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
	    final ObjectInputStream input = new ObjectInputStream(sock.getInputStream());

	    // loop to send messages
	    Message msg = null, resp = null;
	    do{
		// Read and send message.  Since the Message class
		// implements the Serializable interface, the
		// ObjectOutputStream "output" object automatically
		// encodes the Message object into a format that can
		// be transmitted over the socket to the server.
		msg = new Message(readSomeText());
		output.writeObject(msg);

		// Get ACK and print.  Since Message implements
		// Serializable, the ObjectInputStream can
		// automatically read this object off of the wire and
		// encode it as a Message.  Note that we need to
		// explicitly cast the return from readObject() to the
		// type Message.
		resp = (Message)input.readObject();
		System.out.println("\nServer says: " + resp.theMessage + "\n");

	    }while(!msg.theMessage.toUpperCase().equals("EXIT"));
	    
	    // shut things down
	    sock.close();

	}
	catch(Exception e){
	    System.err.println("Error: " + e.getMessage());
	    e.printStackTrace(System.err);
	}

    } //-- end main(String[])


    /**
     * Simple method to print a prompt and read a line of text.
     *
     * @return A line of text read from the console
     */
    private static String readSomeText()
    {
	try{
	    System.out.println("Enter a line of text, or type \"EXIT\" to quit.");
	    System.out.print(" > ");	
	    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    return in.readLine();
	}
	catch(Exception e){
	    // Uh oh...
	    return "";
	}

    } //-- end readSomeText()

} //-- end class EchoClient
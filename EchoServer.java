package clientserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;  // The server uses this to bind to a port
import java.net.Socket;        // Incoming connections are represented as sockets

/**
 * A simple server class.  Accepts client connections and forks
 * EchoThreads to handle the bulk of the work.
 *
 * 
 */
public class EchoServer
{
    /** The server will listen on this port for client connections */
    public static final int SERVER_PORT = 8765;

    /**
     * Main routine.  Just a dumb loop that keeps accepting new
     * client connections.
     *
     */
    public static void main(String[] args)
    {
	try{
	    // This is basically just listens for new client connections
	    final ServerSocket serverSock = new ServerSocket(SERVER_PORT);
	    
	    // A simple infinite loop to accept connections
	    Socket sock = null;
	    while(true){
		sock = serverSock.accept();     // Accept an incoming connection
		processConnection(sock);
	    }                                   // Loop to work on new connections while this
                                                // the accept()ed connection is handled

	}
	catch(Exception e){
	    System.err.println("Error: " + e.getMessage());
	    e.printStackTrace(System.err);
	}

    }  //-- end main(String[])

    public static void processConnection(Socket socket){
        try{
	    // Print incoming message
	    System.out.println("** New connection from " + socket.getInetAddress() + ":" + socket.getPort() + " **");

	    // set up I/O streams with the client
	    final ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
	    final ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

	    // Loop to read messages
	    Message msg = null;
	    int count = 0;
	    do{
		// read and print message
		msg = (Message)input.readObject();
		System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "] " + msg.theMessage);

		// Write an ACK back to the sender
		count++;
		output.writeObject(new Message("Recieved message #" + count));

	    }while(!msg.theMessage.toUpperCase().equals("EXIT"));

	    // Close and cleanup
	    System.out.println("** Closing connection with " + socket.getInetAddress() + ":" + socket.getPort() + " **");
	    socket.close();

	}
	catch(Exception e){
	    System.err.println("Error: " + e.getMessage());
	    e.printStackTrace(System.err);
	}

    }
} //-- End class EchoServer
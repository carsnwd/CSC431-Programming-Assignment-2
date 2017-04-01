import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Client side for our client
 * server project. Sends a request
 * with a string to reverse.
 * @author Carson Wood
 *
 */
public class Client
{
    private static final int PORT = 4446;

    private static final String HOST = "localhost";

    private static final String CLIENT_ID = "A";

    private Random rand;

    //Output to server
    PrintWriter out;

    //Input from server
    Scanner conIn;

    //Input from user
    Scanner in;

    /**
     * Creates a new instance of the client
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException
    {
        Client c = new Client();
        c.run();
    }

    /**
     * Runs the client
     *
     * @throws UnknownHostException
     * @throws IOException
     * @throws InterruptedException
     */
    public void run() throws UnknownHostException, IOException, InterruptedException
    {
        String choice; //To advance client and send next string
        String msg; //Recieve messages from server

        //Opens connection
        Socket connection = openConnection();

        //Gets I/O streams from server
        System.out.println("Getting output stream...");
        out = new PrintWriter(connection.getOutputStream());
        System.out.println("Getting input stream...");
        conIn = new Scanner(connection.getInputStream());
        msg = conIn.nextLine();
        System.out.println(msg);
        in = new Scanner(System.in);

        generateAndSendPackets(connection);
    }

    /**
     * Increments current packet message number, selects a
     * random destination, and sends the packet.
     * @param connection
     * @throws InterruptedException
     * @throws IOException
     */
    private void generateAndSendPackets(Socket connection) throws InterruptedException, IOException
    {
        String packet;
        for (int messageNumber = 0; messageNumber <= 20; messageNumber++)
        {
            packet = CLIENT_ID; //SOURCE
            packet = packet + randomDestination(); //DESTINATION
            //ADD CHECKSUM TO MESSAGE //CHECKSUM
            packet = packet + messageNumber; //DATA
            //ADD OTHER DATA TO MESSAGE? //DATA
            send(packet);
            Thread.sleep(4000);
        }
        closeConnection(connection); //Closes the connection
    }

    /**
     * Select random destination
     * to send to.
     */
    public String randomDestination()
    {
        List<String> destinations = Arrays.asList("A","B","C","D");
        destinations.remove(CLIENT_ID); //Do not send it to itself...
        rand = new Random();
        return destinations.get(rand.nextInt(destinations.size()));
    }

    /**
     * Open the connection
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    private Socket openConnection() throws UnknownHostException, IOException
    {
        System.out.println("Connecting to server...");
        Socket connection = new Socket(HOST, PORT); //Connects to server
        System.out.println("Connection made!");
        return connection;
    }

    /**
     * Closes the connection
     * @param connection
     * @throws IOException
     */
    private void closeConnection(Socket connection) throws IOException
    {
        System.out.println("Closing connection...");
        connection.close();
    }

    /**
     * Sends a message to the server
     * with the string to reverse.
     * @param packet
     */
    private void send(String packet)
    {
        System.out.println("You sent " + packet + ", sending message...");
        out.println(packet);
        out.flush();
        String msg = conIn.nextLine();
        System.out.println(msg);
        out.flush();
    }
}
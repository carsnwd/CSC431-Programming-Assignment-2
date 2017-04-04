package runner;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Client side for our client
 * server project. Sends a request
 * with a string to reverse.
 * @author Abe and Carson
 *
 */
public class Client
{
    private static final int PORT = 9000;

    private static final String HOST = "localhost";

    private static final byte CLIENT_ID = (byte)11;

    private Random rand;

    //For reciving packets
    private ServerSocket clientRecieverSocket = null;

    //Output to server
    private PrintWriter out;

    //Input from server
    private Scanner conIn;

    //Input from user
    private Scanner in;

    //Data Output Stream
    private DataOutputStream dos;

    /**
     * Creates a new instance of the client
     * @param args
     * @throws UnknownHostException
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException
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
    private void run() throws IOException, InterruptedException
    {
        String msg; //Recieve messages from server

        //Opens connection
        Socket connection = openConnection();

        //Gets I/O streams from server
        System.out.println("Getting output stream...");
        out = new PrintWriter(connection.getOutputStream());
        System.out.println("Getting input stream...");
        conIn = new Scanner(connection.getInputStream());
        System.out.println();
        dos = new DataOutputStream(connection.getOutputStream());
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
        byte packet[] = new byte[5];
        for (byte messageNumber = 0; messageNumber <= 20; messageNumber++)
        {
            packet[0] = CLIENT_ID; //SOURCE
            packet[1] = randomDestination(); //DESTINATION
            packet[3] = messageNumber; //DATA
            packet[4] = messageNumber;
            //Need more data?
            packet[2] = computeCheckSum(packet); //COMPUTE CHECKSUM
            send(packet); //SEND PACKET
            Thread.sleep(4000); //WAIT TO SEND NEXT PACKET
        }
        closeConnection(connection); //Closes the connection
    }


    /**
     * Given a packet, it computes the checksum for
     * the packet with internal Checksum library
     * @param packet
     * @return
     */
    private byte computeCheckSum(byte[] packet)
    {
        Checksum checkSum = new CRC32();
        checkSum.update(packet, 0, packet.length);
        return (byte)checkSum.getValue();
    }

    /**
     * Select random destination
     * to send to.
     */
    private byte randomDestination()
    {
        List<Byte> destinations = Arrays.asList((byte)22,(byte)33,(byte)44);
        //destinations.remove(CLIENT_ID); //Do not send it to itself...
        rand = new Random();
        return destinations.get(rand.nextInt(destinations.size())); //converts string to byte
    }

    /**
     * Open the connection
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    private Socket openConnection() throws IOException
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
    private void send(byte[] packet) throws IOException
    {
        System.out.println("You sent " + packet + ", sending message...");
        System.out.println(packet[0] + " " + packet[1] + " " + packet[2] + " " + packet[3] + " " + packet[4]);
        dos.write(packet, 0, packet.length);
        dos.flush();
    }
}
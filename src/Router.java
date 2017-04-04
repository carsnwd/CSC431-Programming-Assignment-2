import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

/**
 * Router class routes messages
 * around to different routers/clients
 *
 * @Author Abe and Carson
 */
public class Router{
    //Stores routing table
    private Hashtable<Byte, String> routingTable;

    //Stores socket
    private static ServerSocket SERVER;

    //Stores router ID (CHANGE THIS IS HARDCODED!!!!!!)
    private static final int ROUTER_ID = 1;

    public static void main(String args[]) throws IOException
    {
        Router r = new Router();
        System.out.println("Waiting for connection....");
        while(true)
        {
            Socket incomingConnection = SERVER.accept();
            System.out.println("Connection recieved from "
                    + incomingConnection.getInetAddress()
                    + " on port "
                    + incomingConnection.getLocalPort());
            RouterThreadHandler rht = new RouterThreadHandler(incomingConnection, r);
            rht.start();
        }
    }

    /**
     * Creates a router object
     * with a server socket that opens to listen for packets
     * and a routing table
     */
    public Router()
    {
        //Creates routing tables HashTable<Byte, String> for router to use based on its ID
        RoutingTableFactory rtf = new RoutingTableFactory();
        try{
            routingTable = rtf.getRoutingTable(ROUTER_ID);
        } catch(RoutingTableFactory.InvalidRouterIDException e){
            e.printStackTrace();
        }
        try {
            SERVER = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for the routers
     * Routing Table
     * @return
     */
    public Hashtable<Byte, String> getRoutingTable()
    {
        return this.routingTable;
    }

    /**
     * Thread for handling router processes for
     * forwarding a packet to a different link.
     */
    private static class RouterThreadHandler extends Thread
    {
        private Socket connection;
        private Router router;
        private Scanner in;
        private PrintWriter out;
        //Used for sending the packet byte[], can't use Scanner for byte[] :(
        private DataInputStream dis;
        RouterThreadHandler(Socket c, Router r) throws IOException
        {
            this.router = r;
            this.connection = c;
            in = new Scanner(c.getInputStream());
            out = new PrintWriter(c.getOutputStream());
            dis = new DataInputStream(c.getInputStream());
        }

        public void run()
        {
            out.println("R: Connected to router " + ROUTER_ID + " at " + SERVER.getLocalPort());
            out.flush();
            while(connection.isConnected())
            {
                byte[] packet = new byte[5];
                System.out.println("Waiting for packet...");
                //Reads packet byte[] from the client, stores in packet.
                try{
                    dis.readFully(packet);
                }catch(Exception e){
                    e.printStackTrace();
                }
                //Print out each byte of packet.
                System.out.println("Packet recieved: " + packet[0] + " " + packet[1] + " " + packet[2] + " " + packet[3] + packet[5]);
                //Get routing table, look up what link to send packet to.
                Hashtable<Byte, String> routingTable = router.getRoutingTable();
                String destination = routingTable.get(packet[1]);
                System.out.println("Forwarding to " + destination);
            }
        }
    }
}

/**********
 * CODE FIDDLING WITH STRINGS -> BYTES AND VICE VERSA
 public static void main(String[] args)
 {
 byte packet[] = new byte[4];
 packet[0] = 'D';
 packet[1] = 'B';
 packet[3] = 29;

 Checksum checkSum = new CRC32();
 checkSum.update(packet, 0, packet.length);
 packet[2] = (byte)checkSum.getValue();

 System.out.println(packet[0] + " " + packet[1] + " " + packet[2] + " " + packet[3]);
 String str = new String(packet);
 System.out.println(str);
 }
 */



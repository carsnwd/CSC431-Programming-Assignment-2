package runner;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * Router class routes messages
 * around to different routers/clients
 *
 * @Author Abe and Carson
 */
public class Router{

	private final String IP_ADDR = "157.160.36.11";
	private final int PORT = 9000;

    //Stores routing table
    private Hashtable<Byte, String> routingTable;

    //Stores socket
    private static ServerSocket SERVER;

    private static Socket CLIENT;

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
            RouterHandlerThread rht = new RouterHandlerThread(incomingConnection, r);
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
    private static class RouterHandlerThread extends Thread
    {
        private Socket connection;
        private Router router;
        private Scanner in;
        private PrintWriter out;
        //Used for sending the packet byte[], can't use Scanner for byte[] :(
        private DataInputStream dis;
        RouterHandlerThread(Socket c, Router r) throws IOException
        {
            this.router = r;
            this.connection = c;
            in = new Scanner(c.getInputStream());
            out = new PrintWriter(c.getOutputStream());
            dis = new DataInputStream(c.getInputStream());
        }

        /**
         * Given a packet, it computes the checksum for
         * the packet with internal Checksum library
         * @param p
         * @return
         */
        private boolean checkCheckSum(byte[] p)
        {
            byte[] packet = p;
            byte[] tempPacket = new byte[5];
            tempPacket[0] = packet[0];
            tempPacket[1] = packet[1];
            tempPacket[3] = packet[3];
            tempPacket[4] = packet[4];
            Checksum checkSum = new CRC32();
            checkSum.update(tempPacket, 0, tempPacket.length);
            byte cc = (byte)checkSum.getValue();
            System.out.println(packet[0] + " " + packet[1] + " " + packet[2] + " " + packet[3] + " " + packet[4]);
            tempPacket[2] = (byte)checkSum.getValue();
            System.out.println(tempPacket[0] + " " + tempPacket[1] + " " + tempPacket[2] + " " + tempPacket[3] + " " + tempPacket[4]);
            if((byte)checkSum.getValue() == packet[2]){
                System.out.println(packet[2] + "," + cc);
                return true;
            }else{
                System.out.println(packet[2] + "," + cc);
                return false;
            }
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
                System.out.println("Packet recieved: " + packet[0] + " " + packet[1] + " " + packet[2] + " " + packet[3] + packet[4]);
                //Get routing table, look up what link to send packet to.
                Hashtable<Byte, String> routingTable = router.getRoutingTable();
                if(checkCheckSum(packet)){
                    String destination = routingTable.get(packet[1]);
                    try {
                        Socket targetRouter = new Socket(destination, 9000);
                        DataOutputStream dos = new DataOutputStream(targetRouter.getOutputStream());
                        dos.write(packet);
                        dos.flush();
                        System.out.println("Forwarding to " + destination);
                        targetRouter.close();
                        dos.close();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Looks good! FORWARD THAT PACKET BOOOOOOOI");
                }else{
                    System.out.println("Checksum invalid!!!!");
                }
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



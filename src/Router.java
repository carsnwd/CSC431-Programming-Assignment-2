import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;

public class Router{
    private Hashtable<Character, String> routingTable;

    private static ServerSocket SERVER;

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
            System.out.println("Here");
            RouterThreadHandler rht = new RouterThreadHandler(incomingConnection, r);
            rht.start();
        }
    }

    public Router()
    {
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

    public Hashtable<Character, String> getRoutingTable()
    {
        return this.routingTable;
    }

    private static class RouterThreadHandler extends Thread
    {
        private Socket connection;
        private Router router;
        private Scanner in;
        private PrintWriter out;
        RouterThreadHandler(Socket c, Router r) throws IOException
        {
            this.router = r;
            this.connection = c;
            in = new Scanner(c.getInputStream());
            out = new PrintWriter(c.getOutputStream());
        }

        public void run()
        {
            out.println("R: Connected to router " + ROUTER_ID + " at " + SERVER.getLocalPort());
            out.flush();
            while(connection.isConnected())
            {
                System.out.println("Waiting for packet...");
                String stringPacket = in.next();
                byte[] packet = stringPacket.getBytes();
                System.out.println("Packet recieved! Routing now...");
                Hashtable<Character, String> routingTable = router.getRoutingTable();
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



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;

public class Router {
    private Hashtable<Character, String> routingTable;

    public ServerSocket server;

    private final int ROUTER_ID = 1;

    public Router(){
        RoutingTableFactory rtf = new RoutingTableFactory();
        try{
            routingTable = rtf.getRoutingTable(ROUTER_ID);
        } catch(RoutingTableFactory.InvalidRouterIDException e){
            e.printStackTrace();
        }
        try {
            server = new ServerSocket(9000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptConnection(){
        while(true){
            Socket incoming;
            try {
                incoming = server.accept();
                RouterHelperThread rht = new RouterHelperThread(incoming, routingTable);
                rht.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRoute(Character dest){
        return routingTable.get(dest);
    }

    /**
     * Handles incoming connections, and reroutes them as needed
     * @author cw3788
     *
     */
    public class RouterHelperThread extends Thread{

        public Socket source; //This is the socket that the message came from
        public String message; //This is the message that was sent through the socket
        private Hashtable<Character, String> routingTable; //This contains all the routes to all the clients

        /**
         * This is the constructor for RouterHelperThread
         * @param source
         * @param tendies
         */
        public RouterHelperThread(Socket source, Hashtable<Character, String> tendies){
            this.source = source;
            this.routingTable = tendies;
        }

        /**
         * This routes the message to the appropriate router or client, by doing a lookup on the
         * hashtable and then creating a socket and sending the message to the appropriate location
         * @param dest
         */
        public void route(Character dest){
            try {
                Socket target = new Socket(routingTable.get(dest), 9000);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(target.getOutputStream()));
                pw.write(message);
                pw.flush();



            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * This picks out the individual sections of the message and verifies that the message is clean
         */
        public void translateMessage(){
            Character src = message.charAt(0);
            Character dest = message.charAt(1);
            Character checksum = message.charAt(2);
            Character data1 = message.charAt(3);
            Character data2 = message.charAt(4);


            int sum = src + dest + checksum + data1 + data2;

            String sumInBinary = Integer.toBinaryString(sum);
            boolean passedChecksum;

            if(sumInBinary.equals("11111111")){
                passedChecksum = true;
            }else{
                passedChecksum = false;
            }


            //verify with checksum
            if(passedChecksum){
                route(dest);
            }

        }

        @Override
        public void run(){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(source.getInputStream()));
                message = br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
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



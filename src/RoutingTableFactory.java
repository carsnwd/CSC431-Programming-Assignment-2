import util.Constants;

import java.util.Hashtable;

/**
 * Generates routing table for different routers.
 * Give router ID, gives back a hashmap with its
 * routing table.
 *
 * @author Carson
 */
public class RoutingTableFactory {
    /**
     * Given router ID, it gets the respective
     * Routing Table as a hashmap.
     * @param router
     * @return
     * @throws InvalidRouterIDException
     */

    private static final byte CLIENT_ID_A = Constants.CLIENT_ID_A;
    private static final byte CLIENT_ID_B = Constants.CLIENT_ID_B;
    private static final byte CLIENT_ID_C = Constants.CLIENT_ID_C;
    private static final byte CLIENT_ID_D = Constants.CLIENT_ID_D;
    private static final String IP_ROUTER1 = Constants.IP_ROUTER1;
    private static final String IP_ROUTER2 = Constants.IP_ROUTER2;
    private static final String IP_ROUTER3 = Constants.IP_ROUTER3;
    private static final String IP_ROUTER4 = Constants.IP_ROUTER4;

    public Hashtable<Byte, String> getRoutingTable(int router) throws InvalidRouterIDException
    {
        Hashtable<Byte, String> routingTable = new Hashtable<>();
        if(router == 1)
        {
            routingTable.put(CLIENT_ID_A,IP_ROUTER1);
            routingTable.put(CLIENT_ID_B, IP_ROUTER2);
            routingTable.put(CLIENT_ID_C, IP_ROUTER2);
            routingTable.put(CLIENT_ID_D, IP_ROUTER4);
            return routingTable;
        }else if(router == 2)
        {
            routingTable.put(CLIENT_ID_A,IP_ROUTER1);
            routingTable.put(CLIENT_ID_B, IP_ROUTER2);
            routingTable.put(CLIENT_ID_C, IP_ROUTER3);
            routingTable.put(CLIENT_ID_D, IP_ROUTER3);
            return routingTable;
        }else if(router == 3)
        {
            routingTable.put(CLIENT_ID_A,IP_ROUTER4);
            routingTable.put(CLIENT_ID_B, IP_ROUTER2);
            routingTable.put(CLIENT_ID_C, IP_ROUTER3);
            routingTable.put(CLIENT_ID_D, IP_ROUTER4);
            return routingTable;
        }else if(router == 4)
        {
            routingTable.put(CLIENT_ID_A,IP_ROUTER1);
            routingTable.put(CLIENT_ID_B, IP_ROUTER1);
            routingTable.put(CLIENT_ID_C, IP_ROUTER3);
            routingTable.put(CLIENT_ID_D, IP_ROUTER4);
            return routingTable;
        }else
        {
            throw new InvalidRouterIDException(router);
        }
    }

    /**
     * Exception for when the router
     * ID given is not valid (1-4)
     */
    public class InvalidRouterIDException extends Exception
    {
        //Routing ID
        private int router;

        public InvalidRouterIDException(int router)
        {
            this.router = router;
        }
        public double getRouter()
        {
            return router;
        }
    }
}

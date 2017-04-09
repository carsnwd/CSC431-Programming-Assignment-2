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
    public Hashtable<Byte, String> getRoutingTable(int router) throws InvalidRouterIDException
    {
        Hashtable<Byte, String> routingTable = new Hashtable<>();
        if(router == 1){
            routingTable.put((byte)11,"157.160.13.168");
            routingTable.put((byte)22, "157.160.70.20");
            routingTable.put((byte)33, "L12");
            routingTable.put((byte)44, "L14");
            return routingTable;
        }else if(router == 2){
            routingTable.put((byte)11,"157.160.143.220");
            routingTable.put((byte)22, "157.160.70.20");
            routingTable.put((byte)33, "L23");
            routingTable.put((byte)44, "L23");
            return routingTable;
        }else if(router == 3){
            routingTable.put((byte)11,"L34");
            routingTable.put((byte)22, "L23");
            routingTable.put((byte)33, "LC");
            routingTable.put((byte)44, "L34");
            return routingTable;
        }else if(router == 4){
            routingTable.put((byte)11,"L14");
            routingTable.put((byte)22, "L14");
            routingTable.put((byte)33, "L34");
            routingTable.put((byte)44, "LD");
            return routingTable;
        }else{
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

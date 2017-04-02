import java.util.Hashtable;

/**
 * Generates routing table for different routers.
 * Give router ID, gives back a hashmap with its
 * routing table.
 *
 * @author Carson
 */
public class RoutingTableFactory {
    public Hashtable<Character, String> getRoutingTable(int router) throws InvalidRouterIDException
    {
        Hashtable<Character, String> routingTable = new Hashtable<Character, String>();
        if(router == 1){
            routingTable.put('A',"LA");
            routingTable.put('B', "L12");
            routingTable.put('C', "L12");
            routingTable.put('D', "L14");
            return routingTable;
        }else if(router == 2){
            routingTable.put('A',"L12");
            routingTable.put('B', "LB");
            routingTable.put('C', "L23");
            routingTable.put('D', "L23");
            return routingTable;
        }else if(router == 3){
            routingTable.put('A',"L34");
            routingTable.put('B', "L23");
            routingTable.put('C', "LC");
            routingTable.put('D', "L34");
            return routingTable;
        }else if(router == 4){
            routingTable.put('A',"L14");
            routingTable.put('B', "L14");
            routingTable.put('C', "L34");
            routingTable.put('D', "LD");
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

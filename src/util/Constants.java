package util;

/**
 * Created by Carson on 4/9/17.
 * Holds Constants that are valuable to the program
 */
public class Constants {
	//These are the IP that are associated with our routers
    public static final String IP_ROUTER1 = "157.160.37.186";
    public static final String IP_ROUTER2 = "157.160.37.173";
    public static final String IP_ROUTER3 = "157.160.37.174";
    public static final String IP_ROUTER4 = "157.160.13.181";

    //This is the routers port
    public static final int PORT = 4446;

    //This is the port the client listens for messages on
    public static final int CLIENT_LISTENER_PORT = 4447;

    //These are the destination and source id codes
    public static final byte CLIENT_ID_A = (byte)11;
    public static final byte CLIENT_ID_B = (byte)22;
    public static final byte CLIENT_ID_C = (byte)33;
    public static final byte CLIENT_ID_D = (byte)44;

}

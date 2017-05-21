package pl.ark.chr.buginator.util;

/**
 * Created by Arek on 2017-05-21.
 */
public class NetworkUtil {

    private static String hostIP;
    private static int hostPort;

    public static String getHostIP() {
        return hostIP;
    }

    public static void setHostIP(String hostIP) {
        NetworkUtil.hostIP = hostIP;
    }

    public static int getHostPort() {
        return hostPort;
    }

    public static void setHostPort(int hostPort) {
        NetworkUtil.hostPort = hostPort;
    }
}

package pl.ark.chr.buginator.commons.util;

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

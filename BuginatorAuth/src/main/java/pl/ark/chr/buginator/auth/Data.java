package pl.ark.chr.buginator.auth;

public class Data {

    private static ThreadLocal<String> data = new ThreadLocal<>();

    public static void setData(String tenantName) {
        data.set(tenantName);
    }

    public static String getData() {
        return data.get();
    }
}

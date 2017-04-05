package kohler.zero.jvm;

/**
 * Created by Kohler on 2017/3/23.
 */
public class Main01 {
    public static void main(String[] args) {
        /*-Xmx20m -Xms5m -XX:+PrintCommandLineFlags -XX:+PrintGCDetails -XX:+UseSerialGC*/
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println(Runtime.getRuntime().freeMemory());
        System.out.println(Runtime.getRuntime().totalMemory());

        byte[] b1 = new byte[1024 * 1024];

        System.out.println("use 1M");
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println(Runtime.getRuntime().freeMemory());
        System.out.println(Runtime.getRuntime().totalMemory());

        byte[] b2 = new byte[4 * 1024 * 1024];

        System.out.println("use 4M");
        System.out.println(Runtime.getRuntime().maxMemory());
        System.out.println(Runtime.getRuntime().freeMemory());
        System.out.println(Runtime.getRuntime().totalMemory());

    }
}

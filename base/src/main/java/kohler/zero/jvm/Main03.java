package kohler.zero.jvm;

/**
 * Created by Kohler on 2017/3/23.
 */
public class Main03 {
    private static int count = 0;

    public static void recursion() {

        count++;
        recursion();
    }

    public static void main(String[] args) {
//        -Xss1m 栈空间
        /*try {
            recursion();
        } catch (Throwable e) {
            System.out.println(">>>>>>>>" + count);
            e.printStackTrace();
        }*/

//        -XX:PremSize=5m -XX:MaxPremSize=5m(-XX:MetaspaceSize=5m -XX:MaxMetaspaceSize=5m)

//        System.out.println("some thing");
        new Thread(Main03::recursion).start();
        new Thread(Main03::recursion).start();
        new Thread(() -> {
            byte[]  b1 = new byte[1024];
            while (true) {
//                System.out.println(b1.length);
            }
        }).start();
    }
}

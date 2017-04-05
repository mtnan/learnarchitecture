package kohler.zero.sync;

import java.util.concurrent.Exchanger;

/**
 * Created by Kohler on 2017/3/23.
 */
public class UseExchanger {
    public static void main(String[] args) {

        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                String t1 = exchanger.exchange("t1");
                System.out.println("in t1:" + t1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                String t2 = exchanger.exchange("t2");
                System.out.println("in t2:" + t2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}

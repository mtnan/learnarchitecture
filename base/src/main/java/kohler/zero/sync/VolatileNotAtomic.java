package kohler.zero.sync;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kohler on 2017/3/20.
 */
public class VolatileNotAtomic extends Thread {
    private static volatile int value = 0;
    private static AtomicInteger count = new AtomicInteger(0);

    private void add() {
        for (int i = 0; i < 1000; i++) {
//            value++;
            count.incrementAndGet();
        }

//        System.out.println(value);
        System.out.println(count.get());
    }

    @Override
    public void run() {
        add();
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileNotAtomic[] arrs = new VolatileNotAtomic[10];
        for (int i = 0; i < 10; i++) {
            arrs[i] = new VolatileNotAtomic();
        }

        for (VolatileNotAtomic tmp : arrs) {
            tmp.start();
        }

//        Thread.sleep(1000);
//        System.out.println(VolatileNotAtomic.value);
    }
}

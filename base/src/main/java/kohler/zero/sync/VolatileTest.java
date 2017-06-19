package kohler.zero.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by hao on 2017/6/19.
 */
public class VolatileTest {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 10000; i++) {
                counter.inc();
            }
        }).start();

        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 10000; i++) {
                counter.inc();
            }
        }).start();

        latch.countDown();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(counter.getCount());
    }
}

class Counter {
    private volatile int count;

    public int getCount() {
        return count;
    }

    public synchronized void inc() {
        this.count++;
    }
}
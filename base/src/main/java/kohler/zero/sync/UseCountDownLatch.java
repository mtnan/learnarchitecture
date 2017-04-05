package kohler.zero.sync;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Kohler on 2017/3/24.
 */
public class UseCountDownLatch {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("count1");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("count2");
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("count3");
            countDownLatch.countDown();
        }).start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("finished");
    }
}

package kohler.zero.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Kohler on 2017/3/24.
 */
public class UseCyclicBarrier {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        new Thread(() -> {
            System.out.println("before t1");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("after t1");
        }).start();

        new Thread(() -> {
            System.out.println("before t2");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("after t2");
        }).start();

        new Thread(() -> {
            System.out.println("before t3");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            System.out.println("after t3");
        }).start();
    }
}

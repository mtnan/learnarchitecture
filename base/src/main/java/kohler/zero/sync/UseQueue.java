package kohler.zero.sync;

import java.util.PriorityQueue;

/**
 * Created by Kohler on 2017/3/21.
 */
public class UseQueue {
    public static void main(String[] args) throws InterruptedException {
        /*SynchronousQueue<String> q = new SynchronousQueue<>();

        Thread t1 = new Thread(() -> {
            try {
                String take = q.take();
                System.out.println(take);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();

        TimeUnit.SECONDS.sleep(1);

        Thread t2 = new Thread(() -> {
            q.offer("t2 put");
        });

        t2.start();*/

        PriorityQueue<String> q = new PriorityQueue<>();


    }

}

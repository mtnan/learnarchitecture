package kohler.zero.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by Kohler on 2017/3/21.
 * LockSupport是不可重入的
 * 响应中断，但是不会抛出InterruptedException。
 * 可以先unpark 再 park
 */
public class UseLockSupport {
    public void block() {
        System.out.println("into block");
        LockSupport.park();
        System.out.println("out block");
    }

    public static void main(String[] args) throws InterruptedException {
        UseLockSupport useLockSupport = new UseLockSupport();

        Thread t = new Thread(() -> {
            useLockSupport.block();
        });

        t.start();
        System.out.println("main thread running");
        TimeUnit.SECONDS.sleep(1);
//        LockSupport.unpark(t);
        t.interrupt();
    }
}

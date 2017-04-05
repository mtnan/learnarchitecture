package kohler.zero.sync;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Kohler on 2017/3/21.
 * 相同：ReentrantLock提供了synchronized类似的功能和内存语义，但是添加了类似锁投票、定时锁等候和可中断锁等候的一些特性。
 * 不同：
 * 1）ReentrantLock功能性方面更全面，比如时间锁等候，可中断锁等候，锁投票等，因此更有扩展性。在多个条件变量和高度竞争锁的地方，用ReentrantLock更合适，ReentrantLock还提供了Condition，对线程的等待和唤醒等操作更加灵活，一个ReentrantLock可以有多个Condition实例，所以更有扩展性。
 * 2）ReentrantLock 的性能比synchronized会好点。
 * 3）ReentrantLock提供了可轮询的锁请求，他可以尝试的去取得锁，如果取得成功则继续处理，取得不成功，可以等下次运行的时候处理，所以不容易产生死锁，而synchronized则一旦进入锁请求要么成功，要么一直阻塞，所以更容易产生死锁。
 * Condition的await()和signal()的使用和Object 的wait()和notify()相似，都需要先加锁
 *
 */
public class UseLock {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private boolean running = true;

    public void lock() {
        System.out.println("lock");

        lock.lock();
        try {
            System.out.println("sleep 3s");
            TimeUnit.SECONDS.sleep(3);

            running = false;
            condition.signal();

            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        System.out.println("out lock");
    }

    public void unlock() {
        System.out.println("unlock");

        lock.lock();
        try {

            while (running) {
                condition.await();
                System.out.println("after await");
            }

            System.out.println("wake up");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void sync() {
        System.out.println("in sync");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("out sync");
    }

    public static void main(String[] args) throws InterruptedException {
        UseLock useLock = new UseLock();

        new Thread(() -> {
            useLock.unlock();
        }).start();

        new Thread(() -> {
            useLock.lock();
        }).start();

        new Thread(() -> {
            useLock.sync();
        }).start();

    }
}

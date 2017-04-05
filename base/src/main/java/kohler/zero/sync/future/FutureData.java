package kohler.zero.sync.future;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kohler on 2017/3/21.
 * 1、wait配合notify使用线程阻塞，需要加synchronized
 * 2、这里可以直接使用volatile完成，非阻塞
 * 3、AtomicBoolean
 * 4、Lock
 * 5、LockSupport
 */
public class FutureData implements Data {
    private RealData realData;
    private volatile boolean running = true;
//    private AtomicBoolean running  = new AtomicBoolean(true);

    public RealData getRealData() {
        return realData;
    }

    public /*synchronized*/ void setRealData(RealData realData) {
        if (!running) {
            return;
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.realData = realData;
        running = false;
//        running.set(false);
//        notify();
    }

    @Override
    public /*synchronized*/ String request() {
        while (running) {
            /*try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        return this.realData.request();
    }
}

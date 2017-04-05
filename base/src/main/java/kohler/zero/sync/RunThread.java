package kohler.zero.sync;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kohler on 2017/3/20.
 */
public class RunThread extends Thread{
    /**
     * 线程独立内存会复制一份主内存中的变量
     */
    public /*volatile*/ boolean running = true;

    @Override
    public void run() {
        System.out.println("start thread");

        while (running) {
//            系统调用会干扰？
//            System.out.println("---");
        }

        System.out.println("stoped");
    }

    public static void main(String[] args) throws Exception {
        RunThread runThread = new RunThread();
        runThread.start();

        TimeUnit.SECONDS.sleep(1);

        runThread.running = false;

//        Thread.sleep(1000);

        System.out.println(runThread.running);

    }

}

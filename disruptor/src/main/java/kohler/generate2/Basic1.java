package kohler.generate2;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import kohler.generate1.Trade;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kohler on 2017/4/6.
 */
public class Basic1 {
    public static void main(String[] args) throws InterruptedException {
        long beginMillis = System.currentTimeMillis();
        int buffSize = 1024;

        ExecutorService executor = Executors.newFixedThreadPool(4);
        Disruptor<Trade> disruptor = new Disruptor<Trade>(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, buffSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());

        EventHandlerGroup<Trade> handlerGroup = disruptor.handleEventsWith(new EventHandler1(), new EventHandler2());
        handlerGroup.then(new EventHandler3());

        disruptor.start();

        CountDownLatch countDownLatch = new CountDownLatch(1);

        TradePublisher tradePublisher = new TradePublisher(countDownLatch, disruptor);
        executor.submit(tradePublisher);

        countDownLatch.await();

        disruptor.shutdown();
        executor.shutdown();

        System.out.println("use time => " + (System.currentTimeMillis() - beginMillis));

    }
}

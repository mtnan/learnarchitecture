package kohler.generate1;

import com.lmax.disruptor.*;

import java.util.concurrent.*;

/**
 * Created by Kohler on 2017/4/6.
 */
public class Basic2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int buffer_size = 1024;
        int thread_numbers = 4;

        RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(new EventFactory<Trade>() {
            @Override
            public Trade newInstance() {
                return new Trade();
            }
        }, buffer_size, new YieldingWaitStrategy());

        ExecutorService executorService = Executors.newFixedThreadPool(thread_numbers);

        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        TradeHandler tradeHandler = new TradeHandler();

        WorkerPool<Trade> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), tradeHandler);

//        消息处理器
        BatchEventProcessor<Trade> transProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new TradeHandler());

        workerPool.start(executorService);

        for (int i = 0; i < 8; i++) {
            long seq = ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random() * 999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        transProcessor.halt();//通知事件（消息）处理器 可以技术（并不是马上结束）
        executorService.shutdown();

    }
}

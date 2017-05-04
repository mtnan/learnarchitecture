package kohler.generate1;

import com.lmax.disruptor.*;

import java.util.concurrent.*;

/**
 * Created by Kohler on 2017/4/6.
 */
public class Basic1 {
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

//        消息处理器
        BatchEventProcessor<Trade> transProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new TradeHandler());

//        目的是把消费者的位置信息引用注入到生产者  如果是只有一个生产者的情况可以省略
        ringBuffer.addGatingSequences(transProcessor.getSequence());

//        把消息处理器提交到线程池
        executorService.submit(transProcessor);

        Future<Void> future = executorService.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    seq = ringBuffer.next();
                    ringBuffer.get(seq).setPrice(Math.random() * 9999);
                    ringBuffer.publish(seq);
                }
                return null;
            }
        });

        future.get();//等待生产者结束
        Thread.sleep(1000);//等1秒，等待消费处理完成
        transProcessor.halt();//通知事件（消息）处理器 可以技术（并不是马上结束）
        executorService.shutdown();
    }
}

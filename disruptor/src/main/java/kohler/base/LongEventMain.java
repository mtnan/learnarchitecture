package kohler.base;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Kohler on 2017/4/6.
 */
public class LongEventMain {
    public static void main(String[] args) {
        LongEventFactory factory = new LongEventFactory();

        ThreadFactory threadFactory = Executors.defaultThreadFactory();

//        2的N次方
        int ringBufferSize = 1024 * 1024;

//        1.工厂类对象，用户创建LongEvent，LongEvent是实际消费的数据
//        2.缓冲区大小
//        3.线程创建工厂
//        4.生产者是单个还是多个
//        5.生产和消费的策略
        Disruptor<LongEvent> disruptor = new Disruptor<>(factory, ringBufferSize, threadFactory, ProducerType.SINGLE, new YieldingWaitStrategy());
//        连接消费事件方法
        disruptor.handleEventsWith(new LongEventHandler());

        disruptor.start();
//        存放数据的容器ringBuffer（环形结构）
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

//        LongEventProducer producer = new LongEventProducer(ringBuffer);
//        内部简化提交流程
        LongEventProduceWithTranslator producer = new LongEventProduceWithTranslator(ringBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(8);

        for (int i = 0; i < 100; i++) {
            byteBuffer.putLong(0, i);
            producer.onData(byteBuffer);
        }

        disruptor.shutdown();
    }
}

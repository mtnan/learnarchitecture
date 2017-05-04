package kohler.base;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Created by Kohler on 2017/4/6.
 */
public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer buffer) {
//        下一个事件槽
        long sequence = ringBuffer.next();

        try {
//            用上面的索引取出一个空的事件用于填充（获取该序号对应的事件对象）
            LongEvent event = ringBuffer.get(sequence);
//            获取要通过事件传递的业务数据
            event.setValue(buffer.getLong(0));
        } finally {
//            发布事件
//            最后的publish方法必须包含在finally中以确保必须得到调用，如果某个请求的sequence未被提交，
            ringBuffer.publish(sequence);
        }
    }
}

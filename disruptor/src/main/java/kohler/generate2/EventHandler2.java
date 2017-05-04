package kohler.generate2;

import com.lmax.disruptor.EventHandler;
import kohler.generate1.Trade;

/**
 * Created by Kohler on 2017/4/6.
 */
public class EventHandler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        event.setName(Thread.currentThread().getName());
        System.out.println("handler2 => " + event.getPrice());
    }
}

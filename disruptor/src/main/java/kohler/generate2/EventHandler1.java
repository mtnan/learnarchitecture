package kohler.generate2;

import com.lmax.disruptor.EventHandler;
import kohler.generate1.Trade;

/**
 * Created by Kohler on 2017/4/6.
 */
public class EventHandler1 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        event.setPrice(15);
        System.out.println("handler1 => " + event.getPrice());
    }
}

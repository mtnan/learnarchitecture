package kohler.generate2;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;
import kohler.generate1.Trade;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Kohler on 2017/4/6.
 */
public class TradePublisher implements Runnable {
    private CountDownLatch countDownLatch;
    private Disruptor<Trade> disruptor;

    private static int loop = 10;

    public TradePublisher(CountDownLatch countDownLatch, Disruptor<Trade> disruptor) {
        this.countDownLatch = countDownLatch;
        this.disruptor = disruptor;
    }

    @Override
    public void run() {
        TradeEventTranslator tradeEventTranslator = new TradeEventTranslator();

        for (int i = 0; i < loop; i++) {
            disruptor.publishEvent(tradeEventTranslator);
        }

        countDownLatch.countDown();
    }
}

class TradeEventTranslator implements EventTranslator<Trade> {

    private Random random = new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private Trade generateTrade(Trade trade) {
        trade.setPrice(random.nextDouble() * 9999);

        return trade;
    }
}

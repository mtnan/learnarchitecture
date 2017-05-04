package kohler.base;

import com.lmax.disruptor.EventFactory;

/**
 * Created by Kohler on 2017/4/6.
 */
public class LongEventFactory implements EventFactory {
    @Override
    public Object newInstance() {
        return new LongEvent();
    }
}

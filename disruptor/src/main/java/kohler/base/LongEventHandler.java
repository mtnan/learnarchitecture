package kohler.base;

import com.lmax.disruptor.EventHandler;

/**
 * Created by Kohler on 2017/4/6.
 */
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b) throws Exception {
//        处理数据
        System.out.println(longEvent.getValue());
    }
}

package kohler.zero.sync.future;

/**
 * Created by Kohler on 2017/3/21.
 */
public class FutureClient {
    public Data request(String val) {
        FutureData futureData = new FutureData();

        new Thread(() -> {
            RealData realData = new RealData(val);
            futureData.setRealData(realData);
        }).start();

        return futureData;
    }
}

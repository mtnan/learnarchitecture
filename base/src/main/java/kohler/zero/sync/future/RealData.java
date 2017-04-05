package kohler.zero.sync.future;

/**
 * Created by Kohler on 2017/3/21.
 */
public class RealData implements Data {
    private String val;

    public RealData(String val) {
        this.val = val;
    }

    @Override
    public synchronized String request() {
        return "real data back " + val;
    }
}

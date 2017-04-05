package kohler.zero.sync.future;

/**
 * Created by Kohler on 2017/3/21.
 */
public class Main {
    public static void main(String[] args) {
        FutureClient client = new FutureClient();
        Data futureData = client.request("request future data");
        System.out.println("main thread");

        String res = futureData.request();
        System.out.println(res);
    }
}

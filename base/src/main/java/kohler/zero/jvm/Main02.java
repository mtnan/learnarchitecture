package kohler.zero.jvm;

import java.util.Vector;

/**
 * Created by Kohler on 2017/3/23.
 */
public class Main02 {
    public static void main(String[] args) {
        //-Xmx2m -Xms2m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=h:/test03.dump

        Vector v = new Vector();

        for (int i = 0; i < 5; i++) {
            v.add(new byte[1024 * 1024]);
        }
    }
}

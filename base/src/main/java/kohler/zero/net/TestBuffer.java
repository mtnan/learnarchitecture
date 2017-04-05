package kohler.zero.net;

import java.nio.IntBuffer;

/**
 * Created by Kohler on 2017/3/25.
 */
public class TestBuffer {
    public static void main(String[] args) {
/*
        IntBuffer buffer = IntBuffer.allocate(10);
        buffer.put(24);
        buffer.put(34);
        buffer.put(65);

//        把位置复位为0，也就是position位置：3->0
        buffer.flip();

        System.out.println(buffer);
        System.out.println(buffer.capacity());
        System.out.println(buffer.limit());

        System.out.println(buffer.get(1));
//        get不改变位置
        System.out.println(buffer);

        buffer.put(1, 4);
//        put不改变位置
        System.out.println(buffer);

//        应循环limit
        for (int i = 0; i < buffer.limit(); i++) {
//            get方法取position位置的值，并将position递增一位
            System.out.println(buffer.get());
//            System.out.println(buffer.get(i));
        }
*/

/*
        int[] arr = new int[]{1,2,5};
        IntBuffer buf1 = IntBuffer.wrap(arr);
        System.out.println(buf1);

        IntBuffer buf2 = IntBuffer.wrap(arr, 0, 2);
        System.out.println(buf2);
*/

        IntBuffer buf1 = IntBuffer.allocate(10);
        int[] ar = new int[]{1,2,4};
        buf1.put(ar);
        System.out.println(buf1);

        IntBuffer duplicate = buf1.duplicate();
        System.out.println(duplicate);

        buf1.flip();
        System.out.println(buf1);
        System.out.println("可读数据为：" + buf1.remaining());
        int[] arr2 = new int[buf1.remaining()];

        buf1.get(arr2);
        /*for (int i = 0; i < buf1.limit(); i++) {
            arr2[i] = buf1.get();
        }*/

        for (int i : arr2) {
            System.out.println(i);
        }
    }
}

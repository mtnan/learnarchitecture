package kohler.generate1;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kohler on 2017/4/6.
 */
@Data
public class Trade {
    private String id;
    private String name;
    private double price;
    private AtomicInteger count = new AtomicInteger();
}

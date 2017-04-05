package kohler.dubbo.sample.provider;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Kohler on 2017/3/26.
 */
@Data
public class User implements Serializable {
    private String name;
    private int age;
    private String sex;

}

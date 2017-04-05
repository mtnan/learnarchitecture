package kohler.dubbo.sample.provider;

import java.util.List;

/**
 * Created by Kohler on 2017/3/26.
 */
public interface SampleService {
    String sayHello(String name);
    public List getUsers();
}

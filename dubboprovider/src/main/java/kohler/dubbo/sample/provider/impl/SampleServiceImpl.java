package kohler.dubbo.sample.provider.impl;

import kohler.dubbo.sample.provider.SampleService;
import kohler.dubbo.sample.provider.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kohler on 2017/3/26.
 */
public class SampleServiceImpl implements SampleService {
    public String sayHello(String name) {
        return "hello " + name;
    }

    public List getUsers() {
        List<User> users = new ArrayList<User>();
        User u1 = new User();
        u1.setName("jack");
        u1.setAge(22);
        u1.setSex("m");

        users.add(u1);

        User u2 = new User();
        u2.setName("rose");
        u2.setAge(21);
        u2.setSex("f");

        users.add(u2);

        User u3 = new User();
        u3.setName("gaylun");
        u3.setAge(25);
        u3.setSex("m");

        users.add(u3);

        return users;
    }
}

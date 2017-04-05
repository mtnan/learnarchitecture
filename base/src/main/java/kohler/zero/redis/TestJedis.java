package kohler.zero.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Kohler on 2017/3/19.
 */
public class TestJedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.1.129", 6379);
//        Jedis jedis = new Jedis("127.0.0.1", 6379);

        jedis.set("name", "z3");
        String name = jedis.get("name");
        System.out.println(name);
        jedis.del("name");
        System.out.println(jedis.get("name"));

        jedis.del("user");
        System.out.println(jedis.lpush("user", "z3", "l4", "w5"));
        System.out.println(jedis.lrange("user", 0, 2));
        System.out.println(jedis.llen("user"));
        jedis.set("age", "26");

    }
}

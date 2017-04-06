package kohler.zero.redis;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kohler on 2017/3/19.
 */
public class TestJedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.1.129", 6379);
//        Jedis jedis = new Jedis("127.0.0.1", 6379);

        jedis.set("name", "z3");

        /******************** Key命令 开始 ********************/
        System.out.println("\n************系统命令***************");

        System.out.println("exists => " + jedis.exists("name")); //是否存在，-2不存在，-1没有设置过期时间
//        jedis.del("name"); // 删除
        jedis.expire("name", 10); // 单独设置过期时间，秒级
//        jedis.pexpire("name", 10000L);//过期时间，毫秒级
        System.out.println("ttl => " + jedis.ttl("name"));//查看剩余过期时间，秒级
        System.out.println("pttl => " + jedis.pttl("name"));//查看剩余过期时间，毫秒级

        jedis.set("age", "30");
        jedis.expireAt("age", System.currentTimeMillis() / 1000);//到点过期，单位unix time
//        jedis.pexpireAt("age", System.currentTimeMillis());

        Set<String> keys = jedis.keys("*");// 所有的键
        System.out.println("randomkey =>" + jedis.randomKey());// 随机一个键

        jedis.set("oldkey", "old_value");
        jedis.set("newkey", "new_value");
//        jedis.rename("oldkey", "newkey");//旧key不存在报错，newkey已经存在时，RENAME命令将覆盖旧值
        jedis.renamenx("oldkey", "newkey");//旧值不存在报错，newkey存在时，不处理
        System.out.println("old value => " + jedis.get("oldkey"));
        System.out.println("new value =>" + jedis.get("newkey"));

//        dump, restore 序列化和反序列化

        jedis.del("score");
        jedis.lpush("score", "20", "3", "17"); //数字
        List<String> ages = jedis.sort("score");
        for (String s : ages) {
            System.out.println(s);
        }

        System.out.println("type => " + jedis.type("score")); //类型

        // scan: 迭代
        /******************** Key命令 结束 ********************/

        /******************** String命令 开始 ********************/
        System.out.println("\n************字符串操作***************");
        // string操作
        jedis.set("name", "zhangsan");
//        jedis.setnx("", "") //键不存在时设置
        jedis.setex("age", 20, "25");//设置过期事件，单位为秒
        jedis.psetex("age", 20000L, "25");//设置过期事件，单位为毫秒
        System.out.println(jedis.ttl("age"));
//        jedis.setrange 设置子串

        System.out.println("get => " + jedis.get("name"));
        System.out.println("strlen => " + jedis.strlen("name"));//字符串长度
        System.out.println("getrange => " + jedis.getrange("name", 0, 3));//子串，负数偏移量表示从字符串最后开始计数，-1表示最后一个字符，-2表示倒数第二个
        System.out.println("getSet => " + jedis.getSet("name", "lisi"));//将给定key的值设为value，并返回 key 的旧值(old value)。
        jedis.append("name", "~");
        System.out.println("append => " + jedis.get("name"));//追加

        jedis.mset("name1", "zhangsan", "name2", "lisi", "name3", "wangwu");//同时设置一个或多个 key-value 对。
//        jedis.msetnx() //同上，当键不存在时设置
        List<String> names = jedis.mget("name1", "name2", "name3");//返回所有(一个或多个)给定key的值。
        System.out.println("mget => " + names);

        //位操作
        System.out.println("\n************位操作***************");
        jedis.setbit("bit", 10086, true);//设置比特位值
        System.out.println("bit value => " + jedis.getbit("bit", 10086)); //获取bit位
        System.out.println("bit value => " + jedis.getbit("bit", 1));
        System.out.println("bitcount => " + jedis.bitcount("bit"));//统计比特位为true的值

        jedis.setbit("bit-2", 1, true);
        jedis.setbit("bit-3", 2, true);

        jedis.bitop(BitOP.AND, "res-bit", "bit-2", "bit-3");//位操作，结果保存在第二个参数
        System.out.println("bit result => " + jedis.getbit("res-bit", 1));

        //BITFIELD 命令可以将一个 Redis 字符串看作是一个由二进制位组成的数组， 并对这个数组中储存的长度不同的整数进行访问 （被储存的整数无需进行对齐）

        System.out.println("\n************数字加减操作***************");
        // 数字
        jedis.set("count", "5");
        System.out.println("get => " + jedis.get("count"));
        System.out.println("decr => " + jedis.decr("count"));
        System.out.println("decrBy => " + jedis.decrBy("count", 2));
        System.out.println("incr => " + jedis.incr("count"));
        System.out.println("incrBy => " + jedis.incrBy("count", 5));
        System.out.println("incrByFloat => " + jedis.incrByFloat("count", 5.5));

        /******************** String命令 结束 ********************/
        
        /******************** hash操作 开始 ********************/
        System.out.println("\n************hash操作***************");
        jedis.hset("hashset", "name", "zhangsan");
        jedis.hset("hashset", "age", "25");//设值
        jedis.hsetnx("hashset", "age", "25");//不存在则设置
        Map<String, String> map = new HashMap<>();
        map.put("grade", "3");
        map.put("class", "12");
        map.put("score", "100");
        jedis.hmset("hashset", map); //批量设置

        System.out.println("hkeys => " + jedis.hkeys("hashset"));//所有的键
        System.out.println("hvals => " + jedis.hvals("hashset"));//所有的值
        System.out.println("hlen => " + jedis.hlen("hashset"));//长度
        System.out.println("hexists => " + jedis.hexists("hashset", "name"));//判断是否存在
        System.out.println("hget => " + jedis.hget("hashset", "name"));//获取值
        System.out.println("hmget => " + jedis.hmget("hashset", "name", "grade"));//获取多个值

        System.out.println("hgetAll => " + jedis.hgetAll("hashset"));//获取所有键值对
        System.out.println("hincrBy => " + jedis.hincrBy("hashset", "score", 10)); //操作数字
        System.out.println("hincrByFloat => " + jedis.hincrByFloat("hashset", "score", 5.5)); //操作数字

        jedis.hdel("hashset", "score");//删除

        /******************** hash操作 结束 ********************/

        /******************** list操作 开始 ********************/
        System.out.println("\n************list操作***************");
        jedis.del("users");

        jedis.lpush("users", "zhangsan");
        jedis.lpush("users", "lisi");//left push 从左添加
        jedis.lpush("users", "wang5");//left push 从左添加
        jedis.lpush("users", "zhao6");//left push 从左添加
        jedis.rpush("users", "zhao four");
        jedis.rpush("users", "liuneng");//right push 从右添加

        System.out.println("lrange => " + jedis.lrange("users", 0, -1));//取范围
        System.out.println("llen => " + jedis.llen("users"));//长度
        System.out.println("lindex 2 => " + jedis.lindex("users", 2));//索引所在的值
        jedis.linsert("users", BinaryClient.LIST_POSITION.AFTER, "zhao4", "big head liu");//插值
        jedis.lset("users", 0, "big foot lee");//索引设值
        System.out.println("lrange => " + jedis.lrange("users", 0, -1));

        //出栈
        System.out.println("lpop => " + jedis.lpop("users"));
//        System.out.println("rpop => " + jedis.rpop("users"));
        System.out.println("blpop => " + jedis.blpop(0, "users")); //阻塞的pop
//        System.out.println("brpop => " + jedis.brpop(0, "users")); //阻塞的pop

        System.out.println("lrange => " + jedis.lrange("users", 0, -1));
        System.out.println(jedis.rpoplpush("users", "users2")); //弹出最后一个元素并返回，弹出的元素插入到列表 destination ，作为 destination 列表的的头元素
//        System.out.println(jedis.brpoplpush("users", "users2", 0));//阻塞版rpoplpush
        System.out.println("users2 => " + jedis.lrange("users2", 0, -1));
        jedis.del("users2");

        jedis.lpushx("tmp_users", "wang");//添加到已存在列表，列表不存在则不添加
        jedis.rpushx("tmp_users", "wang");//添加到已存在列表，列表不存在则不添加
        System.out.println("exists tmp_users => " + jedis.exists("tmp_users"));

        jedis.lrem("users", 5, "zhangsan");//删除，count > 0，从左删，删除count个，count = 0全删，count < 0，从右删，删除|count|个
        System.out.println("lrange => " + jedis.lrange("users", 0, -1));
        jedis.ltrim("users", 0, 1);//trim修剪
        System.out.println("lrange => " + jedis.lrange("users", 0, -1));

        /******************** list操作 结束 ********************/

        /******************** set操作 开始 ********************/
        System.out.println("\n************set操作***************");
        jedis.sadd("fruits", "apple", "orange", "banana", "pineapple", "strawberry");//添加元素

        System.out.println("scard => " + jedis.scard("fruits"));//元素数量
        System.out.println("sunion => " + jedis.smembers("fruits"));//返回集合所有成员

        jedis.sadd("fruits2", "apple", "orange");
        System.out.println("sdiff => " + jedis.sdiff("fruits", "fruits2"));//差集，fruits - fruits2
        jedis.sdiffstore("fruits3", "fruits", "fruits2");//fruits3 = fruits - fruits2
        System.out.println("fruits3 => " + jedis.smembers("fruits3"));
//        jedis.sinter("");//所有集合的交集
//        jedis.sinterstore("", "");//所有集合的交集，存到dst
//        jedis.sunion("");//集合的并集
//        jedis.sunionstore("", "");//集合的并集，存到dst

        System.out.println("sismember => " + jedis.sismember("fruits", "or"));//是否存在

//        jedis.smove()//将 member 元素从 source 集合移动到 destination 集合
        System.out.println("spop => " + jedis.spop("fruits3"));//pop
        System.out.println("srandmember => " + jedis.srandmember("fruits3")); //随机
//        jedis.srem("fruits3", "pineapple"); //删除

        /******************** SortedSet 开始 ********************/
        System.out.println("\n************SortedSet操作***************");
        jedis.zadd("students", 95, "zhangsan");
        jedis.zadd("students", 84, "lisi");
        jedis.zadd("students", 89, "wangwu");

        System.out.println(jedis.zcard("students"));
        System.out.println(jedis.zcount("students", 80, 90));
        System.out.println(jedis.zincrby("students", 2, "lisi"));
        System.out.println(jedis.zrange("students", 0, -1));
        System.out.println(jedis.zrangeByScore("students", 80, 90));
        System.out.println(jedis.zrank("students", "lisi"));
        System.out.println(jedis.zrevrank("students", "lisi"));
        System.out.println(jedis.zscore("students", "lisi"));

//        jedis.zunionstore("", "");//并集并存储到dst
//        jedis.zinterstore("", "");//交集并存储到dst
//        jedis.zrem("students", "");//删除
//        jedis.zremrangeByRank()
//        jedis.zremrangeByLex()
//        jedis.zremrangeByScore()

        /******************** SortedSet 结束 ********************/
    }
}

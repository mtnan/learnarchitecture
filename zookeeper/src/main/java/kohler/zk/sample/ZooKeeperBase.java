package kohler.zk.sample;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Kohler on 2017/3/27.
 */
public class ZooKeeperBase {
    public static final String CONNECT_ADDR = "192.168.1.129:2181,192.168.1.130:2181,192.168.1.131:2181";
    public static final int SESSION_OUTTIME = 5000; //ms
    public static final CountDownLatch LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, watchedEvent -> {
            Watcher.Event.KeeperState zkStatus = watchedEvent.getState();
            Watcher.Event.EventType type = watchedEvent.getType();

            if (Watcher.Event.KeeperState.SyncConnected == zkStatus) {
                if (Watcher.Event.EventType.None == type) {
                    LATCH.countDown();
                    System.out.println("zk connect started !");
                }
            }
        });

        LATCH.await();

        System.out.println("...");

//        不允许递归创建子节点
//        String ret = zk.create("/testRoot", "testRoot".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        System.out.println(ret);

//        EPHEMERAL临时节点，生命周期为会话
//        C1端创建一个节点，C2就不能创建同名节点，可用于分布式锁
//        zk.create("/testRoot/child", "child data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

//        版本号-1为跳过版本检查，不支持递归删除
        /*zk.delete("/testRoot", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println(rc);
                System.out.println(path);
//                传入的context，这里是"a"
                System.out.println(ctx);
            }
        }, "a");
        Thread.sleep(5000);*/

//        zk.setData("/testRoot", "test002".getBytes(), -1);
//        byte[] data = zk.getData("/testRoot", false, null);
//        System.out.println(new String(data));

//        zk.create("/testRoot/c1", "child1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zk.create("/testRoot/c2", "child2".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        zk.create("/testRoot/c3", "child3".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        /*List<String> children = zk.getChildren("/testRoot", false);

        for (String child : children) {
            System.out.println("child path: " + child);
            String realPath = "/testRoot/" + child;

            System.out.println(new String(zk.getData(realPath, false, null)));
        }*/

        Stat stat = zk.exists("/testRoot/c2", false);
        if (stat != null) {
            System.out.println(stat.getVersion());
            System.out.println(stat.getDataLength());
            System.out.println(stat.getNumChildren());
            System.out.println(stat.getMzxid());
        }

        zk.close();
    }

}

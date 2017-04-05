package kohler.zk.sample;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Kohler on 2017/3/27.
 */
public class ZooKeeperWatcher implements Watcher {
    AtomicInteger seq = new AtomicInteger();
    private static final int SESSION_TIMEOUT = 10000;
    private static final String CONNECTION_ADDR = "192.168.1.129:2181,192.168.1.130:2181,192.168.1.131:2181";
    private static final String PARENT_PATH = "/p";
    private static final String CHILD_PATH = "/p/c";
    private static final String LOG_PREFIX_OF_MAIN = "[main]";
    private static ZooKeeper zk = null;
    public static CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("into process ... event = " + watchedEvent);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (watchedEvent == null) {
            return;
        }

        Event.KeeperState state = watchedEvent.getState();
        Event.EventType type = watchedEvent.getType();

        String path = watchedEvent.getPath();
        String logPrefix = "[Watcher-" + this.seq.incrementAndGet() + "]";

        System.out.println(logPrefix + " receive watcher notice");
        System.out.println(logPrefix + " state: \t" + state.toString());
        System.out.println(logPrefix + " type: \t" + type.toString());

        if (Event.KeeperState.SyncConnected == state) {
            if (Event.EventType.None == type) {
                System.out.println("None");
            } else if (Event.EventType.NodeCreated == type) {
                System.out.println("NodeCreated");
            } else if (Event.EventType.NodeDataChanged == type) {
                System.out.println("NodeDataChanged");
            } else if (Event.EventType.NodeChildrenChanged == type) {
                System.out.println("NodeChildrenChanged");
            } else if (Event.EventType.NodeDeleted == type) {
                System.out.println("NodeDeleted");
            }
        } else if (Event.KeeperState.Disconnected == state) {
            System.out.println("state Disconnected");
        } else if (Event.KeeperState.AuthFailed == state) {

        } else if (Event.KeeperState.Expired == state) {

        }
    }

    public void createConnection(String addr, int timeout) {
        try {
            zk = new ZooKeeper(addr, timeout, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createPath(String path, String data, boolean needWatch) {
        try {
            zk.exists(path, needWatch);
            System.out.println(LOG_PREFIX_OF_MAIN + " create node success, Path: " +
                    zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT) + ", content: " + data
            );
        } catch (KeeperException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void deleteAllTestPath(boolean needWatch) {
        if (this.exists(CHILD_PATH, needWatch) != null) {
            this.deleteNode(CHILD_PATH);
        }

        if (this.exists(PARENT_PATH, needWatch) != null) {
            this.deleteNode(PARENT_PATH);
        }
    }

    public Stat exists(String path, boolean needWatch) {
        try {
            return this.zk.exists(path, needWatch);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteNode(String path) {
        try {
            zk.delete(path, -1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String readData(String path, boolean needWatch) {
        try {
            return new String(zk.getData(path, needWatch, null));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeperWatcher watcher = new ZooKeeperWatcher();

        watcher.createConnection(CONNECTION_ADDR, SESSION_TIMEOUT);
        watcher.deleteAllTestPath(true);

        if (watcher.createPath(PARENT_PATH, System.currentTimeMillis() + "", true)) {
            Thread.sleep(1000);
        }

        Thread.sleep(10000);
        watcher.release();
    }
}

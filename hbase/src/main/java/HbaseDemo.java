import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kohler on 2017/5/2.
 */
public class HbaseDemo {
    static HBaseAdmin hBaseAdmin;
    static HTable hTable;
    static String TN = "phone";

    public static void before() throws IOException {
        System.setProperty("hadoop.home.dir", "H:\\software\\linux\\hadoop-2.7.3");
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "node1,node2,node3");

        hBaseAdmin = new HBaseAdmin(conf);
        hTable = new HTable(conf, TN);
    }

    public static void t1() throws IOException {
        if (hBaseAdmin.tableExists(TN)) {
            hBaseAdmin.disableTable(TN);
            hBaseAdmin.deleteTable(TN);
        }

        HTableDescriptor desc = new HTableDescriptor(TableName.valueOf(TN));
        HColumnDescriptor family = new HColumnDescriptor("cf1");
        family.setInMemory(true);
        family.setMaxVersions(1);

        desc.addFamily(family);

        hBaseAdmin.createTable(desc);
    }

    public static void insert() throws IOException {
        String rowkey = "234346586756345";
        Put put = new Put(rowkey.getBytes());
        put.addColumn("cf1".getBytes(), "type".getBytes(), "1".getBytes());
        put.addColumn("cf1".getBytes(), "time".getBytes(), "20160502".getBytes());
        put.addColumn("cf1".getBytes(), "phonenum".getBytes(), "13023639502".getBytes());

        hTable.put(put);
    }

    public static void get() throws IOException {
        String rowkey = "234346586756345";
        Get get = new Get(rowkey.getBytes());

        get.addColumn("cf1".getBytes(), "type".getBytes());
        get.addColumn("cf1".getBytes(), "time".getBytes());

        Result result = hTable.get(get);
//        System.out.println(result.toString());
        Cell cell = result.getColumnLatestCell("cf1".getBytes(), "type".getBytes());
        System.out.println(new String(CellUtil.cloneValue(cell)));
    }

    public static void insertCall() throws ParseException, IOException {
        List<Put> puts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String phoneNum = getPhoneNum("170");

            for (int j = 0; j < 100; j++) {
                String date = getDate("2017");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                long time = sdf.parse(date).getTime();

                String key = phoneNum + (Long.MAX_VALUE - time);
                System.out.println(key);
                Put put = new Put(key.getBytes());
                put.addColumn("cf1".getBytes(), "type".getBytes(), (r.nextInt() + "").getBytes());
                put.addColumn("cf1".getBytes(), "time".getBytes(), date.getBytes());
                put.addColumn("cf1".getBytes(), "phonenum".getBytes(), getPhoneNum("130").getBytes());

                puts.add(put);
            }
        }

        hTable.put(puts);
    }

    public static void scanDB() throws Exception {
        Scan scan = new Scan();
//        170952379359223370550807947807
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        String startRowkey = "17095237935" + (Long.MAX_VALUE - sdf.parse("20170301000000").getTime());
        scan.setStartRow(startRowkey.getBytes());

        String stopRowkey = "17095237935" + (Long.MAX_VALUE - sdf.parse("20170201000000").getTime());
        scan.setStopRow(stopRowkey.getBytes());

        ResultScanner scanner = hTable.getScanner(scan);

        for (Result result : scanner) {
            Cell cell = result.getColumnLatestCell("cf1".getBytes(), "phonenum".getBytes());
            Cell cell2 = result.getColumnLatestCell("cf1".getBytes(), "time".getBytes());
            System.out.println(new String(CellUtil.cloneValue(cell)) + " == " + new String(CellUtil.cloneValue(cell2)));
        }
    }

    public static void scanWithfilter() throws Exception {
        FilterList list = new FilterList(FilterList.Operator.MUST_PASS_ALL);

        PrefixFilter prefixFilter = new PrefixFilter("17095237935".getBytes());
        list.addFilter(prefixFilter);

        SingleColumnValueFilter filter1 = new SingleColumnValueFilter("cf1".getBytes(),
                "phonenum".getBytes(),
                CompareFilter.CompareOp.EQUAL,
                "13032926660".getBytes());

        list.addFilter(filter1);

        Scan scan = new Scan();

        scan.setFilter(list);
        ResultScanner scanner = hTable.getScanner(scan);

        for (Result result : scanner) {
            Cell cell = result.getColumnLatestCell("cf1".getBytes(), "phonenum".getBytes());
            Cell cell2 = result.getColumnLatestCell("cf1".getBytes(), "time".getBytes());

            System.out.println(new String(CellUtil.cloneRow(cell)) + " == " + new String(CellUtil.cloneValue(cell)) + " == " + new String(CellUtil.cloneValue(cell2)));
        }
    }

    public static void main(String[] args) throws Exception {
        before();

//        insert();
//        get();
//        scanDB();
        scanWithfilter();

        after();
    }

    public static Random r = new Random();

    public static String getPhoneNum(String prefix) {
        return prefix + String.format("%08d", r.nextInt(99999999));
    }

    public static String getDate(String year) {
        return year + String.format("%02d%02d%02d%02d%02d",
                r.nextInt(12) + 1,
                r.nextInt(30) + 1,
                r.nextInt(60),
                r.nextInt(60),
                r.nextInt(60)
        );
    }

    public static void after() throws IOException {
        hBaseAdmin.close();
        hTable.close();
    }
}

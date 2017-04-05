# JVM笔记

方法区存放类的相关信息，包括类、静态变量、常亮。可以理解为永久区（Perm）

---

Java堆分为新生代和老年代。其中新生代存放新生的对象或者年龄不大的对象，老年代则存放老年对象。

新生代分为eden去、s0区、s1区，s0和s1也称为from和to区域，他们是两块大小相等的并且可以互换的角色空间。

大多数情况下对象首先分配到eden区，在第一次新生代回收后如果对象还存活，则会进入s0区或者s1区，之后每经过一次新生代回收，如果对象存活则它的年龄就加1，当对象达到一定的年龄后，则进入老年代。【新生代（eden|s0|s1）】【老年代】

新生代垃圾算法：复制-清除算法，大对象直接进入老年代

老年代：复制-整理算法

---

虚拟机栈和本地方法栈是线程私有的内存空间，一个栈一般由局部变量表、操作数栈、帧数据区

---

参数：

（-XX 对于系统级别的（jvm）配置，如日志、垃圾回收器；非-XX的基本上都是对应用层面上的配置；+表示启用 -表示禁用）

-XX:+PrintGC 使用这个参数，虚拟机启动后，将会打印GC日志

-XX:+UseSerialGC 配置串行回收器

-XX:+PrintGCDetails 可以查看详细信息，包括各个区的情况

-XX:+PrintHeapAtGC 打印Heap

-Xms: 设置java程序启动时初始堆大小

-Xmx: 设置java程序能获得的最大堆大小

-Xmx20m -Xms5m -XX:+PrintCommandLineFlags: 可以将隐式或者显示传给虚拟机的参数输出。新生代回收器主要有：Serial收集器、ParNew收集器、Parallel Scanvenge收集器。老年代收集器有：Serial Old收集器、Parallel Old收集器、CMS收集器。而G1收集器则是集新生代和老年代内存回收功能于一身的收集器。

实际工作中可以直接将初始的堆内存大小与最大的堆内存设置相等，这样的好处是可以减少程序运行时的垃圾回收次数，从而提高性能

-Xmn:可以设置新生代的大小，设置一个比较大的新生代会减少老年代的大小，这个参数对系统的性能以及GC行为有很大的影响，新生代大小一般会设置为整个堆空间的1/3到1/4左右。

-XX:SurvivorRatio:用来设置新生代中eden空间和from/to空间的比例。含义：-XX:SurvivorRatio=eden/from=eden/to。

-XX:NewRatio:老年代/新生代的比例

**优化策略尽可能将对象预留在新生代，减少老年代的GC次数。**

---

-XX:+HeapDumpOnOutOfMemoryError 参数可以在内存溢出时导出整个堆信息。

-XX:HeapDumpPath 可以设置导出堆的内存存放路径。

---

-Xss: 栈空间

---

-XX:PremSize

-XX:MaxPremSize 方法区空间，java8移除了PermSize用MetaSpace代替(-XX:MetaspaceSize=5m -XX:MaxMetaspaceSize=5m)

---

-client 1.7后移除

-server 启动较慢，运行快

---

判定对象是否为垃圾对象有如下算法：引用计数算法、根搜索算法

---

垃圾收集算法

标记清除法、复制算法（新生代）、标记压缩法（老年代）

---

垃圾收集器

垃圾收集器是内存回收算法的具体实现，Java 虚拟机规范中对垃圾收集器应该如何实现并没有任何规定，因此不同厂商、不同版本的虚拟机所提供的垃圾收集器都可能会有很大的差别。Sun HotSpot 虚拟机 1.6 版包含了如下收集器：Serial、ParNew、Parallel Scavenge、CMS、Serial Old、Parallel Old。这些收集器以不同的组合形式配合工作来完成不同分代区的垃圾收集工作。

垃圾回收分析

在用代码分析之前，我们对内存的分配策略明确以下三点：

- 对象优先在 Eden 分配。
- 大对象直接进入老年代。
- 长期存活的对象将进入老年代。

对垃圾回收策略说明以下两点：

- 新生代 GC（Minor GC）：发生在新生代的垃圾收集动作，因为 Java 对象大多都具有朝生夕灭的特性，因此Minor GC 非常频繁，一般回收速度也比较快。
- 老年代 GC（Major GC/Full GC）：发生在老年代的 GC，出现了 Major GC，经常会伴随至少一次 Minor GC。由于老年代中的对象生命周期比较长，因此 Major GC 并不频繁，一般都是等待老年代满了后才进行 Full GC，而且其速度一般会比 Minor GC 慢 10 倍以上。另外，如果分配了 Direct Memory，在老年代中进行 Full GC时，会顺便清理掉 Direct Memory 中的废弃对象。

---

TLAB（Thread Local Allocation Buffer）线程本地分配缓存，是一个线程专用的内存分配区域，是为了加速对象分配而生的。每个线程都会产生一个TLAB，为该线程独享的工作区域，Java虚拟机使用这种TLAB区来避免多线程冲突问题，提高了对象分配的效率。当大对象无法在TLAB分配时，则会直接分配到堆上。
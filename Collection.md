# 集合框架面经总结
### Collection接口下有 List(ArrayList,LinkedList) Queue(BlockingQueue) Set(HashSet) Map接口下有(HashMap,TreeMap)
- ArrayList：底层数据结构为数组，它初始化的时候数据量是0，当你add的时候默认会变成10，它的扩容机制是每次扩容容量是它之前容量的1.5倍，查找访问通过索引下标，速度快，增删元素的效率低，线程不安全。 当遇到线程安全问题时，考虑使用JUC包下的CopyOnWriteArrayList。如果使用尾插法并且指定初始容量可以极大提高它的性能。
- LinkedList： 底层数据结构为双向链表，提供头插和尾插两种方式，适合插入删除频繁的情况下，内部维护了链表的长度。
- HashMap：1.7 底层数据结构 数组加单链表，数据节点是一个Entry内部类。发生hash冲突时，新元素插入到链表头中，即新元素总是添加到数组中，旧元素移动到链表中。利用头插法进行resize() ，扩容时调用resize()然后又调用了transfer()方法，把里面的Entry进行rehash()。在扩容resize()过程中在将旧数组上的数据转移到新数组上时，转移操作 = 按旧链表的正序遍历链表、在新链表的头部依次插入，即在转移数据、扩容后，容易出现链表逆序的情况 。多线程下resize()容易出现死循环，此时若(多线程)并发执行 put()操作，一旦出现扩容情况，则容易出现环形链表，从而在获取数据、遍历链表时 形成死循环，即死锁的状态 。
- HashMap：1.8 底层数据结构 数组加单链表(红黑树)内部Entry节点改为Node节点存储。当单链表长度 >=8并且hash桶大小大于64时会将单链表转化为红黑树，当红黑树节点个数小于6时退化为链表提高插入删除性能。转移数据操作 = 按旧链表的正序遍历链表、在新链表的尾部依次插入，所以不会出现链表逆序、倒置的情况，故不容易出现环形链表的情况 ，但jdk1.8仍是线程不安全的，因为没有加同步锁保护。
- 扩容机制：如果在初始化HashMap时没有给定容量，则容量为默认的16。并且默认的负载因子为0.75，扩容的阈值threshold为 负载因子*初始容量(0.75 * 16) 当put操作时，若size > threshold 则会将原来的数据节点扩容为原来的两倍而且一定是2的次幂。  
[具体区别](https://blog.csdn.net/sky_xin/article/details/84926333)  
- HashMap允许key和value都是null，但是HashTable不允许。
- 当HashMap的key为自定义的类型时，需要重写hashcode和equals方法。因为默认会调用Object类的方法。
### 为什么是2的次幂
[详细](https://blog.csdn.net/sidihuo/article/details/78489820) 
- 为了提升模运算的计算速度。减少碰撞，或者说为了让HashMap里面元素分布尽量均匀些，应该是哈希算法的功能。
- HashMap为了存取高效，要尽量较少碰撞，就是要尽量把数据分配均匀，每个链表长度大致相同，这个实现就在把数据存到哪个链表中的算法。
- 这个算法实际就是取模，hash%length，计算机中直接求余效率不如位移运算，源码中做了优化hash&(length-1)，hash%length==hash&(length-1)的前提是length是2的n次方。
- 为什么这样能均匀分布减少碰撞呢？2的n次方实际就是1后面n个0，2的n次方-1  实际就是n个1；其实就是按位“与”的时候，每一位都能&1，这样会充分利用到二进制下hash值的后面几位。如果不是2的次幂，这样n-1 的二进制后面一定会有0的位，这样这位与操作之后一定是0，所以数组上的某些位置可能一直得不到利用，数组元素分布不均匀，导致使用效率降低。
### 线程安全的map有HashTable，或者加Synchronized、Lock，或者Collections.synchronizedMap()，为什么选择用ConcurrentHashMap
- 锁粒度小，并发度更高。普通的HashTable直接对里面的方法加了全局的synchronized对象锁，ConcurrentHashMap在jdk1.8之后底层数据结构也变成了数组加链表加红黑树，它只会锁住目前我在的Node节点的值，在上锁时使用了CAS加synchronized，再加上jdk1.6以后对synchronized进行了锁升级的优化，所以它的效率是更高的。
- HashTable是加全局锁Synchronized，ConcurrentHashMap加的是分段锁。并且ConcurrentHashMap在1.7和1.8有一些改变。  
### ConcurrentHashMap原理，以及jdk7和jdk8的区别。
- jdk7：
    - 数据结构：Segment(继承ReentrantLock)+Entry+单链表，一个Segment中包含一个小型的HashMap。
    - 元素查询：两次hash，第一次hash定位到segment，第二次hash定位到元素所在的链表的头部。
    - 锁：Segment分段锁(继承ReentrantLock)，锁定正在操作的segment，其他的segment不受影响，并发度为segment的大小。(默认segment最大值为16)
- jdk8：
    - 数据结构：synchronized+CAS+Node+单链表(红黑树)，Node的val和next都用volatile修饰，保证可见性。查找替换赋值都用CAS。
    - 锁：锁定链表的Head节点(jdk1.7锁的是segment，里面有很多的链表)，不影响其他元素的读写，锁粒度更细，效率更高。扩容时，阻塞所有的读写操作，并发扩容。
    - 读操作无锁：Node的val和next都是volatile修饰的，读写线程对该共享变量互相可见。
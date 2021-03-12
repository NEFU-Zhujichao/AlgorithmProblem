# 集合框架面经总结
### Collection接口下有List(ArrayList,LinkedList)Set(HashSet)Map(HashMap)
- ArrayList：底层数据结构为数组，它初始化的时候数据量是0，当你add的时候默认会变成10，它的扩容机制是每次扩容容量是它之前容量的1.5倍，查找访问通过索引下标，速度快，增删元素的效率低，线程不安全。 当遇到线程安全问题时，考虑使用JUC包下的CopyOnWriteArrayList
- LinkedList： 底层数据结构为双向链表，提供头插和尾插两种方式，适合插入删除频繁的情况下，内部维护了链表的长度。
- HashMap：1.7 底层数据结构 数组加单链表，数据节点是一个Entry内部类。发生hash冲突时，新元素插入到链表头中，即新元素总是添加到数组中，旧元素移动到链表中。利用头插法进行resize() ，扩容时调用resize()然后又调用了transfer()方法，把里面的Entry进行rehash()。在扩容resize()过程中在将旧数组上的数据转移到新数组上时，转移操作 = 按旧链表的正序遍历链表、在新链表的头部依次插入，即在转移数据、扩容后，容易出现链表逆序的情况 。多线程下resize()容易出现死循环，此时若(多线程)并发执行 put()操作，一旦出现扩容情况，则容易出现环形链表，从而在获取数据、遍历链表时 形成死循环，即死锁的状态 。
- HashMap：1.8 底层数据结构 数组加单链表(红黑树)内部Entry节点改为Node节点存储。当单链表长度 >=8并且hash桶大小大于64时会将单链表转化为红黑树，当红黑树节点个数小于6时退化为链表提高插入删除性能。转移数据操作 = 按旧链表的正序遍历链表、在新链表的尾部依次插入，所以不会出现链表逆序、倒置的情况，故不容易出现环形链表的情况 ，但jdk1.8仍是线程不安全的，因为没有加同步锁保护。
- 扩容机制：如果在初始化HashMap时没有给定容量，则容量为默认的16。并且默认的负载因子为0.75，扩容的阈值threshold为 负载因子*初始容量(0.75 * 16) 当put操作时，若size > threshold 则会将原来的数据节点扩容为原来的两倍。
[具体区别](https://blog.csdn.net/sky_xin/article/details/84926333)
```java
public class Main{
/**实际存储的key-value键值对的个数*/
transient int size;

/**阈值，当table == {}时，该值为初始容量（初始容量默认为16）；当table被填充了，也就是为table分配内存空间后，
 threshold一般为 capacity*loadFactory。HashMap在进行扩容时需要参考threshold，后面会详细谈到*/
        int threshold;

/**负载因子，代表了table的填充度有多少，默认是0.75
 加载因子存在的原因，还是因为减缓哈希冲突，如果初始桶为16，等到满16个元素才扩容，某些桶里可能就有不止一个元素了。
 所以加载因子默认为0.75，也就是说大小为16的HashMap，到了第13个元素，就会扩容成32。
 */
final float loadFactor;

/**HashMap被改变的次数，由于HashMap非线程安全，在对HashMap进行迭代时，
 如果期间其他线程的参与导致HashMap的结构发生变化了（比如put，remove等操作），
 需要抛出异常ConcurrentModificationException*/
transient int modCount;
}
```
### 线程安全的map有HashTable，或者加Synchronized、Lock，或者Collections.synchronizedMap()，为什么选择用ConcurrentHashMap
- 并发度更高。普通的HashTable直接对里面的方法加了synchronized对象锁，ConcurrentHashMap在jdk1.8之后底层数据结构也变成了数组加链表加红黑树，它只会锁住目前我在的Node节点的值，在上锁时使用了CAS加synchronized，再加上jdk1.6以后对synchronized进行了锁升级的优化，所以它的效率是更高的。
### 锁升级过程
synchronize锁升级过程：jdk高版本之后对synchronize关键字进行了很多优化，其中一项就是锁升级，以前synchronize默认就是悲观锁，加锁解锁的开销都比较大。所以引入了偏向锁、轻量级锁、重量级锁。 
CAS：compare and swap。会出现ABA问题，中间有线程快速改过值了但是又改回了原值，一般会像乐观锁一样解决此类问题。
![CAS](https://img-blog.csdnimg.cn/20200602171359487.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmd5eTEzMA==,size_16,color_FFFFFF,t_70)
**偏向锁**：
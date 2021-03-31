package singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Singleton {
    private static boolean flag = false;

    private static volatile Singleton singleton;

    private Singleton(){
        synchronized (Singleton.class){
            if (flag == false){
                flag = true;
            }else {
                throw new RuntimeException("不要视图使用反射破坏单例");
            }
        }
    }
    public static Singleton getInstance(){
        // 第一个if判断如果存在实例就直接返回，不然没有第一层的话进来就会加锁，影响性能。
        if (singleton == null){
            synchronized (Singleton.class){
                // 第二个if 假设两个线程都判断完了第一个if，然后第一个线程拿到了锁，创建实例
                // 等到锁释放后，如果不进行判断那么第二个线程又会创建出一个实例
                if (singleton == null) {
                    singleton = new Singleton();
                    /**
                     * 1. 分配内存空间
                     * 2. 执行构造函数，初始化对象
                     * 3. 把这个对象指向分配的内存空间
                     * 123 A ->
                     * 132 发生指令重排。 B -> 拿到了一个虚无的对象，此时还没有完成构造！
                     */
                }
            }
        }
        return singleton;
    }
    // 反射破坏单例模式
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
        //Singleton instance = Singleton.getInstance();
        Field flag = Singleton.class.getDeclaredField("flag");
        flag.setAccessible(true);
        Constructor<Singleton> singletonConstructor = Singleton.class.getDeclaredConstructor(null);
        singletonConstructor.setAccessible(true);
        // 如果两个对象都是反射创建的，不走构造函数，则单例模式依旧会被破坏
        // 加一个标志位
        Singleton instance = singletonConstructor.newInstance();
        // 单例又被破坏了
        flag.set(instance,false);
        Singleton instance2 = singletonConstructor.newInstance();
        System.out.println(instance == instance2);

    }
}

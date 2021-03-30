package singleton;

public class Singleton {
    private static volatile Singleton singleton;
    private Singleton(){}
    public static Singleton getInstance(){
        // 第一个if判断如果存在实例就直接返回，不然没有第一层的话进来就会加锁，影响性能。
        if (singleton == null){
            synchronized (Singleton.class){
                // 第二个if 假设两个线程都判断完了第一个if，然后第一个线程拿到了锁，创建实例
                // 等到锁释放后，如果不进行判断那么第二个线程又会创建出一个实例
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                Singleton instance = Singleton.getInstance();
                System.out.println(instance.hashCode());
            },i+"").start();
        }

    }
}

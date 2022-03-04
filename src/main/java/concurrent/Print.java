package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程交替打印 A B C
 */
public class Print {
    private static int count = 0;
    private static Lock lock = new ReentrantLock();
    private static Condition condition1 = lock.newCondition();
    private static Condition condition2 = lock.newCondition();
    private static Condition condition3 = lock.newCondition();
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 0){
                        condition1.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    count++;
                    condition2.signal();
                }
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        },"线程A").start();

        new Thread(() -> {
            try {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 1){
                        condition2.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    count++;
                    condition3.signal();
                }
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        },"线程B").start();

        new Thread(() -> {
            try {
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    while (count % 3 != 2){
                        condition3.await();
                    }
                    System.out.println(Thread.currentThread().getName());
                    count++;
                    condition1.signal();
                }
            }catch (Exception e){

            }finally {
                lock.unlock();
            }
        },"线程C").start();
    }
}

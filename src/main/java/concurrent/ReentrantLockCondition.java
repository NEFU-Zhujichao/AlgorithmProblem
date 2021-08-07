package concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个线程交替循环打印 1 2 3
 */
public class ReentrantLockCondition {
    private static Lock lock = new ReentrantLock();
    private static Condition[] conditions = {lock.newCondition(), lock.newCondition(), lock.newCondition()};
    private volatile int state = 1;

    private void handle(int state) {
        lock.lock();
        try {
            while(true) {
                while(this.state != state) {
                    conditions[state - 1].await();
                }
                System.out.println(this.state);
                Thread.sleep(2000);
                this.state = state % 3 + 1;
                conditions[this.state - 1].signal();
                conditions[state - 1].await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockCondition rlc = new ReentrantLockCondition();
        new Thread(()->rlc.handle(1)).start();
        new Thread(()->rlc.handle(2)).start();
        new Thread(()->rlc.handle(3)).start();
    }
}

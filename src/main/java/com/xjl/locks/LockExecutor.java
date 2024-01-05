package com.xjl.locks;

import com.xjl.locks.common.ILock;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * lock：
 * 1、原子锁X
 * 2、共享排他场景X、S
 * 3、多个锁并发场景组合场景
 * 4、意向锁【仿照mysql表锁、行锁】
 * 5、组合场景下，锁优先级、排序
 * 释放策略【主动释放、自动释放】
 * @Auther xujln
 */
public interface LockExecutor {
    /**
     * 加锁,【需要线程内解锁】
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     */
     boolean lock(ILock...locks);

    /**
     * 加锁,【需要线程内解锁】
     * @param locks
     * @return
     */
     boolean tryLock(ILock...locks);

    /**
     * 加锁,【需要线程内解锁】
     *
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
     boolean tryLock(long waitTime, long leaseTime, TimeUnit unit, ILock...locks) throws InterruptedException;

    /**
     * 加锁,【需要线程内解锁】
     * @param waitTime
     * @param unit
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
     default boolean tryLock(long waitTime, TimeUnit unit, ILock...locks) throws InterruptedException{
         return tryLock(waitTime,-1,unit,locks);
     }
    /**
     * 事务锁 ,不需要解锁
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
    default boolean transactionLock(ILock...locks)  throws InterruptedException{

        boolean  locked = this.lock(locks);
        if (locked) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCompletion(int status) {
                    unlock();
                }
            });
        }

        return locked;
    }
    /**
     * 如果当前没有事务 抛出异常
     * 事务锁 ,不需要解锁
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
    default boolean transactionTryLock(ILock...locks)  throws InterruptedException{
        boolean  locked = this.tryLock(locks);
        if (locked) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCompletion(int status) {
                    unlock();
                }
            });
        }

        return locked;
    }

    /**
     *  如果当前没有事务 抛出异常
     *  事务锁 ,不需要解锁
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
    default boolean transactionTryLock(long waitTime, long leaseTime, TimeUnit unit, ILock...locks)  throws InterruptedException{
        boolean  locked = this.tryLock(waitTime,leaseTime,unit,locks);
        if (locked) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCompletion(int status) {
                    unlock();
                }
            });
        }

        return locked;
    }
    /**
     *  如果当前没有事务 抛出异常
     *  事务锁 ,不需要解锁
     * @param waitTime
     * @param unit
     * @param locks
     * @return 加锁是否成功，加锁成功 返回true 否则返回false
     * @throws InterruptedException
     */
    default boolean transactionTryLock(long waitTime, TimeUnit unit, ILock...locks)  throws InterruptedException{
        boolean  locked = this.tryLock(waitTime,unit,locks);
        if (locked) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                public void afterCompletion(int status) {
                    unlock();
                }
            });
        }

        return locked;
    }

    /**
     * 自动加锁、解锁
     *          <code>
     *           String hello = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"),IPcLockBuilder.shared("lock2"),IPcLockBuilder.fair("lock2"));
     *           String hello2 = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"));
     *
     *           static String get(){
     *                  return "Hello";
     *           }
     *            </code>
     * @param runnable
     * @param locks
     */
    default void lock(Runnable runnable, ILock...locks){
        if(!lock(locks)){
            throw new RuntimeException("为取得锁");
        }
        try {
            runnable.run();
        }finally {
           unlock();
        }
    }

    /**
     * 自动加锁、解锁
     *      <pre>
     *       String hello = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"),IPcLockBuilder.shared("lock2"),IPcLockBuilder.fair("lock2"));
     *       String hello2 = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"));
     *
     *       static String get(){
     *              return "Hello";
     *       }
     *        </pre>
     *
     * @param callable
     * @param locks
     * @return 返回业务值
     * @param <V>
     */
    default <V> V lock(Callable<V> callable, ILock...locks){
        if(!lock(locks)){
            throw new RuntimeException("为取得锁");
        }
        try {
           return callable.call();
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    /**
     * 自动加锁、解锁
     * @param runnable
     * @param locks
     */
    default void tryLock(Runnable runnable, ILock...locks){
        if(!tryLock(locks)){
            throw new RuntimeException("未取得锁");
        }
        try {
            runnable.run();
        }finally {
            unlock();
        }
    }

    /**
     * 自动加锁、解锁
     * @param runnable
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param locks
     * @throws InterruptedException
     */
    default void tryLock(Runnable runnable, long waitTime, long leaseTime, TimeUnit unit, ILock...locks) throws InterruptedException {
        if(!tryLock(waitTime,leaseTime,unit,locks)){
            throw new RuntimeException("为取得锁");
        }
        try {
            runnable.run();
        }finally {
            unlock();
        }
    }

    /**
     * 自动加锁、解锁
     * <pre>
     *  String hello = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"),IPcLockBuilder.shared("lock2"),IPcLockBuilder.fair("lock2"));
     *  String hello2 = lockProvider.tryLock(Main::get, IPcLockBuilder.exclusive("lock2"));
     *
     *     static String get(){
     *         return "Hello";
     *     }
     *     </pre>
     * @param callable
     * @param locks
     * @return 返回业务值
     * @param <V>
     */
    default <V> V tryLock(Callable<V> callable, ILock...locks){
        if(!tryLock(locks)){
            throw new RuntimeException("为取得锁");
        }
        try {
            return callable.call();
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    /**
     * 自动加锁、解锁
     * @see LockExecutor#tryLock(Callable, ILock...)
     * @param callable
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @param locks
     * @return
     * @param <V>
     * @throws InterruptedException
     */
    default <V> V tryLock(Callable<V> callable, long waitTime, long leaseTime, TimeUnit unit, ILock...locks) throws InterruptedException {
        if(!tryLock(waitTime,leaseTime,unit,locks)){
            throw new RuntimeException("为取得锁");
        }
        try {
            return callable.call();
        }catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            unlock();
        }
    }

    /**
     * 释放锁
     * @see LockExecutor#tryLock(Runnable, ILock...)
     * @see LockExecutor#tryLock(Callable, ILock...)
     * { @link PcLockExecutor#tryLock(Callable, IPcLock...)}
     */
    void unlock();


    /**
     * 加锁是否成功，加锁成功 返回true 否则返回false
     * @return
     */
    boolean isLocked();
}

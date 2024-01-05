package com.xjl.locks.common;

public interface LockProvider {
    /**
     * 加锁,【需要线程内解锁】
     * @param locks
     * @return
     */
    default boolean lock(ILock...locks){

//        LockAgent.getInstance().lock()
        return Boolean.FALSE;
    }

    /**
     * 加锁,【需要线程内解锁】
     * @param locks
     * @return
     */
    default boolean tryLock(ILock...locks){

//        LockAgent.getInstance().lock()
        return Boolean.FALSE;
    }

    /**
     * 事务锁 ,不需要解锁
     * @param locks
     * @return
     * @throws InterruptedException
     */
    default boolean dynamicLock(ILock...locks)  throws InterruptedException{

//        LockAgent.getInstance().lock()
        return Boolean.FALSE;
    }

    default boolean dynamicTryLock(String[] identifications)  throws InterruptedException{

//        LockAgent.getInstance().lock()
        return Boolean.FALSE;
    }

     static LockProvider getInstance(){
        return new LockProvider() {};
    }
}

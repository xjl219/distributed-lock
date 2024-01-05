package com.xjl.locks.spi;


import com.xjl.locks.IXLock;

/**
 * 共享锁
 */
public abstract class SharedLock implements IXLock {

    @Override
    public LockType getType() {
        return LockType.SHARED;
    }
}

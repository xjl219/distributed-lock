package com.xjl.locks.spi;


import com.xjl.locks.IXLock;

/**
 * 公平锁
 */
public abstract class FairLock implements IXLock {
    @Override
    public LockType getType() {
        return LockType.FAIRLOCK;
    }
}

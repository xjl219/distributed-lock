package com.xjl.locks.common;

public abstract class SharedLock implements ILock {

    @Override
    public LockType getType() {
        return LockType.SHARED;
    }
}

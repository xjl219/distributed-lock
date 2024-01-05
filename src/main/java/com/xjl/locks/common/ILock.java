package com.xjl.locks.common;

import com.xjl.locks.utils.LockContextManager;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public interface ILock extends Supplier<String[]> {
    enum LockType{
        SHARED,EXCLUSIVE,FAIRLOCK
    }

    default  LockType getType(){
        return  LockType.EXCLUSIVE;
    }
    String lockPrex = "LOCK:";
    boolean isAddTenantSegment=true;


    default  String getLockPrex(){
        return lockPrex;
    }

    default   String[] getKeys(String serviceCode) {
        String[] strings = get();
        String[] keyArray = Arrays.stream(strings).distinct().map(k -> genLockKey(k,serviceCode)).toArray(String[]::new);
        return keyArray;
    }
    static String genLockKey(String key, String serviceCode) {
        String rs;
        if (isAddTenantSegment) {
            String yTenantId = LockContextManager.getContext().getYTenantId();
            if (Objects.isNull(yTenantId)) {
                yTenantId = "noneTenant";
            }

            rs = lockPrex + serviceCode + ":" + yTenantId + ":" + key;
        } else {
            rs = lockPrex + serviceCode + ":" + key;
        }

        return rs;
    }
}

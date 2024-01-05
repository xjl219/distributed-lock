package com.xjl.locks;


import com.xjl.locks.common.ILock;
import com.xjl.locks.utils.LockContextManager;

import java.util.Arrays;
import java.util.Objects;

/**
 *   1、排他锁 EXCLUSIVE
 *   2、共享 SHARED
 *   3、公平 FAIRLOCK
 */
public interface IXLock extends ILock {
    String lockPrex = "LOCK:";
    boolean isAddTenantSegment=true;


    default  LockType getType(){
        return  LockType.EXCLUSIVE;
    }
//    default  long getWaitTime(){
//        return -1;
//    }
//    default  long getLeaseTime(){
//        return -1;
//    }
//    default TimeUnit getTimeUnit(){
//        return TimeUnit.MILLISECONDS;
//    }

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

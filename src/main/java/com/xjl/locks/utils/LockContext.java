package com.xjl.locks.utils;

public interface LockContext {
    default String getYTenantId() {
        return "default";
    }

    default String getServiceCode() {
        return "default";
    }


}

package io.luowei.middleware.sdk.service;

import io.luowei.middleware.sdk.service.model.ThreadPoolConfigEntity;

import java.util.List;

public interface DynamicThreadPoolService {

    List<ThreadPoolConfigEntity> getThreadPoolList();

    ThreadPoolConfigEntity getThreadPool(String threadPoolName);

    void updateThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity);

}

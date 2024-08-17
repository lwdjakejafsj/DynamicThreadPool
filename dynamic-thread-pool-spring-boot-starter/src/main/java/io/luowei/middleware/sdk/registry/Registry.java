package io.luowei.middleware.sdk.registry;

import io.luowei.middleware.sdk.service.model.ThreadPoolConfigEntity;

import java.util.List;

public interface Registry {
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolEntities);

    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}

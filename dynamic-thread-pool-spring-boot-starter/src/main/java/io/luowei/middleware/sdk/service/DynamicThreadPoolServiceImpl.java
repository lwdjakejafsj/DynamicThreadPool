package io.luowei.middleware.sdk.service;

import com.alibaba.fastjson.JSON;
import io.luowei.middleware.sdk.service.model.ThreadPoolConfigEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

public class DynamicThreadPoolServiceImpl implements DynamicThreadPoolService{
    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolService.class);

    private final String applicationName;

    private final Map<String, ThreadPoolExecutor> threadPoolExecutorMap;

    public DynamicThreadPoolServiceImpl(String applicationName, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        this.applicationName = applicationName;
        this.threadPoolExecutorMap = threadPoolExecutorMap;
    }

    @Override
    public List<ThreadPoolConfigEntity> getThreadPoolList() {
        Set<String> keySet = threadPoolExecutorMap.keySet();

        List<ThreadPoolConfigEntity> threadPoolConfigEntityList = new ArrayList<>();
        keySet.forEach(key -> {
            ThreadPoolExecutor executor = threadPoolExecutorMap.get(key);
            ThreadPoolConfigEntity entity = new ThreadPoolConfigEntity(applicationName,key);
            entity.setCorePoolSize(executor.getCorePoolSize());
            entity.setMaximumPoolSize(executor.getMaximumPoolSize());
            entity.setActiveCount(executor.getActiveCount());
            entity.setPoolSize(executor.getPoolSize());
            entity.setQueueType(executor.getQueue().getClass().getSimpleName());
            entity.setQueueSize(executor.getQueue().size());
            entity.setRemainingCapacity(executor.getQueue().remainingCapacity());
            threadPoolConfigEntityList.add(entity);
        });

        return threadPoolConfigEntityList;
    }

    @Override
    public ThreadPoolConfigEntity getThreadPool(String threadPoolName) {
        ThreadPoolExecutor executor = threadPoolExecutorMap.get(threadPoolName);
        if (executor == null) return new ThreadPoolConfigEntity(applicationName,threadPoolName);

        ThreadPoolConfigEntity entity = new ThreadPoolConfigEntity(applicationName,threadPoolName);
        entity.setCorePoolSize(executor.getCorePoolSize());
        entity.setMaximumPoolSize(executor.getMaximumPoolSize());
        entity.setActiveCount(executor.getActiveCount());
        entity.setPoolSize(executor.getPoolSize());
        entity.setQueueType(executor.getQueue().getClass().getSimpleName());
        entity.setQueueSize(executor.getQueue().size());
        entity.setRemainingCapacity(executor.getQueue().remainingCapacity());

        if (logger.isDebugEnabled()) {
            logger.info("动态线程池，配置查询 应用名:{} 线程名:{} 池化配置:{}", applicationName, threadPoolName, JSON.toJSONString(entity));
        }

        return entity;
    }

    @Override
    public void updateThreadPool(ThreadPoolConfigEntity threadPoolConfigEntity) {
        if (null == threadPoolConfigEntity || !applicationName.equals(threadPoolConfigEntity.getAppName())) return;
        ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(threadPoolConfigEntity.getThreadPoolName());
        if (null == threadPoolExecutor) return;

        // 设置参数 「调整核心线程数和最大线程数」
        threadPoolExecutor.setCorePoolSize(threadPoolConfigEntity.getCorePoolSize());
        threadPoolExecutor.setMaximumPoolSize(threadPoolConfigEntity.getMaximumPoolSize());
    }
}

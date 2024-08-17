package io.luowei.middleware.sdk.trigger.listener;

import com.alibaba.fastjson.JSON;
import io.luowei.middleware.sdk.registry.Registry;
import io.luowei.middleware.sdk.service.DynamicThreadPoolService;
import io.luowei.middleware.sdk.service.model.ThreadPoolConfigEntity;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 动态线程池变更监听
 * author: luowei
 * date:
 */
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final DynamicThreadPoolService dynamicThreadPoolService;
    private final Registry registry;
    public ThreadPoolConfigAdjustListener(DynamicThreadPoolService dynamicThreadPoolService, Registry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        logger.info("动态线程池，调整线程池配置。线程池名称:{} 核心线程数:{} 最大线程数:{}", threadPoolConfigEntity.getThreadPoolName(), threadPoolConfigEntity.getPoolSize(), threadPoolConfigEntity.getMaximumPoolSize());
        dynamicThreadPoolService.updateThreadPool(threadPoolConfigEntity);

        // 更新后上报最新数据
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.getThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);

        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.getThreadPool(threadPoolConfigEntity.getThreadPoolName());
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
        logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
    }
}

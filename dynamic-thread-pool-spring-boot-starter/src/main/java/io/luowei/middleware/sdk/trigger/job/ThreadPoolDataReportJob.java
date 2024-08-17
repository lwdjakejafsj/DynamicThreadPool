package io.luowei.middleware.sdk.trigger.job;

import com.alibaba.fastjson.JSON;
import io.luowei.middleware.sdk.registry.Registry;
import io.luowei.middleware.sdk.service.DynamicThreadPoolService;
import io.luowei.middleware.sdk.service.model.ThreadPoolConfigEntity;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ThreadPoolDataReportJob {
    private final Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);
    private final DynamicThreadPoolService dynamicThreadPoolService;
    private final Registry registry;

    public ThreadPoolDataReportJob(DynamicThreadPoolService dynamicThreadPoolService, Registry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void execReportThreadPoolList() {
        List<ThreadPoolConfigEntity> threadPoolList = dynamicThreadPoolService.getThreadPoolList();
        registry.reportThreadPool(threadPoolList);

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolList) {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
        }
    }

}

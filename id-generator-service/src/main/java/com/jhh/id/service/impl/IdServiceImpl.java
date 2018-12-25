package com.jhh.id.service.impl;

import static com.jhh.id.common.enums.IdEnums.Type.DB;
import static com.jhh.id.common.enums.IdEnums.Type.REDIS;

import com.jhh.id.common.exception.BizException;
import com.jhh.id.entity.Segment;
import com.jhh.id.generator.IGenerator;
import com.jhh.id.generator.impl.MemoryIdGenerator;
import com.jhh.id.generator.impl.RedisIdGenerator;
import com.jhh.id.service.ISegmentService;
import com.jhh.id.service.IdService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 根据ID区间生成该区间所有的ID序列并保存在阻塞队列中
 *
 * @author renbangjie
 */
@Slf4j
@RequiredArgsConstructor
public class IdServiceImpl implements IdService, InitializingBean {

    /**
     * 分段表长度 根据id_segment表的数据量合理配置
     */
    private static final int GENERATOR_COUNT = 500;

    /**
     * 异步加载阈值
     */
    private final double threshold;

    /**
     * 线程池
     */
    private final ExecutorService executorService;

    /**
     * 可以改为在启动时初始化
     */
    private ConcurrentMap<String, IGenerator> generatorMap = new ConcurrentHashMap<>(
            GENERATOR_COUNT);


    @Override
    public Number nextId(String bizName) {
        IGenerator iGenerator = generatorMap.get(bizName);
        if (iGenerator == null) {
            throw new BizException("没有找到" + bizName + "号段相关信息.");
        }
        try {
            return iGenerator.nextId(3000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.info(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    public void refresh() {
        afterPropertiesSet();
    }


    @Override
    public void afterPropertiesSet() {
        List<Segment> segments = segmentService.selectList();
        String bizName = null;
        IGenerator generator = null;
        for (Segment segment : segments) {
            bizName = segment.getBizName();
            if (DB.getCode() == segment.getType()) {
                generator = new MemoryIdGenerator(bizName, threshold,
                        () -> segmentService
                                .incrementAndGet(segment.getBizName()),
                        executorService);
            }
            if (REDIS.getCode() == segment.getType()) {
                generator = new RedisIdGenerator(bizName, threshold,
                        () -> segmentService.incrementAndGet(segment.getBizName()),
                        executorService, redisTemplate);
            }
            generatorMap.putIfAbsent(bizName, generator);
        }
    }

    @Autowired
    private ISegmentService segmentService;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> redisTemplate;

}

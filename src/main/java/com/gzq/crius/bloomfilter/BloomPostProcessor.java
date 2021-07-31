package com.gzq.crius.bloomfilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 加载配置类，生成service实例
 * @author guozhenquan
 * @date 2021/7/31 15:49
 * @version 1.0
 */
@Slf4j
@Component
public class BloomPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    public static final String BLOOM_FILTER_CONFIG_PREFIX = "bloom-filter-config";

    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        com.example.demo.redisson.BloomFilterConfig bloomFilterConfig = getConfig();
        for (com.example.demo.redisson.BloomFilterConfig.BloomConfigItem config : bloomFilterConfig.getItems()) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(com.example.demo.redisson.BloomFilterService.class)
                    .addConstructorArgValue(config).getBeanDefinition();
            registry.registerBeanDefinition(config.getName(), beanDefinition);
        }
    }

    /**
     * 获取配置信息
     * @return
     */
    private com.example.demo.redisson.BloomFilterConfig getConfig() {
        // 获取配置信息
        com.example.demo.redisson.BloomFilterConfig bloomFilterConfig = Binder.get(this.environment).bind(BLOOM_FILTER_CONFIG_PREFIX, com.example.demo.redisson.BloomFilterConfig.class).get();
        // 检测配置⾥是否有 名称 或 Redis key 重复
        List<com.example.demo.redisson.BloomFilterConfig.BloomConfigItem> configItems = bloomFilterConfig.getItems();
        long nameCount = configItems.stream().map(com.example.demo.redisson.BloomFilterConfig.BloomConfigItem::getName).distinct().count();
        long keyCount = configItems.stream().map(com.example.demo.redisson.BloomFilterConfig.BloomConfigItem::getKey).distinct().count();
        if (nameCount != configItems.size() || keyCount != configItems.size()) {
            throw new RuntimeException("bloom filter name or key repeat");
        }
        return bloomFilterConfig;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info("postProcessBeanFactory");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}

package com.pbkj.crius.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    /**
     * 当前IOC 
     *
     */
    private static ApplicationContext applicationContext;

    // redis key前缀
    public static String redisProjectNamePrefix = null;
    /**
     * * 设置当前上下文环境，此方法由spring自动装配 
     *
     */
    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        applicationContext = arg0;
        String contextPath = applicationContext.getEnvironment().getProperty("server.servlet.context-path");
        redisProjectNamePrefix = contextPath.split("_")[0].split("/")[1] + "::";
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
    /**
     * 从当前IOC获取bean 
     *
     * @param id
     * bean的id 
     * @return
     *
     */
    public static Object getObject(String id) {
        Object object = null;
        object = applicationContext.getBean(id);
        return object;
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static <T> List<T> deepCopy(List<T> source) {
        if (CollectionUtils.isEmpty(source)) {
            return emptyList();
        }
        List<T> result = new ArrayList<>();
        try {
            Class<?> aClass = source.get(0).getClass();
            for (T t : source) {
                T o = (T)aClass.newInstance();
                BeanUtils.copyProperties(t, o);
                result.add(o);
            }
        } catch (Exception e) {

        }
        return result;
    }
}

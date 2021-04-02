package com.pbkj.crius.aspect;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: songjn1
 * @create: 2019-08-23
 **/
@Aspect
@Component
@Slf4j
public class SystemControllerAspect {
    @Pointcut("execution(public * cn.codemao.botmao.admin.controller..*.*(..))")
    public void systemLogAspectServicelog() {
    }

    @Before("systemLogAspectServicelog()")
    private void afterScriptUpdateLog(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        printLogInfo(joinPoint, request);
    }

    void printLogInfo(JoinPoint joinPoint, HttpServletRequest request) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("------喵喵喵------请求信息：")
                .append("URL = {" + request.getRequestURI() + "},\t")
                .append("HTTP_METHOD = {" + request.getMethod() + "},\t")
                .append("IP = {" + request.getRemoteAddr() + "},\t")
                .append("CLASS_METHOD = {" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "},\t");

        if (joinPoint.getArgs().length == 0) {
            requestLog.append("ARGS = {} ");
        } else {
            try {
                requestLog.append("ARGS = " + new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                        .writeValueAsString(joinPoint.getArgs()[0]) + "");
            } catch (Exception e) {
                requestLog.append("ARGS = {未知参数} ");
            }
        }
        String msg = requestLog.toString();
        log.info(msg);
    }
}

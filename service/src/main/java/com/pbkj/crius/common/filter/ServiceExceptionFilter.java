package com.pbkj.crius.common.filter;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alibaba.fastjson.JSON.toJSON;

/**
 * @author GZQ
 * @description 抛出异常的总处理$
 * @date 2020/7/24 16:50
 **/
public class ServiceExceptionFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(ServiceExceptionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try {
//            filterChain.doFilter(request, response);
//        } catch (Throwable e) {
//            logger.error("access {}, URL-params:{}, body-params:{},  access ip:{}",
//                    request.getRequestURI(), request.getQueryString(),
//                    toJSON(request.getParameterMap()), request.getRemoteAddr(), e);
//
//            if (e instanceof NestedServletException) {
//                if (e.getCause().getClass().equals(ServiceException.class)) {
//                    ServiceException se = (ServiceException)e.getCause();
//                    int code = se.getCode();
//                    String message = se.getMessage();
//                    response.setStatus(code);
//                    JSONObject json = new JSONObject();
//                    json.put("msg", message);
//                    ResponseUtils.output(response, JSONObject.toJSONString(json));
//                } else {
//                    response.setStatus(ERROR_SYSTEM);
//                    JSONObject json = new JSONObject();
//                    json.put("msg", "系统错误");
//                    ResponseUtils.output(response, JSONObject.toJSONString(json));
//                }
//            } else if (e instanceof ServiceException) {
//                int code = ((ServiceException) e).getCode();
//                String message = e.getMessage();
//                response.setStatus(code);
//                JSONObject json = new JSONObject();
//                json.put("msg", message);
//                ResponseUtils.output(response, JSONObject.toJSONString(json));
//                return;
//            } else {
//                response.setStatus(ERROR_SYSTEM);
//                JSONObject json = new JSONObject();
//                json.put("msg", "系统错误");
//                ResponseUtils.output(response, JSONObject.toJSONString(json));
//            }
//        }
//    }
    }
}
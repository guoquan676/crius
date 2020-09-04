package com.pbkj.crius.common.filter;

import cn.hutool.json.JSONUtil;
import com.pbkj.crius.common.constant.ErrorCodeEnum;
import com.pbkj.crius.common.exception.ServiceException;
import com.pbkj.crius.common.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.alibaba.fastjson.JSON.toJSON;

/**
 * @author GZQ
 * @date 2020/7/24 16:50
 **/
public class ServiceExceptionFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(ServiceExceptionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            logger.error("access {}, URL-params:{}, body-params:{},  access ip:{}",
                    request.getRequestURI(), request.getQueryString(),
                    toJSON(request.getParameterMap()), request.getRemoteAddr(), e);
            if (e instanceof ServiceException) {
                ServiceException se = (ServiceException) e;
                int code = se.getCode();
                String message = e.getMessage();
                response.setStatus(code);
                ResponseUtils.output(response, JSONUtil.toJsonStr(ResponseUtils.getResponseErrorBody(message)));
            } else {
                Integer code = ErrorCodeEnum.SYSTEM_ERROR.getCode();
                response.setStatus(code);
                String message = ErrorCodeEnum.getMsgByCode(code);
                ResponseUtils.output(response, JSONUtil.toJsonStr(ResponseUtils.getResponseErrorBody(message)));
            }
        }
    }
}
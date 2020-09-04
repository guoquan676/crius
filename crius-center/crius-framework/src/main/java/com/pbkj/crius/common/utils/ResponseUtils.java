package com.pbkj.crius.common.utils;


import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import static org.apache.commons.lang.StringUtils.endsWith;
import static org.apache.commons.lang.StringUtils.startsWith;


public class ResponseUtils {

    public static void output(OutputStream response, String data) throws IOException {
        IOUtils.write(data, response, "UTF-8");
    }

    /**
     * 输出指定字符串
     */
    public static void output(HttpServletResponse response, String text) {
        try {
            if (startsWith(text, "{") && endsWith(text, "}")) {
                response.setContentType("application/json; charset=utf-8");
            } else {
                response.setContentType("text/html;charset=utf-8");
            }
            PrintWriter pw = response.getWriter();
            if (pw == null) {
                return;
            }
            pw.print(text);
            pw.flush();
            pw.close();
        } catch (Exception e) {
        }
    }

    public static ResponseErrorBody getResponseErrorBody(String msg) {
        ResponseErrorBody responseErrorBody = new ResponseErrorBody();
        responseErrorBody.setMsg(msg);
        return responseErrorBody;
    }

    public static class ResponseErrorBody {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}

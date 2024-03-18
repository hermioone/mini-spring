package org.hermione.minis.web;


import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
 * response 的结果转换器
 */
public interface HttpMessageConverter {
    void write(Object obj, HttpServletResponse response) throws IOException;
}

package org.hermione.minis.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("DuplicatedCode")
public class WebUtils {
    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        if (prefix == null) {
            prefix = "";
        }
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String unprefixed = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);

                params.put(unprefixed, value);
            }
        }
        return params;
    }

    public static Map<String, Object> getParameters(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String value = request.getParameter(paramName);
            params.put(paramName, value);
        }
        return params;
    }

    public static Object getParameter(HttpServletRequest request, String parameterName) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>();
        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (StringUtils.equals(parameterName, paramName)) {
                return request.getParameter(paramName);
            }
        }
        return null;
    }
}

package com.example.api.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.ssi.ByteArrayServletOutputStream;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class SwaggerFixFilter implements Filter {

    public final static String SPRINGFOX_ISSUE_1835_PREFIX = "<Json>";
    public final static String SPRINGFOX_ISSUE_1835_SUFFIX = "</Json>";

    private final Environment env;

    public SwaggerFixFilter(Environment env) {
        this.env = env;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String json;

        try (ByteArrayServletOutputStream s = new ByteArrayServletOutputStream()) {
            HttpServletResponseWrapper w = new HttpServletResponseWrapper(res) {
                @Override
                public void setContentType(String type) {
                }

                @Override
                public void setHeader(String name, String value) {
                }

                @Override
                public void addHeader(String name, String value) {
                }

                @Override
                public void flushBuffer() {
                }

                @Override
                public PrintWriter getWriter() {
                    throw new UnsupportedOperationException("getWriter, use getOutputStream");
                }

                @Override
                public ServletOutputStream getOutputStream() {
                    return s;
                }
            };
            chain.doFilter(request, w);
            json = new String(s.toByteArray(), "UTF-8");
        }

        // Change Swagger JSON - remove host
        json = json.replace(",\"host\":\"localhost\",", ",");

        String behindProxy = req.getHeader("X-Real-IP");
        if (behindProxy == null) {
            behindProxy = req.getHeader("X-Forwarded-For");
        }

        String path = env.getProperty("server.contextPath", "/");
        String rpath = System.getenv("REAL_CONTEXT_PATH");
        if (rpath != null) {
            path = rpath;
        }
        if (behindProxy != null && path != null) {
            json = json.replace(",\"basePath\":\"/\",", ",\"basePath\":\"" + path + "\",");
        }
        log.info("JSON: {}", json);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (json.startsWith(SPRINGFOX_ISSUE_1835_PREFIX)) {
            json = json.substring(SPRINGFOX_ISSUE_1835_PREFIX.length());
        }

        if (json.endsWith(SPRINGFOX_ISSUE_1835_SUFFIX)) {
            json = json.substring(0, json.length() - SPRINGFOX_ISSUE_1835_SUFFIX.length());
        }
        res.getOutputStream().write(json.getBytes("UTF-8"));
    }

    @Override
    public void destroy() {
    }
}
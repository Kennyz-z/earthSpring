package com.example.demo.component;

import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomCORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*"); // 允许所有来源
        httpResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE"); // 允许的HTTP方法
        httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type"); // 允许的请求头
        filterChain.doFilter(request, response);
    }

    // 其他方法
}

package com.chen.crm.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("验证登录过滤器");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");

        String path = request.getServletPath();
        //放行
        if ("/login.jsp".equals(path) || "/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            if (null == user){
                //没有登录过
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
            //登录过
            filterChain.doFilter(servletRequest,servletResponse);
         }
        }


    @Override
    public void destroy() {

    }
}

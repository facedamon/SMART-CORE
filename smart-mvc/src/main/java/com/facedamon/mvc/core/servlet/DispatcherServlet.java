package com.facedamon.mvc.core.servlet;

import com.facedamon.mvc.common.*;
import com.facedamon.mvc.common.model.Data;
import com.facedamon.mvc.common.model.Param;
import com.facedamon.mvc.common.model.View;
import com.facedamon.util.ReflectUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        InitLoader.init();
        ServletContext servletContext = config.getServletContext();
        ServletRegistration registration = servletContext.getServletRegistration("jsp");
        registration.addMapping(ConfigHolder.getJspPath(),"*");
        ServletRegistration defaultRegistration = servletContext.getServletRegistration("default");
        defaultRegistration.addMapping(ConfigHolder.getAssetPath(),"*");

        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();
        Handler handler = ControllerHolder.getHandler(requestMethod,requestPath);
        if (null != handler){
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHolder.getBean(controllerClass);
            Map<String,Object> params = HttpUtil.requestParams2Map(req);
            Param param = new Param(params);
            Method method= handler.getActionMethod();
            Object result = ReflectUtil.invokeMethod(controllerBean,method,param);

            if (result instanceof View){
                View view = (View) result;
                String path = view.getPath();
                //TODO
            }else if (result instanceof Data){

            }
        }
        super.service(req, resp);
    }
}

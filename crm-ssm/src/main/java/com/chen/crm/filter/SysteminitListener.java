package com.chen.crm.filter;

import com.chen.crm.settings.domain.DicType;
import com.chen.crm.settings.domain.DicValue;
import com.chen.crm.settings.service.DicTypeService;
import com.chen.crm.settings.service.DicValueService;
import com.chen.crm.settings.service.Impl.DicTypeServiceImpl;
import com.chen.crm.utils.ServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysteminitListener implements ServletContextListener {

    //@Autowired
    //private DicTypeService dicTypeService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("上下文域对象启动");

        //取service对象
      // DicTypeService dicTypeService = (DicTypeService) ServiceFactory.getService(new DicTypeServiceImpl());
        DicTypeService dicTypeService = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext()).getBean(DicTypeService.class);

        //取得上下文域对象
       ServletContext application = servletContextEvent.getServletContext();
       //取数据字典
       Map<String, List<DicValue>> stringListMap = dicTypeService.getAll();

       //把map集合解析为上下文域对象中保存的键值对
       Set<String> set = stringListMap.keySet();

        for (String key:set) {
            application.setAttribute(key,stringListMap.get(key));
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

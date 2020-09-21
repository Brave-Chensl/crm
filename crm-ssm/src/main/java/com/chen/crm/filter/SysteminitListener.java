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
import java.util.*;

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

        //解析Stage2Possibility.properties属性文件
        Map<String,String> pMap = new HashMap<String,String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys =  rb.getKeys();
            while (keys.hasMoreElements()){
                //取出每一个key
               String key =  keys.nextElement();//迭代器比for循环快，数据量小使用for循环，数据量大使用迭代器效率高
                //取出每一个value
                String value = rb.getString(key);
                //把数据保存到缓存中
                pMap.put(key,value);
            }
            application.setAttribute("pMap",pMap);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

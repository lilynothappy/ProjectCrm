package com.lynn.crm.web.listener;

import com.lynn.crm.settings.domain.DicValue;
import com.lynn.crm.settings.service.DicService;
import com.lynn.crm.settings.service.imp.DicServiceImp;
import com.lynn.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /*
        该方法是用来监听上下文域对象的方法，当服务器启动，上下文域对象创建
        对象创建完毕后，马上执行该方法

        event：该参数能够取得监听的对象
                监听的是什么对象，就可以通过该参数能取得什么对象
                例如我们现在监听的是上下文域对象，通过该参数就可以取得上下文域对象
     */
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("服务器缓存处理数字字典开始");

        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImp());


        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();
        for (String key : set) {
            application.setAttribute(key, map.get(key));

        }
        System.out.println("服务器缓存处理数字字典结束");

        //------------------------------------------------------------------------

        //数据字典处理完毕后，处理Stage2Possibility.properties文件
        /*

            处理Stage2Possibility.properties文件步骤：
                解析该文件，将该属性文件中的键值对关系处理成为java中键值对关系（map）

                Map<String(阶段stage),String(可能性possibility)> pMap = ....
                pMap.put("01资质审查",10);
                pMap.put("02需求分析",25);
                pMap.put("07...",...);

                pMap保存值之后，放在服务器缓存中
                application.setAttribute("pMap",pMap);
         */

        Map<String, String> pMap = new HashMap<String, String>();

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        Enumeration<String> e = rb.getKeys();

        while (e.hasMoreElements()) {
            //阶段
            String key = e.nextElement();
            //可能性
            String value = rb.getString(key);

            pMap.put(key, value);
        }

        application.setAttribute("pMap", pMap);


    }
}

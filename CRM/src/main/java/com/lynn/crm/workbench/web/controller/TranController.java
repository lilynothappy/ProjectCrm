package com.lynn.crm.workbench.web.controller;

import com.lynn.crm.settings.domain.User;
import com.lynn.crm.settings.service.UserService;
import com.lynn.crm.settings.service.imp.UserServiceImp;
import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.PrintJson;
import com.lynn.crm.utils.ServiceFactory;
import com.lynn.crm.utils.UUIDUtil;
import com.lynn.crm.workbench.domain.Tran;
import com.lynn.crm.workbench.domain.TranHistory;
import com.lynn.crm.workbench.service.CustomerService;
import com.lynn.crm.workbench.service.TranService;
import com.lynn.crm.workbench.service.imp.CustomerServiceImp;
import com.lynn.crm.workbench.service.imp.TranServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");
        String path=request.getServletPath();

        if("/workbench/transaction/add.do".equals(path)){
            add(request,response);

        }else if("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);

        }else if("/workbench/transaction/save.do".equals(path)){
            save(request,response);

        }else if("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);

        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){
            getHistoryListByTranId(request,response);

        }else if("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得交易阶段数量统计图表的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImp());

        Map<String,Object> map = ts.getCharts();

        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入改变阶段操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime= DateTimeUtil.getSysTime();
        String editBy= ((User) request.getSession().getAttribute("user")).getName();


        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);


        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImp());
        boolean flag = ts.changeStage(t);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("possibility",possibility);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据交易Id获取交易历史列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImp());

        List<TranHistory> tranHistoryList = ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        for(TranHistory th : tranHistoryList){
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细页面操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImp());

        Tran t = ts.getById(id);

        /*较为完整的三种写法
         ServletContext application =this.getServletContext();
         ServletContext application =request.getServletContext();
         ServletContext application =this.getServletConfig().getServletContext();
        */
        Map<String,String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String stage = t.getStage();
        String possibility = pMap.get(stage);

        //另外一种向前端传递possibility的方法为，在实体类Tran中添加possibility后封装到t中
        request.setAttribute("possibility",possibility);
        request.setAttribute("t",t);

        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到交易添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//本应获取客户Id，此处暂时只有客户名称
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String createTime= DateTimeUtil.getSysTime();
        String createBy= ((User) request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        //t.setCustomerId();
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImp());
        boolean flag = ts.save(t,customerName);

        if(flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得客户名称列表(按照客户名称进行模糊查询)");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImp());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);


    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转到交易添加页的操作");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImp());

        List<User> uList=us.getUserList();

        request.setAttribute("uList",uList);

        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);


    }


}

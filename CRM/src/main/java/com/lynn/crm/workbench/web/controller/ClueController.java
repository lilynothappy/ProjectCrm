package com.lynn.crm.workbench.web.controller;

import com.lynn.crm.settings.domain.User;
import com.lynn.crm.settings.service.UserService;
import com.lynn.crm.settings.service.imp.UserServiceImp;
import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.PrintJson;
import com.lynn.crm.utils.ServiceFactory;
import com.lynn.crm.utils.UUIDUtil;
import com.lynn.crm.vo.CluePageVo;
import com.lynn.crm.workbench.domain.Activity;
import com.lynn.crm.workbench.domain.Clue;
import com.lynn.crm.workbench.domain.Tran;
import com.lynn.crm.workbench.service.ActivityService;
import com.lynn.crm.workbench.service.ClueService;
import com.lynn.crm.workbench.service.imp.ActivityServiceImp;
import com.lynn.crm.workbench.service.imp.ClueServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索控制器");
        String path=request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);

        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);

        }else if("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);

        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);

        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);

        }else if("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);

        }else if("/workbench/clue/getActivityList.do".equals(path)){
            getActivityList(request,response);

        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);

        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);

        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);

        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("执行线索转换得操作");

        String clueId = request.getParameter("clueId");

        String flag = request.getParameter("flag");

        Tran t = null;

        String createBy= ((User) request.getSession().getAttribute("user")).getName();

        //如果需要穿件交易
        if("a".equals(flag)){
            t = new Tran();

            //接受交易表单中的参数
            String id = UUIDUtil.getUUID();
            String moneny = request.getParameter("moneny");
            String name = request.getParameter("name");
            String expectediDate = request.getParameter("expectediDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime= DateTimeUtil.getSysTime();

            t.setId(id);
            t.setMoney(moneny);
            t.setName(name);
            t.setExpectedDate(expectediDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);

        }

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImp());

        boolean flag1 = cs.convert(clueId,t,createBy);

        if(flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行关联市场活动的操作");

        String cid = request.getParameter("cid");
        String aids[] =request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImp());

        boolean flag = cs.bund(cid,aids);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动列表（根据名称模糊查+排除已经关联的市场活动信息");

        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("name",name);
        map.put("clueId",clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> list = as.getActivityListByName(map);

        PrintJson.printJsonObj(response,list);

    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("关联市场活动的模态窗口中获取市场活动列表(排除已经关联的市场活动信息)");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> list = as.getActivityList(clueId);

        PrintJson.printJsonObj(response,list);
    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索与市场活动关系解除");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImp());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取线索关联的市场活动");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<Activity> list = as.getActivityListByClueId(clueId);

        PrintJson.printJsonObj(response,list);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页面的操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImp());

        Clue c = cs.detail(id);

        request.setAttribute("c",c);

        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询线索信息列表的操作（结合条件查询+分页查询)");

        String fullname = request.getParameter("fullname");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);

        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("source",source);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        CluePageVo<Clue> vo = cs.pageList(map);

        PrintJson.printJsonObj(response,vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime= DateTimeUtil.getSysTime();
        String createBy= ((User) request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImp());

        boolean flag = cs.save(clue);

        PrintJson.printJsonFlag(response,flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImp());

        List<User> uList=us.getUserList();

        PrintJson.printJsonObj(response,uList);

    }


}

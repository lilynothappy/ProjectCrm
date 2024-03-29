package com.lynn.crm.workbench.web.controller;

import com.lynn.crm.settings.domain.User;
import com.lynn.crm.settings.service.UserService;
import com.lynn.crm.settings.service.imp.UserServiceImp;
import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.PrintJson;
import com.lynn.crm.utils.ServiceFactory;
import com.lynn.crm.utils.UUIDUtil;
import com.lynn.crm.vo.PaginationVo;
import com.lynn.crm.workbench.domain.Activity;
import com.lynn.crm.workbench.domain.ActivityRemark;
import com.lynn.crm.workbench.service.ActivityService;
import com.lynn.crm.workbench.service.imp.ActivityServiceImp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到市场活动控制器");
        String path=request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);

        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);

        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);

        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);

        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);

        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(request,response);

        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);

        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);

        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);

        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行修改市场活动备注操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String eidtFlag = "1";

        ActivityRemark ar =new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(eidtFlag);

        ActivityService as =(ActivityService)ServiceFactory.getService(new ActivityServiceImp());
        boolean flag = as.updateRemark(ar);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加市场活动备注操作");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = request.getParameter("activityId");

        ActivityRemark ar = new ActivityRemark();

        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setNoteContent(noteContent);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        boolean flag = as.saveRemark(ar);

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("ar",ar);

        PrintJson.printJsonObj(response,map);


    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据Id删除市场活动备注");

        String id=request.getParameter("id");

        ActivityService ars= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        boolean flag=ars.deleteRemark(id);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("根据市场活动id，获取备注信息列表");

        String activityId=request.getParameter("activityId");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        List<ActivityRemark>  arList=as.getRemarkListByAid(activityId);

        PrintJson.printJsonObj(response,arList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到详细信息页面的操作");

        String id=request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        Activity a=as.detail(id);

        request.setAttribute("a",a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);



    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场修改活动");

        String id= request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String editTime= DateTimeUtil.getSysTime();
        String editBy= ((User) request.getSession().getAttribute("user")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        boolean flag=as.update(a);

        PrintJson.printJsonFlag(response,flag);



    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场修改查询活动");

        String id=request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        Map<String,Object> map=as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);



    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的删除操作");

        String ids[]=request.getParameterValues("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        boolean flag=as.delete(ids);

        PrintJson.printJsonFlag(response,flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询市场活动信息列表的操作（结合条件查询+分页查询)");

        String name=request.getParameter("name");
        String owner=request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");

        String pageNoStr=request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);

        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);

        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<String, Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        PaginationVo<Activity> vo=as.pageList(map);

        PrintJson.printJsonObj(response,vo);
    }


    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImp());

        List<User> uList=us.getUserList();

        PrintJson.printJsonObj(response,uList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");

        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();
        String createBy= ((User) request.getSession().getAttribute("user")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());

        boolean flag=as.save(a);

        PrintJson.printJsonFlag(response,flag);
    }
}

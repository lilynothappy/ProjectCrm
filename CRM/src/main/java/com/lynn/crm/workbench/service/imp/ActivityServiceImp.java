package com.lynn.crm.workbench.service.imp;

import com.lynn.crm.settings.dao.UserDao;
import com.lynn.crm.settings.domain.User;
import com.lynn.crm.utils.SqlSessionUtil;
import com.lynn.crm.vo.PaginationVo;
import com.lynn.crm.workbench.dao.ActivityDao;
import com.lynn.crm.workbench.dao.ActivityRemarkDao;
import com.lynn.crm.workbench.domain.Activity;
import com.lynn.crm.workbench.domain.ActivityRemark;
import com.lynn.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImp implements ActivityService {

    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityDao activityDao=SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public boolean save(Activity a) {

        boolean flag=true;

        int count = activityDao.save(a);
        if(count!=1){
            flag=false;
        }

        return flag;
    }

    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        int total=activityDao.getTotalByCondition(map);

        List<Activity> list=activityDao.getActivityListByCondition(map);

        PaginationVo vo=new PaginationVo();
        vo.setTotal(total);
        vo.setDataList(list);

        return vo;
    }

    public boolean delete(String[] ids) {

        boolean flag=true;

        int count1=activityRemarkDao.getCountByAids(ids);

        int count2=activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){
            flag=false;
        }

        int count3=activityDao.delete(ids);
        if(count3!=ids.length){
            flag=false;
        }

        return flag;
    }

    public Map<String, Object> getUserListAndActivity(String id) {
        List<User> uList=userDao.getUserList();
        Activity a=activityDao.getById(id);

        Map<String, Object> map=new HashMap<String, Object>();
        map.put("uList",uList);
        map.put("a",a);

        return map;
    }

    public boolean update(Activity a) {
        boolean flag=true;

        int count = activityDao.update(a);
        if(count!=1){
            flag=false;
        }

        return flag;
    }

    public Activity detail(String id) {
        Activity a=activityDao.detail(id);

        return a;
    }

    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        List<ActivityRemark>  arList=activityRemarkDao.getRemarkListByAid(activityId);

        return arList;
    }

    public boolean deleteRemark(String id) {
        boolean flag=true;

        int count=activityRemarkDao.deleteRemark(id);

        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean saveRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count != 1){
            flag = false;
        }

        return flag;
    }

    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> list = activityDao.getActivityListByClueId(clueId);

        return list;
    }

    public List<Activity> getActivityList(String clueId) {
        List<Activity> list = activityDao.getActivityList(clueId);

        return list;
    }

    public List<Activity> getActivityListByName(Map<String, String> map) {
        List<Activity> list = activityDao.getActivityListByName(map);

        return list;
    }


}

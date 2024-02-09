package com.lynn.crm.workbench.service;

import com.lynn.crm.vo.PaginationVo;
import com.lynn.crm.workbench.domain.Activity;
import com.lynn.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity a);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);


    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityList(String clueId);

    List<Activity> getActivityListByName(Map<String, String> map);
}

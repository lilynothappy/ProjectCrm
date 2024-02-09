package com.lynn.crm.workbench.dao;

import com.lynn.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {


    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}

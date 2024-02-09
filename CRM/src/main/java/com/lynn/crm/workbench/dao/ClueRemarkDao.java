package com.lynn.crm.workbench.dao;

import com.lynn.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int deleteById(String id);
}

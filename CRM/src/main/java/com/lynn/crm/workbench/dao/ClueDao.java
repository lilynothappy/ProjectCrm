package com.lynn.crm.workbench.dao;


import com.lynn.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue clue);

    int getTotalByCondition(Map<String, Object> map);

    List<Clue> getClueListByCondition(Map<String, Object> map);

    Clue detail(String id);

    Clue getbyId(String clueId);

    int delete(String clueId);
}

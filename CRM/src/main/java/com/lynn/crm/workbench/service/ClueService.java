package com.lynn.crm.workbench.service;

import com.lynn.crm.vo.CluePageVo;
import com.lynn.crm.workbench.domain.Clue;
import com.lynn.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {

    boolean save(Clue clue);

    CluePageVo<Clue> pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}

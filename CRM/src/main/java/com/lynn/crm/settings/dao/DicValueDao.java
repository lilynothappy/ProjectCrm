package com.lynn.crm.settings.dao;

import com.lynn.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {

    List<DicValue> getListByCode(String code);
}

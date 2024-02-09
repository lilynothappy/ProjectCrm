package com.lynn.crm.settings.service.imp;

import com.lynn.crm.settings.dao.DicTypeDao;
import com.lynn.crm.settings.dao.DicValueDao;
import com.lynn.crm.settings.domain.DicType;
import com.lynn.crm.settings.domain.DicValue;
import com.lynn.crm.settings.service.DicService;
import com.lynn.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImp implements DicService {
    private DicTypeDao dicTypedao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValuedao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {
        List<DicType> dicTypeList = dicTypedao.getTypeList();

        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();

        for(DicType dt : dicTypeList){
            String code = dt.getCode();
            List<DicValue> dicValueList = dicValuedao.getListByCode(code);
            map.put(code,dicValueList);
        }


        return map;
    }
}

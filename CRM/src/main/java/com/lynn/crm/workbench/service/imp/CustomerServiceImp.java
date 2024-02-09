package com.lynn.crm.workbench.service.imp;

import com.lynn.crm.utils.SqlSessionUtil;
import com.lynn.crm.workbench.dao.CustomerDao;
import com.lynn.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImp implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {
        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }
}

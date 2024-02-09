package com.lynn.crm.workbench.dao;

import com.lynn.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getByName(String company);

    int save(Customer customer);

    List<String> getCustomerName(String name);
}

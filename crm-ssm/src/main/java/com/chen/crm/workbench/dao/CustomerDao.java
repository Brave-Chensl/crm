package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer selectTranByName(String company);

    int save(Customer customer);

    List<String> selectCustomerList(String name);
}

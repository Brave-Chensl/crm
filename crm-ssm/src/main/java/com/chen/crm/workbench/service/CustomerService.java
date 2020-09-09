package com.chen.crm.workbench.service;

import com.chen.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<String> selectCustomerList(String name);
}

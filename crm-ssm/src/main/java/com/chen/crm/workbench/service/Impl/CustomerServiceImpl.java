package com.chen.crm.workbench.service.Impl;

import com.chen.crm.workbench.dao.CustomerDao;
import com.chen.crm.workbench.domain.Customer;
import com.chen.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public List<String> selectCustomerList(String name) {
      List<String> customerList = customerDao.selectCustomerList(name);

        return customerList;
    }
}

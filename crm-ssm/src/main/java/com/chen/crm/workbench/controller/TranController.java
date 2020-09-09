package com.chen.crm.workbench.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.workbench.domain.Customer;
import com.chen.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/tran")
public class TranController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping("/addtran.do")
    public String addtran(Model model){
       List<User> users = userService.selectUserList();
       model.addAttribute(users);
       return "transaction/save";
    }

    @RequestMapping("/getCustomerName.do")
    public @ResponseBody Object getCustomerName(String name){
        System.out.println("收到自动补全请求"+name);
        List<String> customers = customerService.selectCustomerList(name);
       return customers;
    }

}

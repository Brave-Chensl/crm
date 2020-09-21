package com.chen.crm.workbench.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.workbench.dao.TranDao;
import com.chen.crm.workbench.domain.Customer;
import com.chen.crm.workbench.domain.Tran;
import com.chen.crm.workbench.domain.TranHistory;
import com.chen.crm.workbench.service.CustomerService;
import com.chen.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/tran")
public class TranController {

    @Autowired
    private CustomerService customerService;

  @Autowired
    private TranService tranService;

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
  @RequestMapping("/save.do")
    public String save(HttpServletRequest request, Model model, Tran tran,String customerName) {
      System.out.println("收到创建交易请求" + tran.getName());
      tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
      boolean flag = tranService.save(tran,customerName);
      if (flag) {
          return "transaction/index";
      } else {
          return "transaction/error";
      }
  }
  @RequestMapping("/select.do")
    public @ResponseBody Object select(){
       List<Tran> trans = tranService.select();
       return trans;
  }
  @RequestMapping("/detail.do")
    public String detail(HttpServletRequest request,Model model,String id){
       Tran tran = tranService.selectDetailById(id);

      ServletContext servletContext = request.getServletContext();
      Map<String,String> attribute = (Map<String, String>) servletContext.getAttribute("pMap");
      String s = attribute.get(tran.getStage());
      model.addAttribute(tran);
      model.addAttribute("s",s);
       return "transaction/detail";
  }
    @RequestMapping("/selectTranHistoryList.do")
    public @ResponseBody Object selectTranHistoryList(HttpServletRequest request,String id){
        List<TranHistory> tranHistoryList = tranService.selectTranHistoryList(id);
        Map<String,Object> map = new HashMap<String, Object>();
        ServletContext servletContext = request.getServletContext();
        Map<String,String> attribute = (Map<String, String>) servletContext.getAttribute("pMap");
        Tran tran = tranService.selectDetailById(id);
        String s = attribute.get(tran.getStage());
        map.put("s",s);
        map.put("tranList",tranHistoryList);
        return map;
    }
    @RequestMapping("/changeStage.do")
    public @ResponseBody Object changeStage(HttpServletRequest request,Tran tran){
        tran.setEditTime(DateTimeUtil.getSysTime());
        tran.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
       String possibility = pMap.get(tran.getStage());
        boolean flag =  tranService.changeStage(tran);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",flag);
        map.put("t",tran);
        map.put("possibility",possibility);
        return map;
    }

    @RequestMapping("/getEcharts.do")
    public @ResponseBody Object getEcharts(){
       Map<String,Object> map = tranService.getEcharts();
       return map;
    }
}

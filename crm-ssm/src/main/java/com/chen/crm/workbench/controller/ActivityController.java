package com.chen.crm.workbench.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService actvivtyService;

  //处理ajax返回用户信息
    @RequestMapping("/find.do")
    public @ResponseBody List<User> findUser(){
        System.out.println("执行service调用service实现方法b");
       List<User> userList = userService.selectUserList();
       //model.addAttribute(userList);
        System.out.println("controller无异常");
       return userList;
    }


    //添加市场活动
    @RequestMapping("/save.do")
    public @ResponseBody Object addActivity(HttpServletRequest request,Activity activity) {

        boolean fig = false;
        //System.out.println(activity.getCost());
        activity.setId(UUIDUtil.getUUID());
       String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setCreateBy(createBy);//添加创建人
        activity.setCreateTime(DateTimeUtil.getSysTime()); //添加当前时间
       int count = actvivtyService.addActivity(activity);
        if (count==1){
            fig=true;
        }
        return fig;

    }
}

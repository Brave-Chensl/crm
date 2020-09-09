package com.chen.crm.workbench.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.domain.Clue;
import com.chen.crm.workbench.domain.Tran;
import com.chen.crm.workbench.service.ActivityService;
import com.chen.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clue")
public class clueController {

    @Autowired
   private UserService userService;

    @Autowired
   private ActivityService activityService;

    @Autowired
    private ClueService clueService;

    @RequestMapping("/selectUserList.do")
    public @ResponseBody Object selectuserList(){
       List<User> users = userService.selectUserList();
       return users;
    }

    @RequestMapping("/add.do")
    public @ResponseBody Object add(HttpServletRequest request,Clue clue){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        clue.setCreateTime(DateTimeUtil.getSysTime());

      boolean flg =  clueService.addClue(clue);

        return flg;
     }
    @RequestMapping("/detail.do")
    public String detail(Model model, String id){
      Clue clue  =  clueService.detailClue(id);
        model.addAttribute(clue);
        return "clue/detail";
     }
 @RequestMapping("/getActivitys.do")
    public @ResponseBody Object getActivitys(String id){
      List<Activity> activityList  =  clueService.getActivitys(id);

        return activityList;
     }
@RequestMapping("/deleteByRelation.do")
    public @ResponseBody Object deleteByRelation(String id){
      boolean flg =  clueService.deleteByRelation(id);

        return flg;
     }

     @RequestMapping("/getactivityList.do")
    public @ResponseBody Object getactivityList(String message,String cid){
         Map<String,String> map = new HashMap<String, String>();
         System.out.println(message);
         map.put("message",message);
         map.put("clueId",cid);
      List<Activity> activityList =  activityService.getActivityList(map);

        return activityList;
     }
 @RequestMapping("/addCARelation.do")
    public @ResponseBody Object addCARelation(String cid,String[] aid){

       boolean flag = clueService.addClueAndActivity(cid,aid);

        return flag;
     }
@RequestMapping("/getActivityListByName.do")
    public @ResponseBody Object getActivityListByName(String aname){

       List<Activity> activityList = clueService.getActivityListByName(aname);

        return activityList;
     }

@RequestMapping("/convert.do")
    public String convert(String mark,Tran tran,String clueId,HttpServletRequest request) {
    boolean flag = false;
    String createBy = ((User) request.getSession().getAttribute("user")).getName();
    Tran t = null;//标记
    if ("y".equals(mark)) {
        t = tran;
    }
    flag = clueService.addconvert(clueId, createBy, t);
    if (flag) {
        return "clue/index";
    } else {
        return "clue/errer";
    }
}
}

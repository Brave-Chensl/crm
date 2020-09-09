package com.chen.crm.workbench.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.vo.PaginationVO;
import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.domain.ActivityRemark;
import com.chen.crm.workbench.service.ActivityRemarkService;
import com.chen.crm.workbench.service.ActivityService;
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
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService actvivtyService;

    @Autowired
    private ActivityRemarkService activityRemarkService;

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
    @RequestMapping("/pageList.do")
    public @ResponseBody Object pageList(HttpServletRequest request){

        String No = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(No);  //第几页
        String Size = request.getParameter("pageSize");
        int pageSize =Integer.valueOf(Size); //每页展示多少条数据
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        System.out.println("收到请求"+pageNo);
        //计算出略过的数据
        int skipCount = (pageNo-1) *pageSize;

        Map<String,Object> activityMap = new HashMap<String, Object>();
        activityMap.put("name",name);
        activityMap.put("owner",owner);
        activityMap.put("startDate",startDate);
        activityMap.put("endDate",endDate);
        activityMap.put("name",name);
        activityMap.put("skipCount",skipCount);
        activityMap.put("pageSize",pageSize);
       PaginationVO<Activity> vo = (PaginationVO<Activity>) actvivtyService.selectActivity(activityMap);
        System.out.println(".do执行完毕"+vo.getTotal());
        return vo;
    }

    @RequestMapping("/delete.do")
    public @ResponseBody Object deleteActivity(String[] id){
       boolean flag =  actvivtyService.deleteActivityById(id);
        return flag;
    }

    @RequestMapping("/edit.do")
    public @ResponseBody Object editActivity(String id){
       Map<String,Object> map = actvivtyService.selectById(id);

        return map;
    }
    @RequestMapping("/update.do")
    public @ResponseBody Object updateActivity(HttpServletRequest request,Activity activity){
        boolean fig = false;
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setEditBy(createBy);//添加修改人
        activity.setEditTime(DateTimeUtil.getSysTime()); //添加当前时间
        int count = actvivtyService.updateActivity(activity);
        if (count==1){
            fig=true;
        }
        return fig;
    }
    @RequestMapping("/detail.do")
    public String activityDetail(Model model,String id){
      Activity activity =  actvivtyService.detailById(id);
      model.addAttribute(activity);
        return "activity/detail";
    }
    @RequestMapping("/remark.do")
    public @ResponseBody Object activityRemark(String id){
        List<ActivityRemark> activityRemarkList = activityRemarkService.activityByIdRemark(id);

        return activityRemarkList;
    }

    @RequestMapping("/deleteRemark.do")
    public @ResponseBody Object deleteRemark(String id){
        boolean flg = activityRemarkService.deleteByIdRemark(id);

        return flg;
    }
    @RequestMapping("/addRemark.do")
    public @ResponseBody Object addRemark(HttpServletRequest request,ActivityRemark remark){
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setCreateTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("0");
        //System.out.println(remark.getActivityId());
        boolean flg = activityRemarkService.addRemark(remark);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("remark",remark);
        map.put("success",flg);
        return map;
    }
    @RequestMapping("/updateRemark.do")
    public @ResponseBody Object updateRemark(HttpServletRequest request,ActivityRemark remark){
        remark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setEditTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("1");
        //System.out.println(remark.getActivityId());
        boolean flg = activityRemarkService.updateRemark(remark);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("remark",remark);
        map.put("success",flg);
        return map;
    }
}

package com.chen.crm.workbench.service.Impl;

import com.chen.crm.settings.dao.UserDao;
import com.chen.crm.settings.domain.User;
import com.chen.crm.vo.PaginationVO;
import com.chen.crm.workbench.dao.ActivityDao;
import com.chen.crm.workbench.dao.ActivityRemarkDao;
import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
   private ActivityDao activityDao;

    @Autowired
    private ActivityRemarkDao activityRemarkDao;

    @Autowired
    private UserDao userDao;



    @Override
    public int addActivity(Activity activity) {
       int count = activityDao.addActivity(activity);
        return count;
    }

    @Override
    public PaginationVO<Activity> selectActivity(Map<String,Object> map) {
       //取total

        System.out.println("service执行");
        int activityCount = activityDao.selectActivityCount(map);
        //取dataList
        List<Activity> dataList = activityDao.selectActivityList(map);
        //把数据封装到vo对象中，返回
        PaginationVO vo = new PaginationVO();
        vo.setTotal(activityCount);
        vo.setDataList(dataList);
        System.out.println("ov执行结束");
        return vo;
    }

    @Override
    public boolean deleteActivityById(String[] id) {
        boolean flag = true;
        //查询关联的备注有几条
        int count1 = activityRemarkDao.activityCountById(id);
        //删除的备注
        int count2 = activityRemarkDao.deleteActivityRemark(id);

        if (count1!=count2){
            flag=false;
        }
        //删除activity表本身
      int count = activityDao.deleteActivityById(id);
        if (count!=id.length){
            flag=false;
        }

        return flag;
    }

    @Override
    public Map<String,Object> selectById(String id) {
        Activity activity = activityDao.selectById(id);
        Map<String,Object> map = new HashMap<String, Object>();
        List<User> ulist = userDao.selectUserList();
        map.put("a",activity);
        map.put("ulist",ulist);
        return map;
    }

    @Override
    public int updateActivity(Activity activity) {
       int count = activityDao.updateActivity(activity);
        return count;
    }

    @Override
    public Activity detailById(String id) {
        Activity activity =  activityDao.detailById(id);
        return activity;
    }

    @Override
    public List<Activity> getActivityList(Map<String, String> map) {

       List<Activity> activityList = activityDao.getActivityList(map);
       return activityList;

    }

}

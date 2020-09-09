package com.chen.crm.workbench.dao;

import com.chen.crm.vo.PaginationVO;
import com.chen.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int addActivity(Activity activity);

    PaginationVO<Activity> selectActivity(Map<String,Object> map);

    List<Activity> selectActivityList(Map<String, Object> map);

    int selectActivityCount(Map<String, Object> map);

    int deleteActivityById(String[] id);

    Activity selectById(String id);

    int updateActivity(Activity activity);

    Activity detailById(String id);

    List<Activity> selectActivitys(String[] id);

    List<Activity> getActivityList(Map<String,String> map);

    List<Activity> getActivityListByName(String aname);
}

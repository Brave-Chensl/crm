package com.chen.crm.workbench.service;

import com.chen.crm.vo.PaginationVO;
import com.chen.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int addActivity(Activity activity);

    PaginationVO<Activity> selectActivity(Map<String,Object> map);

    boolean deleteActivityById(String[] id);

    Map<String,Object> selectById(String id);

    int updateActivity(Activity activity);

    Activity detailById(String id);

    List<Activity> getActivityList(Map<String,String> map);
}

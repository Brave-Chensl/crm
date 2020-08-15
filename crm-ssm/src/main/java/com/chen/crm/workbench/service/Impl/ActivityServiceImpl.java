package com.chen.crm.workbench.service.Impl;

import com.chen.crm.workbench.dao.ActivityDao;
import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
   private ActivityDao activityDao;

    @Override
    public int addActivity(Activity activity) {
       int count = activityDao.addActivity(activity);
        return count;
    }
}

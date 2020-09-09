package com.chen.crm.workbench.service.Impl;

import com.chen.crm.workbench.dao.ActivityRemarkDao;
import com.chen.crm.workbench.domain.ActivityRemark;
import com.chen.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public List<ActivityRemark> activityByIdRemark(String id) {
       List<ActivityRemark> activityRemarkList = activityRemarkDao.selectByIdRemark(id);

        return activityRemarkList;
    }

    @Override
    public boolean deleteByIdRemark(String id) {
       int i =  activityRemarkDao.deleteByIdRemark(id);
       if (i==1){
           return true;
       }
       return false;
    }

    @Override
    public boolean addRemark(ActivityRemark remark) {

       int i = activityRemarkDao.insertRemark(remark);
       if (i==1){
           return true;
       }
        return false;
    }

    @Override
    public boolean updateRemark(ActivityRemark remark) {
        int i = activityRemarkDao.updateRemark(remark);
        if (i==1){
            return true;
        }
        return false;
    }
}

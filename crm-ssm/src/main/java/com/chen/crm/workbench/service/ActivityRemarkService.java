package com.chen.crm.workbench.service;

import com.chen.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> activityByIdRemark(String id);

    boolean deleteByIdRemark(String id);

    boolean addRemark(ActivityRemark remark);

    boolean updateRemark(ActivityRemark remark);
}

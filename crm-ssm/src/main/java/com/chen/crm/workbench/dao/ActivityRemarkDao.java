package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int activityCountById(String[] id);

    int deleteActivityRemark(String[] id);

    List<ActivityRemark> selectByIdRemark(String id);

    int deleteByIdRemark(String id);

    int insertRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark remark);
}

package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectByClueId(String clueId);

    int delete(String clueId);
}

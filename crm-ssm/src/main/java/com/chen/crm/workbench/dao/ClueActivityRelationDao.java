package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {


    String[] getActivitys(String id);

    int deleteByRelation(String id);


    int add(ClueActivityRelation car);

    List<ClueActivityRelation> getClueActivityRelationByClueId(String clueId);

    int delete(ClueActivityRelation car);
}

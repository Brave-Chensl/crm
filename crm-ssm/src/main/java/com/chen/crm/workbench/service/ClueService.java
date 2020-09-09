package com.chen.crm.workbench.service;

import com.chen.crm.workbench.domain.Activity;
import com.chen.crm.workbench.domain.Clue;
import com.chen.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {

    boolean addClue(Clue clue);

    Clue detailClue(String id);

    List<Activity> getActivitys(String id);

    boolean deleteByRelation(String id);

    boolean addClueAndActivity(String cid, String[] aid);

    List<Activity> getActivityListByName(String aname);

    boolean addconvert(String clueId,String createBy,Tran tran);

}

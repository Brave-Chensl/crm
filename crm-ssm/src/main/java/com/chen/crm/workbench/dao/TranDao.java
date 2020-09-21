package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {


    int save(Tran t);

    List<Tran> select();

    Tran selectDetailById(String id);

    int changeStage(Tran tran);

    int getCount();

    List<Map<String, Object>> getEcharts();
}

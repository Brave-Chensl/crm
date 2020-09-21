package com.chen.crm.workbench.service;

import com.chen.crm.workbench.domain.Tran;
import com.chen.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran,String customerName);

    List<Tran> select();

    Tran selectDetailById(String id);

    List<TranHistory> selectTranHistoryList(String id);

    boolean changeStage(Tran tran);

    Map<String, Object> getEcharts();
}

package com.chen.crm.workbench.dao;

import com.chen.crm.workbench.domain.Clue;

public interface ClueDao {


    int addClue(Clue clue);

    Clue getClueById(String id);

    Clue selectClueById(String clueId);

    int delete(String clueId);
}

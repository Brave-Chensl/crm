package com.chen.crm.settings.dao;

import com.chen.crm.settings.domain.DicType;
import com.chen.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {


    List<DicValue> getDicValueList(String code);
}

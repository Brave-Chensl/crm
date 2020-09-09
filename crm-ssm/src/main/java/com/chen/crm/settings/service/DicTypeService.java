package com.chen.crm.settings.service;

import com.chen.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicTypeService {
    Map<String, List<DicValue>> getAll();
}

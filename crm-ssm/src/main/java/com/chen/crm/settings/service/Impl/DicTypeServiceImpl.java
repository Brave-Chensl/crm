package com.chen.crm.settings.service.Impl;

import com.chen.crm.settings.dao.DicTypeDao;
import com.chen.crm.settings.dao.DicValueDao;
import com.chen.crm.settings.domain.DicType;
import com.chen.crm.settings.domain.DicValue;
import com.chen.crm.settings.service.DicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicTypeServiceImpl implements DicTypeService {


    @Autowired
    private DicTypeDao dicTypeDao;

    @Autowired
    private DicValueDao dicValueDao;



    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map=new HashMap<String, List<DicValue>>();

        //取得类型数据
        List<DicType> dicTypeList =  dicTypeDao.getDicTypeList();

        //遍历类型
        for (DicType key:dicTypeList) {
            //取得类型的code值
            String code = key.getCode();
            //根据code值取出对应的value
              List<DicValue> dicValues =  dicValueDao.getDicValueList(code);

              map.put(code+"List",dicValues);
        }



            return map;

    }
}

package com.chen.crm.workbench.service.Impl;

import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.workbench.dao.CustomerDao;
import com.chen.crm.workbench.dao.TranDao;
import com.chen.crm.workbench.dao.TranHistoryDao;
import com.chen.crm.workbench.domain.Customer;
import com.chen.crm.workbench.domain.Tran;
import com.chen.crm.workbench.domain.TranHistory;
import com.chen.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

   @Autowired
   private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran,String customerName) {
        /*
        *
        * 交易添加业务:在做添加之前,参数t里面就少了一项信息,就是客户的主键,
        * customerId先处理客户相关的需求(1)
        * 判断customerName,根据客户名称在客户表进行精确查询如果有这个客户
        * ,则取出这个客户的id,封装到t对象中如果没有这个客户,则再客户表新建一条客户信息,
        * 然后将新建的客户的id取出,封装到t对象中
        * (2)经过以上操作后，t对象中的信息就全了,需要执行添加交易的操作
        * (3)添加交易完毕后,需要创建一条交易历史*/
        boolean flag=true;
        Customer customer = customerDao.selectTranByName(customerName);
        if (customer==null){
            //需要创建客户
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setOwner(tran.getOwner());
            //添加客户
           int i = customerDao.save(customer);
            if (i!=1){
                flag=false;
        }            }


    //通过以上的处理，不论查出来是否是已经有的客户还是新的客户，总之客户已经有了，把客户id封装到tran对象中
        tran.setCustomerId(customer.getId());
        //添加交易
       int i = tranDao.save(tran);
       if (i!=1){
           flag=false;
       }

       //添加交易历史
        TranHistory th=new TranHistory();
       th.setId(UUIDUtil.getUUID());
       th.setMoney(tran.getMoney());
       th.setTranId(tran.getId());
       th.setStage(tran.getStage());
       th.setExpectedDate(tran.getExpectedDate());
       th.setCreateBy(tran.getCreateBy());
       th.setCreateTime(tran.getCreateTime());

       int j = tranHistoryDao.save(th);
        if (j != 1) {
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Tran> select() {
      List<Tran> trans =  tranDao.select();

        return trans;
    }

    @Override
    public Tran selectDetailById(String id) {
       Tran tran = tranDao.selectDetailById(id);

        return tran;
    }

    @Override
    public List<TranHistory> selectTranHistoryList(String id) {
      List<TranHistory> tranHistories = tranHistoryDao.selectTranHistoryList(id);

        return tranHistories;
    }

    @Override
    public boolean changeStage(Tran tran) {
        boolean flag = true;
        //改变交易阶段
        int count1 = tranDao.changeStage(tran);
        if (count1!=1){
            flag=false;
        }
        //生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(tran.getExpectedDate());
        th.setStage(tran.getStage());
        th.setTranId(tran.getId());
        th.setMoney(tran.getMoney());

       int count2 = tranHistoryDao.save(th);
        if (count2!=1){
            flag =false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getEcharts() {

       List<Map<String,Object>> mapList = tranDao.getEcharts();

       int count= tranDao.getCount();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("count",count);
        map.put("dataList",mapList);
        return map;
    }
}

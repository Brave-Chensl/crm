package com.chen.crm.workbench.service.Impl;

import com.chen.crm.utils.DateTimeUtil;
import com.chen.crm.utils.UUIDUtil;
import com.chen.crm.workbench.dao.*;
import com.chen.crm.workbench.domain.*;
import com.chen.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
//交易相关表
    @Autowired
    private TranDao tranDao;

    @Autowired
    private TranHistoryDao tranHistoryDao;

    //市场活动
    @Autowired
    private ActivityDao activityDao;

    //线索相关表
    @Autowired
    private ClueDao clueDao;

    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;

    @Autowired
    private ClueRemarkDao clueRemarkDao;

    //客户相关表
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerRemarkDao customerRemarkDao;

    @Autowired
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //联系人相关
    @Autowired
    private ContactsDao contactsDao;

    @Autowired
    private ContactsRemarkDao contactsRemarkDao;



    @Override
    public boolean addClue(Clue clue) {
       boolean flg = false;

        int i = clueDao.addClue(clue);
        if (i==1){
            flg=true;
        }

        return flg;
    }

    @Override
    public Clue detailClue(String id) {
       Clue clue = clueDao.getClueById(id);

        return clue;
    }

    @Override
    public List<Activity> getActivitys(String id) {
       String[] stringList= clueActivityRelationDao.getActivitys(id);
        List<Activity> activityList =  activityDao.selectActivitys(stringList);
        return activityList;
    }

    @Override
    public boolean deleteByRelation(String id) {
        boolean flg = false;
       int i = clueActivityRelationDao.deleteByRelation(id);
        if (i==1){
            flg=true;
        }
        return flg;
    }

    @Override
    public boolean addClueAndActivity(String cid,String[] aid) {
        boolean flag=true;
        for (String activityId:aid) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(activityId);
            car.setClueId(cid);

            int i =clueActivityRelationDao.add(car);
            if (i!=1){
                flag = false;
            }

        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
       List<Activity> activityList = activityDao.getActivityListByName(aname);
        return activityList;
    }

    @Override
    public boolean addconvert(String clueId,String createBy,Tran t) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        //通过线索id取得线索信息
       Clue clue = clueDao.selectClueById(clueId);

       String company = clue.getCompany();
        //通过公司名称，看是否有此客户
       Customer customer = customerDao.selectTranByName(company);
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setDescription(clue.getDescription());
           int i = customerDao.save(customer);
            if (i!=1){
                flag=false;
            }
        }
        //通过线索对象，取得联系人信息，保存联系人
           Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setJob(clue.getJob());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());

        int i = contactsDao.save(contacts);
        if (i!=1){
            flag =false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        //查询出与该线索关联的备注
       List<ClueRemark> clueRemarks = clueRemarkDao.selectByClueId(clueId);

        //取得每一个备注
        for (ClueRemark cr:clueRemarks){
           //取得备注信息
            String notContent = cr.getNoteContent();
            //创建客户备注对象
           CustomerRemark customerRemark = new CustomerRemark();
           // 把备注信息封装到客户备注中
           customerRemark.setNoteContent(notContent);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            //添加客户备注
           int count = customerRemarkDao.save(customerRemark);
            if (count!=1){
                flag=false;
            }
            //创建联系人备注对象
           ContactsRemark contactsRemark = new ContactsRemark();
           // 把备注信息封装到客户备注中
            contactsRemark.setNoteContent(notContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            //添加联系人备注
           int count1 = contactsRemarkDao.save(contactsRemark);
            if (count1!=1){
                flag=false;
            }
        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getClueActivityRelationByClueId(clueId);
        //取出每一条市场活动的id
        for (ClueActivityRelation car:clueActivityRelationList) {
           String activityId = car.getActivityId();
            //创建市场活动与联系人的关联关系
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
           int count2 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count2!=1){
                flag=false;
            }
        }
        //如果有创建交易需求，创建一条交易
        if (t!=null){
            //把信息尽量完善
            t.setId(UUIDUtil.getUUID());
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setContactsId(contacts.getId());
            t.setCustomerId(customer.getId());
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setContactSummary(clue.getContactSummary());
            //添加交易
           int count = tranDao.save(t);
           if (count!=1){
               flag = false;
           }
           // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
           tranHistory.setCreateBy(createBy);
           tranHistory.setCreateTime(createTime);
           tranHistory.setExpectedDate(t.getExpectedDate());
           tranHistory.setId(UUIDUtil.getUUID());
           tranHistory.setMoney(t.getMoney());
           tranHistory.setStage(t.getStage());
           tranHistory.setTranId(t.getId());
          int count2 = tranHistoryDao.save(tranHistory);
          if (count2 != 1){
              flag = false;
          }

        }
        //删除线索备注
       int count = clueRemarkDao.delete(clueId);
        if (count!=clueRemarks.size()){
            flag  =false;
        }
        //删除线索和市场活动的关系
        for (ClueActivityRelation car:clueActivityRelationList) {
            int count2 = clueActivityRelationDao.delete(car);
            if (count2 != 1) {
                flag = false;
            }
        }
        int count3 =  clueDao.delete(clueId);
        if (count3!=1){
            flag = false;
        }
        return flag;
    }

}

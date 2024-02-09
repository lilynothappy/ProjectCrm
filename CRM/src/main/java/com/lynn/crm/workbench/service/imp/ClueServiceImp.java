package com.lynn.crm.workbench.service.imp;

import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.SqlSessionUtil;
import com.lynn.crm.utils.UUIDUtil;
import com.lynn.crm.vo.CluePageVo;
import com.lynn.crm.workbench.dao.*;
import com.lynn.crm.workbench.domain.*;
import com.lynn.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImp implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    public boolean save(Clue clue) {
        boolean flag=true;

        int count=clueDao.save(clue);

        if(count!=1){
            flag=false;
        }
        return flag;
    }

    public CluePageVo<Clue> pageList(Map<String, Object> map) {
        int total = clueDao.getTotalByCondition(map);
        List<Clue> list = clueDao.getClueListByCondition(map);

        CluePageVo<Clue> vo = new CluePageVo<Clue>();
        vo.setTotal(total);
        vo.setList(list);

        return vo;
    }

    public boolean bund(String cid, String[] aids) {
        boolean flag = true;

        for(String aid : aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);

            int count = clueActivityRelationDao.bund(car);
            if(count!=1){
                flag=false;
            }
        }
        return flag;

        /*boolean flag = true;

        for(String aid:aids){

            //取得每一个aid和cid做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            //添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if(count!=1){
                flag = false;
            }

        }

        return flag;*/
    }

    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);

        return flag;
    }

    public boolean convert(String clueId, Tran t, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getbyId(clueId);

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getByName(company);
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(company);
            customer.setAddress(clue.getAddress());
            customer.setOwner(clue.getOwner());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setDescription(clue.getDescription());
            customer.setNextContactTime(clue.getNextContactTime());

            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(clue.getFullname());
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());

        int count2 = contactsDao.save(contacts);
        if(count2 != 1){
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkslist = clueRemarkDao.getListByClueId(clueId);

        //4.1取出每一条备注信息
        for(ClueRemark clueRemark : clueRemarkslist){
            String noteContent = clueRemark.getNoteContent();

            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");

            int count3 = customerRemarkDao.save(customerRemark);
            if(count3 != 1){
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");

            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4 != 1){
                flag = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationsList = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationsList){
            String activityId =clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5 != 1){
                flag = false;
            }
        }


        //(6) 如果有创建交易需求，创建一条交易
        if(t != null){
            //控制器中封装好的信息：id, name, money, stage, expectedDate, activityId, createTime, createBy
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactsId(contacts.getId());
            t.setContactSummary(clue.getContactSummary());

            int count6 = tranDao.save(t);
            if(count6 != 1){
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());

            int count7 = tranHistoryDao.save(tranHistory);
            if(count7 != 1){
                flag = false;
            }
        }

        //(8) 删除线索备注
        for(ClueRemark clueRemark : clueRemarkslist){
            String id = clueRemark.getId();
            int count8 = clueRemarkDao.deleteById(id);
            if(count8 != 1){
                flag = false;
            }
        }

        //(9) 删除线索和市场活动的关系
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationsList){
            String id = clueActivityRelation.getId();
            int count9 = clueActivityRelationDao.deleteById(id);
            if(count9 != 1){
                flag = false;
            }
        }

        //(10) 删除线索
        int count10 = clueDao.delete(clueId);
        if(count10 != 1){
            flag = false;
        }

        return flag;
    }

}

package com.lynn.crm.workbench.service.imp;

import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.SqlSessionUtil;
import com.lynn.crm.utils.UUIDUtil;
import com.lynn.crm.workbench.dao.CustomerDao;
import com.lynn.crm.workbench.dao.TranDao;
import com.lynn.crm.workbench.dao.TranHistoryDao;
import com.lynn.crm.workbench.domain.Customer;
import com.lynn.crm.workbench.domain.Tran;
import com.lynn.crm.workbench.domain.TranHistory;
import com.lynn.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImp implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public Tran getById(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(tranId);

        return tranHistoryList;
    }

    public Map<String, Object> getCharts() {
        int total = tranDao.getTotal();

        List<Map<String,Object>> dataList = tranDao.getCharts();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }

    public boolean changeStage(Tran t) {
        boolean flag = true;

        TranHistory history = new TranHistory();
        history.setId(UUIDUtil.getUUID());
        history.setTranId(t.getId());
        history.setStage(t.getStage());
        history.setCreateTime(DateTimeUtil.getSysTime());
        history.setCreateBy(t.getEditBy());
        history.setExpectedDate(t.getExpectedDate());
        history.setMoney(t.getMoney());

        int count1 = tranHistoryDao.save(history);
        if (count1 != 1) {
            flag = false;
        }

        int count2 = tranDao.update(t);
        if (count2 != 1) {
            flag = false;
        }

        return flag;
    }

    public boolean save(Tran t, String customerName) {

        /*
            交易添加业务：
                在做添加之前，参数t里面就少了一项信息，就是客户的主键，customerId
                先处理客户相关的需求

                （1）判断customerName，根据客户名称在客户表进行精确查询
                       如果有这个客户，则取出这个客户的id，封装到t对象中
                       如果没有这个客户，则再客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

                （2）经过以上操作后，t对象中的信息就全了，需要执行添加交易的操作

                （3）添加交易完毕后，需要创建一条交易历史
         */
        boolean flag =true;

        Customer customer = customerDao.getByName(customerName);
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setOwner(t.getOwner());
            customer.setContactSummary(t.getContactSummary());
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setDescription(t.getDescription());
            customer.setNextContactTime(t.getNextContactTime());

            int count1 = customerDao.save(customer);
            if(count1 != 1){
                flag = false;
            }

        }

        t.setCustomerId(customer.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if(count2 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory history = new TranHistory();
        history.setTranId(t.getId());
        history.setStage(t.getStage());
        history.setMoney(t.getMoney());
        history.setId(UUIDUtil.getUUID());
        history.setCreateBy(t.getCreateBy());
        history.setCreateTime(DateTimeUtil.getSysTime());
        history.setExpectedDate(t.getExpectedDate());

        int count3 = tranHistoryDao.save(history);
        if(count3 != 1){
            flag = false;
        }

        return flag;
    }
}

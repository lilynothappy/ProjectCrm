package com.lynn.crm.settings.service.imp;

import com.lynn.crm.exception.LoginException;
import com.lynn.crm.settings.dao.UserDao;
import com.lynn.crm.settings.domain.User;
import com.lynn.crm.settings.service.UserService;
import com.lynn.crm.utils.DateTimeUtil;
import com.lynn.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImp implements UserService {
    private UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map=new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user=userDao.login(map);

        if(user==null){
            throw new LoginException("账号密码错误");
        }

        String expireTime=user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }

        String lockState=user.getLockState();
        if("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        String allowIps=user.getAllowIps();

        //if(allowIps!=null&&allowIps!=""){}
        //以上表示ip地址不需要验证，只要不为空就可以访问

        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }

        return user;
    }


    public List<User> getUserList() {

        List<User> uList=userDao.getUserList();

        return uList;
    }
}

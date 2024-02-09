package com.lynn.crm.settings.service;

import com.lynn.crm.exception.LoginException;
import com.lynn.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}

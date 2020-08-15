package com.chen.crm.settings.service;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.exception.LoginException;

import java.util.List;

public interface UserService {
    User login(String name, String pass, String addr) throws LoginException;

    List<User> selectUserList();
}

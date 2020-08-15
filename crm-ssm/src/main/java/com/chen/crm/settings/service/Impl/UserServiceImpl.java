package com.chen.crm.settings.service.Impl;

import com.chen.crm.settings.dao.UserDao;
import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.exception.LoginException;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public User login(String name,String pass,String addr) throws LoginException {

        User user = userDao.selectLogin(name,pass);
        //判断是否为空
        if (user==null){
            throw new LoginException("用户名密码错误");
        }
        //判断账号过期
        String expireTime = user.getExpireTime();
        String currentTime= DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号已失效");
        }

        //判断ip地址
        String ip = user.getAllowIps();
        if (!ip.contains(addr)){
            throw new LoginException("ip地址受限");
        }


        return user;
    }

    @Override
    public List<User> selectUserList() {
        System.out.println("进入servivr实现类");
       List<User> userList = userDao.selectUserList();
        System.out.println("实现类无异常");
        return userList;
    }
}

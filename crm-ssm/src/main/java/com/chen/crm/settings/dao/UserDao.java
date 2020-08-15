package com.chen.crm.settings.dao;

import com.chen.crm.settings.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    List<User> selectUser();

    User selectLogin(@Param("name") String name,@Param("pass") String pass);

    List<User> selectUserList();
}

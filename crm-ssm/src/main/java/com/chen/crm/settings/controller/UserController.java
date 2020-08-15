package com.chen.crm.settings.controller;

import com.chen.crm.settings.domain.User;
import com.chen.crm.settings.service.UserService;
import com.chen.crm.utils.MD5Util;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/login.do")
    public @ResponseBody Object userLogin(@Param("name") String name,@Param("pass") String pass, HttpServletRequest request){
       Map<String,Object> map = new HashMap<String, Object>();
        pass = MD5Util.getMD5(pass);
       String addr = request.getRemoteAddr();
       try {
        User user = userService.login(name,pass,addr);
        map.put("success",true);
        request.getSession().setAttribute("user",user);
       return map;
     }catch (Exception e){
        e.printStackTrace();
        map.put("success",false);
        map.put("msg",e.getMessage());
        return map;
       }
    }
}

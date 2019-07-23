package com.dubboxdemo.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dubboxdemo.service.UserService;

/**
 * @Author: WK
 * @Data: 2019/7/21 0:44
 * @Description: com.dubboxdemo.service.impl
 */
@Service
public class UserServiceImpl implements UserService{

    @Override
    public String getName() {
        return "bigData";
    }
}

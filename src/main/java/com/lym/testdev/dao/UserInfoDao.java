package com.lym.testdev.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.lym.testdev.model.UserInfo;
import com.lym.testdev.util.MyMapper;

@DS("mysql")
public interface UserInfoDao extends MyMapper<UserInfo> {

}
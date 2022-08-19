package com.oms.testdev.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.oms.testdev.model.UserInfo;
import com.oms.testdev.util.MyMapper;

@DS("mysql")
public interface UserInfoDao extends MyMapper<UserInfo> {

}
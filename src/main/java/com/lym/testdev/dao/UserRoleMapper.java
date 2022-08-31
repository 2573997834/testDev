package com.lym.testdev.dao;

import com.lym.testdev.common.model.UserRole;
import com.lym.testdev.common.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {

    List<String> selectRoleIds(@Param("userid") String userid);

}
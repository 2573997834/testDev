package com.lym.testdev.dao;

import com.lym.testdev.model.UserRole;
import com.lym.testdev.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {

    List<String> selectRoleIds(@Param("userid") String userid);

}
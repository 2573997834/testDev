package com.oms.testdev.dao;

import com.oms.testdev.model.UserRole;
import com.oms.testdev.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper extends MyMapper<UserRole> {

    List<String> selectRoleIds(@Param("userid") String userid);

}
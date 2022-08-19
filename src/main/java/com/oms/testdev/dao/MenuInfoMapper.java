package com.oms.testdev.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.oms.testdev.model.MenuInfo;
import com.oms.testdev.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS("mysql")
public interface MenuInfoMapper extends MyMapper<MenuInfo> {

    List<MenuInfo> selectMenuByUserid(@Param("userid") String userid);

}
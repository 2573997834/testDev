package com.lym.testdev.dao;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.lym.testdev.common.model.MenuInfo;
import com.lym.testdev.common.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS("mysql")
public interface MenuInfoMapper extends MyMapper<MenuInfo> {

    List<MenuInfo> selectMenuByUserid(@Param("userid") String userid);

}
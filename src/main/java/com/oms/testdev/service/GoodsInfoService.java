package com.oms.testdev.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.oms.testdev.model.GoodsInfo;
import com.oms.testdev.util.PageUtil;

/**
 * service层
 *
 * @author itar
 * @email wuhandzy@gmail.com
 * @date 2022-08-18 02:51:20
 * @since jdk 1.8
 */
public interface GoodsInfoService {

    /*<AUTOGEN--BEGIN>*/

    PageUtil<GoodsInfo> selectPaged(PageUtil<GoodsInfo> pageInfo);

    GoodsInfo selectByPrimaryKey(String id);

    Integer deleteByPrimaryKey(String id);

    Integer insert(GoodsInfo goodsInfo);

    Integer updateByPrimaryKeySelective(GoodsInfo goodsInfo);


}

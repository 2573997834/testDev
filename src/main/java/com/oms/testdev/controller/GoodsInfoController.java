package com.oms.testdev.controller;

import com.oms.testdev.model.GoodsInfo;
import com.oms.testdev.service.GoodsInfoService;
import com.oms.testdev.util.PageUtil;
import com.oms.testdev.util.Result;
import oracle.ucp.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goodsinfo")
public class GoodsInfoController {

    @Autowired
    private GoodsInfoService goodsInfoService;

    /**
     * 分页查询数据
     */
    @PostMapping("/queryPage")
    public PageUtil<GoodsInfo> selectPaged(@RequestBody PageUtil<GoodsInfo> pageUtil) {
        PageUtil<GoodsInfo> page = goodsInfoService.selectPaged(pageUtil);
        return page;
    }

    /**
     * 通过id查询
     *
     * @return
     */
    @RequestMapping("/select_by_id")
    public Result<GoodsInfo> selectByPrimaryKey(String id) {
        Result<GoodsInfo> result = new Result<>();
        GoodsInfo po = goodsInfoService.selectByPrimaryKey(id);
        result.setData(po);
        return result;
    }

    /**
     * 通过ID删除
     *
     * @return
     */
    @RequestMapping("/delete_by_id")
    public Result<Integer> deleteByPrimaryKey(String id) {
        Result<Integer> result = new Result<>();
        Integer num = goodsInfoService.deleteByPrimaryKey(id);
        result.setData(num);
        return result;
    }

    /**
     * 新增数据
     *
     * @return
     */
    @RequestMapping("/save_goodsInfo")
    public Result<Integer> insert(GoodsInfo goodsInfo) {
        Result<Integer> result = new Result<>();
        Integer num = goodsInfoService.insert(goodsInfo);
        result.setData(num);
        return result;
    }

    /**
     * 修改数据
     *
     * @return
     */
    @RequestMapping("/update_goodsInfo")
    public Result<Integer> updateByPrimaryKeySelective(GoodsInfo goodsInfo) {
        Result<Integer> result = new Result<>();
        Integer num = goodsInfoService.updateByPrimaryKeySelective(goodsInfo);
        result.setData(num);
        return result;
    }

}

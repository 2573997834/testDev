package com.lym.testdev.config;

import com.lym.testdev.dao.MenuInfoMapper;
import com.lym.testdev.model.MenuInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MenuLoad implements CommandLineRunner {

    public static List<String> menuList = new ArrayList<>();

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("正在加载菜单路径...");
        List<MenuInfo> menuInfoList = menuInfoMapper.selectAll();
        List<String> list = menuInfoList.stream().map(obj -> obj.getPath()).distinct().collect(Collectors.toList());
        menuList.addAll(list);
        log.info("菜单路径加载完成");
    }
}

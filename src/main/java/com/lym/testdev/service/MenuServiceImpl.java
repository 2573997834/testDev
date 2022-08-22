package com.lym.testdev.service;

import com.lym.testdev.dao.MenuInfoMapper;
import com.lym.testdev.model.MenuInfo;
import com.lym.testdev.model.MenuTree;
import com.lym.testdev.util.SessionUser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl {

    @Autowired
    private MenuInfoMapper menuInfoMapper;

    public List<MenuTree> getMenu() {
        String userid = SessionUser.getCurrentUser().getUserid();
        List<MenuInfo> menuInfos = menuInfoMapper.selectMenuByUserid(userid);
        if (CollectionUtils.isNotEmpty(menuInfos)) {
            List<MenuTree> list = new ArrayList<>();
            for (MenuInfo menuInfo : menuInfos) {
                MenuTree tree = new MenuTree();
                tree.setKey(menuInfo.getMenuid());
                tree.setParentid(menuInfo.getParentid());
                tree.setPath(menuInfo.getPath());
                tree.setTitle(menuInfo.getName());
                tree.setIcon(menuInfo.getIcon());
                list.add(tree);
            }
            MenuTree tree = new MenuTree();
            tree.setKey("0");
            buildTree(tree,list);
            List<MenuTree> children = tree.getChildren();
            return children;
        }
        return null;
    }

    public static MenuTree buildTree(MenuTree rootNode, List<MenuTree> list) {
        List<MenuTree> chilMenus = new ArrayList<>();
        for (MenuTree menuNode : list) {
            if (StringUtils.isEmpty(menuNode.getPath())) {
                menuNode.setPath(menuNode.getKey());
            }
            if (menuNode.getParentid().equals(rootNode.getKey())) {
                chilMenus.add(buildTree(menuNode, list));
            }
        }
        rootNode.setChildren(chilMenus);
        return rootNode;
    }

}

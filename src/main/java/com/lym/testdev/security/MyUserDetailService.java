package com.lym.testdev.security;

import com.lym.testdev.dao.MenuInfoMapper;
import com.lym.testdev.dao.UserInfoDao;
import com.lym.testdev.model.MenuInfo;
import com.lym.testdev.model.UserDetail;
import com.lym.testdev.model.UserInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/11 15:37
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private MenuInfoMapper menuInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetail userDetail = new UserDetail();
        UserInfo selUser = new UserInfo();
        selUser.setUsername(username);
        List<UserInfo> userInfoList = userInfoDao.select(selUser);
        if (CollectionUtils.isNotEmpty(userInfoList) && userInfoList.size() == 1) {
            UserInfo userInfo = userInfoList.get(0);
            userDetail.setUser(userInfo);
            if (userInfo != null) {
                try {
                    List<String> paths = new ArrayList<>();
                    List<MenuInfo> menuInfoList = menuInfoMapper.selectMenuByUserid(userInfo.getUserid());
                    if (CollectionUtils.isNotEmpty(menuInfoList)) {
                        for (MenuInfo menuInfo : menuInfoList) {
                            String path = menuInfo.getPath();
                            if (StringUtils.isNotEmpty(path)) {
                                paths.add(path);
                            }
                        }
                    }
                    userDetail.setPaths(paths);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new UsernameNotFoundException("用户名不存在！");
            }
        } else {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        return userDetail;
    }
}

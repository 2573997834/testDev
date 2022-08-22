package com.lym.testdev.controller;

import com.lym.testdev.model.MenuTree;
import com.lym.testdev.model.UserInfo;
import com.lym.testdev.service.MenuServiceImpl;
import com.lym.testdev.service.UserServiceImpl;
import com.lym.testdev.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: lym
 * @Description:
 * @Date: 2022/8/9 13:44
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MenuServiceImpl menuService;

    @PostMapping("/doLogin")
    @ResponseBody
    public Result<Map<String, String>> doLogin(HttpServletResponse response, @RequestBody UserInfo userInfo) {
        Result<Map<String, String>> login = userService.login(userInfo);
        //CookieUtil.addCookie("token", login.getData().get("token"), 1800, response);
        return login;
    }

    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestBody UserInfo userInfo) {
        Result reg = userService.register(userInfo);
        return reg;
    }

    @GetMapping("/userNameCheck")
    @ResponseBody
    public Result userNameCheck(String username) {
        Result res = userService.userNameCheck(username);
        return res;
    }

    @GetMapping("/getMenu")
    @ResponseBody
    public Result<List<MenuTree>> getMenu() {
        List<MenuTree> menu = menuService.getMenu();
        Result<List<MenuTree>> result = new Result<>(0, "查询成功", menu);
        return result;
    }

}

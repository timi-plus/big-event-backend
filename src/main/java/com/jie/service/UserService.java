package com.jie.service;

import com.jie.pojo.User;

public interface UserService {
    //根据用户名查询 用户
    User findByUserName(String username);
    // 注册
    void register(String username, String password);

    // 更新
    void update(User user);

    //更新用户头像
    void updateAvatar(String avatar);

    void updatePwd(String newPwd);
}

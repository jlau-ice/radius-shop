package com.ice.service;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.exception.BusinessException;
import com.ice.entity.Admin;
import com.ice.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
    @Value("${app.mock.admin-login:true}")
    private boolean mockAdminLogin;

    public String login(String username, String password) {
        if (mockAdminLogin) {
            cn.dev33.satoken.stp.StpUtil.login(1L);
            return cn.dev33.satoken.stp.StpUtil.getTokenValue();
        }
        Admin admin = lambdaQuery().eq(Admin::getUsername, username).eq(Admin::getStatus, 1).one();
        if (admin == null || !BCrypt.checkpw(password, admin.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        cn.dev33.satoken.stp.StpUtil.login(admin.getId());
        return cn.dev33.satoken.stp.StpUtil.getTokenValue();
    }
}

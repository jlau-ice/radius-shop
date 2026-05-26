package com.ice.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.exception.BusinessException;
import com.ice.entity.User;
import com.ice.mapper.UserMapper;
import com.ice.utils.WxMiniService;
import com.ice.vo.LoginVO;
import com.ice.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong;
import static cn.dev33.satoken.stp.StpUtil.login;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {
    private final WxMiniService wxMiniService;

    public LoginVO wxLogin(String code, String nickname, String avatarUrl) {
        JSONObject session = wxMiniService.code2Session(code);
        String openId = session.getStr("openid");

        User user = lambdaQuery().eq(User::getOpenId, openId).one();
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setUnionId(session.getStr("unionid"));
            user.setNickname(nickname != null ? nickname : "用户" + RandomUtil.randomNumbers(5));
            user.setAvatarUrl(avatarUrl);
            save(user);
        }

        login(user.getId());

        return LoginVO.builder()
                .token(cn.dev33.satoken.stp.StpUtil.getTokenValue())
                .user(toVO(user))
                .build();
    }

    public UserVO toVO(User u) {
        return UserVO.builder()
                .id(u.getId()).nickname(u.getNickname()).avatarUrl(u.getAvatarUrl()).phone(u.getPhone())
                .build();
    }

    public Long currentUserId() {
        return getLoginIdAsLong();
    }

    public User currentUser() {
        return getById(currentUserId());
    }
}

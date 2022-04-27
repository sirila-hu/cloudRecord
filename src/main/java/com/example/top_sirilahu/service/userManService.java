package com.example.top_sirilahu.service;

import com.example.top_sirilahu.VO.passwordVO;
import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class userManService
{
    userRepository userRepo;

    public userManService() {
    }

    @Autowired
    public userManService(userRepository userRepo) {
        this.userRepo = userRepo;
    }

    //修改密码
    @Transactional
    public void changePass(userEntity user, passwordVO password) throws Exception
    {
        if (password.isNull())
        {
            throw new Exception("不可提交空数据");
        }

        if (!password.isPassSame())
        {
            throw new Exception("两次密码输入不一致");
        }
        //获取原密码
        String origin_password = userRepo.findPasswordByUID(user.getUID());

        if (!origin_password.equals(password.getOrigin_password()))
        {
            throw new Exception("原始密码不正确");
        }

        userRepo.changePassword(password.getNew_password(), user.getUID());
    }
}

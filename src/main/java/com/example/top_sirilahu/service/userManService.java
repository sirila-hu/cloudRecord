package com.example.top_sirilahu.service;

import com.example.top_sirilahu.entity.userEntity;
import com.example.top_sirilahu.repository.userRepository;
import com.example.top_sirilahu.requestModel.PassChangeModel;
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

    @Transactional
    //修改密码
    public void changePass(userEntity user, PassChangeModel passChangeModel) throws Exception
    {
        if (passChangeModel.isNull())
        {
            throw new Exception("不可提交空数据");
        }

        if (!passChangeModel.isPassSame())
        {
            throw new Exception("两次密码输入不一致");
        }

        if (!user.getPassword().equals(passChangeModel.getOrigin_password()))
        {
            throw new Exception("原始密码不正确");
        }

        userRepo.editUnit(passChangeModel.getNew_password(), user.getUID());
    }
}

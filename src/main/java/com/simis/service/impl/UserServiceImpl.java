package com.simis.service.impl;

import com.simis.dao.UserDao;
import com.simis.model.UserModel;
import com.simis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 一拳超人
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserModel findByUserName(String userName) {
        return userDao.queryByUserName(userName);
    }

    @Override
    public boolean validateUser(String userName, String passWord) {
        return userDao.validateUser(userName, passWord);
    }

    @Override
    public void update(UserModel userModel) {
        userDao.updateObject(userModel);
    }
}



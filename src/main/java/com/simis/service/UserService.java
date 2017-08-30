package com.simis.service;

import com.simis.model.UserModel;

/**
 * Created by 一拳超人
 */
public interface UserService {

    /**
     * @Title: findByUserName
     * @Description: 查询用户
     * @param userName
     * @return UserModel
     * @throws
     */
    UserModel findByUserName(String userName);

    /**
     * @Title: getCustomer
     * @Description: 验证登录
     * @param userName
     * @return boolean
     * @throws
     */
    boolean validateUser(String userName,String passWord);

    /**
     * @Title: update
     * @Description: 更新对象
     * @param userModel
     * @return void
     * @throws
     */
    void update(UserModel userModel);
}

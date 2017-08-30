package com.simis.service;

import com.simis.model.CustomerModel;

/**
 * Created by 一拳超人 on 17/3/29.
 */
public interface CustomerService {

    /**
     * @Title: executeRegister
     * @Description: 用户注册
     * @param customerModel
     * @return void
     * @throws
     */
    void executeRegister(CustomerModel customerModel);


    /**
     * @Title: getCustomer
     * @Description: 查询用户
     * @param userName
     * @return void
     * @throws
     */
    CustomerModel getCustomer(String userName);

    /**
     * @Title: getCustomer
     * @Description: 验证登录
     * @param userName
     * @return boolean
     * @throws
     */
    boolean validateCustomer(String userName,String passWord);

    /**
     * @Title: update
     * @Description: 更新对象
     * @param customerModel
     * @return void
     * @throws
     */
    void update(CustomerModel customerModel);

    /**
     * @Title: update
     * @Description: 删除examTime的数据
     * @param examTime
     * @return void
     * @throws
     */
    void deleteByExamTime(String examTime);
}

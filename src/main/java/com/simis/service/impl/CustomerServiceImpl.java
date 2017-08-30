package com.simis.service.impl;

import com.simis.common.Constants;
import com.simis.dao.CustomerDao;
import com.simis.dao.ExamBatchDao;
import com.simis.model.CustomerModel;
import com.simis.model.ExamBatchModel;
import com.simis.service.CustomerService;
import com.simis.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by 一拳超人 on 17/3/29.
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ExamBatchDao examBatchDao;

    @Override
    public void executeRegister(CustomerModel customerModel) {
        CustomerModel oldModel = getCustomer(customerModel.getCardNo());
        if(oldModel!=null){
            LOGGER.info("身份证号为{}的已存在注册信息,不需再注册!",customerModel.getCardNo());
            return;
        }
        customerModel.setUserName(customerModel.getCardNo());//用户名就是身份证号
        customerModel.setPassWord(MD5Util.MD5Encode(customerModel.getPassWord(),null));
        customerModel.setCreator(customerModel.getName());
        customerModel.setOperator(customerModel.getName());
        customerModel.setIsAlreadyPaid(Constants.NOT_PAID);
        String examTime = customerModel.getExamTime();
        ExamBatchModel examBatchModel = examBatchDao.getModelByExamTime(examTime);
        customerModel.setExamTime(customerModel.getExamTime());
        customerModel.setState("0");
        customerModel.setCreateTime(new Date());
        customerModel.setModifyTime(new Date());
        customerDao.saveObject(customerModel);
        LOGGER.info("保存客户注册信息,客户用户名为{}",customerModel.getCardNo());
        //已报名人数
        int registerPepNum = examBatchModel.getRegisterPepNum();
        LOGGER.info("查询考试时间为{}的已报名人数为{}",examTime,registerPepNum);
        //默认注册时把资源数加1,以防止交费中间有人占座
        examBatchModel.setRegisterPepNum(registerPepNum+1);
        Date now = new Date();
        examBatchModel.setModifyTime(now);
        examBatchModel.setOperator(Constants.OPERATOR);
        examBatchDao.updateObject(examBatchModel);
        LOGGER.info("更新考试时间model,更新时间为{},更新的注册报名人数为{}",now,registerPepNum+1);
    }

    @Override
    public CustomerModel getCustomer(String userName) {
        return customerDao.queryByUserName(userName);
    }

    @Override
    public boolean validateCustomer(String userName, String passWord) {
        return customerDao.validateCustomer(userName, passWord);
    }

    @Override
    public void update(CustomerModel customerModel) {
        customerDao.updateObject(customerModel);
    }


    @Override
    public void deleteByExamTime(String examTime) {
        customerDao.deleteByExamTime(examTime);
    }
}

package com.simis.controller;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simis.common.Constants;
import com.simis.model.DictionaryModel;
import com.simis.model.ExamBatchModel;
import com.simis.model.UserModel;
import com.simis.service.CustomerService;
import com.simis.service.DictionaryService;
import com.simis.service.ExamBatchService;
import com.simis.service.UserService;
import com.simis.util.DateTimeUtil;
import com.simis.vo.DictionaryVo;
import com.simis.vo.ExamBatchParamVo;
import com.simis.vo.FeeInfoVo;
import com.simis.vo.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ArnoldLee on 17/4/8.
 */
@Controller
@RequestMapping(value = "/manage")
public class ManageController {

    /**
     * 控制台输出用的log
     */
    protected static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ManageController.class);


    private Map<String,String> sessionMap = Maps.newHashMap();

    @Autowired
    private UserService userService;

    @Autowired
    private ExamBatchService examBatchService;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private CustomerService customerService;


    @RequestMapping(value = "/customer/toregister")
    public String toRegister(){
        return "register";
    }

    @RequestMapping(value = "/onlinepay/topay")
    public String toPay(){
        return "/pay/pay";
    }

    @RequestMapping(value = "/tomanage")
    public String toManage(){
        return "manage";
    }

    @RequestMapping(value = "/toadmin")
    public String toAdmin(){
        return "admin";
    }


    @ResponseBody
    @RequestMapping("/login")
    public ResultMsg Login(String userName,String passWord,HttpSession httpSession){
        ResultMsg resultMsg = new ResultMsg();
        if(!DateTimeUtil.validateIsEnd(resultMsg,dictionaryService.queryByKey(Constants.END_DAYS_SYMBOL).getValue())){
            return resultMsg;
        }
        UserModel userModel = userService.findByUserName(userName);
        if(userModel != null){
            boolean result = userService.validateUser(userName, passWord);
            if(result){
                sessionMap.put("managerId",userName);
                resultMsg.setMsg("登录成功");
                resultMsg.setSuccess(true);
                return resultMsg;
            }
        }
        resultMsg.setMsg("用户名/密码错误,登录失败");
        resultMsg.setSuccess(false);
        return resultMsg;
    }


    @ResponseBody
    @RequestMapping("/changeExamBatch")
    public ResultMsg changeExamBatch(String paramVos){
        ResultMsg resultMsg = new ResultMsg();
        String operator = sessionMap.get("managerId");
        try {
            Gson gson = new Gson();
            List<ExamBatchParamVo> list = gson.fromJson(paramVos, new TypeToken<List<ExamBatchParamVo>>() {}.getType());
            examBatchService.saveExamBatch(list,operator);
        } catch (Exception e) {
            LOGGER.error("保存考试时间异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("保存失败!");
            return resultMsg;
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("保存成功!");
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/getExamBatch")
    public List<ExamBatchModel> getExamBatch(int month){
        int year = DateTimeUtil.getNowYear();
        List<ExamBatchModel> results = examBatchService.getMonthTimeList(year,month);
        return results;
    }

    @ResponseBody
    @RequestMapping("/getAllExamBatch")
    public List<ExamBatchModel> getAllExamBatch(){
        List<ExamBatchModel> results = examBatchService.getAllList();
        return results;
    }


    @ResponseBody
    @RequestMapping("/changeVideoInfo")
    public ResultMsg changeVideoInfo(String videoAddress,String videoCode){
        ResultMsg resultMsg = new ResultMsg();
        String operator = sessionMap.get("managerId");
        try {
            DictionaryModel videoAdressDModel = dictionaryService.queryByKey(Constants.VIDEO_ADDRESS_KEY);
            videoAdressDModel.setValue(videoAddress);
            videoAdressDModel.setModifyTime(new Date());
            videoAdressDModel.setOperator(operator);
            dictionaryService.update(videoAdressDModel);

            DictionaryModel videoCodeDModel = dictionaryService.queryByKey(Constants.VIDEO_CODE_KEY);
            videoCodeDModel.setValue(videoCode);
            videoCodeDModel.setModifyTime(new Date());
            videoCodeDModel.setOperator(operator);
            dictionaryService.update(videoCodeDModel);
        } catch (Exception e) {
            LOGGER.error("保存视频信息异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("保存失败!");
            return resultMsg;
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("保存成功!");
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/showVideoInfo")
    public DictionaryVo showVideoInfo(){
        DictionaryVo dictionaryVo = new DictionaryVo();
        try {
            DictionaryModel videoAdressDModel = dictionaryService.queryByKey(Constants.VIDEO_ADDRESS_KEY);
            DictionaryModel videoCodeDModel = dictionaryService.queryByKey(Constants.VIDEO_CODE_KEY);
            if(videoAdressDModel!=null && !StringUtils.isEmpty(videoAdressDModel.getValue())){
                dictionaryVo.setVideoAddress(videoAdressDModel.getValue());
            }
            if(videoCodeDModel!=null && !StringUtils.isEmpty(videoCodeDModel.getValue())){
                dictionaryVo.setVideoCode(videoCodeDModel.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("查询视频信息异常",e);
        }
        return dictionaryVo;
    }


    @ResponseBody
    @RequestMapping("/changeFeeInfo")
    public ResultMsg changeFeeInfo(String examFee,String bookFee,String videoFee,String mailFee){
        ResultMsg resultMsg = new ResultMsg();
        String operator = sessionMap.get("managerId");
        try {
            DictionaryModel examFeeDModel = dictionaryService.queryByKey(Constants.EXAM_FEE_KEY);
            examFeeDModel.setValue(examFee);
            examFeeDModel.setModifyTime(new Date());
            examFeeDModel.setOperator(operator);
            dictionaryService.update(examFeeDModel);

            DictionaryModel bookFeeDModel = dictionaryService.queryByKey(Constants.BOOK_FEE_KEY);
            bookFeeDModel.setValue(bookFee);
            bookFeeDModel.setModifyTime(new Date());
            bookFeeDModel.setOperator(operator);
            dictionaryService.update(bookFeeDModel);

            DictionaryModel videoFeeDModel = dictionaryService.queryByKey(Constants.VIDEO_FEE_KEY);
            videoFeeDModel.setValue(videoFee);
            videoFeeDModel.setModifyTime(new Date());
            videoFeeDModel.setOperator(operator);
            dictionaryService.update(videoFeeDModel);

            DictionaryModel mailFeeDModel = dictionaryService.queryByKey(Constants.MAIL_FEE_KEY);
            mailFeeDModel.setValue(mailFee);
            mailFeeDModel.setModifyTime(new Date());
            mailFeeDModel.setOperator(operator);
            dictionaryService.update(mailFeeDModel);
        } catch (Exception e) {
            LOGGER.error("保存费用信息异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("保存失败!");
            return resultMsg;
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("保存成功!");
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/showFeeInfo")
    public FeeInfoVo showFeeInfo(){
        FeeInfoVo dictionaryVo = new FeeInfoVo();
        try {
            DictionaryModel examFeeDModel = dictionaryService.queryByKey(Constants.EXAM_FEE_KEY);
            DictionaryModel bookFeeDModel = dictionaryService.queryByKey(Constants.BOOK_FEE_KEY);
            DictionaryModel videoFeeDModel = dictionaryService.queryByKey(Constants.VIDEO_FEE_KEY);
            DictionaryModel mailFeeDModel = dictionaryService.queryByKey(Constants.MAIL_FEE_KEY);


            if(examFeeDModel!=null && !StringUtils.isEmpty(examFeeDModel.getValue())){
                dictionaryVo.setExamFee(examFeeDModel.getValue());
            }
            if(bookFeeDModel!=null && !StringUtils.isEmpty(bookFeeDModel.getValue())){
                dictionaryVo.setBookFee(bookFeeDModel.getValue());
            }
            if(videoFeeDModel!=null && !StringUtils.isEmpty(videoFeeDModel.getValue())){
                dictionaryVo.setVideoFee(videoFeeDModel.getValue());
            }
            if(mailFeeDModel!=null && !StringUtils.isEmpty(mailFeeDModel.getValue())){
                dictionaryVo.setMailFee(mailFeeDModel.getValue());
            }
        } catch (Exception e) {
            LOGGER.error("查询费用信息异常",e);
        }
        return dictionaryVo;
    }

    @ResponseBody
    @RequestMapping("/deleteData")
    public ResultMsg deleteData(String examTime){
        ResultMsg resultMsg = new ResultMsg();
        try {
            customerService.deleteByExamTime(examTime);
        } catch (Exception e) {
            LOGGER.error("删除数据异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("删除失败!");
            return resultMsg;
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("删除成功!");
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/changePassword")
    public ResultMsg changePassword(String userName,String password){
        ResultMsg resultMsg = new ResultMsg();
        try {
            UserModel userModel = userService.findByUserName(userName);
            if(userModel == null){
                resultMsg.setSuccess(false);
                resultMsg.setMsg("无此用户!");
                return resultMsg;
            }
            userModel.setPassWord(password);
            userModel.setModifyTime(new Date());
            userService.update(userModel);
        } catch (Exception e) {
            LOGGER.error("修改密码异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("修改密码失败!");
            return resultMsg;
        }
        resultMsg.setSuccess(true);
        resultMsg.setMsg("修改密码成功!");
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/getEndDay")
    public ResultMsg getEndDay(){
        ResultMsg resultMsg = new ResultMsg();
        try {
            DictionaryModel dictionaryModel = dictionaryService.queryByKey(Constants.END_DAYS_SYMBOL);
            if(dictionaryModel == null){
                resultMsg.setSuccess(true);
                resultMsg.setMsg("");
                return resultMsg;
            }
            resultMsg.setSuccess(true);
            resultMsg.setMsg(dictionaryModel.getValue());
        } catch (Exception e) {
            LOGGER.error("显示异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("显示异常!");
            return resultMsg;
        }
        return resultMsg;
    }

    @ResponseBody
    @RequestMapping("/updateEndDay")
    public ResultMsg updateEndDay(String endDay){
        ResultMsg resultMsg = new ResultMsg();
        try {
            DictionaryModel dictionaryModel = dictionaryService.queryByKey(Constants.END_DAYS_SYMBOL);
            if(dictionaryModel == null){
                resultMsg.setSuccess(true);
                resultMsg.setMsg("");
                return resultMsg;
            }
            dictionaryModel.setValue(endDay);
            dictionaryService.update(dictionaryModel);
            resultMsg.setSuccess(true);
            resultMsg.setMsg("更新成功");
        } catch (Exception e) {
            LOGGER.error("更新异常",e);
            resultMsg.setSuccess(false);
            resultMsg.setMsg("更新异常!");
            return resultMsg;
        }
        return resultMsg;
    }


}

package com.simis.service.impl;

import com.simis.common.Constants;
import com.simis.dao.ExamBatchDao;
import com.simis.model.ExamBatchModel;
import com.simis.service.DictionaryService;
import com.simis.service.ExamBatchService;
import com.simis.util.DateTimeUtil;
import com.simis.vo.ExamBatchParamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ArnoldLee on 17/4/29.
 */
@Service("examBatchService")
public class ExamBatchServiceImpl implements ExamBatchService {

    @Autowired
    private ExamBatchDao examBatchDao;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<ExamBatchModel> getNowTimeList() {
        return examBatchDao.getNowTimeList();
    }

    @Override
    public List<ExamBatchModel> getAllList() {
        return examBatchDao.getAllList();
    }

    @Override
    public void saveExamBatch(List<ExamBatchParamVo> list,String operator) {
        int year = DateTimeUtil.getNowYear();
        List<ExamBatchModel> addList = new ArrayList<>();
        int month = list.get(0).getMonth();
        List<ExamBatchModel> oldList = checkIsHasRows(year,month);
        if(oldList == null){//新增数据
            for(ExamBatchParamVo paramVo : list){
                ExamBatchModel batchModel = editExamBatchModel(paramVo);
                addList.add(batchModel);
            }
            examBatchDao.batchSaveVO(addList);
        }
        else{//修改数据
            if(list.size() != oldList.size()){

                for(ExamBatchParamVo paramVo : list){
                    ExamBatchModel newBatchModel = editExamBatchModel(paramVo);
                    ExamBatchModel oldModel = examBatchDao.getModelByExamTime(paramVo.getExamTime());
                    if(oldModel!=null){
                        newBatchModel.setIsFull(oldModel.getIsFull());
                        newBatchModel.setPaidPepNum(oldModel.getPaidPepNum());
                        newBatchModel.setRegisterPepNum(oldModel.getRegisterPepNum());
                        newBatchModel.setExamCode(oldModel.getExamCode());
                    }
                    addList.add(newBatchModel);
                }
                for(ExamBatchModel batchModel : oldList){
                    //没有时间相等的,那么直接置为无效
                    batchModel.setSystemState(Constants.DATA_INVALID_STATUS);
                    batchModel.setModifyTime(new Date());
                    batchModel.setOperator(operator);
                }
                examBatchDao.batchSaveVO(addList);
                examBatchDao.batchUpdateVO(oldList);
            }
            else{
                for(ExamBatchModel batchModel : oldList){
                    for(ExamBatchParamVo paramVo : list){
                        if(paramVo.getExamType().equals(batchModel.getExamType())){
                            batchModel.setExamTime(paramVo.getExamTime());
                            batchModel.setExamPepNum(paramVo.getExamPepNum());
                            batchModel.setExamCode(paramVo.getExamCode());
                            batchModel.setModifyTime(new Date());
                            batchModel.setOperator(operator);
                        }
                    }
                }
                examBatchDao.batchUpdateVO(oldList);
            }
        }
    }

    //检验是否已经有记录了
    private List<ExamBatchModel> checkIsHasRows(int year,int month){
        List<ExamBatchModel> oldList = examBatchDao.getMonthTimeList(year,month);
        if(oldList!=null && oldList.size() > 0){
            return oldList;
        }
        return null;
    }

    //组装model
    private ExamBatchModel editExamBatchModel(ExamBatchParamVo paramVo){
        ExamBatchModel resultModel = new ExamBatchModel();
        resultModel.setYear(DateTimeUtil.getNowYear());
        resultModel.setMonth(paramVo.getMonth());
        resultModel.setExamTime(paramVo.getExamTime());//考试时间
        resultModel.setExamType(paramVo.getExamType());//考试类型
        resultModel.setIsFull(Constants.NO);//报名是否已满
        resultModel.setExamPepNum(paramVo.getExamPepNum());//预计总人数
        resultModel.setRegisterPepNum(0);//已注册人数
        resultModel.setPaidPepNum(0);//已缴费人数
        resultModel.setExamCode(paramVo.getExamCode());//考试验证码
        resultModel.setCreateTime(new Date());
        resultModel.setCreator(paramVo.getOperator());
        resultModel.setModifyTime(new Date());
        resultModel.setOperator(paramVo.getOperator());
        return resultModel;
    }


    @Override
    public List<ExamBatchModel> getMonthTimeList(int year, int month) {
        List<ExamBatchModel> results = examBatchDao.getMonthTimeList(year,month);
        return results;
    }

    @Override
    public void updatePaidPepNum(String examTime) {
        ExamBatchModel examBatchModel = examBatchDao.getModelByExamTime(examTime);
        Integer paidPepNum = examBatchModel.getPaidPepNum();//已经付款考试人数
        Integer examPepNum = examBatchModel.getExamPepNum();//理论考试人数
        if(examPepNum.compareTo(paidPepNum+1) == 0){
            examBatchModel.setIsFull(Constants.YES);//人数已经报满
        }
        examBatchModel.setPaidPepNum(paidPepNum+1);
        examBatchDao.updateObject(examBatchModel);
    }

    @Override
    public boolean getIsFull(String examTime) {
        ExamBatchModel examBatchModel = examBatchDao.getModelByExamTime(examTime);
        if(examBatchModel == null){
            return false;
        }
        if(Constants.YES.equals(examBatchModel.getIsFull())){
            return true;
        }
        return false;
    }
}

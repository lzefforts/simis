package com.simis.service;

import com.simis.model.ExamBatchModel;
import com.simis.vo.ExamBatchParamVo;

import java.util.List;

/**
 * 考试批次服务
 * Created by ArnoldLee on 17/4/29.
 */
public interface ExamBatchService {

    /**
     * @Title: getNowTimeList
     * @Description: 查询当前可以选择的批次时间
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getNowTimeList();

    /**
     * @Title: getAllList
     * @Description: 把所有的考试匹配表查询出来
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getAllList();

    /**
     * @Title: saveExamBatch
     * @Description: 设置考试批次时间
     * @param  list
     * @param operator
     */
    void saveExamBatch(List<ExamBatchParamVo> list,String operator);
    /**
     * @Title: getNowTimeList
     * @Description: 查询当前可以选择的批次时间
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getMonthTimeList(int year,int month);

    /**
     * @Title: updatePaidPepNum
     * @Description: 根据examTime更新paidPepNum
     * @return
     */
    void updatePaidPepNum(String examTime);

    /**
     * @Title: getIsFull
     * @Description: 根据examTime查询是否交费人数已经满了
     * @return
     */
    boolean getIsFull(String examTime);
}

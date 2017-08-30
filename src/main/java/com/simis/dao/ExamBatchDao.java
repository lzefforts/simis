package com.simis.dao;

import com.simis.base.BaseHibernateDao;
import com.simis.model.ExamBatchModel;

import java.util.List;

/**
 * Created by ArnoldLee on 17/4/29.
 */
public interface ExamBatchDao extends BaseHibernateDao {

    /**
     * @Title: getNowTimeList
     * @Description: 查询当前可以选择的批次时间
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getNowTimeList();

    /**
     * @Title: setExamBatch
     * @Description: 设置考试批次时间
     * @param  list
     */
    void setExamBatch(List<ExamBatchModel> list);

    /**
     * @Title: getList
     * @Description: 把所有的考试匹配表查询出来
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getAllList();

    /**
     * @Title: getNowTimeList
     * @Description: 查询当前可以选择的批次时间
     * @return List<ExamBatchModel>
     */
    List<ExamBatchModel> getMonthTimeList(int year,int month);

    /**
     * @Title: getNowTimeList
     * @Description: 查询当前可以选择的批次时间
     * @return List<ExamBatchModel>
     */
    ExamBatchModel getModelByExamTime(String examTime);
}

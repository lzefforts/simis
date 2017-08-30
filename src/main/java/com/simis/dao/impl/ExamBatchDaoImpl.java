package com.simis.dao.impl;

import com.simis.base.impl.BusiHibernateDaoImpl;
import com.simis.dao.ExamBatchDao;
import com.simis.model.ExamBatchModel;
import com.simis.util.DateTimeUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ArnoldLee on 17/4/29.
 */
@Repository("examBatchDao")
public class ExamBatchDaoImpl extends BusiHibernateDaoImpl implements ExamBatchDao {

    @Override
    public List<ExamBatchModel> getNowTimeList() {
        int year = DateTimeUtil.getNowYear();
//        int month = DateTimeUtil.getNowMonth();
        StringBuilder hql = new StringBuilder("");
        hql.append("from ExamBatchModel where year=:year ");
         hql.append(" and now()<=examTime and isFull <> '1' ");
        hql.append(" order by examTime asc ");
        Map<String,Object> params = new HashMap<>();
        params.put("year",year);
//        params.put("month",month);
        List result = this.query(hql.toString(),params);
        return result;
    }

    @Override
    public void setExamBatch(List<ExamBatchModel> list) {
        this.batchSaveOrUpdateVO(list);
    }

    @Override
    public List<ExamBatchModel> getAllList() {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ExamBatchModel where systemState=1 order by examTime ");
        List result = this.query(hql.toString(),null);
        return result;
    }

    @Override
    public List<ExamBatchModel> getMonthTimeList(int year, int month) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ExamBatchModel where year=:year and month=:month and systemState=1 order by examType ");
        Map<String,Object> params = new HashMap<>();
        params.put("year",year);
        params.put("month",month);
        List result = this.query(hql.toString(),params);
        return result;
    }

    @Override
    public ExamBatchModel getModelByExamTime(String examTime) {
        StringBuilder hql = new StringBuilder("");
        hql.append("from ExamBatchModel where examTime=:examTime and systemState=1");
        Map<String,Object> params = new HashMap<>();
        params.put("examTime",examTime);
        List result = this.query(hql.toString(),params);
        if(result!=null && result.size() > 0){
            return (ExamBatchModel)result.get(0);
        }
        return null;
    }
}

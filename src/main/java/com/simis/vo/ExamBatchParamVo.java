package com.simis.vo;

import java.io.Serializable;

/**
 * Created by ArnoldLee on 17/5/10.
 */
public class ExamBatchParamVo implements Serializable {
    private static final long serialVersionUID = 874387511118618202L;

    //月份
    private int month;
    //考试时间
    private String examTime;
    //考试人数
    private Integer examPepNum;
    //考试验证码
    private String examCode;
    //考试类型
    private String examType;
    //操作者
    private String operator;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getExamTime() {
        return examTime;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public Integer getExamPepNum() {
        return examPepNum;
    }

    public void setExamPepNum(Integer examPepNum) {
        this.examPepNum = examPepNum;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }
}

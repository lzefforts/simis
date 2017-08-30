package com.simis.model;

/**
 * Created by ArnoldLee on 17/4/29.
 */

import javax.persistence.*;
import java.util.Date;

/**
 *
 *  考试时间批次表
 *
 * */
@Entity
@Table(name = "sims_exam_batch")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ExamBatchModel extends BaseModel  {


    private static final long serialVersionUID = -7923021326394250197L;
    /**
     * pkid 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pkid")
    private Long pkid;

    /**
     * 年度
     */
    @Column(name="year")
    private int year;

    /**
     * 月份
     */
    @Column(name="month")
    private int month;

    /**
     * 考试类型,0为普通考试
     */
    @Column(name="exam_type")
    private String examType;

    /**
     * 考试时间
     */
    @Column(name="exam_time")
    private String examTime;

    /**
     * 考试计划报名总人数
     */
    @Column(name="exam_pep_num")
    private Integer examPepNum;


    /**
     * 考试报名注册人数
     */
    @Column(name="register_pep_num")
    private Integer registerPepNum;

    /**
     * 考试报名注册,且已交费人数
     */
    @Column(name="paid_pep_num")
    private Integer paidPepNum;

    /**
     * 考试人数是否已经报满,0否1是
     */
    @Column(name="is_full")
    private String isFull;


    /**
     * 考试编码
     */
    @Column(name="exam_code")
    private String examCode;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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

    public String getIsFull() {
        return isFull;
    }

    public void setIsFull(String isFull) {
        this.isFull = isFull;
    }

    public int getPaidPepNum() {
        return paidPepNum;
    }

    public void setPaidPepNum(Integer paidPepNum) {
        this.paidPepNum = paidPepNum;
    }

    public Integer getRegisterPepNum() {
        return registerPepNum;
    }

    public void setRegisterPepNum(Integer registerPepNum) {
        this.registerPepNum = registerPepNum;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }
}

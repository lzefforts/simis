package com.simis.model;

import javax.persistence.*;

/**
 *
 *  客户考试时间批次关系表
 *
 * */
@Entity
@Table(name = "sims_exam_batch_relation")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CustomerExamBatchRelationModel extends BaseModel {


    private static final long serialVersionUID = -1801167664132295524L;
    /**
     * pkid 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pkid")
    private Long pkid;

    /**
     * 客户表主键
     */
    @Column(name="customer_pkid")
    private Long customerPkid;

    /**
     * 考试批次表主键
     */
    @Column(name="exam_batch_pkid")
    private Long examBatchPkid;

    public Long getPkid() {
        return pkid;
    }

    public void setPkid(Long pkid) {
        this.pkid = pkid;
    }

    public Long getCustomerPkid() {
        return customerPkid;
    }

    public void setCustomerPkid(Long customerPkid) {
        this.customerPkid = customerPkid;
    }

    public Long getExamBatchPkid() {
        return examBatchPkid;
    }

    public void setExamBatchPkid(Long examBatchPkid) {
        this.examBatchPkid = examBatchPkid;
    }
}

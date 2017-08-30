package com.simis.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 3445047357574205753L;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
        name = "create_time",
        updatable = false
    )
    private Date createTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
        name = "modify_time"
    )
    private Date modifyTime;
    
    @Column(
        name = "creator",
        nullable = false,
        length = 128,
        updatable = false
    )
    private String creator;
    
    @Column(
        name = "operator",
        nullable = false,
        length = 128
    )
    private String operator;
    
    @Column(
        name = "state",
        length = 20
    )
    private String state;
    
//    @Version
//    private Long version = new Long(0L);
    
    @Column(
        name = "remark",
        length = 512
    )
    private String remark;
    
    @Column(
        name = "sys_state",
        length = 16
    )
    private String systemState = "1";

    public BaseModel() {
    }

    public String getCreator() {
        return this.creator;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    public Long getVersion() {
//        return this.version;
//    }
//
//    public void setVersion(Long version) {
//        this.version = version;
//    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSystemState() {
        return this.systemState;
    }

    public void setSystemState(String systemState) {
        this.systemState = systemState;
    }
}

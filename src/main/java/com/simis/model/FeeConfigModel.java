package com.simis.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 
 *  费用表
 * 
 * */
@Entity
@Table(name = "sims_fee_config")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class FeeConfigModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6310528261755207319L;

	/**
	 * pkid 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pkid")
	private Long pkid;


	/**
	 * 费用类型
	 */
	@Column(name="fee_type")
	private String feeType;

	/**
	 * 费用金额
	 */
	@Column(name="fee_amt")
	private BigDecimal feeAmt;


	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}


	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public BigDecimal getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(BigDecimal feeAmt) {
		this.feeAmt = feeAmt;
	}
}

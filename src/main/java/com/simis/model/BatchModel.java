package com.simis.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 
 *  批次表，
 * 
 * */
@Entity
@Table(name = "sims_batch")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BatchModel extends BaseModel {

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
	 * 批次号
	 */
	@Column(name="father_no")
	private String fatherNo;

	/**
	 * 客户表主键id
	 */
	@Column(name="customer_id")
	private Long customerId;

	/**
	 * 预交费表主键id
	 */
	@Column(name="pay_amt_id")
	private Long payAmtId;


	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getFatherNo() {
		return fatherNo;
	}

	public void setFatherNo(String fatherNo) {
		this.fatherNo = fatherNo;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getPayAmtId() {
		return payAmtId;
	}

	public void setPayAmtId(Long payAmtId) {
		this.payAmtId = payAmtId;
	}
}

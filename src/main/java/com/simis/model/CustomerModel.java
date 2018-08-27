package com.simis.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 
 * 	客户model
 * */

@Entity
@Table(name = "sims_customer")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CustomerModel  extends BaseModel {

	private static final long serialVersionUID = 3208010369551778136L;

	/**
	 * pkid 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pkid")
	private Long pkid;
	
	/**
	 * 客户姓名
	 */
	@Column(name="name")
	private String name;
	
	/**
	 * 性别
	 */
	@Column(name="sex")
	private String sex;

	/**
	 *手机号
	 */
	@Column(name="phone")
	private String phone;

	/**
	 * 邮件
	 */
	@Column(name="email")
	private String email;
	
	/**
	 * 证件类型,0:身份证,1:军人证,2:护照
	 */
	@Column(name="card_type")
	private String cardType = "0";
	
	/**
	 * 证件号码
	 */
	@Column(name="card_no")
	private String cardNo;
	
	/**
	 * 登录名，默认为证件号码
	 */
	@Column(name="username")
	private String userName;
	
	/**
	 * 登录密码
	 */
	@Column(name="password")
	private String passWord;
	
	/**
	 * 语言委员会注册时间
	 */
	@Column(name="clgov_register_date")
	private String clGovRegisterDate;
	
	/**
	 * 是否已经交费，0：否，1：是
	 */
	@Column(name="is_already_paid")
	private String isAlreadyPaid;

	/**
	 * 是否已经邮件通知，0：否，1：是
	 */
	@Column(name="is_already_email")
	private String isAlreadyEmail;
	
	/**
	 * 交费日期
	 */
	@Column(name="pay_date")
	private String payDate;


	/**
	 * 考试时间
	 */
	@Column(name="exam_time")
	private String examTime;


	/**
	 * 总费用
	 */
	@Column(name="total_fee")
	private BigDecimal totalFee;


	/**
	 * 考试费用
	 */
	@Column(name="exam_fee")
	private BigDecimal examFee;

	/**
	 * 书本费用
	 */
	@Column(name="book_fee")
	private BigDecimal bookFee;

	/**
	 * 视频费用
	 */
	@Column(name="video_fee")
	private BigDecimal videoFee;

	/**
	 * 支付订单号
	 */
	@Column(name="order_no")
	private String orderNo;

	/**
	 * 收件地址
	 */
	@Column(name="address")
	private String address;

	/**
	 * 邮寄费用
	 */
	@Column(name="mail_fee")
	private BigDecimal mailFee;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getClGovRegisterDate() {
		return clGovRegisterDate;
	}

	public void setClGovRegisterDate(String clGovRegisterDate) {
		this.clGovRegisterDate = clGovRegisterDate;
	}

	public String getIsAlreadyPaid() {
		return isAlreadyPaid;
	}

	public void setIsAlreadyPaid(String isAlreadyPaid) {
		this.isAlreadyPaid = isAlreadyPaid;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getExamTime() {
		return examTime;
	}

	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getExamFee() {
		return examFee;
	}

	public void setExamFee(BigDecimal examFee) {
		this.examFee = examFee;
	}

	public BigDecimal getBookFee() {
		return bookFee;
	}

	public void setBookFee(BigDecimal bookFee) {
		this.bookFee = bookFee;
	}

	public BigDecimal getVideoFee() {
		return videoFee;
	}

	public void setVideoFee(BigDecimal videoFee) {
		this.videoFee = videoFee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getMailFee() {
		return mailFee;
	}

	public void setMailFee(BigDecimal mailFee) {
		this.mailFee = mailFee;
	}

	public String getIsAlreadyEmail() {
		return isAlreadyEmail;
	}

	public void setIsAlreadyEmail(String isAlreadyEmail) {
		this.isAlreadyEmail = isAlreadyEmail;
	}
}

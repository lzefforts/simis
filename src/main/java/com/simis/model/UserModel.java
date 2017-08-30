package com.simis.model;

import javax.persistence.*;


/**
 * 
 *  用户表，用于后台管理的用户
 * 
 * */
@Entity
@Table(name = "sims_user")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UserModel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5680795728478067877L;

	/**
	 * pkid 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="pkid")
	private Long pkid;
	
	/**
	 * 登录用户名
	 */
	@Column(name="username")
	private String userName;
	
	/**
	 * 登录密码
	 */
	@Column(name="password")
	private String passWord;
	
	/**
	 * 权限
	 * */
	@Column(name="priority")
	private Long priority;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
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

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}
	
}

package com.simis.model;

import javax.persistence.*;

/**
 * 
 *  字典表
 * 
 * */
@Entity
@Table(name = "sims_dictionary")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class DictionaryModel extends BaseModel {

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
	 * 字典key
	 */
	@Column(name="dict_name")
	private String key;

	/**
	 * 字典value
	 */
	@Column(name="dict_value")
	private String value;


	/**
	 * 字典描述
	 */
	@Column(name="description")
	private String description;

	public Long getPkid() {
		return pkid;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

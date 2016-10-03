package com.isso.idm.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BaseDTO implements java.io.Serializable {

	private static final long serialVersionUID = -7200095849148417467L;

	protected static final String DATE_FORMAT = "yyyy-MM-dd";

	protected static final String TIME_FORMAT = "HH:mm:ss";

	protected static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	protected static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	/**
	 * 建立日期 db_column: CREATE_DATE
	 */
	@JsonFormat(pattern=DATE_TIME_FORMAT)
	private java.util.Date createDate;
	/**
	 * 修改日期 db_column: MODIFY_DATE
	 */
	@JsonFormat(pattern=DATE_TIME_FORMAT)
	private java.util.Date modifyDate;
	/**
	 * 建立人 db_column: CREATE_BY
	 */

	private java.lang.String createBy;
	/**
	 * 修改人 db_column: MODIFY_BY
	 */

	private java.lang.String modifyBy;

	/**
	 * @return the createDate
	 */
	public java.util.Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modifyDate
	 */
	public java.util.Date getModifyDate() {
		return modifyDate;
	}

	/**
	 * @param modifyDate
	 *            the modifyDate to set
	 */
	public void setModifyDate(java.util.Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	/**
	 * @return the createBy
	 */
	public java.lang.String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy
	 *            the createBy to set
	 */
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the modifyBy
	 */
	public java.lang.String getModifyBy() {
		return modifyBy;
	}

	/**
	 * @param modifyBy
	 *            the modifyBy to set
	 */
	public void setModifyBy(java.lang.String modifyBy) {
		this.modifyBy = modifyBy;
	}

}

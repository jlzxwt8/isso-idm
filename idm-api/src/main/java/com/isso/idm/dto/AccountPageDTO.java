
package com.isso.idm.dto;

import java.io.Serializable;
import java.util.List;

import com.isso.idm.base.dto.BasePageDTO;

public class AccountPageDTO extends BasePageDTO<AccountDTO> implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -8752968743888350277L;

	public AccountPageDTO(long totalElements, int pageSize, int pageIndex, List<AccountDTO> accountList) {
		super(totalElements, pageSize, pageIndex,accountList);
	}

}

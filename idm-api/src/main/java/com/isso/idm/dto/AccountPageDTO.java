
package com.isso.idm.dto;

import java.io.Serializable;
import java.util.List;

import com.isso.idm.base.dto.BasePageDTO;

public class AccountPageDTO extends BasePageDTO<AccountDTO> implements Serializable {


	public AccountPageDTO(long totalElements, int pageSize, int pageIndex, List<AccountDTO> systemList) {
		super(totalElements, pageSize, pageIndex,systemList);
	}

}

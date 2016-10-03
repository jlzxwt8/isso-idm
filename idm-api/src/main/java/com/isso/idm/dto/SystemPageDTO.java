
package com.isso.idm.dto;

import java.io.Serializable;
import java.util.List;

import com.isso.idm.base.dto.BasePageDTO;

public class SystemPageDTO extends BasePageDTO<SystemDTO> implements Serializable {

	private static final long serialVersionUID = -5526248723140373348L;

	public SystemPageDTO(long totalElements, int pageSize, int pageIndex, List<SystemDTO> systemList) {
		super(totalElements, pageSize, pageIndex,systemList);
	}

}

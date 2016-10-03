package com.isso.idm;

import java.util.List;

import com.isso.idm.dto.SystemDTO;
import com.isso.idm.dto.SystemPageDTO;


public interface ISystemService {

	/**
	 * 创建应用系统
	 * @Author : Wang Tong
	 * @param system
	 * @return
	 * @throws IdmServiceException
	 */
	public long createSystem(SystemDTO system) throws IdmServiceException;

	/**
	 * 更新应用系统
	 * @Author : Wang Tong
	 * @param system
	 * @throws IdmServiceException
	 */
	public void updateSystem(SystemDTO system) throws IdmServiceException;

	/**
	 * 删除应用系统
	 * @Author : Wang Tong
	 * @param system
	 * @throws IdmServiceException
	 */
	public void deleteSystem(SystemDTO system) throws IdmServiceException;

	/**
	 * 查询应用系统
	 * @Author : Wang Tong
	 * @param pageIndex
	 * @param pageSize
	 * @return 应用系统实体列表
	 * @throws IdmServiceException
	 */
	public SystemPageDTO findSystems(int pageIndex, int pageSize) throws IdmServiceException;

	/**
	 * 根据系统编码查找应用系统
	 * @Author : Wang Tong
	 * @param systemCode
	 * @return 应用系统实体
	 * @throws IdmServiceException
	 */
	public SystemDTO findBySystemCode(String systemCode) throws IdmServiceException;
	
	/**
	 * 根据系统编号查找应用系统
	 * @Author : Wang Tong
	 * @param systemId
	 * @return 应用系统实体
	 * @throws IdmServiceException
	 */
	public SystemDTO findBySystemId(Long systemId) throws IdmServiceException;
	
	/**
	 * 获得用户拥有应用系统权限
	 * @Author : Wang Tong
	 * @param accountCode
	 * @return
	 * @throws IdmServiceException
	 */
	public List<SystemDTO> findSystemByAccount(String accountCode) throws IdmServiceException;
}

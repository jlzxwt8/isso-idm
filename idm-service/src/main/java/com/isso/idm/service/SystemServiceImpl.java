
package com.isso.idm.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.isso.idm.ISystemService;
import com.isso.idm.IdmServiceException;
import com.isso.idm.constant.IdmServiceConstant;
import com.isso.idm.constant.IdmServiceErrorConstant;
import com.isso.idm.dto.SystemDTO;
import com.isso.idm.dto.SystemPageDTO;
import com.isso.idm.repository.ISystemRepository;
import com.isso.idm.domain.System;


@Service("SystemService")
@Transactional(readOnly = true)
public class SystemServiceImpl implements ISystemService {
	private final Logger logger = LoggerFactory.getLogger("ADMIN_OPERATION");
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ISystemRepository systemRepository;

	@Override
	@Transactional
	public long createSystem(SystemDTO systemDto) throws IdmServiceException {
		long systemId = 0;
		System system = null;
		try {
			// 检查是否有重复数据
			if (checkRepetition(systemDto.getSystemName(), systemDto.getSystemUrl())) {
				throw new IdmServiceException(IdmServiceErrorConstant.DATA_REPETITION, "应用系统以存在");
			}

			system = toDomain(systemDto);
			system.setCreateBy(systemDto.getCreateBy());
			system.setCreateDate(systemDto.getCreateDate());
			system.setModifyBy(systemDto.getModifyBy());
			system.setModifyDate(systemDto.getModifyDate());
			system = systemRepository.save(system);
			logger.info("新增应用系统 " + system.getSystemName());
			systemId = system.getSystemId();
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_CREATE, "应用系统创建失败", e);
		} finally {
			system = null;
		}
		return systemId;
	}

	@Override
	@Transactional
	public void updateSystem(SystemDTO systemDto) throws IdmServiceException {
		System system = null;
		try {
			system = systemRepository.findOne(systemDto.getSystemId());

			// 如果数据发生变化，检查是否有重复数据
			if (!system.getSystemName().equals(systemDto.getSystemName())
					|| !system.getSystemUrl().equals(systemDto.getSystemUrl())) {
				if (checkRepetition(systemDto.getSystemName(), systemDto.getSystemUrl())) {
					throw new IdmServiceException(IdmServiceErrorConstant.DATA_REPETITION, "应用系统以存在");
				}
			}

			// 更新数据
			system.setSystemName(systemDto.getSystemName());
			system.setSystemSecret(systemDto.getSystemSecret());
			system.setSystemUrl(systemDto.getSystemUrl());
			system.setModifyBy(systemDto.getModifyBy());
			system.setModifyDate(systemDto.getModifyDate());
			system = systemRepository.save(system);
			logger.info("更新应用系统 " + system.getSystemName());
		} catch (IllegalArgumentException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "应用系统不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_UPDATE, "应用系统更新失败", e);
		} finally {
			system = null;
		}
	}

	@Override
	@Transactional
	public void deleteSystem(SystemDTO systemDto) throws IdmServiceException {
		System system = null;
		try {
			system = systemRepository.findOne(systemDto.getSystemId());
			system.setModifyBy(systemDto.getModifyBy());
			system.setModifyDate(systemDto.getModifyDate());
			system.setStatus(IdmServiceConstant.SYSTEM_INACTIVE);
			logger.info("应用系统禁用 " + system.getSystemName());
		} catch (IllegalArgumentException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "应用系统不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_DELETE, "应用系统删除失败", e);
		} finally {
			system = null;
		}
	}

	@Override
	public SystemPageDTO findSystems(int pageIndex, int pageSize) throws IdmServiceException {
		SystemPageDTO systemPage = null;
		List<SystemDTO> systemDtoList = null;
		SystemDTO systemDTO = null;
		Long totalElements = 0L;
		List<System> systemList = null;
		String sql = "from System s where status=1";

		try {
			totalElements = entityManager.createQuery("select count(s) " + sql, Long.class)
					.getSingleResult();
			if (totalElements > 0) {
				systemList = entityManager
						.createQuery("select s " + sql + " order by s.createDate",
								System.class).setFirstResult((pageIndex - 1) * pageSize)
						.setMaxResults(pageSize).getResultList();

				systemDtoList = new ArrayList<SystemDTO>();
				for (System system : systemList) {
					systemDTO = toDto(system);
					systemDtoList.add(systemDTO);
					systemDTO = null;
				}

				systemPage = new SystemPageDTO(totalElements, pageSize, pageIndex, systemDtoList);
			}
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "应用系统查询失败", e);
		} finally {
			systemDtoList = null;
			systemDTO = null;
			totalElements = null;
			systemList = null;
		}
		return systemPage;
	}

	@Override
	public SystemDTO findBySystemCode(String systemCode) throws IdmServiceException {
		SystemDTO systemDTO = null;
		List<System> systemList = null;
		try {
			systemList = systemRepository.findBySystemCode(systemCode);
			if (!CollectionUtils.isEmpty(systemList)) {
				systemDTO = toDto(systemList.get(0));
			} else {
				throw new IdmServiceException();
			}
		} catch (IdmServiceException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "应用系统不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "应用系统查询失败", e);
		}
		return systemDTO;
	}
	
	@Override
	public SystemDTO findBySystemId(Long systemId) throws IdmServiceException {
		SystemDTO systemDTO = null;
		System system = null;
		try {
			system = systemRepository.findOne(systemId);
			if(system == null)
				throw new IdmServiceException();
			else{
				systemDTO = toDto(system);
			}
		} catch (IdmServiceException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "应用系统不存在");
		} catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS, "应用系统查询失败", e);
		}
		return systemDTO;
	}

	private boolean checkRepetition(String systemName, String systemUrl) {
		boolean repetition = false;
		long count = systemRepository.getCountBySystemNameAndSystemUrl(systemName,
				systemUrl);
		if (count > 0) {
			repetition = true;
		}
		return repetition;
	}

	@Override
	public List<SystemDTO> findSystemByAccount(String accountCode)
			throws IdmServiceException {
		List<SystemDTO> systemDtoList = null;
		SystemDTO systemDTO = null;
		List<String> systemCodeList = null;
		List<System> systemList = null;
		try {
				systemCodeList = systemRepository.findSystemCodeByAccount(accountCode);
				systemDtoList = new ArrayList<SystemDTO>();
				for (String systemCode : systemCodeList) {
					systemList = systemRepository.findBySystemCode(systemCode);
					if (!CollectionUtils.isEmpty(systemList)) {
						systemDTO = toDto(systemList.get(0));
						systemDtoList.add(systemDTO);
						systemDTO = null;
					} else {
						throw new IdmServiceException();
					}
				}
		}catch (IdmServiceException e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_NOT_FOUND, "应用系统不存在");
		}catch (Exception e) {
			throw new IdmServiceException(IdmServiceErrorConstant.DATA_PROCESS,
					e.getMessage());
		} finally {
			systemCodeList = null;
			systemList = null;
		}
		return systemDtoList;
	}

	
	private System toDomain(SystemDTO systemDTO) {
		System system = new System();
		system.setSystemCode(systemDTO.getSystemCode());
		system.setSystemName(systemDTO.getSystemName());
		system.setSystemUrl(systemDTO.getSystemUrl());
		system.setSystemSecret(systemDTO.getSystemSecret());
		system.setStatus(IdmServiceConstant.ACCOUNT_ACTIVE);
		return system;
	}

	private SystemDTO toDto(System system) {
		SystemDTO systemDTO = new SystemDTO();
		systemDTO.setSystemId(system.getSystemId());
		systemDTO.setSystemCode(system.getSystemCode());
		systemDTO.setSystemName(system.getSystemName());
		systemDTO.setSystemUrl(system.getSystemUrl());
		systemDTO.setSystemSecret(system.getSystemSecret());
		systemDTO.setCreateBy(system.getCreateBy());
		systemDTO.setCreateDate(system.getCreateDate());
		systemDTO.setModifyBy(system.getModifyBy());
		systemDTO.setModifyDate(system.getModifyDate());
		return systemDTO;
	}
}

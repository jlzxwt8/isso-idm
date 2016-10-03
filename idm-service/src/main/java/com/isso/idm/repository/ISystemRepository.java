
package com.isso.idm.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.isso.idm.domain.System;

public interface ISystemRepository extends CrudRepository<System, Long> {


	
	@Query("select s from System s where s.systemCode=:systemCode")
	public List<System> findBySystemCode(@Param("systemCode") String systemCode);

	@Query("select count(s) from System s where s.systemName=:systemName and systemUrl=:systemUrl")
	public Long getCountBySystemNameAndSystemUrl(@Param("systemName") String systemName,
			@Param("systemUrl") String systemUrl);
	
	@Query(value = "select systemCode from accountsystem a where a.accountCode=:accountCode", nativeQuery = true)
	public List<String> findSystemCodeByAccount(@Param("accountCode") String accountCode);
}
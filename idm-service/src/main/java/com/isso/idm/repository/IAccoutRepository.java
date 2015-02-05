
package com.isso.idm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.isso.idm.domain.Account;


public interface IAccoutRepository extends CrudRepository<Account, Long> {

	@Query("select a from Account a where a.accountCode=:accountCode")
	public List<Account> findByAccountCode(@Param("accountCode") String accountCode);
}

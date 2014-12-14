/****************************************************************************************
 * @File name   :      ITmDpAccoutRepository.java
 *
 * @Author      :      LEIKZHU
 *
 * @Date        :      Sep 2, 2014
 *
 * @Copyright Notice: 
 * Copyright (c) 2014 SGM, Inc. All  Rights Reserved.
 * This software is published under the terms of the SGM Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 * 
 * 
 * --------------------------------------------------------------------------------------
 * Date								Who					Version				Comments
 * Sep 2, 2014 3:29:46 PM			LEIKZHU				1.0				Initial Version
 ****************************************************************************************/
package com.isso.idm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.isso.idm.domain.Account;

@Transactional
public interface IAccoutRepository extends JpaRepository<Account, Long> {

	@Query("select a from Accout a where a.accountCode=:accountCode")
	public List<Account> findByAccountCode(@Param("accountCode") String accountCode);
}

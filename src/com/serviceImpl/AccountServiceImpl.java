package com.serviceImpl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dao.AccountDao;
import com.entity.Account;
import com.service.AccountService;

@Service("accountService")
public class AccountServiceImpl implements AccountService{
	
	@Resource
	private AccountDao accountDao;
	
	@Override
	public Account login(Account account) {
		return accountDao.login(account);
	}

}

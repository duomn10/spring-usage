package com.duomn.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.duomn.entity.UserModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager="hibernateTx", defaultRollback=true)
public class TestUserService {

	@Autowired
	private IUserService userService;
	
	@Test
	public void testList() {
		List<UserModel> list = userService.listAll();
		System.out.println(list.size());
	}
	
}
